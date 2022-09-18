package util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filtro de imagem da aplicação. Serão aceitos somente JPG.
 * 
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 */
public class AppImageFileFilter extends FileFilter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		String name = file.getName();
		int i = name.lastIndexOf('.');
		if (i > 0 && i < name.length()-1) {
			String ext = name.substring(i + 1).toLowerCase();
			if (ext.equals("jpg") || ext.equals("jpeg")) {
				return true;
			}
		}
      	return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return AppResourceBundle.getInstance().getString("application.file.format.description") + " (*.jpg)";
	}
}