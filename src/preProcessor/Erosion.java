package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Erosion {


	private int row[] = {-1, 0, 1, 0};
	private int col[] = {0, 1, 0, -1};


	public File getImage(File imageFile) {

		//File testImage = new File("input/test.jpg");
		BufferedImage testImg = null;

		try {
			testImg = ImageIO.read(imageFile);

		} catch (IOException e) {
			System.out.println("no input file");

		}


		int widthTest = testImg.getWidth();
		int heightTest = testImg.getHeight();

		Color myWhite = new Color(255, 255, 255); // Color white
		int rgb = myWhite.getRGB();

		BufferedImage theImage = new BufferedImage(widthTest, heightTest, BufferedImage.TYPE_INT_RGB);



		int blackCount = 0;

		for (int i = 0; i < widthTest; i++) {
			for (int j = 0; j < heightTest; j++) {
				Color c1 = new Color(testImg.getRGB(i, j));
				int grayValue = (int) c1.getRed();
				int count = 0;

				for(int index = 0; index<4; index++) {

					int tempX = j + row[i] ;
					int tempY = i + col[i];
					if(tempX>= 0 && tempX < heightTest && tempY >= 0 && tempY < widthTest) {
						//System.out.println("in");
						Color color = new Color(testImg.getRGB(tempY, tempX));
						int neighbourValue = (int) color.getRed();
						//System.out.println(grayValue);
						if(neighbourValue == 0) {
							count++;
							//System.out.println(i + " " + count);
						}
					}

				}

				if(count == 4)
					theImage.setRGB(i, j, 0);
				else
					theImage.setRGB(i, j, rgb);



				//if(grayValue == 0)
				//	blackCount++;
			}

		}

		System.out.println(blackCount);



		return null;
	}


}
