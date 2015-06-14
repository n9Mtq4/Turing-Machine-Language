package com.n9mtq4.lang.turing;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by will on 6/9/15 at 12:10 AM.
 */
public class TuringMachine {
	
	private File file;
	private LineNumberReader read;
	private DataRegister register;
	private int state;
	private int pos;
	private boolean halted;
	private int lineNumber;
	
	public TuringMachine(File file) throws FileNotFoundException {
		this.file = file;
		this.read = getNewReader();
		this.state = 0;
		this.pos = 0;
		this.halted = false;
		try {
			createRegister();
			insertData();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SyntaxError syntaxError) {
			syntaxError.printStackTrace();
		}
	}
	
	public DataRegister run() throws SyntaxError {
		
		while (!halted) {
			
			String[] rules = findRulesForState(state);
			if (rules.length > 2) throw new SyntaxError(rules[rules.length - 1], lineNumber, "there can only be two rules per state");
			try {
				
				for (int i = 0; i < rules.length; i++) {
					rules[i] = rules[i].replaceAll(" ", "");
				}
				
				for (String s : rules) {
					
					String[] tokens = s.split(":");
//					if
					int byteCheck = Integer.parseInt(tokens[0]);
					if (register.getAt(pos) == byteCheck) {
						
						if (!tokens[1].trim().equalsIgnoreCase("")) {
//							new value
							int newByte = Integer.parseInt(tokens[1]);
							register.setAt(pos, newByte);
						}
						
						if (!tokens[2].trim().equalsIgnoreCase("")) {
//							relative moving
							int relativeMove = Integer.parseInt(tokens[2]);
							pos += relativeMove;
						}
						
						if (tokens.length > 3 && !tokens[3].trim().equalsIgnoreCase("")) {
//							setting a new state
							int newState = Integer.parseInt(tokens[3]);
							if (newState == -1) this.halted = true;
							state = newState;
						}
						
						break; // break out of the rules, if one was true. prevents being moved to diff value
						
					}
					
				}
				
			}catch (Exception e1) {
				e1.printStackTrace();
				throw new SyntaxError(rules[rules.length - 1], lineNumber, e1.getMessage());
			}
			
		}
		
		return register;
		
	}
	
	private String aToS(String[] a) {
		String s = "[";
		for (String s1 : a) {
			s += s1 + ", ";
		}
		return s.substring(0, s.length() - 2) + "]";
	}
	
//	PARSING THE CODE
	private String[] findRulesForState(int state) throws SyntaxError {
		
		try {
			resetReader();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			String line;
			ArrayList<String> lines = new ArrayList<String>();
			while ((line = read.readLine()) != null) {
				if (line.startsWith(":" + state)) {
					
					String line1;
					while ((line1 = read.readLine()) != null && !(line1.startsWith(":")) && !line1.trim().equalsIgnoreCase("")) {
						lines.add(line1);
					}
					
				}
			}
			
			this.lineNumber = read.getLineNumber();
			resetReader();
			
			if (lines.size() == 0) throw new SyntaxError("no state with id " + state);
			return lines.toArray(new String[lines.size()]);
			
		}catch (IOException e) {
			throw new SyntaxError("no state with id " + state);
		}
		
	}
	
	private void createRegister() throws IOException, SyntaxError {
		
		int regSize = -1;
		String line;
		while ((line = read.readLine()) != null) {
			
			if (line.startsWith("reg ") && line.trim().split(" ").length == 2) {
				String sizeInString = line.trim().split(" ")[1];
				try {
					regSize = Integer.parseInt(sizeInString);
				}catch (NumberFormatException e) {
//					throw syntax error
					throw new SyntaxError(line, read.getLineNumber(), "register size is not an integer");
				}
			}
			
		}
		resetReader();
		
		if (regSize == -1) {
//			throw syntax error
			throw new SyntaxError("no register size specified");
		}
		
		register = new DataRegister(regSize);
		
	}
	
	private void insertData() throws IOException {
		
		String line;
		while ((line = read.readLine()) != null) {
			
			if (line.startsWith("in ")) {
				
				String in = line.split(" ")[1];
				char[] chars = in.toCharArray();
				int[] data = new int[chars.length];
				for (int i = 0; i < chars.length; i++) {
					data[i] = chars[i] == '0' ? 0 : 1;
				}
				System.out.println("INSERTING: " + in); //TODO: debug
				register.insertValues(data, 0);
				
			}
			
		}
		resetReader();
		
	}
	
	private void resetReader() throws FileNotFoundException {
		try {
			this.read.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		this.read = getNewReader();
	}
	
	private LineNumberReader getNewReader() throws FileNotFoundException {
		return new LineNumberReader(new FileReader(file));
	}
	
}
