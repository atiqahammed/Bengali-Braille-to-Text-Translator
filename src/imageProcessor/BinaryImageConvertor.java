package imageProcessor;

import java.awt.Color;
import java.io.File;

import util.Constant;

public class BinaryImageConvertor extends ImageProcessor{

	private int threshold;

	@Override
	protected void operatePixel(int indexOfRow, int indexOfColumn) {
		Color c = new Color(inputImage.getRGB(indexOfColumn, indexOfRow));
		int grayScale = (int) c.getRed();
		Color color;
		if (grayScale < threshold)
			color = Constant.WHITE;

		else
			color = Constant.BLACK;

		outputImage.setRGB(indexOfColumn, indexOfRow, color.getRGB());

	}

	@Override
	protected void init() {
		imageFileName = "Binary_Image_" + imageFileName;
	}

	public File getBinaryImage(File imageFile, int theshold) {
		this.threshold = theshold;
		return process(imageFile);
	}

}
