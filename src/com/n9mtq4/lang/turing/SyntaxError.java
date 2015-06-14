package com.n9mtq4.lang.turing;

/**
 * Created by will on 6/8/15 at 11:25 PM.
 */
public class SyntaxError extends Exception {
	
	public SyntaxError(String message) {
		super("Syntax Error: " + message);
	}
	
	public SyntaxError(String lineText, int lineNumber, String message) {
		
		super("Syntax Error at line " + lineNumber + "(" + lineText + "): " + message);
		
	}
	
}
