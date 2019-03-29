package imageProcessor;

import java.awt.Color;
import java.io.File;

public class GrayScale extends ImageProcessor{

	@Override
	protected void operatePixel(int indexOfRow, int indexOfColumn) {
		Color currentPixelColor = new Color(inputImage.getRGB(indexOfColumn, indexOfRow));

		int red = (int) currentPixelColor.getRed();
		int green = (int) currentPixelColor.getGreen();
		int blue = (int) currentPixelColor.getBlue();

		int averageValue = (red + green + blue) / 3;

		Color newColor = new Color(averageValue, averageValue, averageValue);
		outputImage.setRGB(indexOfColumn, indexOfRow, newColor.getRGB());

	}

	@Override
	protected void init() {
		imageFileName = "Gray_Scale_" + imageFileName;
	}

	public File convertImageToGrayScale(File inputImageFile) {
		return process(inputImageFile);
	}

}
