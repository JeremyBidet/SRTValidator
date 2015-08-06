/**
 * 
 */
package fr.whyt.validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import fr.whyt.srt.SRT;


/**
 * @author Jeremy
 *
 */
public class SRTErrorLog {

	private static final String logpath = "log";
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uu-MM-dd_HH-mm-ss");
	
	private final File file;
	private final ArrayList<SRTException> errors;
	
	public SRTErrorLog(File file, ArrayList<SRTException> errors) {
		this.file = file;
		this.errors = errors;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public ArrayList<SRTException> getErrors() {
		return this.errors;
	}
	
	public void log() {
		new File(logpath).mkdir();
		
		LocalDateTime current_date_time = LocalDateTime.now();
		String computed_logpath = logpath + '/' + file.getName() + '_' + current_date_time.format(dtf);
		File log = new File(computed_logpath);
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(log), SRT.charset));
			
			bw.write(this.toString());
			
			bw.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SRTErrorLog
				&& ((SRTErrorLog) obj).file.equals(this.file)
				&& ((SRTErrorLog) obj).errors.equals(this.errors);
	}
	
	@Override
	public String toString() {
		return this.errors.isEmpty() ?
				"No errors detected !" :
				this.errors.stream()
						.map(e -> e.toString())
						.reduce((e1, e2) -> e1 + "\n" + e2)
						.get();
	}
	
}
