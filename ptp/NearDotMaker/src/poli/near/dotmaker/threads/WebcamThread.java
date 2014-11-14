package poli.near.dotmaker.threads;

import poli.near.dotmaker.image.ShowImage;

public class WebcamThread implements Runnable {
	private static ShowImage swc = null;
	private int x = 0;
	private int y = 0;
	
	public WebcamThread(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void run() { 
		swc = new ShowImage(x, y);
	}
	
	public static ShowImage getWebcamObject() {
		return swc;
	}
}
