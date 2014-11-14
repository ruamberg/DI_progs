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


//Até agora, classe mais difícil. Não deve ser entendida O.o

@SuppressWarnings("serial")
public class GetStartingImage extends JFrame {
	//Classe que herda de JFrame, ou seja, é um JFrame
	
	//Classe que é instanciada internamente ao ser pressionado o botão "Get and Start"
	private class SnapMeAction extends AbstractAction {
		
		public SnapMeAction() {
			//Define o título do botão pelo construtor da superclasse
			super("Get and Start");
		}
		
		//método reescrito. É acionado internamente quando a ação (o botão ser pressionado)
		//acontece. É aqui que se diz o que vai acontecer quando o botão for pressionado
		@Override
		public void actionPerformed(ActionEvent e) {
			//tenta executar o bloco... isso já foi mencionado em algum canto 
			try {
				//No geral, esse for salva uma imagem para todas as webcams conectadas ao pc
				//Como no projeto temos uma e bela simples webcam, só tira uma foto
				for (int i = 0; i < webcams.size(); i++) {
					Webcam webcam = webcams.get(i);
					File file = new File(String.format("test-%d.jpg", i));
					ImageIO.write(webcam.getImage(), "JPG", file);
					System.out.format("Image for %s saved in %s \n", webcam.getName(), file);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//Torna o JFrame não visível. Importante para o andamento do código (ver DotMaker)
			setVisible(false);
		}
	}
	//Botão TurnOn. Mesma ideia do que já foi explicado. Uma ação quando o botão for pressionado
	private class StartAction extends AbstractAction implements Runnable {

		public StartAction() {
			super("Turn On");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//Troca a atividade dos botões
			btStart.setEnabled(false);
			btSnapMe.setEnabled(true);
			//Manda uma mensagem para a camera... esquece essa linha
			executor.execute(this);
		}
		//Método reescrito de Runnable
		//Diz o que vai acontecer enquanto estiver ativo ou no momento da ativação
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
			//Desliga a camera e altera o estado dos botões
			for (WebcamPanel panel : panels) {
				panel.stop();
			}
		}
	}
	
	//Atributos da classe GetStartingImage
	//Em ordem, a mensagem de execução (aa linha esquecida!)
	private Executor executor = Executors.newSingleThreadExecutor();
	
	//O tamanho da imagem. Definida num tipo Dimension
	private Dimension size = new Dimension (640,480);
	//Lista das webcams e lista de paineis para cada webcam
	private List<Webcam> webcams = Webcam.getWebcams();
	private List<WebcamPanel> panels = new ArrayList<WebcamPanel>();
	
	//os botões!! e a associação de cada um a sua respectiva classe
	private JButton btSnapMe = new JButton(new SnapMeAction());
	private JButton btStart = new JButton(new StartAction());
	private JButton btStop = new JButton(new StopAction());

	//finalmente, o construtor da classe GetStartingImage
	public GetStartingImage() {
		//Cria o frame pelo super... O construtor da classe-mãe (superclasse, tanto faz)
		//e define o título
		super("Cam Image Control");
		
		//Cria os paineis para todas as webcams disponíveis
		for (Webcam webcam : webcams) {
			webcam.setViewSize(size);
			WebcamPanel panel = new WebcamPanel(webcam, size, false);
			panel.setFPSDisplayed(true);
			panel.setFillArea(true);
			//Linha importante. Adiciona o painel criado a lista de paineis
			panels.add(panel);
		}

		//estado inicial dos botões
		btSnapMe.setEnabled(false);
		btStop.setEnabled(false);

		setLayout(new FlowLayout());
		
		//adiciona os paineis da lista de paineis ao JFrame
		//Se você se perguntar pq os for's dessa classe são tão estranhos, 
		//saiba que são otimizações presentes em java. É chamado for otimizado para array
		for (WebcamPanel panel : panels) {
			add(panel);
		}
		
		//adiciona os botões ao JFrame
		add(btSnapMe);
		add(btStart);
		add(btStop);
		
		//Define algumas configurações do JFrame
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
