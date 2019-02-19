package preProcessor;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GrayScale extends ImageProcessor {

	@Override
	protected void writePixel(int x, int y) {
		Color c = new Color(image.getRGB(y, x));
		int red = (int) (c.getRed() * 0.299);
		int green = (int) (c.getGreen() * 0.587);
		int blue = (int) (c.getBlue() * 0.114);
		Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
		image.setRGB(y, x, newColor.getRGB());

	}

	@Override
	protected void initializedPreOperation() {
		imageFileName = "Gray_Scale_" + imageFileName;
	}

}
