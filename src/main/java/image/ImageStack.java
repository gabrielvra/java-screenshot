package image;

import java.util.Stack;

/**
 * Armazena o histórico das imagens a cada alteração.
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 * @param <T> Tipo do objeto armazenado.
 */
public class ImageStack<T> extends Stack<T> {

	private final int maxSize;
	private static final long serialVersionUID = 4L;

	/**
	 * Construtor da estrutura de armazenamento
	 * @param size - Tamanho máximo de objetos armazenados
	 */
	public ImageStack(int size) {
	    super();
	    this.maxSize = size;
	}

	@Override
	public T push(T object) {
		//Remover o primeiro objeto caso ultrapassar o número máximo de objetos armazenados.
	    while (this.size() > maxSize) {
	        this.remove(0);
	    }
	    return super.push(object);
	}
}