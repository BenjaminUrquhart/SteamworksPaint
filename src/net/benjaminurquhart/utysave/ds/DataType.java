package net.benjaminurquhart.utysave.ds;

// https://github.com/YoYoGames/GMEXT-Steamworks/blob/main/source/Steamworks_vs/Steamworks/YYRValue.h
public enum DataType {
	REAL,
	STRING,
	ARRAY,
	POINTER,
	VEC3,
	UNDEFINED,
	STRUCT,
	INT32,
	VEC4,
	VEC44,
	INT64,
	ACCESSOR,
	NULL,
	BOOLEAN,
	ITERATOR,
	REFERENCE;
	
	public static DataType from(int id) {
		return values()[id];
	}
}
