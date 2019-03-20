package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class MedianFilter extends ImageProcessor {

	int karnelSize;

	private int row[] = { -1, 0, 1, 0 };
	private int col[] = { 0, 1, 0, -1 };
	protected BufferedImage image2;
	int total = 0;
	private Color white = new Color(255, 255, 255);
	private Color black = new Color(0, 0, 0);

	@Override
	protected void writePixel(int x, int y) {

		ArrayList<Integer> colorValue = new ArrayList<Integer>();

		for (int i = -1 * karnelSize; i <= karnelSize; i++) {
			for (int j = -1 * karnelSize; j <= karnelSize; j++) {

				int tempX = x + i;
				int tempY = y + j;

				if (tempX >= 0 && tempX < height && tempY >= 0 && tempY < width) {
					Color c = new Color(image.getRGB(tempY, tempX));
					int grayScale = (int) c.getRed();
					colorValue.add(grayScale);
				}
			}
		}

		Collections.sort(colorValue);

		int medianValue = colorValue.get(colorValue.size()/2);
		Color color;
		if(medianValue == 0)
			color = black;
		else color = white;
		image2.setRGB(y, x, color.getRGB());


		// Color c = new Color(image.getRGB(y, x));
		// int grayScale = (int) c.getRed();

		// image2.setRGB(y, x, color.getRGB());

	}

	@Override
	protected void initializedPreOperation() {
		imageFileName = "Median_" + imageFileName;
		image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	}

	protected File getImageFile() {
		imagePath = imageFileName + "." + imageType;
		File ouptut = new File(imagePath);
		try {
			ImageIO.write(image2, imageType, ouptut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ouptut;
	}

	public File getFilteredImage(File imageFile, int karnelSize) {

		this.karnelSize = karnelSize;

		return convert(imageFile);
	}

}
