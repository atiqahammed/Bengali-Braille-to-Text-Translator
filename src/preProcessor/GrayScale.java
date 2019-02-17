package preProcessor;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GrayScale implements IPreprocessor{


	BufferedImage  image;
	   int width;
	   int height;

	@Override
	public File convert(File imageFile) {
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        width = image.getWidth();
        height = image.getHeight();

        for(int i=0; i<height; i++) {

           for(int j=0; j<width; j++) {

              Color c = new Color(image.getRGB(j, i));
              int red = (int)(c.getRed() * 0.299);
              int green = (int)(c.getGreen() * 0.587);
              int blue = (int)(c.getBlue() *0.114);
              Color newColor = new Color(red+green+blue,

              red+green+blue,red+green+blue);

              image.setRGB(j,i,newColor.getRGB());
           }
        }

        File ouptut = new File("grayscale.jpg");
        try {
			ImageIO.write(image, "jpg", ouptut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ouptut;
	}

}
