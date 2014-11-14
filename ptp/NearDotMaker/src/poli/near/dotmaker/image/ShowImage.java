package poli.near.dotmaker.image;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ShowImage extends JFrame {
	//a palavra chave extends sinaliza herança. ShowImage herda JFrame
	//JFrame é a moldura de todo e qualquer programa com interface gráfica
	
	//atributos privados (garantem encapsulamento)
	private int width = 0;
	private int height = 0;
	
	//métodos sem tipo e com o mesmo nome da classe não são métodos. São construtores
	//Os construtores são estruturas sempre executadas na construção (instância) de um objeto
	//Em Java, pode se fazer a sobrecarga de métodos e construtores, criando vários,
	//com a diferença da lista de argumentos. Em tempo de execução, a máquina decide
	//qual o método ou construtor correto
	//throws significa "lança" (do verbo lançar. Significa que o código está lançando uma
	//sinalização contra um possível erro (exceção) para que o erro possa ser tratado em 
	//tempo de execução
	public ShowImage(String imagePath, int x, int y) throws IOException {
		//super aciona a estrutura semelhante na superclasse (o construtor de JFrame, no caso)
		//O construtor de JFrame que recebe uma String cria uma moldura e define um título
		super("Show Image");
		
		//Lê a imagem e trata ela para ser adicionada ao JFrame
        BufferedImage image = ImageIO.read(new File(imagePath));
        String info = "Dimensões: " + image.getWidth() + "x" +
                image.getHeight() + " Bandas: " + image.getRaster().getNumBands();
        ImageIcon icon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(icon);
        
        //Pega as dimensões x e y respectivamente
        width = image.getWidth();
        height = image.getHeight();

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        contentPane.add(new JLabel(info), BorderLayout.SOUTH);
        
        //define as características do JFrame criado no super
        this.setLocation(x,y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(image.getWidth(), image.getHeight());
        this.setVisible(true);
    }
	//Outro construtor, mas recebe um JPanel como parâmetro (Sobrecarga de métodos e construtores)
	public ShowImage(JPanel panel, int x, int y) {
		super("Show Image");
		
        String info = "Dimensões: " + 400 + "x" +
               480;
        
        width = 400;
        height = 480;

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(new JLabel(info), BorderLayout.SOUTH);
        panel.updateUI();
        
        this.setLocation(x,y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setVisible(true);
    }

	//Construtor sem parâmetros gera um JFrame com a imagem da webcam
	public ShowImage(int x, int y) {
		super("Webcam Image");
		
		//Mesmo código utilizado em GetStartingImage
		List<Webcam> webcams = Webcam.getWebcams();
		List<WebcamPanel> panels = new ArrayList<WebcamPanel>();
		Dimension size = new Dimension(640, 480);
		WebcamPanel panel = null;
		
		for (Webcam webcam : webcams) {
			panel = new WebcamPanel(webcam, size, false);
			panel.setFPSDisplayed(true);
			panel.setFillArea(true);
			panels.add(panel);
		}
        
		width = (int) size.getWidth();
        height = (int) size.getHeight();
        
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.CENTER);
        
        panel.start();
        
        this.setLocation(x,y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(size);
        this.setVisible(true);
	}
	
	public ShowImage(BufferedImage image, int x, int y) {
		//super aciona a estrutura semelhante na superclasse (o construtor de JFrame, no caso)
		//O construtor de JFrame que recebe uma String cria uma moldura e define um título
		super("Show Image");
		
		//Lê a imagem e trata ela para ser adicionada ao JFrame
        String info = "Dimensões: " + image.getWidth() + "x" +
                image.getHeight() + " Bandas: " + image.getRaster().getNumBands();
        ImageIcon icon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(icon);
        
        //Pega as dimensões x e y respectivamente
        width = image.getWidth();
        height = image.getHeight();

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        contentPane.add(new JLabel(info), BorderLayout.SOUTH);
        
        //define as características do JFrame criado no super
        this.setLocation(x,y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(image.getWidth(), image.getHeight());
        this.setVisible(true);
    }
	
	//Métodos getters. Pelo fato dos parâmetros serem privados (private), eles devem ser
	//acessados de alguma maneira. Essa maneira são os getters e os setters.
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}