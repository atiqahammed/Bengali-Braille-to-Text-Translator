package imageProcessor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

public abstract class ImageProcessor {

	protected BufferedImage inputImage;
	protected BufferedImage outputImage;

	protected int imageHeight;
	protected int imageWidth;

	protected String imageFileName;
	protected String imageType;

	protected void initializeImageInformation(File imageFile) {
		try {
			inputImage = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imageWidth = inputImage.getWidth();
		imageHeight = inputImage.getHeight();
		imageFileName = getName(imageFile);
		imageType = getImageType(imageFile);
		outputImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
	}

	protected String getImageType(File imageFile) {
		String srting = FileSystemView.getFileSystemView().getSystemTypeDescription(imageFile);
		String[] splited = srting.toLowerCase().split(" ");
		String type = splited[0];
		return type;
	}

	protected String getName(File imageFile) {
		String nameWithExtension = stripExtension(imageFile.getName());
		return nameWithExtension;
	}


	protected String stripExtension(String str) {
		if (str == null)
			return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1)
			return str;
		return str.substring(0, pos);
	}

	protected abstract void operatePixel(int indexOfRow, int indexOfColumn);
	protected abstract void init();

	public File process(File inputFile) {
		initializeImageInformation(inputFile);
		init();

		for (int rowIndex = 0; rowIndex < imageHeight; rowIndex++) {
			for (int columnIndex = 0; columnIndex < imageWidth; columnIndex++) {
				operatePixel(rowIndex, columnIndex);
			}
		}

		return getImageFile();
	}

	protected File getImageFile() {
		String imagePath = imageFileName + "." + imageType;
		File ouptut = new File(imagePath);
		try {
			ImageIO.write(outputImage, imageType, ouptut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ouptut;
	}

}
