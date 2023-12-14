package net.benjaminurquhart.utysave;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -180492044351040270L;
	private static final BufferedImage BG;
	private static final Dimension SIZE;
	private static final int SCALE = 2;
	
	static {
		BufferedImage bgImage = null;
		Dimension size = null;
		try {
			bgImage = ImageIO.read(ImagePanel.class.getResource("/spr_crayon_id.png"));
			size = new Dimension(bgImage.getWidth() * SCALE, bgImage.getHeight() * SCALE);
		}
		catch(Exception e) {
			e.printStackTrace();
			size = new Dimension(208, 147);
		}
		SIZE = size;
		BG = bgImage;
	}
	
	private boolean drawBG;
	private Image image;
	
	public ImagePanel() {
		this.setPreferredSize(SIZE);
		this.setMaximumSize(SIZE);
		this.setMinimumSize(SIZE);
		this.setSize((int)SIZE.getWidth(), (int)SIZE.getHeight());
		this.drawBG = true;
	}
	
	public void setImage(Image image) {
		this.image = image;
		this.repaint();
	}
	
	public Image getImage() {
		return image;
	}
	
	public void drawBackground(boolean drawBG) {
		this.drawBG = drawBG;
	}
	
	public boolean drawBackground() {
		return drawBG;
	}
	
	@Override
	public void paint(Graphics g) {
		if(BG != null && drawBG) {
			g.drawImage(BG, 0, 0, (int)SIZE.getWidth(), (int)SIZE.getHeight(), null);
		}
		if(image != null) {
			g.drawImage(image, 0, 0, (int)SIZE.getWidth(), (int)SIZE.getHeight(), null);
		}
		g.dispose();
	}
}
