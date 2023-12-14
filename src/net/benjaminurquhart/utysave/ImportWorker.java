package net.benjaminurquhart.utysave;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import net.benjaminurquhart.utysave.ds.DSGrid;

public class ImportWorker extends SwingWorker<Void, Void> {
	
	private DSGrid grid;
	private File file;
	
	public ImportWorker(File file, DSGrid grid) {
		this.file = file;
		this.grid = grid;
	}

	@Override
	protected Void doInBackground() throws Exception {
		UI ui = UI.getInstance();
		try {
			ui.progressBar.setIndeterminate(true);
			ui.progressBar.setString("Converting...");
			renderToGrid(ImageIO.read(file), grid);
			ui.imageView.setImage(ExportWorker.render(grid));
			ui.onFinish(null);
		}
		catch(Exception e) {
			ui.onFinish(e);
		}
		return null;
	}
	
	public static void renderToGrid(BufferedImage img, DSGrid grid) {
		BufferedImage image = new BufferedImage(grid.width, grid.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(img.getScaledInstance(grid.width, grid.height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
		g.dispose();
		Map<Integer, Integer> colorMaps = new HashMap<>();
		int pixel;
		for(int x = 0; x < grid.width; x++) {
			for(int y = 0; y < grid.height; y++) {
				pixel = image.getRGB(x, y);
				grid.grid[y][x] = (pixel >> 24) == 0 ? 0 : colorMaps.computeIfAbsent(pixel, p -> {
					int best = 0, tmp;
					int bestDist = Integer.MAX_VALUE;
					for(int i = 0; i < Main.COLORS.length; i++) {
						tmp = getRGBDist(p, (Main.COLORS[i][0]<<16) | (Main.COLORS[i][1]<<8) | (Main.COLORS[i][2]));
						if(tmp < bestDist) {
							bestDist = tmp;
							best = i + 1;
						}
					}
					return best;
				});
			}
		}
	}
	
	// Don't judge this too hard I copied it from an old project
	private static int getRGBDist(int a, int b) {
		int redA = (a>>16)&0xff;
		int greenA = (a>>8)&0xff;
		int blueA = a&0xff;
		
		int redB = (b>>16)&0xff;
		int greenB = (b>>8)&0xff;
		int blueB = b&0xff;
		
		return (int)(Math.pow(redA-redB, 2)+Math.pow(greenA-greenB, 2)+Math.pow(blueA-blueB, 2));
	}
}
