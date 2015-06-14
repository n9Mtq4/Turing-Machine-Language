package com.n9mtq4.lang.turing;

/**
 * Created by will on 6/8/15 at 10:52 PM.
 */
public class DataRegister {
	
	public int[] data;
	
	public DataRegister(int pTwo) {
		this.data = new int[1 << pTwo];
		System.out.println("REGISTER SIZE: " + data.length); //TODO: debug
	}
	
	public int getAt(int pos) {
		return data[pos];
	}
	
	public void setAt(int pos, int value) {
		data[pos] = value;
	}
	
	public void insertValues(int[] insert, int offSet) {
		System.arraycopy(insert, 0, data, offSet, insert.length);
	}
	
}
