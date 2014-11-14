package poli.near.dotmaker.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ShowImagePanel extends JPanel {
	//� uma subclasse de JPanel, ou seja, � um JPanel (polimorfismo)
	
	//atributo privado do tipo BufferedImage (lembrando que classe define um tipo
	//ou seja, BufferedImage � uma classe que gera objetos do tipo BufferedImage
	private BufferedImage image = null;
	private boolean hasCircle = false;
	private boolean hasImage = false;
	private int centerX = 0;
	private int centerY = 0;
	
	//O construtor
	public ShowImagePanel(String imagePath) {
		hasImage = true;
		try {
			image = ImageIO.read(new File(imagePath));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ShowImagePanel(int centerX, int centerY) {
		hasCircle = true;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public ShowImagePanel(String imagePath, int centerX, int centerY) {
		hasImage = true;
		hasCircle = true;
		try {
			image = ImageIO.read(new File(imagePath));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	//M�todo do JPanel (Override, anota��o de reescrita de m�todo)
	//M�todo que vai printar o conte�do do JPanel, nunca chamado diretamente.
	//Apenas creia que ele vai ser executado
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Desenha a imagem no JPanel
        if(hasImage) {	
        	g.drawImage(image, 0, 0, null);
        }
        if(hasCircle) {
        	g.setColor(new Color(255, 0, 0));
            g.drawOval(centerX-5, centerY-5,10,10);
            g.fillOval(centerX-5, centerY-5,10,10);
        }
    }
}
