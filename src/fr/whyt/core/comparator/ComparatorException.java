/**
 * 
 */
package fr.whyt.core.comparator;

import fr.whyt.core.srt.Sub;



/**
 * @author Jeremy
 *
 */
public class ComparatorException extends Exception {

	private static final long	serialVersionUID	= 3875047571336452019L;

	public enum ComparatorExceptionType {
		SUB_QUANTITY,
		SUB_NUMBER,
		START_TIME,
		END_TIME;
	}
	
	private final Sub current;
	private final Sub base;
	private final ComparatorExceptionType exception;
	
	public ComparatorException(Sub current, Sub base, ComparatorExceptionType exception) {
		this.current = current;
		this.base = base;
		this.exception = exception;
	}
	
	public ComparatorException(String message, Sub current, Sub base, ComparatorExceptionType exception) {
		super(message);
		this.current = current;
		this.base = base;
		this.exception = exception;
	}
	
	public ComparatorException(Throwable throwable, Sub current, Sub base, ComparatorExceptionType exception) {
		super(throwable);
		this.current = current;
		this.base = base;
		this.exception = exception;
	}
	
	public ComparatorException(String message, Throwable throwable, Sub current, Sub base, ComparatorExceptionType exception) {
		super(message, throwable);
		this.current = current;
		this.base = base;
		this.exception = exception;
	}
	
	public Sub getCurrentSub() {
		return this.current;
	}
	
	public Sub getBaseSub() {
		return this.base;
	}
	
	public ComparatorExceptionType getExceptionType() {
		return this.exception;
	}
	
	@Override
	public String toString() {
		return super.toString() + " - <" + this.exception + ">" + 
				(this.current == null && this.base == null ?
						"" :
						" between:\n" + this.current + "\n\tand\n" + this.base);
	}
	
}
