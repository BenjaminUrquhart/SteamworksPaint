package net.benjaminurquhart.utysave;

import javax.swing.JFrame;

public class Main {
	
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
