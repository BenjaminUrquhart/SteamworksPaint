package net.benjaminurquhart.utysave.ds;

import java.nio.ByteBuffer;

public class DSList extends DSStruct {

	public final Object[] values;
	
	public DSList(ByteBuffer buff) {
		verifyHeader(buff, StructType.LIST, StructType.LIST_LEGACY);
		values = new Object[buff.getInt()];
		for(int i = 0; i < values.length; i++) {
			values[i] = DSUtil.readValue(buff);
		}
	}
}
