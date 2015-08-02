/**
 * 
 */
package fr.whyt.validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.whyt.srt.SRTFile;
import fr.whyt.srt.Sub;



/**
 * @author Jeremy
 *
 */
public class Validator {
	
	private static final String srt_validator = ""
			// a srt file is composed of
			+ "(<?srtfile>"
				// zero or more sub(s) followed by two end-line character
				+ "(" + Validator.sub_validator + "\n\n)*"
				// then last sub followed by zero or more end-line character
				+ "(" + Validator.sub_validator + "\n*)"
			+ ")";
	
	private static final String sub_validator = ""
		// a sub is composed of
		+ "(<?sub>"
			// a sub number followed by one end-line character
			+ "(<?subnumber>\\d)" + "\n"
			// a start time formatted as HH:mm:ss,SSS followed by a space, a "-->", an another space, an end time formatted as HH:mm:ss,SSS and one end-line character
			+ "(<?starttime>(<?starttimeh>\\d{2}):(<?starttimem>\\d{2}):(<?starttimes>\\d{2}),(<?starttimems>\\d{3}))"
			+ " --> "
			+ "(<?endtime>(<?endtimeh>\\d{2}):(<?endtimem>\\d{2}):(<?endtimes>\\d{2}),(<?endtimems>\\d{3}))" + "\n"
			// one or more string separated by one end-line character
			+ "(<?substring>([^\n]+\n)+)"
		+ ")";
	
	private static final DateTimeFormatter dtf = Sub.dtf;
	
	private static final Pattern srt_pattern = Pattern.compile(srt_validator);
	private static final Pattern sub_pattern = Pattern.compile(sub_validator);
	
	
	public static SRTFile validate(String filename) {
		try {
		
			String whole_file = Files.lines(Paths.get(filename)).reduce((s1, s2) -> s1 + s2).get();
			ArrayList<Sub> subs = new ArrayList<Sub>();
			ArrayList<SRTException> srt_exceptions = new ArrayList<SRTException>();
			
			Matcher srt_matcher = srt_pattern.matcher(whole_file);
			Matcher sub_matcher = sub_pattern.matcher(whole_file);
			
			if(srt_matcher.matches()) {
				int check_number = 0;
				int line = 0;
				while( sub_matcher.find() ) {
					/** getting parsed sub **/
					int number = Integer.parseInt(sub_matcher.group("subnumber"));
					LocalTime lt_start_time = null;
					String start_time = sub_matcher.group("starttime");
					LocalTime lt_end_time = null;
					String end_time = sub_matcher.group("endtime");
					String sub_string = sub_matcher.group("substring");
					
					/** updating line position **/
					int tmp_line = line;
					//     last + number+time + sub string count                + last end-line
					line = line + 2           + sub_string.split("\n").length-1 + 1;
					
					/** validating parsed sub **/
					boolean error = false;
					String sub = sub_matcher.group("sub");
					if( number != check_number++ ) {
						srt_exceptions.add(new SRTException("", tmp_line, 0, sub));
						error = true;
					}
					try {
						lt_start_time = LocalTime.parse(start_time, dtf);
						lt_end_time = LocalTime.parse(end_time, dtf);
					} catch (DateTimeParseException dtpe) {
						int offset = 0;
						if(lt_start_time != null && lt_end_time == null) {
							offset += 17;
						}
						if(dtpe.getCause().getMessage().contains("SecondOfMinute")) {
							offset += 6;
						}
						else if(dtpe.getCause().getMessage().contains("MinuteOfHour")) {
							offset += 3;
						}
						else if(dtpe.getCause().getMessage().contains("HourOfDay")) {
							offset += 0;
						} else {
							offset = dtpe.getErrorIndex();
						}
						srt_exceptions.add(new SRTException(dtpe.getCause().getMessage(), tmp_line+1, offset, sub));
						error = true;
					}
					if(sub_string.length() <= 0) {
						srt_exceptions.add(new SRTException("There are no subtitles here !", tmp_line+2, 0, sub));
						error = true;
					}
					
					/** add sub if no error **/
					if( !error ) {
						subs.add(new Sub(number, tmp_line, lt_start_time, lt_end_time, sub_string));
					}
				}
			}
			
			return new SRTFile(Paths.get(filename).toFile(), subs, srt_exceptions);
		
		} catch ( IOException e ) {
			System.err.println("This file does not exists ! " + filename);
		}
		
		return null;
	}
	
}
