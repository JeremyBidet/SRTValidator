/**
 * 
 */
package fr.whyt.main;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import fr.whyt.core.synchronizer.Synchronizer;
import fr.whyt.core.synchronizer.Synchronizer.Sign;


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
//		RawSRTFile rsf = SRT.deserialize("srt/test.srt");
//		SRTErrorLog sel = Validator.validate(rsf);
//		sel.log();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
		LocalTime lt = LocalTime.parse("12:31:45,345", dtf);
		System.out.println("lt:"+lt);
		Duration duration = Synchronizer.create(Sign.MINUS, 0, 1, 2, 112);
		System.out.println("duration:"+duration);
		lt = lt.plus(duration);
		System.out.println("lt:"+lt);
		
	}
	
	
}
