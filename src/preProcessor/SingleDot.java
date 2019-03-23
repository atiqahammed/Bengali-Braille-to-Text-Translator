package preProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import dataStructure.Dot;
import dataStructure.Point;

public class SingleDot {

	int height;
	int width;
	BufferedImage testImg = null;
	private Color white = new Color(255, 255, 255);
	private Color black = new Color(0, 0, 0);
	private Color red = new Color(255, 0, 0);


	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();


	public File getRectangularDottedFile(File imageFile) {

		BufferedImage image;
		initializeVariables(imageFile);
		System.out.println(imageFile.getName());

		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {

				Point point = new Point(x, y);

				Color color = new Color(testImg.getRGB(x, y));
				int grayLevel = (int) color.getRed();


				if(grayLevel == 255) { // black pixel

					if(!isAPartOfExistingDot(point)) { //not in any dot

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

		for(int i = 0; i< uniqueDots.size(); i++) {

			Dot dot = uniqueDots.get(i);
			dot.processDot();

			for(int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
				for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
					outputImage.setRGB(x, y, white.getRGB());
				}
			}
		}

		Dot dot = uniqueDots.get(2);
		dot.processDot();

		for(int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
			for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
				outputImage.setRGB(x, y, red.getRGB());
			}
		}

		System.out.println(uniqueDots.size());

		File outputfile = new File("single.jpg");
		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		return outputfile;
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


		for(int i = -9; i <= 9; i++) {
			for(int j = -9; j <=9; j++) {
				int x = point.getX() + i;
				int y = point.getY() + j;

				if(x >= 0 && x< width && y >= 0 && y < height) {
					String stringIndex = getStringIndex(x, y);
					if(isAPartOfADot.containsKey(stringIndex)) {
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
