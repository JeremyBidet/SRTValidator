/**
 * 
 */
package fr.whyt.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Jeremy
 *
 */
public class Main {

	
	public static void main(String[] args) {
//		System.out.println(Validator.validate("srt/test.srt"));
//		System.out.println("---------------------------------------");
//		SRTFile srt_file = Validator.detectError("srt/test.srt");
//		System.out.println("toString() :\n" + srt_file + "\n-------------------------------------\n");
//		System.err.println("log:\n" + srt_file.log());
		
		//System.out.println(SRT.deserialize("srt/test.srt"));
		
		String time_validator 	= "\\d{2}:\\d{2}:\\d{2},\\d{3}";
		Pattern time_pattern	= Pattern.compile(time_validator);
		
		String arrow_validator 	= " --> ";
		Pattern arrow_pattern	= Pattern.compile(arrow_validator);
		
		String times = "00:00:00,000 --> 11:11:11,111";
		
		Matcher m	= time_pattern.matcher(times);
		Matcher m2 	= arrow_pattern.matcher(times);
		
		String start_time 	= new String();
		String arrow_sign 	= new String();
		String end_time 	= new String();

		int last_end = 0;
		
		m.reset();
		m.region(0, 12);
		System.out.println("Trying to find a match in region: " + m.regionStart() + ',' + m.regionEnd());
		if(m.find()) {
			start_time = m.group();
			System.out.println("\tlast match region: " + m.start() + "," + m.end() + " - last match: " + m.group());
			System.out.println("START TIME FOUND !");
			last_end = m.end();
		} else {
			System.err.println("Start time not found !");
		}
		System.out.println();
		
		m.reset();
		m.region(last_end, last_end+5);
		System.out.println("Trying to find a match in region: " + m.regionStart() + ',' + m.regionEnd());
		if( m2.find() ) {
			arrow_sign = m2.group();
			System.out.println("\tlast match region: " + m2.start() + "," + m2.end() + " - last match: " + m2.group());
			System.out.println("ARROW SIGN FOUND !");
			last_end = m2.end();
		} else {
			System.err.println("Arrow sign not found !");
		}
		System.out.println();
		
		m.reset();
		m.region(last_end, m.regionEnd());
		System.out.println("Trying to find a match in region: " + m.regionStart() + ',' + m.regionEnd());
		if(m.find()) {
			end_time = m.group();
			System.out.println("\tlast match region: " + m.start() + "," + m.end() + " - last match: " + m.group());
			System.out.println("END TIME FOUND !");
			last_end = m.end();
		} else {
			System.err.println("End time not found !");
		}
		System.out.println();
		
		System.out.println(start_time + arrow_sign + end_time);
		
	}
	
	
}
