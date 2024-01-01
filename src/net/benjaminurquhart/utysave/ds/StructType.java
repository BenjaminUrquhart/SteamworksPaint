package net.benjaminurquhart.utysave.ds;

public enum StructType {

	MAP(0x0193),
	MAP_LEGACY(0x0192),
	
	LIST(0x012F),
	LIST_LEGACY(0x012E),
	
	GRID(0x025B);
	
	private final int header;
	
	private StructType(int header) {
		this.header = header;
	}
	
	public int header() {
		return header;
	}
	
	public static StructType from(int header) {
		for(StructType type : values()) {
			if(type.header == header) {
				return type;
			}
		}
		throw new IllegalArgumentException(String.format("No type with header 0x%04x", header));
	}
}
