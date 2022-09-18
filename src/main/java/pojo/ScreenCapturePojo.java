package pojo;

import java.awt.Toolkit;

/**
 * Armazenar a área obtida na seleção do usuário.
 * 
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 */
public class ScreenCapturePojo {

	private int x;
	private int y;
	private int endx;
	private int endy;
	private boolean fullscreen;
	
	/**
	 * Contrutor padrão
	 * Se for fullscreen, inicializa o pojo com o tamanho total da tela.
	 * @param fullscreen
	 */
	public ScreenCapturePojo(boolean fullscreen) {
		this.fullscreen = fullscreen;
		if (fullscreen) {
	        Toolkit tk = Toolkit.getDefaultToolkit();  
	        this.x = 0;
	        this.y = 0;
	        this.endx = (int) tk.getScreenSize().getWidth();
	        this.endy = (int) tk.getScreenSize().getHeight();
		}
	}
	
	/**
	 * Início da seleção X
	 * @return Posição X inicial
	 */
	public int getStartx() {
		return x;
	}
	
	/**
	 * Seta o início de X
	 * @param startx
	 */
	public void setStartx(int startx) {
		this.x = startx;
	}
	
	/**
	 * Início da seleção Y
	 * @return Posição Y inicial
	 */
	public int getStarty() {
		return y;
	}
	
	/**
	 * Seta o início de Y
	 * @param starty
	 */
	public void setStarty(int starty) {
		this.y = starty;
	}
	
	/**
	 * Fim da seleção de X
	 * @return
	 */
	public int getEndx() {
		return endx;
	}
	
	/**
	 * Seta o fim da seleção de X
	 * @param endx
	 */
	public void setWidth(int endx) {
		this.endx = endx;
	}
	
	/**
	 * Fim da seleção de Y
	 * @return
	 */
	public int getEndy() {
		return endy;
	}
	
	/**
	 * Seta o fim da seleção de Y
	 * @param endy
	 */
	public void setEndy(int endy) {
		this.endy = endy;
	}
	
	/**
	 * Verifica se os pontos são válidos para criar uma imagem.
	 * @return Instância de {@link Boolean}
	 */
	public Boolean canCreate() {
		return getStartx() != getEndx() || getStarty() != getEndy();
	}
	
	/**
	 * Um ponto na coordenada X
	 * @return Coordenada X
	 */
	public int getX() {
		return (getStartx() < getEndx()) ? getStartx() : getEndx();
	}
	
	/**
	 * Um ponto na coordenada Y
	 * @return Coordenada Y do mouse
	 */
	public int getY() {
		return (getStarty() < getEndy()) ? getStarty() : getEndy();
	}
	
	/**
	 * Retorna a largura da imagem
	 * @return Uma largura.
	 */
	public int getWidth() {
		int x2 = (getStartx() > getEndx()) ? getStartx() : getEndx();
		return (x2 - getX()) + 1;
	}
	
	/**
	 * Retorna o tamanho da altura da imagem.
	 * @return Uma altura.
	 */
	public int getHeight() {
		int y2 = (getStarty() > getEndy()) ? getStarty() : getEndy();
		return (y2 - getY()) + 1;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
}