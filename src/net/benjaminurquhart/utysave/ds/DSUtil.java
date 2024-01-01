package net.benjaminurquhart.utysave.ds;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import com.google.common.io.LittleEndianDataOutputStream;

// A bit overkill for something that's only supposed to be reading and writing
// doubles, but idk what people are doing to their saves nowadays.
// Besides, might as well document this format, even though it's pretty simple.
public class DSUtil {

	public static Object readValue(ByteBuffer buff) {
		DataType type = DataType.from(buff.getInt());
		switch(type) {
		case STRING: {
			byte[] bytes = new byte[buff.getInt()];
			buff.get(bytes);
			return new String(bytes);
		}
		
		case ARRAY: {
			Object[] out = new Object[buff.getInt()];
			for(int i = 0; i < out.length; i++) {
				out[i] = readValue(buff);
			}
			return out;
		}
		
		case REAL:
		case BOOLEAN: return buff.getDouble();
		
		case INT64:
		case POINTER:
		case REFERENCE: return buff.getLong();
		
		case INT32: return buff.getInt();
		
		case UNDEFINED: return null;
		
		default: {
			System.err.println("WARINING: attempted deserialization for unsupported type " + type + ", treating as undefined");
			return null;
		}
		}
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
		return sb.toString().toUpperCase();
	}
	
	public static void writePtr(LittleEndianDataOutputStream out, long ptr) throws IOException {
		out.writeInt(DataType.POINTER.ordinal());
		out.writeLong(ptr);
	}
	
	public static void writeRef(LittleEndianDataOutputStream out, long ref) throws IOException {
		out.writeInt(DataType.REFERENCE.ordinal());
		out.writeLong(ref);
	}
	
	public static void writeValue(LittleEndianDataOutputStream out, Object obj) throws IOException {
		if(obj == null) {
			out.writeInt(DataType.UNDEFINED.ordinal());
		}
		else if(obj instanceof Boolean b) {
			out.writeInt(DataType.BOOLEAN.ordinal());
			out.writeDouble(Double.doubleToRawLongBits(b.booleanValue() ? 1 : 0));
		}
		else if(obj instanceof Number n) {
			if(obj instanceof Long) {
				out.writeInt(DataType.INT64.ordinal());
				out.writeLong(n.longValue());
			}
			else if(obj instanceof Integer) {
				out.writeInt(DataType.INT32.ordinal());
				out.writeInt(n.intValue());
			}
			else {
				out.writeInt(DataType.REAL.ordinal());
				out.writeLong(Double.doubleToRawLongBits(n.doubleValue()));
			}
		}
		else if(obj.getClass().isArray()) {
			int len = Array.getLength(obj);
			out.writeInt(DataType.ARRAY.ordinal());
			out.writeInt(len);
			for(int i = 0; i < len; i++) {
				writeValue(out, Array.get(obj, i));
			}
		}
		else {
			byte[] val = String.valueOf(obj).getBytes();
			out.writeInt(DataType.STRING.ordinal());
			out.writeInt(val.length);
			out.write(val);
		}
	}
}
