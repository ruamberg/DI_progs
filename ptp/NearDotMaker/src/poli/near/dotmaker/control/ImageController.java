package poli.near.dotmaker.control;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageController {
	public static BufferedImage getSubimage(String imagePath, int x, int y, int w, int h) {
		BufferedImage image = null;
		File file = new File(imagePath);
		try {
			image = ImageIO.read(file);
			image = image.getSubimage(x, y, w, h);
			ImageIO.write(image, "JPG", file);
			image = ImageIO.read(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return image;
	}
	
	public static BufferedImage getSubimage(BufferedImage image, int x, int y, int w, int h) throws IOException {
		image = image.getSubimage(x, y, w, h);
		return image;
	}
	
	public static int[][] getArray(String imagePath, int reduction) {
		BufferedImage imageIn = null;
		
		int[][] arr = new int[640/reduction][480/reduction];
		Color pixel = null;
		
		try {	
			imageIn = ImageIO.read(new File(imagePath));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage image = toBlackAndWhite(imageIn);
		
		for(int j = 0; j < image.getHeight(); j+=reduction) {
			for(int i = 0; i < image.getWidth(); i+=reduction) {
				pixel = new Color(image.getRGB(i, j));
				if((pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3 == 255) {
					arr[i/reduction][j/reduction] = 0;
				}
				else if((pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3 == 0) {
					arr[i/reduction][j/reduction] = 1;
				}
			}
		}
		
		return arr;
	}
	
	public static BufferedImage toBlackAndWhite(BufferedImage imageIn) {  
		  
        int w = imageIn.getWidth();  
        int h = imageIn.getHeight();  
        byte[] comp = { 0, -1 };  
        IndexColorModel cm = new IndexColorModel(2, 2, comp, comp, comp);  
        BufferedImage imageOut = new BufferedImage(w, h,  
                BufferedImage.TYPE_BYTE_INDEXED, cm);  
        Graphics2D g = imageOut.createGraphics();  
        g.drawRenderedImage(imageIn, null);  
        g.dispose();  
  
        return imageOut;  
    }
}
