package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

public abstract class ImageProcessor implements IImageProcessor {

	protected BufferedImage image;
	protected int width;
	protected int height;
	protected String imageFileName;
	protected String imagePath;
	protected String imageType;

	protected void initializeImage(File imageFile) {
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = image.getWidth();
		height = image.getHeight();
		imageFileName = getName(imageFile);
		imageType = getImageType(imageFile);
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

	@Override
	public File convert(File imageFile) {
		initializeImage(imageFile);
		initializedPreOperation();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				writePixel(i, j);
			}
		}
		return getImageFile();
	}

	protected File getImageFile() {
		imagePath = imageFileName + "." + imageType;
		File ouptut = new File(imagePath);
		try {
			ImageIO.write(image, imageType, ouptut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ouptut;
	}

	protected abstract void writePixel(int x, int y);

	protected abstract void initializedPreOperation();

}
