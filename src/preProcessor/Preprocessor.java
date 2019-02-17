package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Preprocessor implements IPreprocessor {

	protected BufferedImage image;
	protected int width;
	protected int height;
	protected String imageFileName;

	protected void initializeImage(File imageFile) {
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = image.getWidth();
		height = image.getHeight();
		imageFileName = getName(imageFile);
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

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				writePixel(i, j);
			}
		}
		return writeImage();
	}

	protected abstract void writePixel(int x, int y);
	protected abstract File writeImage();

}
