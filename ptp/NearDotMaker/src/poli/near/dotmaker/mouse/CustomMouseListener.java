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
//MouseListener � uma interface. A interface (como contrato) tem apenas as clausulas.
//Essas cl�usulas no c�digo s�o as assinaturas dos m�todos que devem ser implementados
//pela classe que ousar (hehe) implement�-los. Isso, em retorno, vai garantir polimorfismo.
//De modo que CustomMouseListener pode ser chamado como um MouseListener
public class CustomMouseListener implements MouseListener {
	private JFrame si = null;
	private int hasConfirmed = 0;
	
	//Construtor de CustomMouseListener. Define o atributo si
	public CustomMouseListener(JFrame si) {
		this.si = si;
		System.out.println("CML created");
	}
	//Outra linha in�til. S� para dizer que � uma reescrita de m�todo
	//No caso, a reescrita � a implementa��o ds m�todos de MouseListener
	//Cada m�todo de MouseListener � acessado quando ele ouve algo espec�fico
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
