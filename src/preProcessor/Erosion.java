package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Erosion extends ImageProcessor{


	private int row[] = {-1, 0, 1, 0};
	private int col[] = {0, 1, 0, -1};
	protected BufferedImage image2;
	int total = 0;



	@Override
	protected void writePixel(int x, int y) {

		//System.out.println(height * width);
		Color c = new Color(image.getRGB(y, x));
		int grayScale = (int) c.getRed();

		/*if(grayScale == 0) {
			System.out.println("paichi");
			total++;
			System.out.println("-- " + total);
		}*/

		int count = 0;


		for(int i = 0; i < 4; i++) {
			int tempX = row[i];
			int tempY = col[i];
			if(tempX>= 0 && tempX < height && tempY >= 0 && tempY < width) {
				//System.out.println("in");
				Color color = new Color(image.getRGB(tempY, tempX));
				int grayValue = (int) color.getRed();
				//System.out.println(grayValue);
				if(grayValue == 0) {
					count++;
					System.out.println(i + " " + count);
				}
			}

		}



		Color color;
		if (count == 4) {
			color = new Color(0, 0,  0);
		}

		else {
			color = new Color(255, 255, 255);
		}

		//image2.setRGB(y, x, color.getRGB());
	}

	@Override
	protected void initializedPreOperation() {

		imageFileName = "EROSION_Image_" + imageFileName;
		image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	}

	public File getImage(File imageFile) {
		return convert(imageFile);
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

}
