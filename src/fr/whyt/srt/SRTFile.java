/**
 * 
 */
package fr.whyt.srt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import fr.whyt.validator.SRTException;


/**
 * @author Jeremy
 *
 */
public class SRTFile {
	
	private final File file;	
	private final ArrayList<Sub> subs;
	
	private final ArrayList<SRTException> srt_exceptions;
	
	public SRTFile(String pathname, ArrayList<Sub> subs, ArrayList<SRTException> srt_exceptions) {
		this.file = new File(pathname);
		this.subs = subs;
		this.srt_exceptions = srt_exceptions;
	}
	
	public SRTFile(File file, ArrayList<Sub> subs, ArrayList<SRTException> srt_exceptions) {
		this.file = file;
		this.subs = subs;
		this.srt_exceptions = srt_exceptions;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public Sub getSub(int sub) {
		return this.subs.get(sub);
	}
	
	public ArrayList<Sub> getSubs() {
		return this.subs;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SRTFile
				&& ((SRTFile) obj).file.equals(this.file)
				&& ((SRTFile) obj).subs.equals(this.subs);
	}
	
	@Override
	public String toString() {
		return this.file + "\n"
				+ this.subs.stream()
					.map(s -> s.toString())
					.reduce((s1, s2) -> s1 + "\n\n" + s2)
					.get();
	}
	
	
	public String log() {
		return this.srt_exceptions.stream()
				.map(se -> se.toString())
				.reduce((se1, se2) -> se1 + "\n\n" + se2)
				.get();
	}
	
	public boolean save(String new_name) {
		if( this.srt_exceptions.size() > 0 ) {
			return false;
		}
		
		try {
			Files.newBufferedWriter(Paths.get(new_name), StandardOpenOption.CREATE).write(
					this.subs.stream()
						.map(s -> s.toString())
						.reduce((s1, s2) -> s1 + "\n\n" + s2)
						.get()
			);
			return true;
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		}
	}
	
}
