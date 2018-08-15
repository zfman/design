package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class DrawPanel extends JPanel{
	private Graphics g;
	
	private BufferedImage bufferedImage=null;
	
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		if(bufferedImage!=null)
		setPreferredSize(new Dimension(this.bufferedImage.getWidth(), this.bufferedImage.getHeight()));
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(bufferedImage==null) g.fillRect(0, 0, getWidth(), getHeight());
		else g.drawImage(bufferedImage,0,0,Color.white, null);
	}
}
