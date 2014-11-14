package poli.near.dotmaker.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import poli.near.dotmaker.image.ShowImage;
import poli.near.dotmaker.image.ShowImagePanel;
import poli.near.dotmaker.threads.DotMaker;
import poli.near.dotmaker.threads.Tracker;

//Classe que implementa MouseListener
//MouseListener é uma interface. A interface (como contrato) tem apenas as clausulas.
//Essas cláusulas no código são as assinaturas dos métodos que devem ser implementados
//pela classe que ousar (hehe) implementá-los. Isso, em retorno, vai garantir polimorfismo.
//De modo que CustomMouseListener pode ser chamado como um MouseListener
public class CustomMouseListener implements MouseListener {
	private JFrame si = null;
	private int hasConfirmed = 0;
	
	//Construtor de CustomMouseListener. Define o atributo si
	public CustomMouseListener(JFrame si) {
		this.si = si;
		System.out.println("CML created");
	}
	//Outra linha inútil. Só para dizer que é uma reescrita de método
	//No caso, a reescrita é a implementação ds métodos de MouseListener
	//Cada método de MouseListener é acessado quando ele ouve algo específico
	//click no objeto a ser ouvido, mouse entrando nesse objeto, saindo desse objeto, 
	//pressionado nesse objeto e solto nesse objeto, repectivamente
	@Override
	public void mouseClicked(MouseEvent arg0) {		
		System.out.println("Click!");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		System.out.println("Entered");
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		System.out.println("Exited");
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		hasConfirmed = -1;
		double[] xy = MouseGetter.getMouseXY(si);
		int finalWidth = (int) xy[0];
		int finalHeight = (int) xy[1];
		hasConfirmed = JOptionPane.showConfirmDialog(null, "Confirmar ponto escolhido?");
		if(hasConfirmed == JOptionPane.YES_OPTION) {
			System.out.println("Select!");
			System.out.println(finalWidth/10 + " " + finalHeight/10);
			ShowImagePanel sip = new ShowImagePanel(DotMaker.IMAGE_PATH, finalWidth, finalHeight);
			si.dispose();
			si = new ShowImage(sip, 0, 100);
			si.setTitle("Confirmed!");
			new Thread(new Tracker(finalWidth, finalHeight, si)).start();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		System.out.println("Released");
	}
	
}
