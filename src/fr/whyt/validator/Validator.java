/**
 * 
 */
package fr.whyt.validator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
	
	private static final String sub_number_validator 	= "\\d+";
	private static final String time_validator 			= "\\d{2}:\\d{2}:\\d{2},\\d{3}";
	private static final String times_validator			= "(<?starttime>" + Validator.time_validator + ") --> (<?endtime>" + Validator.time_validator + ")";
	private static final String sub_string_validator 	= "[^\n]+";
	private static final String sub_strings_validator 	= "((<?substring>" + Validator.sub_string_validator + ")\n)+";
	private static final String sub_validator = ""
			+ "(<?subnumber>" + Validator.sub_number_validator + ")\n"
			+ "(?<times>" + Validator.times_validator + ")\n"
			+ "(<?substrings>" + Validator.sub_strings_validator + ")";
	private static final String srt_validator = "(<?sub>" + Validator.sub_validator + "\n*)+";

	private static final Pattern sub_number_pattern		= Pattern.compile(Validator.sub_number_validator);
	private static final Pattern time_pattern			= Pattern.compile(Validator.time_validator);
	private static final Pattern times_pattern			= Pattern.compile(Validator.times_validator);
	private static final Pattern sub_string_pattern		= Pattern.compile(Validator.sub_string_validator);
	private static final Pattern sub_strings_pattern	= Pattern.compile(sub_strings_validator);
	private static final Pattern sub_pattern 			= Pattern.compile(Validator.sub_validator);
	private static final Pattern srt_pattern 			= Pattern.compile(Validator.srt_validator);
	
	private static final DateTimeFormatter dtf = Sub.dtf;
	
	private static final Charset srt_charset = Charset.forName("ISO-8859-1");
	
	/**
	 * Validate a SRT file !<br>Does not return errors or SRT file wrapper.
	 * @param filepath the file path of the SRT file.
	 * @return true if, and only if, the SRT file match the SRT pattern.
	 */
	public static boolean validate(String filepath) {
		try {
			
			String whole_file = Files.lines(Paths.get("srt/test.srt"), Validator.srt_charset)
					.reduce((s1, s2) -> s1 + s2)
					.get();
			
			Matcher srt_matcher = srt_pattern.matcher(whole_file);
			
			return srt_matcher.matches();
			
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		}	
	}
	
	/**
	 * 
	 * @param filepath
	 * @return
	 */
	public static SRTFile detectError(String filepath) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), Validator.srt_charset));
			
			ArrayList<Sub> subs = new ArrayList<Sub>();
			ArrayList<SRTException> srt_exceptions = new ArrayList<SRTException>();
			
			StringBuilder sb_sub = new StringBuilder();
			StringBuilder sub_strings = new StringBuilder();
			String line = new String();
			int line_no = 1;
			
			while( (line = br.readLine()) != null ) {
				int start_line_no = line_no;
				String sub_number_line = line;
				String times_line = (line = br.readLine());
				sub_strings.delete(0, sub_strings.length());
				while( (line = br.readLine()).equals("") == false ) {
					sub_strings.append(line).append('\n');
					line_no++;
				}
				String sub_strings_lines = sub_strings.toString();
				line_no++;
				line_no++;
				
				sb_sub.delete(0, sb_sub.length());
				sb_sub.append(sub_number_line).append('\n');
				sb_sub.append(times_line).append('\n');
				sb_sub.append(sub_strings_lines);
				String sub = sb_sub.toString();
				
				Matcher sub_number_matcher 	= sub_number_pattern.matcher(sub_number_line);
				Matcher times_matcher		= times_pattern.matcher(times_line);
				Matcher sub_strings_matcher = sub_strings_pattern.matcher(sub_strings_lines);
				
				/** if any error, sub is not added to sub list. **/
				boolean valide = true;
				
				/** validate sub number **/
				int sub_number = -1;
				if( sub_number_matcher.matches() ) {
					sub_number = Integer.parseInt(sub_number_matcher.group("subnumber"));
					if( sub_number != subs.size() ) {
						srt_exceptions.add(new SRTException(
								"Sub number does not match ! Expected: " + subs.size() + ", actual: " + sub_number,
								start_line_no, 0, sub
						));
						valide = false;
					}
				} else {
					srt_exceptions.add(new SRTException(
							"Invalid sub number format ! Expected number, actual " + sub_number_line,
							start_line_no, 0, sub)
					);
					valide = false;
				}
				
				/** validate times (start time and end time, with arrow sign) **/
				LocalTime lt_start_time = null;
				LocalTime lt_end_time = null;
				if( times_matcher.matches() ) {
					String start_time = times_matcher.group("starttime");
					String end_time = times_matcher.group("endtime");
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
						srt_exceptions.add(new SRTException(
								dtpe.getCause().getMessage(),
								start_line_no+1, offset, sub
						));
						valide = false;
					}
				} else {
					String[] times_strings = times_line.split(" --> ");
					if(times_strings.length != 2) {
						srt_exceptions.add(new SRTException(
								"Start time and end time should be separated by a \" --> \" sign !",
								start_line_no+1,
								12,
								sub
						));
					} else {
						Matcher start_time_matcher = time_pattern.matcher(times_strings[0]);
						if( start_time_matcher.matches() == false ) {
							srt_exceptions.add(new SRTException(
									"The start time string does not match the pattern ! Expected: hh:mm:ss,ttt, actual: " + times_strings[0] + "\n"
										+ "\twith 'h' as hour, 'm' as minute, 's' as second, 't' as millisecond.",
									start_line_no+1, 0, sub
							));
						}
						Matcher end_time_matcher = time_pattern.matcher(times_strings[1]);
						if( end_time_matcher.matches() == false ) {
							srt_exceptions.add(new SRTException(
									"The end time string does not match the pattern ! Expected: hh:mm:ss,ttt, actual: " + times_strings[0] + "\n"
										+ "\twith 'h' as hour, 'm' as minute, 's' as second, 't' as millisecond.",
									start_line_no+1, 17, sub
							));
						}
					}
					valide = false;
				}
				
				/** validate substrings **/
				if( sub_strings_matcher.matches() == false ) {
					srt_exceptions.add(new SRTException(
							"There are no subtitles here !",
							start_line_no+2, 0, sub
					));
					valide = false;
				}
				
				Matcher sub_matcher = sub_pattern.matcher(sub);
				/** add sub if no error **/
				if( valide && sub_matcher.matches() ) {
					subs.add(new Sub(sub_number, start_line_no, lt_start_time, lt_end_time, sub_strings_lines));
				}
				
				/** read the last \n of the sub **/
				line = br.readLine();
				line_no++;
			}
			
			br.close();
			
			return new SRTFile(Paths.get(filepath).toFile(), subs, srt_exceptions);
		
		} catch ( IOException e ) {
			System.err.println("This file does not exists ! " + filepath);
		}
		
		return null;
	}
	
}
