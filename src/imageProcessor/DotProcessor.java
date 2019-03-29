package imageProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import dataStructure.BrailleDor;
import dataStructure.Dot;
import dataStructure.Point;
import util.Utils;

public class DotProcessor {

	int height;
	int width;
	BufferedImage inputImage = null;
	BufferedImage outputImage;

	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();



	public File getRectangularDottedFile(File imageFile) {

		System.out.println("processing dot...");

		BufferedImage image;
		initializeVariables(imageFile);

		findDots();
		initializeOutputImage();

		System.out.println(uniqueDots.size());

		for (int i = 0; i < uniqueDots.size(); i++) {

			Dot dot = uniqueDots.get(i);
			dot.processDot();

			for (int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
				for (int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
					outputImage.setRGB(x, y, Utils.WHITE.getRGB());
				}
			}
		}


		// out put image initialization


		// nwe part
		int cnt = 0;
		ArrayList<Dot> FirstStepSelectedDot = new ArrayList<>();
		ArrayList<ArrayList<String>> twoDString = new ArrayList<>();


		for(int ii = 0; ii < uniqueDots.size(); ii++) {
			Map<String, Boolean> pixelCounted = new TreeMap<String, Boolean>();
			Dot dot = uniqueDots.get(ii);
			ArrayList<String> pixelList = new ArrayList<String>();
			ArrayList<String> oneDot = new ArrayList<>();

			boolean flag = false;

			for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
				for(int x = dot.getStartingX(); x <= dot.getEndingX(); x ++) {

					String pixel = getStringIndex(x, y);
					if(!isColor(y, x, Utils.WHITE, outputImage))
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

						if(isColor(tempY, tempX, Utils.WHITE, outputImage) && !pixelCounted.containsKey(tempPixelString)) {
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

			ArrayList<String> newString = new ArrayList<>();
			for(int i = 0; i < oneDot.size(); i++) {
				String s = oneDot.get(i);
				newString.add(s);

				String []arr = s.split("-");
				int x = Integer.parseInt(arr[0]);
				int y = Integer.parseInt(arr[1]);

				outputImage.setRGB(x, y, Utils.RED.getRGB());

			}

			System.out.println("-- " + newString.size() + "  index > " + cnt);
			twoDString.add(newString);

			cnt++;

		}

		System.out.println(twoDString.size());
		for(int i = 0; i < twoDString.size(); i++) {
			Dot tempDot = new Dot(twoDString.get(i));
			FirstStepSelectedDot.add(tempDot);
		}

		System.out.println(FirstStepSelectedDot.size());
		initializeOutputImage();

		for(int index = 0; index< twoDString.size(); index++) {

			ArrayList<String> xx = twoDString.get(index);


			BrailleDor brailleDor = new BrailleDor(xx);
			Point center = brailleDor.getCenter();

			for(int x = -1 ; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					int tempX = center.getX() + x;
					int tempY = center.getY() + y;


					if(tempY >= 0 && tempY < height && tempY >= 0 && tempY < width)
						outputImage.setRGB(tempX, tempY, Color.WHITE.getRed());
				}
			}


		}



		File outputfile = new File("dotDetected.jpg");
		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		return outputfile;
	}

	private void initializeOutputImage() {
		outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				outputImage.setRGB(x, y, Utils.BLACK.getRed());
			}
		}

	}

	private void findDots() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Point point = new Point(x, y);
				Color color = new Color(inputImage.getRGB(x, y));
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

	private void initializeVariables(File imageFile) {
		try {
			inputImage = ImageIO.read(imageFile);
			width = inputImage.getWidth();
			height = inputImage.getHeight();
		} catch (IOException e) {
			System.out.println("no input file");
		}

		uniqueDots = new ArrayList<>();
		isAPartOfADot = new TreeMap<String, Boolean>();
		indexStringToDotMapping = new TreeMap<String, Dot>();
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



}
