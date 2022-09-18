package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import pojo.ScreenCapturePojo;

/**
 * Captura parcial da tela do usuário.
 * Um frame fullscreem com opacidade para permitir o usuário selecionar uma parte da tela.
 * Uma ferramenta de seleção de área com o mouse foi desenvolvida para armazenar as coordenadas da seleção da imagem.
 * 
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 */
public class TranslucentWindowCapture extends JFrame {

	private static final long serialVersionUID = 2L;

	private BasicStroke basicStroke;	
	private GradientPaint gradientPaint;
	private Rectangle selectionArea;
	private ScreenCapturePojo pojo;
	private Application reference;
	   
	/**
	 * Construtor da tela de captura parcial.
	 */
	public TranslucentWindowCapture() {
        super("Captura de tela");
        setLayout(new GridBagLayout());
        Toolkit tk = Toolkit.getDefaultToolkit();  
        int width = ((int) tk.getScreenSize().getWidth());  
        int height = ((int) tk.getScreenSize().getHeight());  
        setBounds(0, 0, width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        //Registra atalho ESC para fechar a tela.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent e) {
                System.out.println(e);
                if(e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dispose();
                    return true;
                }
                return false;
            }
        });
        //Registra o evento de início da seleção da área de trabalho.
        addMouseListener(new MouseAdapter() {
        	public void mousePressed (MouseEvent e) {
        		getPojo().setStartx(e.getX());
        		getPojo().setStarty(e.getY());
        		getPojo().setWidth(e.getX());
        		getPojo().setEndy(e.getY());
        		setIgnoreRepaint(false);
                repaint();
            }
        });
        //Registra o evento de fim da seleção da área de trabalho.
        addMouseMotionListener(new MouseMotionAdapter () {
        	public void mouseDragged (MouseEvent e) {
				getPojo().setWidth(e.getX());
				getPojo().setEndy(e.getY());
                setIgnoreRepaint(false);
                repaint();
			}
        });
        //Registra o fim da seleção e finaliza o formulário.
        addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		super.mouseReleased(e);
        		dispose();
        		getReference().captureImage(getPojo());
        		getReference().setVisible(true);
        	}
        });
    }
	
	/**
	 * Inicializa e retorna a instância única de {@link GradientPaint} 
	 * @return Instância de {@link GradientPaint}
	 */
	public GradientPaint getGradientPaint() {
		if (gradientPaint == null) {
			gradientPaint = new GradientPaint(0.0f, 0.0f, Color.BLUE, 1.0f, 1.0f, Color.white, true);
		}
		return gradientPaint;
	}
	
	/**
	 * Inicializa e retorna a instância única de {@link BasicStroke}
	 * @return Instância de {@link BasicStroke}
	 */
	public BasicStroke getBasicStroke() {
		if (basicStroke == null) {
			basicStroke = new BasicStroke (3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float [] {2, 2}, 0);
		}
		return basicStroke;
	}
	
	/**
	 * Inicializa o retângulo de seleção.
	 * @return Instância de {@link Rectangle}
	 */
	public Rectangle getSelectionArea() {
		if (selectionArea == null) {
			selectionArea = new Rectangle();
		}
		return selectionArea;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (getPojo().canCreate()){
			getSelectionArea().x = getPojo().getX();
			getSelectionArea().y = getPojo().getY();
			getSelectionArea().width = getPojo().getWidth();
			getSelectionArea().height = getPojo().getHeight();
			
			//Desenhar o componente de seleção.
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(getBasicStroke());
			g2d.setPaint(getGradientPaint());
			g2d.draw(getSelectionArea());			
		}
	}
	
	/**
	 * Retorna o pojo com o conteúdo da área selecionada.
	 * @return Instância de {@link ScreenCapturePojo}
	 */
	public ScreenCapturePojo getPojo() {
		if (pojo == null) {
			pojo = new ScreenCapturePojo(false);
		}
		return pojo;
	}
	
	/**
	 * Seta um pojo
	 * @param pojo
	 */
	public void setPojo(ScreenCapturePojo pojo) {
		this.pojo = pojo;
	}
	
	/**
	 * Seta uma referência da tela da aplicação.
	 * @param reference
	 */
	public void setReference(Application reference) {
		this.reference = reference;
	}
	
	/**
	 * Retorna a referência da tela principal
	 * @return Instância de {@link Application}
	 */
	public Application getReference() {
		return reference;
	}
}