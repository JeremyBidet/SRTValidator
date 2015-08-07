/**
 * 
 */
package fr.whyt.synchronizer;

import java.time.Duration;

import fr.whyt.srt.SRTFile;
import fr.whyt.srt.Sub;


/**
 * @author Jeremy
 *
 */
public class Synchronizer {
	
	public static enum Sign {
		MINUS,
		PLUS;
	}
	
	public static SRTFile shift(SRTFile srt_file, Duration shift) {
		
		for(Sub sub : srt_file.getSubs()) {
			sub.setStartTime(sub.getStartTime().plus(shift));
			sub.setEndTime(sub.getEndTime().plus(shift));
		}
		
		return srt_file;
	}
	
	public static Duration create(Synchronizer.Sign sign, long hours, long minutes, long seconds, long milliseconds) {
		StringBuilder duration = new StringBuilder();
		
		duration.append( sign.equals(Sign.MINUS)?'-':"" );
		duration.append("PT");
		duration.append(hours).append('H');
		duration.append(minutes).append('M');
		duration.append(seconds).append('.').append(milliseconds).append('S');
		
		return Duration.parse(duration.toString());
	}
	
}
