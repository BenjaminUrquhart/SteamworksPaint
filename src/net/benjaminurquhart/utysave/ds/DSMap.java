package net.benjaminurquhart.utysave.ds;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DSMap extends DSStruct {

	public final Map<Object, Object> map;
	
	public DSMap(ByteBuffer buff) {
		verifyHeader(buff, StructType.MAP, StructType.MAP_LEGACY);
		map = new HashMap<>();
		int len = buff.getInt();
		for(int i = 0; i < len; i++) {
			map.put(DSUtil.readValue(buff), DSUtil.readValue(buff));
		}
	}
}
