package net.benjaminurquhart.utysave.ds;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.common.io.LittleEndianDataOutputStream;

public class DSGrid {
	
	public final int width, height;
	public final Object[][] grid;
	
	public DSGrid(ByteBuffer buff) {
		width = buff.getInt();
		height = buff.getInt();
		grid = new Object[height][width];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				grid[y][x] = DSUtil.readValue(buff);
			}
		}
	}
	
	public String serialize() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(bytes);
		out.writeInt(0x025b);
		out.writeInt(width);
		out.writeInt(height);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				DSUtil.writeValue(out, grid[y][x]);
			}
		}
		return DSUtil.toHexString(bytes.toByteArray());
	}
}
