/**
 * 
 */
package fr.whyt.core.srt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * @author Jeremy
 *
 */
public class SRT {
	
	public static Charset charset = Charset.forName("ISO-8859-1");
	
	/**
	 * Save the current SRT file by replacing it's content.
	 * @param srt_file the SRT file
	 */
	public static void serialize(SRTFile srt_file) {
		serialize(srt_file, srt_file.getFile().getAbsolutePath(), true);
	}
	
	public static void serialize(SRTFile srt_file, String new_filepath, boolean replace) {
		try {
			if(replace) {
				srt_file.getFile().delete();
			}
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(new_filepath, false),
							SRT.charset));
			String output = srt_file.toFormattedString();
			bw.write(output);
			bw.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public static RawSRTFile deserialize(String filepath) {
		try {
			ArrayList<RawSub> subs = new ArrayList<RawSub>();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), SRT.charset));
			
			int line_no = 0;
			String line = new String();
			
			while( (line = br.readLine()) != null ) {
				line_no++;
				// skip empty lines
				if(line.equals("")) {
					continue;
				}
				// keep the line number of the current sub first line (sub number)
				int start_line_no = line_no;
				// read the sub number
				String number = line;
				// read the sub timer
				String timer  = line = br.readLine(); line_no++;
				// read the sub strings
				ArrayList<String> sub_strings = new ArrayList<String>();
				while( (line = br.readLine()) != null && !line.equals("") ) {
					sub_strings.add(line); line_no++;
				}
				line_no++;
				// create the whole sub string
				String sub = number + '\n' + timer + '\n' + sub_strings.stream().reduce((s1,  s2) -> s1 + '\n' + s2).get();
				// add the sub
				subs.add(new RawSub(start_line_no, number, timer, sub_strings, sub));
			}
			
			br.close();
			
			return new RawSRTFile(filepath, subs);
			
		} catch ( FileNotFoundException e ) {
			System.err.println(e.getMessage());
		} catch ( IOException e ) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
}
