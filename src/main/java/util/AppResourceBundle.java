package util;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Resources da aplicação.
 * 
 * @author (gabrielvra@outlook.com)
 *
 */
public class AppResourceBundle extends ResourceBundle {

	private static ResourceBundle instance;

	@Override
	public Enumeration<String> getKeys() {
		return null;
	}

	@Override
	protected Object handleGetObject(String arg0) {
		return null;
	}

	/**
	 * Cria e mantém a instância do resources
	 * @return Instância de {@link ResourceBundle}
	 */
	public static ResourceBundle getInstance() {
		if (instance == null) {
			instance = AppResourceBundle.getBundle("i18n/ApplicationResources");
		}
		return instance;
	}
}