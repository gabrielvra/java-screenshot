package view;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import image.ImageClipboard;
import image.ImageSelectedArea;
import pojo.ScreenCapturePojo;
import pojo.SelectedTool;
import util.AppImageFileFilter;
import util.AppResourceBundle;

/**
 * Tela principal da aplicação.
 * 
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 */
public class Application  extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static Application INSTANCE;
	
	public static Robot robot;
	private ImageSelectedArea imageSelectedArea;

	private JScrollPane scrollPane;
	private JFileChooser fileChooserSave;
	
	private JButton[] buttons;
	
	private SelectedTool currentTool = SelectedTool.EMPTY;
	
	public static final ImageIcon imageSucess = new ImageIcon(Application.class.getResource("/sucess.png"));
	public static final ImageIcon imageError = new ImageIcon(Application.class.getResource("/error.png"));
	public static final ImageIcon imageInformation = new ImageIcon(Application.class.getResource("/info.png"));
	
	/**
	 * Construtor padrão para tela de captura.
	 */
	public Application() {
		super(AppResourceBundle.getInstance().getString("application.title"));
		createButtons();
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(AppResourceBundle.getInstance().getString("application.menu.file"));

		//Menu para salvar a imagem em arquivo
		menu.add(getMenuItemCaptureImagePart());
		menu.add(getMenuItemCaptureFullscreen());
		menu.addSeparator();
		menu.add(getMenuItemCopy());
		menu.add(getMenuItemSave());
		menu.addSeparator();
		menu.add(getMenuExit());
		menuBar.add(menu);
		menu = new JMenu(AppResourceBundle.getInstance().getString("application.menu.about"));
		menuBar.add(menu);
		setJMenuBar(menuBar);
	      
		this.add(getScrollPane(), BorderLayout.CENTER);
		
		setSize(400, 110);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private ImageIcon getImageIcon(String location, int size) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(location)).getScaledInstance(size, size, 4));
	}
	
	private void createButtons() {
	    JToolBar toolBar = new JToolBar();
	    
		buttons = new JButton[8];
        buttons[0] = new JButton(getImageIcon("/screenshot-part.png", 32));
        buttons[0].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.capture_part")); 
        buttons[1] = new JButton(getImageIcon("/screenshot.png", 32));
        buttons[1].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.capture"));
        buttons[2] = new JButton(getImageIcon("/edit-pen.png", 32));
        buttons[2].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.image_edit"));
        buttons[3] = new JButton(getImageIcon("/edit-write.png", 32));
        buttons[3].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.image_write"));
        buttons[4] = new JButton(getImageIcon("/save.png", 32));
        buttons[4].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.save_local")); 
        buttons[5] = new JButton(getImageIcon("/send-email.png", 32));
        buttons[5].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.send_email"));
        buttons[6] = new JButton(getImageIcon("/send-web.png", 32));
        buttons[6].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.send_web"));

        buttons[7] = new JButton(getImageIcon("/info.png", 32));
        buttons[7].setToolTipText(AppResourceBundle.getInstance().getString("application.button.tooltip.revert"));

        // Adiciona todos os botões
        for (JButton jButton : buttons) {
        	jButton.addActionListener(this);
        	toolBar.add(jButton);
		}
        toolBar.setVisible(true);
        this.add(toolBar, BorderLayout.PAGE_START);
	}
	
	private JMenuItem getMenuItemCaptureImagePart() {
		JMenuItem menuItem = new JMenuItem(AppResourceBundle.getInstance().getString("application.menu.capture_part"), getImageIcon("/screenshot-part.png", 20));
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_P, InputEvent.ALT_MASK));
		ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionDisplayCapture();
			}
		};
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
	
	private JMenuItem getMenuItemCaptureFullscreen() {
		JMenuItem menuItem = new JMenuItem (AppResourceBundle.getInstance().getString("application.menu.capture"), getImageIcon("/screenshot.png", 20));
		menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_C, InputEvent.ALT_MASK));
		ActionListener actionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionCaptureFullscreen();
			}
		};
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
	
	/**
	 * Cria e mantém uma instância do componente para salvar os arquivos.
	 * @return Instância de {@link JFileChooser}
	 */
	public JFileChooser getFileChooserSave() {
		if (fileChooserSave == null) {
			fileChooserSave = new JFileChooser();
			fileChooserSave.setCurrentDirectory(new File (System.getProperty ("user.dir")));
			fileChooserSave.setAcceptAllFileFilterUsed(false);
			fileChooserSave.setFileFilter(new AppImageFileFilter());
		}
		return fileChooserSave;
	}

	private JMenuItem getMenuItemCopy() {
		JMenuItem menuItem = new JMenuItem(AppResourceBundle.getInstance().getString("application.menu.copy"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
		ActionListener actionListener = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionClipboard();
			}
		};		
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
	
	private JMenuItem getMenuExit() {
		JMenuItem menuItem = new JMenuItem(AppResourceBundle.getInstance().getString("application.menu.exit"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
		ActionListener actionListener = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionExit();
			}
		};
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
	
	private JMenuItem getMenuItemSave(){
		JMenuItem menuItem = new JMenuItem(AppResourceBundle.getInstance().getString("application.menu.save_as"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
		ActionListener actionListener = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				actionSaveImageFile();
			}
		};
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
	
	/**
	 * Controle dos botões selecionados.
	 */
	public void actionPerformed(ActionEvent e) {
		//Captura parcial
		if (e.getSource() == buttons[0]) {
			actionDisplayCapture();
		}
		//Captura completa
		if (e.getSource() == buttons[1]) {
			actionCaptureFullscreen();
		}
		//Ferramenta linha
		if (e.getSource() == buttons[2]) {
			if (canEditImage()) {
				currentTool = SelectedTool.LINE;
			}
		}		
		//Ferramenta letra
		if (e.getSource() == buttons[3]) {
			if (canEditImage()) {
				currentTool = SelectedTool.WRITE;
			}
		}
		//Salvar
		if (e.getSource() == buttons[4]) {
			actionSaveImageFile();
		}
		//Reverter
		if (e.getSource() == buttons[7]) {
			getImageSelectedArea().undo();
		}
	}
	
	/**
	 * Verifica se existe imagem e se ela pode ser alterada.
	 * @return Se permite alterar a imagem.
	 */
	private boolean canEditImage() {
		if (!getImageSelectedArea().hasImage()) {
			showInfo(AppResourceBundle.getInstance().getString("application.msg.file.image_exists"));
			return false;
		}
		return true;
	}
	/**
	 * Sair do aplicativo.
	 */
	private void actionExit() {
		dispose();
	}
	
	/**
	 * Cria a nova tela para capturar uma imagem.
	 */
	private void actionDisplayCapture() {
		//Configura a ferramenta corrente
		setCurrentTool(SelectedTool.EMPTY);
        GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicDevice = graphicsEnv.getDefaultScreenDevice();
        //Verifica se o sistema suporta translucent.
        if (!graphicDevice.isWindowTranslucencySupported(TRANSLUCENT)) {
        	showError(AppResourceBundle.getInstance().getString("application.msg.translucent_unsupported"));
        	System.exit(0);
        }
        this.setVisible(false);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TranslucentWindowCapture tw = new TranslucentWindowCapture();
                tw.setOpacity(0.55f);
                tw.setVisible(true);
                tw.setReference(INSTANCE);
            }
        });
	}
	
	/**
	 * Capturar a tela inteira
	 */
	private void actionCaptureFullscreen() {
		currentTool = SelectedTool.EMPTY;
		ScreenCapturePojo pojo = new ScreenCapturePojo(true);
		captureImage(pojo);
	}
	
	private void actionSaveImageFile() {
		if (!getImageSelectedArea().hasImage()) {
			showInfo(AppResourceBundle.getInstance().getString("application.msg.file.image_exists"));
			return;
		}
		JFileChooser fileChooserSave = getFileChooserSave();
		fileChooserSave.setSelectedFile(null);
		if (fileChooserSave.showSaveDialog(Application.this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fileChooserSave.getSelectedFile();
		String path = file.getAbsolutePath().toLowerCase();
		if (!path.endsWith(".jpg") && !path.endsWith(".jpeg")) {
			file = new File(path += ".jpg");
		}
        if (file.exists()){
        	if (showConfirm(AppResourceBundle.getInstance().getString("application.msg.file.save_override")) == JOptionPane.NO_OPTION) {
				return;
			}
        }
        ImageWriter imageWriter = null;
		ImageOutputStream imageOutputStream = null;
		try {
			Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpeg");
			if (!iterator.hasNext()){
				showError(AppResourceBundle.getInstance().getString("application.msg.file.save_format_invalid"));
				return;
			}
			imageWriter = (ImageWriter) iterator.next();
			imageOutputStream = ImageIO.createImageOutputStream (file);
			imageWriter.setOutput (imageOutputStream);
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam ();
			imageWriteParam.setCompressionMode (ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality (0.95f);
			imageWriter.write (null, new IIOImage (getImageSelectedArea().getBufferedImage(), null, null), imageWriteParam);
		} catch (IOException e2){
			showError (e2.getMessage ());
		} finally {
			try {
				if (imageOutputStream != null){
					imageOutputStream.flush ();
					imageOutputStream.close ();
				}
				if (imageWriter != null) {
					imageWriter.dispose ();  
				}
			}
			catch (IOException e2){
				
			}
		}
	}
	
	private void actionClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (getImageSelectedArea().getImage() == null) {
        	showInfo("Não existe imagem para copiar.");
        	return;
        }
        ImageIcon icon = new ImageIcon(getImageSelectedArea().getImage());
        ImageClipboard clipboardImage = new ImageClipboard(icon.getImage());
        clipboard.setContents(clipboardImage, clipboardImage);
	}
	
	/**
	 * Método de retorno da tela de captura de imagem.
	 * Deve realizar o print da área selecionada pelo usuário e atribuir ao componente {@link Image}.
	 * @param pojo Instância de {@link ScreenCapturePojo} com as coordenadas.
	 */
	public void captureImage(ScreenCapturePojo pojo) {
		if (pojo != null) {
			this.setVisible(false);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			try {
				Thread.sleep(1000);
				Rectangle captureRect = new Rectangle(pojo.getX(), pojo.getY(), pojo.getWidth(), pojo.getHeight());
				BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
				getImageSelectedArea().getUndoStack().clear();
				getImageSelectedArea().setBufferedImage(screenFullImage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (pojo.isFullscreen()) {
					this.setExtendedState(JFrame.MAXIMIZED_BOTH);
				} else {
	                this.setBounds(0, 0, pojo.getWidth()+20, pojo.getHeight()+120);
				}
				this.setVisible(true);
			}
		} else {
			showError("Área não selecionada.");
		}
	}
	
	/**
	 * Cria e mantém a instância do painel da imagem
	 * @return {@link JScrollPane}
	 */
	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getImageSelectedArea());
			scrollPane.getHorizontalScrollBar().setValue(0);
            scrollPane.getVerticalScrollBar().setValue(0);
		}
		return scrollPane;
	}
	
	/**
	 * Início da aplicação.
	 * @param args - argumentos.
	 */
	public static void main (String [] args){
		try {
			robot = new Robot ();
		} catch (AWTException e) {
			showError(e.getMessage());
			System.exit(0);
		} catch (SecurityException e) {
			showError ("Permissão para utililizar o Robot necessária.");
			System.exit (0);
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				INSTANCE = new Application();
				INSTANCE.setVisible(true);
			}
		});
	}
	
	/**
	 * Exibe mensagem de erro.
	 * @param message
	 */
	public static void showError (String message){
		JOptionPane.showMessageDialog(null, message, AppResourceBundle.getInstance().getString("application.msg.title.error"), JOptionPane.ERROR_MESSAGE, imageError);
	}
	
	/**
	 * Exibe uma mensagme de informação.
	 * @param message
	 */
	public static void showInfo(String message) {
		JOptionPane.showMessageDialog(null, message, AppResourceBundle.getInstance().getString("application.msg.title.info"), JOptionPane.INFORMATION_MESSAGE, imageInformation);
	}
	
	/**
	 * Exibe mensagem de erro caso ocorrer.
	 * @param message
	 */
	public static void showSucess(String message){
		JOptionPane.showMessageDialog(null, message, AppResourceBundle.getInstance().getString("application.msg.title.sucess"), JOptionPane.DEFAULT_OPTION, imageSucess);
	}
	
	/**
	 * Exibe mensagem de confirmação.
	 * @param mensagem
	 * @return
	 */
	public static int showConfirm(String mensagem) {
		return JOptionPane.showConfirmDialog (null, mensagem, AppResourceBundle.getInstance().getString("application.msg.title.yes_no"), JOptionPane.YES_NO_OPTION);
	}
	
	public ImageSelectedArea getImageSelectedArea() {
		if (imageSelectedArea == null) {
			imageSelectedArea = new ImageSelectedArea();
			imageSelectedArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                	switch (currentTool) {
					case LINE:
	                	if (getImageSelectedArea().getBufferedImage() != null) {
	                		getImageSelectedArea().saveToStack();
	                    	getImageSelectedArea().newCurrentLine();
	                    	getImageSelectedArea().getCurrentLine().addPoint(evt.getX(), evt.getY());
	                	}
						break;
					case WRITE:
						JFrame frm = new JFrame("Write");
						
						frm.setBounds(evt.getX() + INSTANCE.getX() + getScrollPane().getX(), evt.getY() + INSTANCE.getY() + getScrollPane().getY() + 20, 100, 150);
						frm.setVisible(true);
						
					    JPanel middlePanel = new JPanel ();
					    middlePanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Display Area" ) );


					    JTextArea display = new JTextArea ( 5, 20 );
					    display.setEditable ( true );
					    JScrollPane scroll = new JScrollPane ( display );
					    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

					    middlePanel.add ( scroll );
					    frm.add(middlePanel);
//						
//						getImageSelectedArea().add(middlePanel);
//						getImageSelectedArea().repaint();
//						break;
					default:
						break;
					}
                }
             });
 			imageSelectedArea.addMouseMotionListener(new MouseMotionAdapter() {
    			@Override
    			public void mouseDragged(MouseEvent evt) {
                	switch (currentTool) {
					case LINE:
	                	if (getImageSelectedArea().getBufferedImage() != null) {
		                	getImageSelectedArea().getCurrentLine().addPoint(evt.getX(), evt.getY());
		    				getImageSelectedArea().repaint();
	                	}
						break;
					default:
						break;
					}
    	          }
    		});		
		}
		return imageSelectedArea;
	}
	
	public void setCurrentTool(SelectedTool currentTool) {
		this.currentTool = currentTool;
	}
}