/**
 * 
 */
package fr.whyt.srt;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * @author Jeremy
 *
 */
public class Sub {

	private int start_line;
	
	private int number;
	private LocalTime start_time;
	private LocalTime end_time;
	private ArrayList<String> sub_strings;
	
	public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
	
	
	public Sub(int start_line, int number, LocalTime start_time, LocalTime end_time, ArrayList<String> sub_strings) {
		this.start_line = start_line;
		
		this.number 	= number;
		this.start_time = start_time;
		this.end_time 	= end_time;
		this.sub_strings = sub_strings;
	}
	
	public Sub(int start_line, int number, String start_time, String end_time, ArrayList<String> sub_strings) {
		this.start_line = start_line;
		
		this.number		= number;
		this.start_time = LocalTime.parse(start_time, Sub.dtf);
		this.end_time 	= LocalTime.parse(end_time, Sub.dtf);
		this.sub_strings = sub_strings;
	}

	
	public int getStartLine() {
		return this.start_line;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public LocalTime getStartTime() {
		return this.start_time;
	}
	
	public LocalTime getEndTime() {
		return this.end_time;
	}
	
	public ArrayList<String> getSubStrings() {
		return this.sub_strings;
	}
	
	public String getFormattedSubStrings() {
		return this.sub_strings.stream().reduce((s1, s2) -> s1 + '\n' + s2).get();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Sub
				&& ((Sub) obj).start_line == this.start_line
				&& ((Sub) obj).number == this.number
				&& ((Sub) obj).sub_strings.equals(this.sub_strings)
				&& ((Sub) obj).start_time.equals(this.start_time)
				&& ((Sub) obj).end_time.equals(this.end_time);
	}
	
	@Override
	public String toString() {
		return this.number + "\n"
				+ this.start_time.format(Sub.dtf) + " --> " + this.end_time.format(Sub.dtf) + "\n"
				+ this.getFormattedSubStrings();
	}
	
}
