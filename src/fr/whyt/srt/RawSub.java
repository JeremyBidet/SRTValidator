/**
 * 
 */
package fr.whyt.srt;

import java.util.ArrayList;



/**
 * @author Jeremy
 *
 */
public class RawSub {

	private int start_line;
	
	private String number;
	private String timer;
	private ArrayList<String> sub_strings;
	
	private String sub;
	
	
	public RawSub(int start_line, String number, String timer, ArrayList<String> sub_strings, String sub) {
		this.start_line = start_line;
		
		this.number = number;
		this.timer = timer;
		this.sub_strings = sub_strings;
		
		assert(sub.equals(number+'\n'+timer+'\n'+sub_strings));
		this.sub = sub;
	}

	
	public int getStartLine() {
		return this.start_line;
	}
	
	public String getNumber() {
		return this.number;
	}
	
	public String getTimer() {
		return this.timer;
	}
	
	public ArrayList<String> getSubStrings() {
		return this.sub_strings;
	}
	
	public String getFormattedSubStrings() {
		return this.sub_strings.stream().reduce((s1,  s2) -> s1 + '\n' + s2).get() + '\n';
	}
	
	public String getSub() {
		return this.sub;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RawSub
				&& ((RawSub) obj).start_line == this.start_line
				&& ((RawSub) obj).number.equals(this.number)
				&& ((RawSub) obj).timer.equals(this.timer)
				&& ((RawSub) obj).sub_strings.equals(this.sub_strings)
				&& ((RawSub) obj).sub.equals(this.sub);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.start_line)		.append('|').append(this.number).append('\n');
		sb.append((this.start_line+1))	.append('|').append(this.timer)	.append('\n');
		
		int subs_line = this.start_line+2;
		for(String sub : this.sub_strings) {
			sb.append(subs_line++).append('|').append(sub).append('\n');
		}
		
		return sb.toString();
	}
	
}
