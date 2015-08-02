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


/**
 * @author Jeremy
 *
 */
public class SRTFile {
	
	private final File file;	
	private final ArrayList<Sub> subs;
	
	public SRTFile(String pathname) {
		this.file = new File(pathname);
		this.subs = new ArrayList<Sub>();
	}
	
	public SRTFile(File file) {
		this.file = file;
		this.subs = new ArrayList<Sub>();
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
				+ this.subs.stream().map(s -> s.toString()).reduce((s1, s2) -> s1 + "\n\n" + s2).get();
	}
	
	public void save(String new_name) {
		try {
			Files.newBufferedWriter(Paths.get(new_name), StandardOpenOption.CREATE).write(
					this.subs.stream().map(s -> s.toString()).reduce((s1, s2) -> s1 + "\n\n" + s2).get()
			);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
}
