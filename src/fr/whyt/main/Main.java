/**
 * 
 */
package fr.whyt.main;

import fr.whyt.srt.RawSRTFile;
import fr.whyt.srt.SRT;
import fr.whyt.validator.SRTErrorLog;
import fr.whyt.validator.Validator;


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
		RawSRTFile rsf = SRT.deserialize("srt/test.srt");
		SRTErrorLog sel = Validator.validate(rsf);
		sel.log();
		
	}
	
	
}
