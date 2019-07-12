package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import dataStructure.BrailleDot;
import dataStructure.Dot;
import dataStructure.Point;
import util.Utils;

public class TextConvertor {
	

	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();
	Map<Integer, ArrayList<Point>> lineMapped2 = new TreeMap<Integer, ArrayList<Point>>();
	ArrayList<ArrayList<String>> twoDString = null;



	// line information
	ArrayList<Integer> lineIndex = new ArrayList<Integer>();
	ArrayList<Point> pointInLine = new ArrayList<Point>();
	
	public ArrayList<ArrayList<String>> getText(ArrayList<ArrayList<Color>> imageArray) {

		initializeVariables();
		findDots();
		processUniqueDots();
		
		ArrayList<Dot> firstStepSelectedDot = selectDotInFirstStep();
		initializeOutputImage();
		ArrayList<Point> allCenter = getAllCenter();
		System.out.println(twoDString.size());
////
//		processLineInformation(allCenter);
////
//		Collections.sort(lineIndex);
		
		
		return null;
	}
	
	private void initializeOutputImage() {
//		outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < Utils.IMAGE_HEIGHT; y++) {
			for (int x = 0; x < Utils.IMAGE_WEIDTH; x++) {
//				outputImage.setRGB(x, y, Utils.BLACK.getRed());
				Utils.IMAGE_ARRAY_OF_PIXEL.get(y).set(x, Utils.BLACK);
			}
		}
	}
	
	private ArrayList<Point> getAllCenter() {
		ArrayList<Point> allCenter = new ArrayList<>();

//		for(int i = 0; i < 100; i++) 
//			for(int j = 0; j < 100; j++) {
//				Utils.IMAGE_ARRAY_OF_PIXEL.get(i).set(j, Utils.YELLOW);
//			}
		
		for(int index = 0; index< twoDString.size(); index++) {
//			System.out.println(index);

			ArrayList<String> xx = twoDString.get(index);
			BrailleDot brailleDot = new BrailleDot(xx);
			Point center = brailleDot.getCenter();
			System.out.println(center.getX() + " " + center.getY());
			allCenter.add(center);

			for(int x = -1 ; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					int tempX = center.getX() + x;
					int tempY = center.getY() + y;

					if(tempY >= 0 && tempY < Utils.APPLICATION_HEIGHT && tempX >= 0 && tempX < Utils.IMAGE_WEIDTH)
//						outputImage.setRGB(tempX, tempY, Color.WHITE.getRGB());
						Utils.IMAGE_ARRAY_OF_PIXEL.get(tempY).set(tempX, Utils.WHITE);
				}
			}
		}
		return allCenter;
	}
	
	private void processLineInformation(ArrayList<Point> allCenter) {
		int iniTialPoint = 1;
		int count = 0;
		int maxH = Utils.INITAL_DIFFRENCE_BETWEEN_LINE;

		for(int i = 0; i < allCenter.size(); i++) {

			int centerY = allCenter.get(i).getY();

			if(centerY - iniTialPoint >= maxH) {
				if(count > 0) {
					lineIndex.add(iniTialPoint);
					lineMapped2.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());
				}

				pointInLine = new ArrayList<Point>();
				pointInLine.add(allCenter.get(i));
				count = 1;
				iniTialPoint = centerY;

			} else {
				count++;
				pointInLine.add(allCenter.get(i));
			}
		}

		if(count > 0) {
			lineIndex.add(iniTialPoint);
			lineMapped2.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());
		}
	}

	
	private ArrayList<Dot> selectDotInFirstStep() {
		ArrayList<Dot> FirstStepSelectedDot = new ArrayList<>();
		twoDString = new ArrayList<>();


		for(int ii = 0; ii < uniqueDots.size(); ii++) {
			Map<String, Boolean> pixelCounted = new TreeMap<String, Boolean>();
			Dot dot = uniqueDots.get(ii);
			ArrayList<String> pixelList = new ArrayList<String>();
			ArrayList<String> oneDot = new ArrayList<>();

			boolean flag = false;

			for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
				for(int x = dot.getStartingX(); x <= dot.getEndingX(); x ++) {

					String pixel = getStringIndex(x, y);
					if(Utils.IMAGE_ARRAY_OF_PIXEL.get(y).get(x).getRed() != Utils.WHITE.getRed())
						flag = true;
					pixelList.add(pixel);
					oneDot.add(pixel);
					pixelCounted.put(pixel, true);
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

						if(tempY < Utils.IMAGE_HEIGHT && tempY >= 0 && tempX < Utils.APPLICATION_WIDTH && tempX >= 0) {
							if(
									Utils.IMAGE_ARRAY_OF_PIXEL.get(tempY).get(tempX).getRed()==/*isColor(tempY, tempX,*/ Utils.WHITE.getRed() && !pixelCounted.containsKey(tempPixelString)) {
								pixelList.add(tempPixelString);
								pixelCounted.put(tempPixelString, true);
								oneDot.add(tempPixelString);
							}
						}

					}
				}

			}

			ArrayList<String> newString = new ArrayList<>();
			for(int i = 0; i < oneDot.size(); i++) {
				String s = oneDot.get(i);
				newString.add(s);

				String []arr = s.split("-");
				int x = Integer.parseInt(arr[0]);
				int y = Integer.parseInt(arr[1]);

				Utils.IMAGE_ARRAY_OF_PIXEL.get(y).set(x, Utils.RED);
//				outputImage.setRGB(x, y, Utils.RED.getRGB());

			}
			twoDString.add(newString);
		}

		for(int i = 0; i < twoDString.size(); i++) {
			Dot tempDot = new Dot(twoDString.get(i));
			FirstStepSelectedDot.add(tempDot);
		}
		return FirstStepSelectedDot;
	}
	
	private void processUniqueDots() {
		for (int i = 0; i < uniqueDots.size(); i++) {

			Dot dot = uniqueDots.get(i);
			dot.processDot();

			for (int x = dot.getStartingX(); x <= dot.getEndingX(); x++) 
				for (int y = dot.getStartingY(); y <= dot.getEndingY(); y++) 
					Utils.IMAGE_ARRAY_OF_PIXEL.get(y).set(x, Utils.WHITE);
		}

	}
	
	private void initializeVariables() {
		uniqueDots = new ArrayList<>();
		isAPartOfADot = new TreeMap<String, Boolean>();
		indexStringToDotMapping = new TreeMap<String, Dot>();
	}
	
	private void findDots() {
		for (int y = 0; y < Utils.IMAGE_HEIGHT; y++) {
			for (int x = 0; x < Utils.IMAGE_WEIDTH; x++) {
				Point point = new Point(x, y);
				Color color = Utils.IMAGE_ARRAY_OF_PIXEL.get(y).get(x);//new Color(inputImage.getRGB(x, y));
				int grayLevel = (int) color.getRed();

				if (grayLevel == 255) { // white pixel
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

		int netghborDotSize = Utils.NEIGHBOUR_DOT_SIZE_FOR_PART_OF_DOT_SELECTION;

		for (int i = -netghborDotSize; i <= netghborDotSize; i++) {
			for (int j = -netghborDotSize; j <= netghborDotSize; j++) {
				int x = point.getX() + i;
				int y = point.getY() + j;

				if (x >= 0 && x < Utils.IMAGE_WEIDTH && y >= 0 && y < Utils.IMAGE_HEIGHT) {
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
