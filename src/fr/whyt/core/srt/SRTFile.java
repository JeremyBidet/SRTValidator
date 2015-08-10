/**
 * 
 */
package fr.whyt.core.srt;

import java.io.File;
import java.util.ArrayList;


/**
 * @author Jeremy
 *
 */
public class SRTFile {
	
	private final File file;	
	private final ArrayList<Sub> subs;
	
	public SRTFile(String pathname, ArrayList<Sub> subs) {
		this.file = new File(pathname);
		this.subs = subs;
	}
	
	public SRTFile(File file, ArrayList<Sub> subs) {
		this.file = file;
		this.subs = subs;
	}
	
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Get the sub-th sub.<br>Warning ! Sub number ID start at 0, so first sub has the number ID : 0.
	 * @param sub the index of the sub in the SRT file.
	 * @return the raw sub at the sub-th position.
	 */
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
				+ this.toFormattedString();
	}
	
	public String toFormattedString() {
		return this.subs.stream()
				.map(s -> s.toString())
				.reduce((s1, s2) -> s1 + "\n\n" + s2)
				.get();
	}
	
}
