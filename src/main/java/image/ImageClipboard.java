package image;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Armazena o conteúdo para a área de transferência.
 * 
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 */
public class ImageClipboard implements Transferable, ClipboardOwner {
	 
    private Image image;
 
    /**
     * Construtor padrão
     * @param image - Instância de {@link Image}
     */
    public ImageClipboard(Image image) {
        this.image = image;
    }

    /**
     * {@inheritDoc}
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(DataFlavor.imageFlavor) && image != null) {
            return image;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
 
    /**
     * {@inheritDoc}
     */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = DataFlavor.imageFlavor;
        return flavors;
    }
 
    /**
     * {@inheritDoc}
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
 
    /**
     * {@inheritDoc}
     */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
 
    }
}