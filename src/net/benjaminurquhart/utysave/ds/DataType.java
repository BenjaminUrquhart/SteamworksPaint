package net.benjaminurquhart.utysave.ds;

public enum DataType {
	REAL,
	STRING;
	
	public static DataType from(int id) {
		if(id == 13) { // Man idk
			return REAL;
		}
		return values()[id];
	}
}
