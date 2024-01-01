package net.benjaminurquhart.utysave;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import net.benjaminurquhart.utysave.ds.DSGrid;

public class ExportWorker extends SwingWorker<Void, Void> {

	private File file;
	private DSGrid grid;
	
	public ExportWorker(File file, DSGrid grid) {
		this.grid = grid;
		this.file = file;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		UI ui = UI.getInstance();
		try {
			ImageIO.write(render(grid), "png", file);
			ui.onFinish(null);
			ui.progressBar.setString("Saved to " + file.getAbsolutePath());
		}
		catch(Exception e) {
			ui.onFinish(e);
		}
		return null;
	}
	
	public static BufferedImage render(DSGrid grid) {
		BufferedImage img = new BufferedImage(grid.width, grid.height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = img.getRaster();
		int color;
		for(int y = 0; y < grid.height; y++) {
			for(int x = 0; x < grid.width; x++) {
				if(grid.grid[y][x] instanceof Number n) {
					color = n.intValue();
				}
				else {
					color = 0;
				}
				if(color > 0) {
					raster.setPixel(x, y, Main.COLORS[color - 1]);
				}
				else {
					raster.setPixel(x, y, Main.DEFAULT);
				}
			}
		}
		return img;
	}
}
