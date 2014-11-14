package poli.near.dotmaker.threads;

/*
 * Código de manipulação de câmera e escolha de destino
 * 25/09/2014
 * Gabriel Alves de Lima
 * 
 * Legenda:
 * 	Classe - modelagem de objetos, estereótipo de coisas reais ou abstratas (Ex. Carro é uma
 * 			 classe de objetos que tem motor, direção, pedais, bancos, pneus, etc.)
 * 	Atributos - características de um objeto, o que é usado para modelar o objeto
 * 				(Ex. um carro pode variar. É uma característica, um atributo)
 * 	Métodos - Meios de comunicação do objeto o qual se faz por uma ação (Funções em C)
 * 				(Ex. acelerar é uma ação que o objeto carro realiza, portanto, um método)
 * 	Objeto - Um local da memória caracterizado por seus atributos e um tipo (A classe a qual o
 * 		     objeto pertence).
 * 				(Ex. o carro é uma criação (instância) da classe Carro)
 * 	Herança - Mecanismo de hereditariedade de métodos e atributos permitidos de uma classe a 
 * 			  outra. Gera uma hierarquia e garante o polimorfismo, porém fere o encapsulamento
 * 				(Ex. Carro é uma superclasse e Sedan é uma subclasse. Sedan herda todas as
 * 				características de um carro e tem outras mais.)
 * 	Polimorfismo - Capacidade de um tipo ser vários ao mesmo tempo (ex. Sedan é um carro,
 * 					ou seja, o Sedan pode ser referido como um carro qualquer) gerando a 
 * 					possibilidade de se referir a um tipo como outro mais geral.
 * 	Encapsulamento - Tratamento dos dados de modo a dar privacidade a alguns valores,
 * 				     para dar segurança ao sistema.
 * 					(Ex. Os valores utilizados pelo método acelerar não precisam ser expostos.
 * 					Isso garante robustez e segurança).
 * 	Interface - Tipo de modelagem que necessita de implementação, garantindo polimorfismo,
 * 				funcionando como um contrato
 * 				(Ex. Uma interface seria MeioDeTransporte. Todo meio de transporte deve ter 
 * 				movimento. Se esse contrato for cumprido, esse objeto será um meio de transporte)
 * 
 * *********************************************************************************************
 * 
 * Classe DotMaker - Classe principal do programa, possui o método main()
 * Classe ShowImage - Ao ser instanciada, a classe gera uma imagem em um JFrame
 * Classe ShowImagePanel - Ao ser instanciada, a classe gera um JPanel com uma imagem
 * Classe GetStartingImage - Liga a câmera e pega a imagem inicial
 * Classe MouseGetter - Busca algumas informações sobre o mouse
 * Classe CustomMouseListener - É uma classe ouvinte (classes ouvintes tem a finalidade de
 * 								captar ações vindas do usuários) e ouve o mouse.
 */

//Mesmo que #include
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.JLabel;
import javax.swing.JPanel;

import poli.near.dotmaker.control.BluetoothController;
import poli.near.dotmaker.control.ImageController;
import poli.near.dotmaker.image.GetStartingImage;
import poli.near.dotmaker.image.ShowImage;
import poli.near.dotmaker.image.ShowImagePanel;
import poli.near.dotmaker.mouse.CustomMouseListener;
import poli.near.dotmaker.mouse.MouseGetter;

//Linha desnecessária, na verdade. Apenas uma anotação da IDE para suprimir avisos
@SuppressWarnings("unused")
public class DotMaker {
	//400 -> 524-124
	public static final String IMAGE_PATH = System.getProperty("user.home") + "\\workspace\\NearDotMaker\\test-0.jpg";
	private static final String CONNECTION_URL = "btspp://001106220310:1" + "";
	
	//Método main - Início do programa
	public static void main(String[] args) {
		boolean shouldWait = true;   				
		
		GetStartingImage gsi = new GetStartingImage();  
		while(shouldWait) {
			if(!gsi.isVisible()) {
				shouldWait = false;
				gsi = null;
			}
		}
		
		BluetoothController.connect(CONNECTION_URL);
		
		createProgram();
	} 
	
	public static void createProgram() {
		try {
			BufferedImage image = ImageIO.read(new File(IMAGE_PATH));
			image = ImageController.getSubimage(image, 124, 0, 400, 480);
			ImageIO.write(image, "JPG", new File(IMAGE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ShowImagePanel sip = new ShowImagePanel(IMAGE_PATH);
		ShowImage si = new ShowImage(sip, 0, 100);  
		Thread swc = new Thread(new WebcamThread(660, 100));
		swc.start ();
		sip.addMouseListener(new CustomMouseListener(si));
		si.setTitle("Select a point");
		
		/*while(true) {
			double[] xy = MouseGetter.getMouseXY(si);
			System.out.println(xy[0] + " " + xy[1]);
		}*/
		
	}
}
