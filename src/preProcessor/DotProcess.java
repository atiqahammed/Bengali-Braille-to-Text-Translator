package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

import javax.imageio.ImageIO;

//import com.sun.javafx.scene.traversal.WeightedClosestCorner;

import dataStructure.Dot;
import dataStructure.Point;

public class DotProcess {

	int height;
	int width;
	BufferedImage testImg = null;
	private Color white = new Color(255, 255, 255);
	private Color black = new Color(0, 0, 0);
	private Color red = new Color(255, 0, 0);
	private Color yellow = new Color(255, 255, 0);

	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();

	public File getRectangularDottedFile(File imageFile) {

		BufferedImage image;// = new BufferedImage();
		initializeVariables(imageFile);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Point point = new Point(x, y);
				Color color = new Color(testImg.getRGB(x, y));
				int grayLevel = (int) color.getRed();

				if (grayLevel == 255) { // black pixel
					if (!isAPartOfExistingDot(point)) { // not in any dot
						Dot newDot = new Dot();
						newDot.addPixels(point);
						String stringIndex = getStringIndex(point.getX(), point.getY());
						isAPartOfADot.put(stringIndex, true);
						indexStringToDotMapping.put(stringIndex, newDot);
						uniqueDots.add(newDot);

					}
				}
			}
		}

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				outputImage.setRGB(x, y, black.getRed());
			}
		}

		//// updating on median square

		ArrayList<Double> allArea = new ArrayList<Double>();

		System.out.println(uniqueDots.size());
		double totalArea = 0;

		for (int i = 0; i < uniqueDots.size(); i++) {

			Dot dot = uniqueDots.get(i);
			dot.processDot();

			for (int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
				for (int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
					outputImage.setRGB(x, y, white.getRGB());
				}
			}
		}




		int cnt = 0;

		for(int ii = 0; ii < uniqueDots.size(); ii++) {
			Map<String, Boolean> pixelCounted = new TreeMap<String, Boolean>();
			Dot dot = uniqueDots.get(ii);
			ArrayList<String> pixelList = new ArrayList<String>();
			ArrayList<String> oneDot = new ArrayList<>();

			boolean flag = false;

			for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
				for(int x = dot.getStartingX(); x <= dot.getEndingX(); x ++) {

					String pixel = getStringIndex(x, y);
					if(!isColor(y, x, white, outputImage))
						flag = true;
					pixelList.add(pixel);
					oneDot.add(pixel);
					pixelCounted.put(pixel, true);
					//count++;
				}
			}

			if(flag)
				continue;


			while(pixelList.size() > 0) {
				String currentPixel = pixelList.get(0);
				pixelList.remove(0);

				String []pixVal = currentPixel.split("-");
				int x = Integer.parseInt(pixVal[0]);
				int y = Integer.parseInt(pixVal[1]);


				for(int neighbourY = -1; neighbourY <= 1; neighbourY++) {
					for(int neighbourX = -1; neighbourX <= 1; neighbourX++) {
						int tempY = y + neighbourY;
						int tempX = x + neighbourX;

						String tempPixelString = getStringIndex(tempX, tempY);

						if(isColor(tempY, tempX, white, outputImage) && !pixelCounted.containsKey(tempPixelString)) {
							//System.out.println("paichi");
							//System.out.println(tempY + " " + tempX);
							pixelList.add(tempPixelString);
							pixelCounted.put(tempPixelString, true);
							oneDot.add(tempPixelString);

						}

					}
				}


				//break;/// error
			}

			for(int i = 0; i < oneDot.size(); i++) {
				String s = oneDot.get(i);

				String []arr = s.split("-");
				int x = Integer.parseInt(arr[0]);
				int y = Integer.parseInt(arr[1]);

				outputImage.setRGB(x, y, red.getRGB());
			}

			cnt++;


		}

		System.out.println(cnt);



		/*

		Map<String, Boolean> pixelCounted = new TreeMap<String, Boolean>();


		Dot dot = uniqueDots.get(1);

		System.out.println("--------------");
		System.out.println(dot.getStartingX() + " " + dot.getEndingY());
		System.out.println("--------------");
		ArrayList<String> pixelList = new ArrayList<String>();
		ArrayList<String> oneDot = new ArrayList<>();
		int count = 0;



		for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
			for(int x = dot.getStartingX(); x <= dot.getEndingX(); x ++) {
				String pixel = getStringIndex(x, y);
				pixelList.add(pixel);

				oneDot.add(pixel);

				pixelCounted.put(pixel, true);
				count++;
			}
		}/// traversing a single dot
		System.out.println(pixelList.size() +" pevious size");

		while(pixelList.size() > 0) {
			String currentPixel = pixelList.get(0);
			pixelList.remove(0);

			String []pixVal = currentPixel.split("-");
			int x = Integer.parseInt(pixVal[0]);
			int y = Integer.parseInt(pixVal[1]);


			for(int neighbourY = -1; neighbourY <= 1; neighbourY++) {
				for(int neighbourX = -1; neighbourX <= 1; neighbourX++) {
					int tempY = y + neighbourY;
					int tempX = x + neighbourX;

					String tempPixelString = getStringIndex(tempX, tempY);

					if(isColor(tempY, tempX, white, outputImage) && !pixelCounted.containsKey(tempPixelString)) {
						//System.out.println("paichi");
						//System.out.println(tempY + " " + tempX);
						pixelList.add(tempPixelString);
						pixelCounted.put(tempPixelString, true);
						oneDot.add(tempPixelString);

					}

				}
			}

			//break;/// error
		}


		System.out.println(oneDot.size() + " after size");
		//System.out.println("count of pixel " + count);


		for(int i = 0; i < oneDot.size(); i++) {
			String s = oneDot.get(i);

			String []arr = s.split("-");
			int x = Integer.parseInt(arr[0]);
			int y = Integer.parseInt(arr[1]);

			outputImage.setRGB(x, y, red.getRGB());
		}





		*/




		File outputfile = new File("dotDetected.jpg");
		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		return outputfile;
	}

	private boolean isAPixelIndex(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height)
			return true;
		return false;
	}

	private boolean isColor(int y, int x, Color color, BufferedImage bufferedImage) {
		Color clr = new Color(bufferedImage.getRGB(x, y));
		return isColorEquals(color, clr);
	}

	private boolean isColorEquals(Color color, Color clr) {
		if (clr.getRed() == color.getRed() && clr.getGreen() == color.getGreen() && clr.getBlue() == color.getBlue())
			return true;
		return false;
	}

	private void initializeVariables(File imageFile) {
		try {
			testImg = ImageIO.read(imageFile);
			width = testImg.getWidth();
			height = testImg.getHeight();
		} catch (IOException e) {
			System.out.println("no input file");
		}

		uniqueDots = new ArrayList<>();
		isAPartOfADot = new TreeMap<String, Boolean>();
		indexStringToDotMapping = new TreeMap<String, Dot>();
	}

	private boolean isAPartOfExistingDot(Point point) {

		for (int i = -9; i <= 9; i++) {
			for (int j = -9; j <= 9; j++) {
				int x = point.getX() + i;
				int y = point.getY() + j;

				if (x >= 0 && x < width && y >= 0 && y < height) {
					String stringIndex = getStringIndex(x, y);
					if (isAPartOfADot.containsKey(stringIndex)) {
						Dot existingDot = indexStringToDotMapping.get(stringIndex);
						existingDot.addPixels(point);
						return true;
					}
				}

			}
		}

		return false;
	}

	private String getStringIndex(int x, int y) {
		return Integer.toString(x) + "-" + Integer.toString(y);
	}

}
