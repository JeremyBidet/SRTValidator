/**
 * 
 */
package fr.whyt.core.comparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import fr.whyt.core.srt.SRT;


/**
 * @author Jeremy
 *
 */
public class ComparatorErrorLog {

	private static final String logpath = "log";
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uu-MM-dd_HH-mm-ss");
	
	private final File current;
	private final File base;
	private final ArrayList<ComparatorException> errors;
	
	public ComparatorErrorLog(File current, File base, ArrayList<ComparatorException> errors) {
		this.current = current;
		this.base = base;
		this.errors = errors;
	}
	
	public File getCurrentFile() {
		return this.current;
	}
	
	public File getBaseFile() {
		return this.base;
	}
	
	public ArrayList<ComparatorException> getErrors() {
		return this.errors;
	}
	
	public void log() {
		new File(logpath).mkdir();
		
		LocalDateTime current_date_time = LocalDateTime.now();
		String computed_logpath = logpath + '/' + current.getName() + '_' + base.getName() + '_' + current_date_time.format(dtf);
		File log = new File(computed_logpath);
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(log), SRT.charset));
			
			StringBuilder sb = new StringBuilder();
			/** create errors list **/
			for(int i=0; i<this.errors.size(); i++) {
				sb	.append('#')
					.append(i)
					.append('\n')
					.append(this.errors.get(i).toString())
					.append('\n');
			}
			/** create footer **/
			sb.append("#####################\n");
			sb.append(this.errors.size() + " error" + (this.errors.size()>1?'s':"") + " founded !").append('\n');
			
			/** write to file **/
			bw.write(sb.toString());
			
			bw.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ComparatorErrorLog
				&& ((ComparatorErrorLog) obj).current.equals(this.current)
				&& ((ComparatorErrorLog) obj).base.equals(this.base)
				&& ((ComparatorErrorLog) obj).errors.equals(this.errors);
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
