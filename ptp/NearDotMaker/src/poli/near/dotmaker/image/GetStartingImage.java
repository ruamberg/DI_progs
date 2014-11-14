package poli.near.dotmaker.image;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;


//At� agora, classe mais dif�cil. N�o deve ser entendida O.o

@SuppressWarnings("serial")
public class GetStartingImage extends JFrame {
	//Classe que herda de JFrame, ou seja, � um JFrame
	
	//Classe que � instanciada internamente ao ser pressionado o bot�o "Get and Start"
	private class SnapMeAction extends AbstractAction {
		
		public SnapMeAction() {
			//Define o t�tulo do bot�o pelo construtor da superclasse
			super("Get and Start");
		}
		
		//m�todo reescrito. � acionado internamente quando a a��o (o bot�o ser pressionado)
		//acontece. � aqui que se diz o que vai acontecer quando o bot�o for pressionado
		@Override
		public void actionPerformed(ActionEvent e) {
			//tenta executar o bloco... isso j� foi mencionado em algum canto 
			try {
				//No geral, esse for salva uma imagem para todas as webcams conectadas ao pc
				//Como no projeto temos uma e bela simples webcam, s� tira uma foto
				for (int i = 0; i < webcams.size(); i++) {
					Webcam webcam = webcams.get(i);
					File file = new File(String.format("test-%d.jpg", i));
					ImageIO.write(webcam.getImage(), "JPG", file);
					System.out.format("Image for %s saved in %s \n", webcam.getName(), file);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//Torna o JFrame n�o vis�vel. Importante para o andamento do c�digo (ver DotMaker)
			setVisible(false);
		}
	}
	//Bot�o TurnOn. Mesma ideia do que j� foi explicado. Uma a��o quando o bot�o for pressionado
	private class StartAction extends AbstractAction implements Runnable {

		public StartAction() {
			super("Turn On");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//Troca a atividade dos bot�es
			btStart.setEnabled(false);
			btSnapMe.setEnabled(true);
			//Manda uma mensagem para a camera... esquece essa linha
			executor.execute(this);
		}
		//M�todo reescrito de Runnable
		//Diz o que vai acontecer enquanto estiver ativo ou no momento da ativa��o
		@Override
		public void run() {
			btStop.setEnabled(true);
			
			//For aprimorado para arrays. Inicia (start()) todas as webcams conectadas e instaladas
			for (WebcamPanel panel : panels) {
				panel.start();
			}
		}
	}
	
	//A mesma ideia de novo, novamente, mais uma vez
	private class StopAction extends AbstractAction {

		public StopAction() {
				super("Turn Off");
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			btStart.setEnabled(true);
			btSnapMe.setEnabled(false);
			btStop.setEnabled(false);
			//Desliga a camera e altera o estado dos bot�es
			for (WebcamPanel panel : panels) {
				panel.stop();
			}
		}
	}
	
	//Atributos da classe GetStartingImage
	//Em ordem, a mensagem de execu��o (aa linha esquecida!)
	private Executor executor = Executors.newSingleThreadExecutor();
	
	//O tamanho da imagem. Definida num tipo Dimension
	private Dimension size = new Dimension (640,480);
	//Lista das webcams e lista de paineis para cada webcam
	private List<Webcam> webcams = Webcam.getWebcams();
	private List<WebcamPanel> panels = new ArrayList<WebcamPanel>();
	
	//os bot�es!! e a associa��o de cada um a sua respectiva classe
	private JButton btSnapMe = new JButton(new SnapMeAction());
	private JButton btStart = new JButton(new StartAction());
	private JButton btStop = new JButton(new StopAction());

	//finalmente, o construtor da classe GetStartingImage
	public GetStartingImage() {
		//Cria o frame pelo super... O construtor da classe-m�e (superclasse, tanto faz)
		//e define o t�tulo
		super("Cam Image Control");
		
		//Cria os paineis para todas as webcams dispon�veis
		for (Webcam webcam : webcams) {
			webcam.setViewSize(size);
			WebcamPanel panel = new WebcamPanel(webcam, size, false);
			panel.setFPSDisplayed(true);
			panel.setFillArea(true);
			//Linha importante. Adiciona o painel criado a lista de paineis
			panels.add(panel);
		}

		//estado inicial dos bot�es
		btSnapMe.setEnabled(false);
		btStop.setEnabled(false);

		setLayout(new FlowLayout());
		
		//adiciona os paineis da lista de paineis ao JFrame
		//Se voc� se perguntar pq os for's dessa classe s�o t�o estranhos, 
		//saiba que s�o otimiza��es presentes em java. � chamado for otimizado para array
		for (WebcamPanel panel : panels) {
			add(panel);
		}
		
		//adiciona os bot�es ao JFrame
		add(btSnapMe);
		add(btStart);
		add(btStop);
		
		//Define algumas configura��es do JFrame
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
