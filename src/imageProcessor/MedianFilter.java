package imageProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import util.Utils;

public class MedianFilter extends ImageProcessor{

	int karnelSize;


	int total = 0;


	@Override
	protected void operatePixel(int indexOfRow, int indexOfColumn) {
		ArrayList<Integer> colorValue = new ArrayList<Integer>();

		for (int i = -1 * karnelSize; i <= karnelSize; i++) {
			for (int j = -1 * karnelSize; j <= karnelSize; j++) {

				int tempX = indexOfRow + i;
				int tempY = indexOfColumn + j;

				if (tempX >= 0 && tempX < imageHeight && tempY >= 0 && tempY < imageWidth) {
					Color c = new Color(inputImage.getRGB(tempY, tempX));
					int grayScale = (int) c.getRed();
					colorValue.add(grayScale);
				}
			}
		}

		Collections.sort(colorValue);

		int medianValue = colorValue.get(colorValue.size()/2);
		Color color;
		if(medianValue == 0)
			color = Utils.BLACK;
		else color = Utils.WHITE;
		outputImage.setRGB(indexOfColumn, indexOfRow, color.getRGB());

	}

	@Override
	protected void init() {
		imageFileName = "Median_" + imageFileName;
	}


	public File getFilteredImage(File imageFile, int karnelSize) {
		this.karnelSize = karnelSize;
		return process(imageFile);
	}
}
