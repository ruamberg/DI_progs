package poli.near.dotmaker.threads;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	}


	@Override
	public void run() {
		int count = 0;
		int goingToRepeat = -5;
		double[] xy = new double[2];
		boolean haveFinished = false;
		
		Point swc = null;
		String aux = null;
		BufferedImage image = null;
		TrackerTools tt = new TrackerTools(width, height);
		
		while(goingToRepeat != JOptionPane.YES_OPTION) {
			goingToRepeat = JOptionPane.showConfirmDialog(null, "Tudo pronto! Vamos come�ar?", "Finalmente!", 2);
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
			//BluetoothController.sendMessage(getVector());
			aux = tt.getVector();
			xy[0] = Double.parseDouble(aux.substring(0, aux.indexOf("|")));
			xy[1] = Double.parseDouble(aux.substring(aux.indexOf("|") + 1, aux.length()));
			System.out.println(xy[0] + " e " + xy[1] + "/" + tt.getVector() + "/" + count);
			if(Math.abs(xy[0]) < 2 && Math.abs(xy[1]) < 2 && count < 20) {
				count++;
			}
			else if(Math.abs(xy[0]) < 5 && Math.abs(xy[1]) < 5 && count >= 20) {
				haveFinished = true;
			} 
			else {
				count = 0;
			}

		}
		
		goingToRepeat = JOptionPane.showConfirmDialog(null, "Finalizado! Deseja realizar a opera��o novamente?");
		if(goingToRepeat == JOptionPane.YES_OPTION) {
			si.dispose();
			WebcamThread.getWebcamObject().dispose();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//takeSnapshot();
			DotMaker.createProgram();
		}
		else {
			si.dispose();
			WebcamThread.getWebcamObject().dispose();
			JOptionPane.showMessageDialog(null, "Fim do programa! At� a pr�xima!");
		    //BluetoothController.closeConnection();
			System.exit(0);
		}
		
	}
}
