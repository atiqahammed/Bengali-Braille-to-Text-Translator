package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.sun.javafx.scene.traversal.WeightedClosestCorner;

public class Temp {


	private int x[] = {-1, -1, -1, 0, 0, 1, 1, 1};
	private int y[] = {-1,  0, 1, -1, 1, -1, 0, 1};
	private Map<String, Boolean> indexTaken = new TreeMap<String, Boolean>();
	private ArrayList<ArrayList<String>> allDot = new ArrayList<ArrayList<String>>();
	private Map<String, ArrayList<String>> pixelDotMap = new TreeMap<String, ArrayList<String>>();

	public void run(File imageFile) {

		ArrayList<ArrayList<String>> dotList = getAllDots(imageFile);
		ArrayList<ArrayList<String>> selection1 = new ArrayList<ArrayList<String>>();


		System.out.println(dotList.size());


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
		for (int i = 0; i < widthTest; i++) {
			for (int j = 0; j < heightTest; j++) {
				theImage.setRGB(i, j, rgb);
			}
		}


		for(int i = 0;i < allDot.size(); i++) {
			ArrayList<String> currentDot = allDot.get(i);

			if(currentDot.size() > 10 && currentDot.size() < 100) {

				selection1.add(currentDot);

				/*
				for(int j = 0; j < currentDot.size(); j++) {
					String index = currentDot.get(j);
					String [] arr = index.split("-");
					int yy = Integer.parseInt(arr[0]);
					int xx = Integer.parseInt(arr[1]);

					theImage.setRGB(yy, xx, 0);
				}*/
			}
		}

		for(int i = 0; i < selection1.size(); i++) {
			ArrayList<String> currentDot = selection1.get(i);

			for(int j = 0; j < currentDot.size(); j++) {
				colorThisPoint(currentDot.get(j), theImage, 0);
			}
		}


		File outputfile = new File("abc.jpg");
		try {
			ImageIO.write(theImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

	}

	private void colorThisPoint(String index, BufferedImage theImage, int rgb) {
		String [] arr = index.split("-");
		int yy = Integer.parseInt(arr[0]);
		int xx = Integer.parseInt(arr[1]);

		theImage.setRGB(yy, xx, rgb);

	}



	private ArrayList<ArrayList<String>> getAllDots(File imageFile) {
		BufferedImage testImg = null;

		try {
			testImg = ImageIO.read(imageFile);

		} catch (IOException e) {
			System.out.println("no input file");

		}

		int widthTest = testImg.getWidth();
		int heightTest = testImg.getHeight();

		int count = 0;
		ArrayList<String> strings = new ArrayList<>();











		for (int i = 0; i < widthTest; i++) {
			for (int j = 0; j < heightTest; j++) {
				Color c1 = new Color(testImg.getRGB(i, j));
				String index = getStringIndex(i, j);

				int value = (int) c1.getBlue();
				if (value == 0) {
					count++;


					boolean neighbourNotListed = true;
					for(int k = 0; k < 8; k++) {

						int tempY = i + y[k];
						int tempX = j + x[k];

						if(tempX >= 0 && tempX < heightTest && tempY >= 0 && tempY < widthTest) {
							Color c = new Color(testImg.getRGB(i, j));
							int colorValue = c.getBlue();

							if(colorValue == 0) {

								String neighbourIndex = getStringIndex(tempY, tempX);
								if(indexTaken.containsKey(neighbourIndex)) {
									neighbourNotListed = false;
									ArrayList<String> currentDot = pixelDotMap.get(neighbourIndex);
									currentDot.add(index);
									pixelDotMap.put(index, currentDot);
									break;
								}
								//break;
							}
						}
					}


					if(neighbourNotListed) {
						ArrayList<String> dot = new ArrayList<String>();

						dot.add(index);

						pixelDotMap.put(index, dot);

						allDot.add(dot);
					}

					indexTaken.put(index, true);






					//String index = getStringIndex(i, j);
					//strings.add(index);

				}

			}
		}
		return allDot;
	}

	private String getStringIndex(int i, int j) {
		return Integer.toString(i) + "-" + Integer.toString(j);
	}

}
