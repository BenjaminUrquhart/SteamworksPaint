package net.benjaminurquhart.utysave.ds;

import java.nio.ByteBuffer;

public class DSList {

	public final Object[] values;
	
	public DSList(ByteBuffer buff) {
		values = new Object[buff.getInt()];
		for(int i = 0; i < values.length; i++) {
			values[i] = DSUtil.readValue(buff);
		}
	}
}
