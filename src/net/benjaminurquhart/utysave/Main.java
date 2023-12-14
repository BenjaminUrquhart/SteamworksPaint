package net.benjaminurquhart.utysave;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.JFrame;

import org.ini4j.Ini;

import net.benjaminurquhart.utysave.ds.DSGrid;
import net.benjaminurquhart.utysave.ds.DSUtil;

public class Main {
	
	public static Ini save;
	public static File file;
	public static DSGrid grid;
	public static JFrame frame;
	public static final int[] DEFAULT = {0, 0, 0, 0};
	public static final int[][] COLORS = {
			{213, 222, 231, 255},
			{137, 163, 187, 255},
			{62, 55, 207, 255},
			{40, 195, 72, 255},
			{237, 221, 53, 255},
			{207, 34, 67, 255},
			{141, 37, 17, 255},
			{68, 6, 6, 255}
	};

	public static void main(String[] args) throws Exception {
		file = new File(System.getenv("LOCALAPPDATA") + "/Undertale_Yellow/Save.sav");
		save = new Ini(file);
		String idData = save.get("SworksFlags", "sworks_id");
		idData = idData.substring(1, idData.length() - 1);
		ByteBuffer buff = ByteBuffer.wrap(DSUtil.decodeHexString(idData));
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff.getInt();
		grid = new DSGrid(buff);
		
		frame = new JFrame("Steamworks ID Manager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.add(UI.getInstance());
		
		frame.pack();
		frame.setResizable(false);
		
		frame.requestFocus();
		frame.setVisible(true);
		
		while(true);
	}
}
