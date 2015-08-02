/**
 * 
 */
package fr.whyt.validator;

import fr.whyt.srt.Sub;


/**
 * @author Jeremy
 *
 */
public class SRTException extends Exception {

	private static final long	serialVersionUID	= -3929889176645089032L;
	
	private final int line;
	private final int offset;
	private final Sub sub;
	
	public SRTException(int line, int offset, Sub sub) {
		this.line 	= line;
		this.offset = offset;
		this.sub 	= sub;
	}
	
	public SRTException(String message, int line, int offset, Sub sub) {
		super(message);
		this.line 	= line;
		this.offset = offset;
		this.sub	= sub;
	}
	
	public SRTException(Throwable throwable, int line, int offset, Sub sub) {
		super(throwable);
		this.line 	= line;
		this.offset = offset;
		this.sub	= sub;
	}
	
	public SRTException(String message, Throwable throwable, int line, int offset, Sub sub) {
		super(message, throwable);
		this.line 	= line;
		this.offset = offset;
		this.sub	= sub;
	}
	
	public int getLine() {
		return this.line;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public Sub getSub() {
		return this.sub;
	}
	
	@Override
	public String toString() {
		return super.toString() + " at line " + this.line + "+" + this.offset + ":\n"
				+ this.sub;
	}
	
}
