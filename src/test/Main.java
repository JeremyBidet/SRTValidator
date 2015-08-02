/**
 * 
 */
package test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Jeremy
 *
 */
public class Main {
	
	public static void main(String[] args) {
		String t = "24:02:03,004";
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
		
		LocalTime lt = LocalTime.parse(t, dtf);
		
		System.out.println(t);
		System.out.println(lt);
	}
}
