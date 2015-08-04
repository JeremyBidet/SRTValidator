/**
 * 
 */
package fr.whyt.main;

import fr.whyt.srt.SRTFile;
import fr.whyt.validator.Validator;


/**
 * @author Jeremy
 *
 */
public class Main {

	
	public static void main(String[] args) {
		System.out.println(Validator.validate("srt/test.srt"));
		System.out.println("---------------------------------------");
		SRTFile srt_file = Validator.detectError("srt/test.srt");
		System.out.println("toString() :\n" + srt_file + "\n-------------------------------------\n");
		System.err.println("log:\n" + srt_file.log());
	}
	
	
}
