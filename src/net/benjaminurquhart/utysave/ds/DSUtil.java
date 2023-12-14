package net.benjaminurquhart.utysave.ds;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.common.io.LittleEndianDataOutputStream;

public class DSUtil {

	public static Object readValue(ByteBuffer buff) {
		DataType type = DataType.from(buff.getInt());
		if(type == DataType.REAL) {
			return buff.getDouble();
		}
		
		byte[] bytes = new byte[buff.getInt()];
		buff.get(bytes);
		return new String(bytes);
	}
	
	public static byte[] decodeHexString(String s) {
		int len = s.length();
		if(len % 2 == 1) {
			throw new IllegalArgumentException("String length must be even (" + len + ")");
		}
		byte[] bytes = new byte[len / 2];
		for(int i = 0; i < len; i += 2) {
			bytes[i / 2] = (byte)Integer.parseInt(s.substring(i, i + 2), 16);
		}
		return bytes;
	}
	
	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	public static void writeValue(LittleEndianDataOutputStream out, Object obj) throws IOException {
		if(obj instanceof Number n) {
			out.writeInt(0);
			out.writeLong(Double.doubleToRawLongBits(n.doubleValue()));
		}
		else {
			byte[] val = String.valueOf(obj).getBytes();
			out.writeInt(1);
			out.writeInt(val.length);
			out.write(val);
		}
	}
}
