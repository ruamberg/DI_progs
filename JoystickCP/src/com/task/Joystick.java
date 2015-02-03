package com.task;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.controller.ClickListener;
import com.controller.BluetoothController;
import com.controller.MouseGetter;

@SuppressWarnings("serial")
public class Joystick extends JFrame {
	
	public Joystick(Dimension size) {
		this.setSize(size);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {  
		    public void windowClosing(WindowEvent evt) {  
		        if (JOptionPane.showConfirmDialog(null,"Deseja sair?") == JOptionPane.YES_OPTION){  
		            //BluetoothController.closeConnection();
		        	System.exit(0);}  
		    }  
		});
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		Point pointer = this.getLocation();
		this.add(new Imaging(pointer));
		this.setTitle("Controlling Pad");
		MouseReader();
	}
	
	public void MouseReader() {
		double[] xy = new double[2];
		int coordinates[] = new int[2];
		String vector = null;
		String received = "";
		while(true) {
			xy = MouseGetter.getMouseXY(this);
			coordinates[0] = (int) xy[0] - 273;
			coordinates[1] = (int) (-xy[1]) + 287;
			if((coordinates[0] < 10 && coordinates[0] > -10) && (coordinates[1] < 10 && coordinates[1] > -10)) {
				coordinates[0] = 0;
				coordinates[1] = 0;
			}
			vector = coordinates[0] + "|" + coordinates[1];
			System.out.println(vector);
			//BluetoothController.sendMessage(vector);
			do {
				received = BluetoothController.receiveMessage(); 
			}
			while(received.equals(""));
		}
	}
}

@SuppressWarnings("serial")
class Imaging extends JPanel {
	private static final String FS = System.getProperty("file.separator");
	private static final String IMAGE_NAME = System.getProperty("user.home") + FS + "workspace" + FS + "JoystickCP" + FS + "bg.gif"; 
	private Image image = Toolkit.getDefaultToolkit().createImage(IMAGE_NAME);
	public int centerX = 0;
	public int centerY = 0;
	
	public Imaging(Point pointer) {
		this.addMouseListener(new ClickListener(this));
		centerX = (int) pointer.getX() - 102;
		centerY = (int) pointer.getY() + 180;
	}
	
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0, 255, 255));
		g.drawImage(image, 0, 0, this);
		g.fillOval(centerX, centerY, 22, 20);
	}
}