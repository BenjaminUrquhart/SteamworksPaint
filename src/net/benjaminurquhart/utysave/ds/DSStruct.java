package net.benjaminurquhart.utysave.ds;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class DSStruct {

	protected StructType verifyHeader(int header, StructType... types) {
		StructType type = StructType.from(header);
		for(StructType t : types) {
			if(type == t) {
				return t;
			}
		}
		throw new IllegalArgumentException(String.format("%s is not one of %s", type, Arrays.toString(types)));
	}
	
	protected StructType verifyHeader(ByteBuffer buff, StructType... types) {
		return verifyHeader(buff.getInt(), types);
	}
}
