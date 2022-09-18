package image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pojo.ImageCoordinatesLine;

/**
 * Armazena a imagem que foi selecionada.
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 */
public class ImageSelectedArea extends JPanel {

	private static final long serialVersionUID = 3L;

	private Graphics2D graphics2d;
	private BufferedImage bufferedImage;
	private Image image;
	private ImageCoordinatesLine currentLine;
	private final ImageStack<BufferedImage> undoStack = new ImageStack<BufferedImage>(15);
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (getCurrentLine() != null) {
			getCurrentLine().draw(getGraphics2d());
		}
		if (image != null)
			g.drawImage(image, 0, 0, this);
	}
	
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.graphics2d = null;
		this.bufferedImage = bufferedImage;
		setImage(bufferedImage);
	}
	
	private void setImage(Image image){
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		revalidate();
		repaint();
	}
	
	public Image getImage() {
		return image;
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	
	public Graphics2D getGraphics2d() {
		if (getBufferedImage() == null) {
			return null;
		} else if (graphics2d == null) {
			graphics2d = getBufferedImage().createGraphics();
			graphics2d.setStroke(new BasicStroke (3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float [] {1, 1}, 0));
	        graphics2d.setPaint(Color.RED);
	        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        graphics2d.setRenderingHints(hints);	        
	        
//		    float alpha = 2 * 0.1f;
//	        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha);
//	        graphics2d.setComposite(alcom);

		}
		return graphics2d;
	}
	
	public void newCurrentLine() {
		currentLine = new ImageCoordinatesLine();
	}
	
	public ImageCoordinatesLine getCurrentLine() {
		return currentLine;
	}
	
	/**
	 * Estrutura de armazenamento de imagens
	 * @return Instância de {@link ImageStack}
	 */
	public ImageStack<BufferedImage> getUndoStack() {
		return undoStack;
	}
	
	/**
	 * Clonar a imagem atual.
	 * @return Instância de {@link BufferedImage}
	 */
	private BufferedImage cloneImage() {
		if (getImage() != null) {
		    BufferedImage clonedImage = new BufferedImage(getBufferedImage().getWidth(), getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
		    Graphics g = clonedImage.createGraphics();
		    g.drawImage(getImage(), 0, 0, getBufferedImage().getWidth(), getBufferedImage().getHeight(), null);
		    return clonedImage;
		}
		return null;
	}	
	
	/**
	 * Armazena a imagem local.
	 */
	public void saveToStack() {
		getUndoStack().push(cloneImage());
	}
	
	public void undo() {
		currentLine = null;
	    if (getUndoStack().size() > 0) {
	        setBufferedImage(getUndoStack().pop());
	    }
	}
	
	public Boolean hasImage() {
		return getImage() != null;
	}
}