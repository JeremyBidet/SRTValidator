/**
 * 
 */
package fr.whyt.srt;

import java.io.File;
import java.util.ArrayList;


/**
 * @author Jeremy
 *
 */
public class RawSRTFile {
	
	private final File file;	
	private final ArrayList<RawSub> subs;
	
	public RawSRTFile(String pathname, ArrayList<RawSub> subs) {
		this.file = new File(pathname);
		this.subs = subs;
	}
	
	public RawSRTFile(File file, ArrayList<RawSub> subs) {
		this.file = file;
		this.subs = subs;
	}
	
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Get the sub-th raw sub.<br>Warning ! Sub number ID start at 0, so first sub has the number ID : 0.
	 * @param sub the index of the raw sub in the SRT file.
	 * @return the raw sub at the sub-th position.
	 */
	public RawSub getSub(int sub) {
		return this.subs.get(sub);
	}
	
	public ArrayList<RawSub> getSubs() {
		return this.subs;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RawSRTFile
				&& ((RawSRTFile) obj).file.equals(this.file)
				&& ((RawSRTFile) obj).subs.equals(this.subs);
	}
	
	@Override
	public String toString() {
		return this.file + "\n"
				+ this.toFormattedString();
	}
	
	public String toFormattedString() {
		return this.subs.stream()
				.map(s -> s.getSub())
				.reduce((s1, s2) -> s1 + "\n\n" + s2)
				.get() + '\n';
	}
	
}
