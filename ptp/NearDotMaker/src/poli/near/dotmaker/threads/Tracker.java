package poli.near.dotmaker.threads;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;





import poli.near.dotmaker.control.BluetoothController;
//import poli.near.dotmaker.control.BluetoothController;
import poli.near.dotmaker.control.ImageController;
import poli.near.dotmaker.control.TrackerTools;

public class Tracker implements Runnable {
	
	private int[][] tom;
	private int width = 0;
	private int height = 0;
	private int reduction = 10;
	private JFrame si = null;
	
	public Tracker(int width, int height, JFrame si) {
		this.si = si;
		this.width = width/reduction;
		this.height = height/reduction;
		
		WebcamThread.getWebcamObject().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WebcamThread.getWebcamObject().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				si.dispose();
				WebcamThread.getWebcamObject().dispose();
				JOptionPane.showMessageDialog(null, "Fim do programa! Até a próxima!", "Fim", 1);
				BluetoothController.closeConnection();
				System.exit(0);
			}
		});
		si.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		si.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				si.dispose();
				WebcamThread.getWebcamObject().dispose();
				JOptionPane.showMessageDialog(null, "Fim do programa! Até a próxima!", "Fim", 1);
				BluetoothController.closeConnection();
				System.exit(0);
			}
		});
	}


	@Override
	public void run() {
		int count = 0;
		char received = '\0';
		int goingToRepeat = -5;
		double[] xy = new double[2];
		boolean haveFinished = false;
		
		Point swc = null;
		String aux = null;
		BufferedImage image = null;
		TrackerTools tt = new TrackerTools(width, height);
		
		while(goingToRepeat != JOptionPane.YES_OPTION) {
			goingToRepeat = JOptionPane.showConfirmDialog(null, "Tudo pronto! Vamos começar?", "Finalmente!", 2);
		}
		
		goingToRepeat = 0;
		
		while(!haveFinished) {
			swc = WebcamThread.getWebcamObject().getLocation();
			tom = ImageController.getArray(DotMaker.IMAGE_PATH, reduction);
			image = tt.takePrintScreen();
			try {
				image = ImageController.getSubimage(image, (int) swc.getX() + 130, (int) swc.getY() + 30, 300, 480);
				ImageIO.write(image, "JPG", new File(DotMaker.IMAGE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			tt.seeNewSides(tom);
			BluetoothController.sendMessage(tt.getVector());
			aux = tt.getVector();
			xy[0] = Double.parseDouble(aux.substring(0, aux.indexOf("|")));
			xy[1] = Double.parseDouble(aux.substring(aux.indexOf("|") + 1, aux.length()));
			System.out.println(xy[0] + " e " + xy[1] + "/" + tt.getVector() + "/" + count);
			if(Math.abs(xy[0]) < 5 && Math.abs(xy[1]) < 5 && count < 20) {
				count++;
			}
			else if(Math.abs(xy[0]) < 5 && Math.abs(xy[1]) < 5 && count >= 20) {
				haveFinished = true;
			} 
			else {
				count = 0;
			}
			
			while(received == '\0') {
				received = BluetoothController.getCharMessage();
			}
		}
		
		goingToRepeat = JOptionPane.showConfirmDialog(null, "Finalizado! Deseja realizar a operação novamente?");
		if(goingToRepeat == JOptionPane.YES_OPTION) {
			si.dispose();
			WebcamThread.getWebcamObject().dispose();
			try {
				Thread.sleep(500);
				BufferedImage nextImage = tt.takeSnapshot();
				ImageIO.write(nextImage, "JPG", new File(DotMaker.IMAGE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DotMaker.createProgram();
		}
		else {
			si.dispose();
			WebcamThread.getWebcamObject().dispose();
			JOptionPane.showMessageDialog(null, "Fim do programa! Até a próxima!", "Fim", 1);
		    BluetoothController.closeConnection();
			System.exit(0);
		}
		
	}
}
