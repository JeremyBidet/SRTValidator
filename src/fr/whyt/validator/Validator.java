/**
 * 
 */
package fr.whyt.validator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.whyt.srt.RawSRTFile;
import fr.whyt.srt.RawSub;
import fr.whyt.srt.Sub;



/**
 * @author Jeremy
 *
 */
public class Validator {
	
	private static final String sub_number_validator 	= "\\d+";
	private static final String time_validator 			= "\\d{2}:\\d{2}:\\d{2},\\d{3}";
	private static final String arrow_validator 		= " --> ";
	private static final String times_validator			= "(?<starttime>" + Validator.time_validator + ") --> (?<endtime>" + Validator.time_validator + ")";
	private static final String sub_string_validator 	= "[^\n]+";
	private static final String sub_strings_validator 	= "((?<substring>" + Validator.sub_string_validator + ")\n)+";
	private static final String sub_validator = ""
			+ "(?<subnumber>" + Validator.sub_number_validator + ")\n"
			+ "(?<times>" + Validator.times_validator + ")\n"
			+ "(?<substrings>" + Validator.sub_strings_validator + ")";
	private static final String srt_validator = ""
			+ "((?<sub>" + Validator.sub_validator + ")\n*)+";

	private static final Pattern sub_number_pattern		= Pattern.compile(Validator.sub_number_validator);
	private static final Pattern time_pattern			= Pattern.compile(Validator.time_validator);
	private static final Pattern arrow_pattern			= Pattern.compile(Validator.arrow_validator);
	private static final Pattern times_pattern			= Pattern.compile(Validator.times_validator);
	private static final Pattern sub_string_pattern		= Pattern.compile(Validator.sub_string_validator);
	private static final Pattern sub_strings_pattern	= Pattern.compile(Validator.sub_strings_validator);
	private static final Pattern sub_pattern 			= Pattern.compile(Validator.sub_validator);
	private static final Pattern srt_pattern 			= Pattern.compile(Validator.srt_validator);
	
	private static final DateTimeFormatter dtf = Sub.dtf;
	
	
	/**
	 * Validate a SRT file. If this is not valid, call {@linkplain Validator#checkError(RawSRTFile) checkError} method.<br>
	 * In both case, return a {@link SRTErrorLog}, with errors if any.
	 * @param raw_srt_file the SRT file as it (in raw format).
	 * @return a SRT error log, empty if no errors.
	 */
	public static SRTErrorLog validate(RawSRTFile raw_srt_file) {
		return checkError(raw_srt_file);
	}
	
	/**
	 * Check errors from the raw SRT file.<br>
	 * @param raw_srt_file the SRT file as it (in raw format).
	 * @return a SRT error log with all errors contained in the SRT file.
	 */
	private static SRTErrorLog checkError(RawSRTFile raw_srt_file) {
		ArrayList<SRTException> errors = new ArrayList<SRTException>();
		
		int i = 0;
		for(RawSub raw_sub : raw_srt_file.getSubs()) {
			/** validate sub number **/
			Matcher sub_number_matcher = sub_number_pattern.matcher(raw_sub.getNumber());
			if( sub_number_matcher.matches() ) {
				int sub_number = Integer.parseInt(sub_number_matcher.group());
				if( sub_number != i ) {
					errors.add(new SRTException(
							"Sub number does not match ! Expected: " + i + ", actual: \"" + sub_number + "\"",
							raw_sub.getStartLine(), 0, raw_sub.toString()));
				}
			} else {
				errors.add(new SRTException(
						"Invalid sub number format ! Expected number, actual: \"" + raw_sub.getNumber() + "\"",
						raw_sub.getStartLine(), 0, raw_sub.toString()));
			}
			/** validate times (start time and end time, with arrow sign) **/
			Matcher times_matcher = times_pattern.matcher(raw_sub.getTimer());
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
					errors.add(new SRTException(
							dtpe.getCause().getMessage(),
							raw_sub.getStartLine()+1, offset, raw_sub.toString()));
				}
			} else {
				Matcher time_matcher 	= Validator.time_pattern.matcher(raw_sub.getTimer());
				Matcher arrow_matcher	= Validator.arrow_pattern.matcher(raw_sub.getTimer());
				int last_end = 0;
				time_matcher.reset();
				time_matcher.region(time_matcher.regionStart(), time_matcher.regionStart()+12);
				if( !time_matcher.find() ) {
					errors.add(new SRTException(
							"The start time string does not match the pattern ! Expected: hh:mm:ss,ttt, actual: \"" + raw_sub.getTimer() + "\"\n"
									+ "with 'h' as hour, 'm' as minute, 's' as second, 't' as millisecond.",
							raw_sub.getStartLine()+1, 0, raw_sub.toString()));
				}
				arrow_matcher.reset();
				if( !arrow_matcher.find() ) {
					errors.add(new SRTException(
							"Start time and end time should be separated by a \" --> \" sign !",
							raw_sub.getStartLine()+1, 12, raw_sub.toString()));
				} else {
					last_end = arrow_matcher.end();
				}
				time_matcher.reset();
				time_matcher.region(time_matcher.regionEnd()-12, time_matcher.regionEnd());
				if( !time_matcher.find() ) {
					errors.add(new SRTException(
							"The end time string does not match the pattern ! Expected: hh:mm:ss,ttt, actual: \"" + raw_sub.getTimer() + "\"\n"
								+ "with 'h' as hour, 'm' as minute, 's' as second, 't' as millisecond.",
							raw_sub.getStartLine()+1, time_matcher.regionStart(), raw_sub.toString()));
				}
			}
			/** validate substrings **/
			Matcher sub_strings_matcher = sub_strings_pattern.matcher(raw_sub.getFormattedSubStrings());
			if( ! sub_strings_matcher.matches() ) {
				errors.add(new SRTException(
						"There are no subtitles in this sub !",
						raw_sub.getStartLine()+2, 0, raw_sub.toString()));
			}
			/** next sub **/
			i++;
		}
		
		return new SRTErrorLog(raw_srt_file.getFile(), errors);
	}
	
}
