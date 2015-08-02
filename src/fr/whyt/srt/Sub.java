/**
 * 
 */
package fr.whyt.srt;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Jeremy
 *
 */
public class Sub {
	
	private int number;
	private int start_line;
	private LocalTime start_time;
	private LocalTime end_time;
	private String sub_string;
	
	public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
	
	
	public Sub(int number, int start_line, LocalTime start_time, LocalTime end_time, String sub_string) {
		this.number		= number;
		this.start_line = start_line;
		this.start_time = start_time;
		this.end_time 	= end_time;
		this.sub_string = sub_string;
	}
	
	public Sub(int number, int start_line, String start_time, String end_time, String sub_string) {
		this.number		= number;
		this.start_line = start_line;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
		this.start_time = LocalTime.parse(start_time, dtf);
		this.end_time 	= LocalTime.parse(end_time, dtf);
		
		this.sub_string = sub_string;
	}

	
	public int getNumber() {
		return this.number;
	}
	
	public int getStartLine() {
		return this.start_line;
	}
	
	public LocalTime getStartTime() {
		return this.start_time;
	}
	
	public LocalTime getEndTime() {
		return this.end_time;
	}
	
	public String getSubString() {
		return this.sub_string;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Sub
				&& ((Sub) obj).number == this.number
				&& ((Sub) obj).start_line == this.start_line
				&& ((Sub) obj).sub_string.equals(this.sub_string)
				&& ((Sub) obj).start_time.equals(this.start_time)
				&& ((Sub) obj).end_time.equals(this.end_time);
	}
	
	@Override
	public String toString() {
		return this.number + "\n"
				+ this.start_time.format(Sub.dtf) + " --> " + this.end_time.format(Sub.dtf) + "\n"
				+ this.sub_string;
	}
	
}
