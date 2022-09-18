package pojo;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Armazena as coordenadas do mouse que foram selecionadas pelo usuário.
 * 
 * @author Gabriel Vieira (gabrielvra@outlook.com)
 *
 */
public class ImageCoordinatesLine {
	
   private List<Integer> xList;
   private List<Integer> yList;

   /**
    * Inicializa as listas de armazenamento.
    */
   public ImageCoordinatesLine() {
      xList = new ArrayList<Integer>();
      yList = new ArrayList<Integer>();
   }

   /**
    * Adiciona a coordenada do mouse
    * @param x - Coordenada X
    * @param y - Coordenada Y
    */
   public void addPoint(int x, int y) {
      xList.add(x);
      yList.add(y);
   }
   
   /**
    * Processa as linhas e desenha o 
    * @param g - Instância de {@link Graphics2D}
    */
   public void draw(Graphics2D g) {
      for (int i = 0; i < xList.size() - 1; ++i) {
         g.drawLine(xList.get(i), yList.get(i), xList.get(i + 1), yList.get(i + 1));
      }
   }
}