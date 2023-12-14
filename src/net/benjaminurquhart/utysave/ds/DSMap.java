package net.benjaminurquhart.utysave.ds;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DSMap {

	public final Map<Object, Object> map;
	
	public DSMap(ByteBuffer buff) {
		map = new HashMap<>();
		int len = buff.getInt();
		for(int i = 0; i < len; i++) {
			map.put(DSUtil.readValue(buff), DSUtil.readValue(buff));
		}
	}
}
