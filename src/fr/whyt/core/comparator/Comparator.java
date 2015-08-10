/**
 * 
 */
package fr.whyt.core.comparator;

import java.util.ArrayList;

import fr.whyt.core.comparator.ComparatorException.ComparatorExceptionType;
import fr.whyt.core.srt.SRTFile;
import fr.whyt.core.srt.Sub;


/**
 * @author Jeremy
 *
 */
public class Comparator {
	
	public enum Display {
		CLASSIC,
		LEGACY;
	}
	
	private final SRTFile current;
	private final SRTFile base;
	
	public Comparator(SRTFile current, SRTFile base) {
		this.current = current;
		this.base = base;
	}
	
	public SRTFile getCurrent() {
		return this.current;
	}
	
	public SRTFile getBase() {
		return this.base;
	}
	
	public ComparatorErrorLog validate() {
		ArrayList<Sub> current_subs = current.getSubs();
		ArrayList<Sub> base_subs = base.getSubs();
		
		ArrayList<ComparatorException> exceptions = new ArrayList<ComparatorException>();
		
		if( current_subs.size() != base_subs.size() ) {
			exceptions.add(new ComparatorException(
					"Current subs quantity (" + current_subs.size() + ") does not match with base subs quantity (" + base_subs.size() + ")",
					null, null, ComparatorExceptionType.SUB_QUANTITY));
		}
		
		int size = current_subs.size();
		for(int i=0; i<size; i++) {
			Sub current_sub = current_subs.get(i);
			Sub base_sub = base_subs.get(i);
			
			if( current_sub.getNumber() != base_sub.getNumber() ) {
				exceptions.add(new ComparatorException(
						"Current subs number (" + current_sub.getNumber() + ") does not match with base subs number (" + base_sub.getNumber() + ")",
						current_sub, base_sub, ComparatorExceptionType.SUB_NUMBER));
			}
			
			if( !current_sub.getStartTime().equals(base_sub.getStartLine()) ) {
				exceptions.add(new ComparatorException(
						"Current subs start time (" + current_sub.getStartTime().format(Sub.dtf) + ") does not match with base subs start time (" + base_sub.getStartTime().format(Sub.dtf) + ")",
						current_sub, base_sub, ComparatorExceptionType.START_TIME));
			}
			if( !current_sub.getEndTime().equals(base_sub.getEndTime()) ) {
				exceptions.add(new ComparatorException(
						"Current subs end time (" + current_sub.getEndTime().format(Sub.dtf) + ") does not match with base subs end time (" + base_sub.getEndTime().format(Sub.dtf) + ")",
						current_sub, base_sub, ComparatorExceptionType.END_TIME));
			}
		}
		
		return new ComparatorErrorLog(this.current.getFile(), this.base.getFile(), exceptions);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Comparator
				&& ((Comparator) obj).current.equals(this.current)
				&& ((Comparator) obj).base.equals(this.base);
	}
	
	/**
	 * Call {@linkplain Comparator#toString(Display) toString(Display)} with the
	 * {@linkplain Comparator.Display#CLASSIC CLASSIC} default argument for Display param.
	 * 
	 * @see Comparator.Display 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.toString(Comparator.Display.CLASSIC);
	}
	
	/**
	 * Display the current SRT file close to the base SRT file.<br>
	 * Each sub is aligned with its equivalent
	 * @param display the type of display used to display the SRT files.
	 * @return the formatted string
	 */
	public String toString(Comparator.Display display) {
		StringBuilder sb = new StringBuilder();
		
		SRTFile left_srt_file = display == Comparator.Display.CLASSIC ? current : base;
		SRTFile right_srt_file = display == Comparator.Display.LEGACY ? current : base;
		String left_title = display == Comparator.Display.CLASSIC ? "Current" : "Base";
		String right_title = display == Comparator.Display.LEGACY ? "Current" : "Base";
		
		int offset = 64;
		
		sb.append(left_title).append( padRight(offset-left_title.length()) ).append(right_title).append('\n');
		sb.append( hr(offset + right_title.length()) );
		
		ArrayList<Sub> left_subs = left_srt_file.getSubs();
		ArrayList<Sub> right_subs = right_srt_file.getSubs();
		int length = Math.min(left_subs.size(), right_subs.size());
		
		for(int i=0; i<length; i++) {
			Sub left = left_subs.get(i);
			Sub right = right_subs.get(i);
			
			String left_number = "" + left.getNumber();
			String left_timer = left.getStartTime().format(Sub.dtf) + " --> " + left.getEndTime().format(Sub.dtf);
			ArrayList<String> left_substrings = left.getSubStrings();
			
			String right_number = "" + right.getNumber();
			String right_timer = right.getStartTime().format(Sub.dtf) + " --> " + right.getEndTime().format(Sub.dtf);
			ArrayList<String> right_substrings = right.getSubStrings();
			
			sb.append(left_number).append( padRight(offset - left_number.length()) ).append(right_number).append('\n');
			sb.append(left_timer).append( padRight(offset - left_timer.length()) ).append(right_timer).append('\n');
			int min_substrings = Math.min(left_substrings.size(), right_substrings.size());
			ArrayList<String> max_substrings = Math.max(left_substrings.size(), right_substrings.size()) == left_substrings.size() ? left_substrings : right_substrings;
			for(int j=0; j<min_substrings; j++) {
				sb.append(left_substrings.get(j)).append( padRight(offset - left_substrings.get(j).length()) ).append(right_substrings.get(j)).append('\n');
			}
			for(int j=0; j<max_substrings.size(); j++) {
				if( Math.max(left_substrings.size(), right_substrings.size()) == left_substrings.size() ) {
					sb.append( padRight(offset) );
				}
				sb.append(max_substrings.get(j)).append('\n');
			}
			sb.append('\n');
		}
		
		return sb.append('\n').toString();
	}
	
	private String hr(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<length; i++) sb.append('-');
		return sb.append('\n').toString();
	}
	
	private String padRight(int offset) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<offset; i++) sb.append(' ');
		return sb.append('\n').toString();
	}
}
