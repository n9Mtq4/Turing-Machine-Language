package com.n9mtq4.lang.turing;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by will on 6/8/15 at 11:49 PM.
 */
public class Debug {
	
	public static void main(String[] args) {
		
		File file = new File("/Users/will/Desktop/turingmachine.tur");
		try {
			
			TuringMachine machine = new TuringMachine(file);
			
			DataRegister reg = machine.run();
			
			String s = "";
			for (int i : reg.data) {
				s += i;
			}
			System.out.println(s);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
