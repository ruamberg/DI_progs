package poli.near.dotmaker.control;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class TrackerTools {
	private int width = 0;
	private int height = 0;
	private int topSide = -1, downSide = -1, leftSide = 80, rightSide = -1;
	private int[] center = new int[2];
	
	public TrackerTools(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public BufferedImage takePrintScreen() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension size = toolkit.getScreenSize();
		Rectangle screenRect = new Rectangle(size);
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		BufferedImage screenCapturedImage = robot.createScreenCapture(screenRect);
		return screenCapturedImage;
	}
	
	/*private BufferedImage takeSnapshot() {
	    List<Webcam> webcams = Webcam.getWebcams();
		
	    BufferedImage image = null;
	    File file = null;
	    
		try {
			Webcam webcam = webcams.get(0);
			file = new File(String.format("test-%d.jpg", 0));
			ImageIO.write(webcam.getImage(), "JPG", file);
			System.out.println("Snap!");
			image = ImageIO.read(new File(DotMaker.IMAGE_PATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return image;
	}*/
	
	public void seeNewSides(int[][] tom) { 
		center[0] = -1; center[1] = 0;
		boolean hasFound = false;
		
		for(int i = 0; i < tom.length; i++) {
			for(int j = 0; j < tom[0].length; j++) {
				if(tom[i][j] == 1 && !hasFound) {
					topSide = i;
					downSide = i;
					leftSide = j;
					rightSide = j;
					hasFound = true;
				}
				if(tom[i][j] == 1 && i > downSide) {
					downSide = i;
				}
				if(tom[i][j] == 1 && j < leftSide) {
					leftSide = j;
				}
				if(tom[i][j] == 1 && j > rightSide) {
					rightSide = j;
				}
			}
		}
		
		center[0] = (rightSide + leftSide) / 2;
		center[1] = (topSide + downSide) / 2;
		tom[center[1]][center[0]] = 3;
	}
	
	public String getVector() {
		double[] xy = new double[2];
		xy[0] = width - center[1];
		xy[1] = -(height - center[0]);
		return xy[0] + "|" + xy[1];
	}
}
