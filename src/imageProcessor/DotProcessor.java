package imageProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	Map<Integer, ArrayList<Integer>> lineMapped = new TreeMap<Integer, ArrayList<Integer>>();
	Map<Integer, ArrayList<Integer>> lineMappedXX = new TreeMap<Integer, ArrayList<Integer>>();
	Map<Integer, ArrayList<Point>> lineMapped2 = new TreeMap<Integer, ArrayList<Point>>();







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
							pixelList.add(tempPixelString);
							pixelCounted.put(tempPixelString, true);
							oneDot.add(tempPixelString);

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

				outputImage.setRGB(x, y, Utils.RED.getRGB());

			}

			//System.out.println("-- " + newString.size() + "  index > " + cnt);
			twoDString.add(newString);

			cnt++;

		}

		//System.out.println(twoDString.size());
		for(int i = 0; i < twoDString.size(); i++) {
			Dot tempDot = new Dot(twoDString.get(i));
			FirstStepSelectedDot.add(tempDot);
		}

		//System.out.println(FirstStepSelectedDot.size());
		initializeOutputImage();

		ArrayList<Point> allCenter = new ArrayList<>();

		for(int index = 0; index< twoDString.size(); index++) {

			ArrayList<String> xx = twoDString.get(index);
			BrailleDor brailleDor = new BrailleDor(xx);
			Point center = brailleDor.getCenter();
			allCenter.add(center);

			for(int x = -1 ; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					int tempX = center.getX() + x;
					int tempY = center.getY() + y;

					if(tempY >= 0 && tempY < height && tempY >= 0 && tempY < width)
						outputImage.setRGB(tempX, tempY, Color.WHITE.getRGB());
				}
			}
		}




		int iniTialPoint = -1;
		int count = 0;
		int maxH = 20;
		ArrayList<Integer> lineIndex = new ArrayList<Integer>();
		ArrayList<Integer> dotsInLine = new ArrayList<Integer>();
		ArrayList<Integer> dotsInLinexx = new ArrayList<Integer>();

		ArrayList<Point> pointInLine = new ArrayList<Point>();


		for(int i = 0; i < allCenter.size(); i++) {


			int centerY = allCenter.get(i).getY();
			//

			if(centerY - iniTialPoint >= maxH) {
				if(count > 2) {
					lineIndex.add(iniTialPoint);
					lineMapped.put(iniTialPoint, (ArrayList<Integer>) dotsInLine.clone());
					lineMapped2.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());
					lineMappedXX.put(iniTialPoint, (ArrayList<Integer>) dotsInLinexx.clone());

				}
				dotsInLine = new ArrayList<>();
				pointInLine = new ArrayList<Point>();
				dotsInLinexx = new ArrayList<>();

				dotsInLine.add(centerY);
				dotsInLinexx.add(allCenter.get(i).getX());
				pointInLine.add(allCenter.get(i));


				count = 1;
				iniTialPoint = centerY;

			} else {
				count++;
				dotsInLine.add(centerY);
				dotsInLinexx.add(allCenter.get(i).getX());
				pointInLine.add(allCenter.get(i));
			}
		}

		if(count > 2) {
			lineIndex.add(iniTialPoint);
			lineMapped.put(iniTialPoint, (ArrayList<Integer>) dotsInLine.clone());
			lineMappedXX.put(iniTialPoint, (ArrayList<Integer>) dotsInLinexx.clone());
			lineMapped2.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());

		}



		for(int i = 0; i < lineIndex.size(); i++) {
			int key = lineIndex.get(i);
			ArrayList<Integer> listX = lineMappedXX.get(key);
			Collections.sort(listX);
			lineMappedXX.put(key, listX);
			//lineMappedXX.put(lineIndex.get(i), Collections.sort(lineMappedXX.get(lineIndex.get(i))));
		}


		System.out.println(lineIndex.size());




		for(int i = 0; i < lineIndex.size(); i++) {

			int index = lineIndex.get(i);
			ArrayList<Integer> xx = lineMappedXX.get(index);
			ArrayList<Point> pp = lineMapped2.get(index);
			// coloring line
			for(int x = 0; x < width; x++) {
				outputImage.setRGB(x, lineIndex.get(i), Utils.RED.getRGB());
			}
		}


		// removing noice in a single line

		for(int i = 0; i < lineIndex.size(); i++) {
			ArrayList<Integer> currentLine = lineMappedXX.get(lineIndex.get(i));
			int previousXIndexOfDot = currentLine.get(0);

			ArrayList<Integer> indexsToRemove = new ArrayList<Integer>();
			for(int j = 1; j < currentLine.size(); j++) {
				if(currentLine.get(j) - previousXIndexOfDot < 20) {
					indexsToRemove.add(previousXIndexOfDot);
				}

				previousXIndexOfDot = currentLine.get(j);
			}

			for(int j = 0; j < indexsToRemove.size(); j++) {
				currentLine.remove(indexsToRemove.get(j));
			}
			//System.out.println(previousXIndexOfDot);
		}



		for(int i = 3; i < 4; i = i+3) {

			ArrayList<Integer> firstLine = lineMappedXX.get(lineIndex.get(i + 0));
			ArrayList<Integer> secondLine = lineMappedXX.get(lineIndex.get(i + 1));
			ArrayList<Integer> thirdLine = lineMappedXX.get(lineIndex.get(i + 2));

			Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);

			/*
			ArrayList<String> letters = new ArrayList<String>();


			int initialUpperDot = firstLine.get(0);
			int initialMiddelDot = secondLine.get(0);
			int initialLowerDot = thirdLine.get(0);

			int smallestXIndex = initialUpperDot;
			if(initialMiddelDot < smallestXIndex) smallestXIndex = initialMiddelDot;
			if(initialLowerDot < smallestXIndex) smallestXIndex = initialLowerDot;

			Utils.FUNCTIONS.printFirstCurrentLine(firstLine, secondLine, thirdLine);


			int previousIndex = smallestXIndex - 35;

			while(firstLine.size() > 0 || secondLine.size() > 0|| thirdLine.size() > 0) {
				String letter = "000000";

				int currentIndex = -1;
				int nextIndex = -1;

				int upperDot = -1;
				int middelDot = -1;
				int lowerDot = -1;

				int nextUpperDot = -1;
				int nextMiddelDot = -1;
				int nextLowerDot = -1;


				// first level identification....
				if(firstLine.size() > 0) {
					if(firstLine.get(0) - previousIndex >= 25 && firstLine.get(0) - previousIndex <= 45) {
						upperDot = firstLine.get(0);
						firstLine.remove(0);
					}
				}

				if(secondLine.size() > 0) {
					if(secondLine.get(0) - previousIndex >= 25 && secondLine.get(0) - previousIndex <= 45) {
						middelDot = secondLine.get(0);
						secondLine.remove(0);
					}
				}

				if(thirdLine.size() > 0) {
					if(thirdLine.get(0) - previousIndex >= 25 && thirdLine.get(0) - previousIndex <= 45) {
						lowerDot = thirdLine.get(0);
						thirdLine.remove(0);
					}
				}

				if(upperDot > 0) currentIndex = upperDot;
				if(currentIndex < 0 && middelDot > 0) currentIndex = middelDot;
				if(middelDot >0 && middelDot < currentIndex) currentIndex = middelDot;
				if(currentIndex < 0 && lowerDot > 0) currentIndex = lowerDot;
				if(lowerDot > 0 && lowerDot < currentIndex) currentIndex = lowerDot;
				//if(middelDot > 0 && middelDot < )




				System.out.println(upperDot +" "+ middelDot+ " "+ lowerDot);





				System.out.println(currentIndex);

				Utils.FUNCTIONS.printFirstCurrentLine(firstLine, secondLine, thirdLine);


				// second level identification....
				if(firstLine.size() > 0) {
					if(firstLine.get(0) - currentIndex>= 25 && firstLine.get(0) - currentIndex <= 45) {
						nextUpperDot = firstLine.get(0);
						firstLine.remove(0);
					}
				}

				if(secondLine.size() > 0) {
					if(secondLine.get(0) - currentIndex>= 25 && secondLine.get(0) - currentIndex<= 45) {
						nextMiddelDot = secondLine.get(0);
						secondLine.remove(0);
					}
				}

				if(thirdLine.size() > 0) {
					if(thirdLine.get(0) - currentIndex >= 25 && thirdLine.get(0) - currentIndex <= 45) {
						nextLowerDot = thirdLine.get(0);
						thirdLine.remove(0);
					}
				}

				System.out.println(nextUpperDot+" "+ nextMiddelDot +" "+ nextLowerDot+ "  >");
				if(nextUpperDot == -1 && nextMiddelDot == -1 && nextLowerDot == -1) {
					if(upperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
					if(middelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
					if(lowerDot!= -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

					letters.add(Utils.LETTERS.getLetters(letter));

					ArrayList<Integer> tempIndex = new ArrayList<Integer>();
					if(firstLine.size() > 0) tempIndex.add(firstLine.get(0));
					if(secondLine.size() > 0) tempIndex.add(secondLine.get(0));
					if(thirdLine.size() > 0) tempIndex.add(thirdLine.get(0));

					Collections.sort(tempIndex);
					if(tempIndex.size()> 0)
					previousIndex = tempIndex.get(0) - 35;
					System.out.println("previous index " + previousIndex);

				}

				else {
					if(upperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
					if(middelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
					if(lowerDot!= -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

					if(nextUpperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 3);
					if(nextMiddelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 4);
					if(nextLowerDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 5);

					System.out.println(letter);

					letters.add(Utils.LETTERS.getLetters(letter));

					ArrayList<Integer> tempIndex = new ArrayList<Integer>();
					if(firstLine.size() > 0) tempIndex.add(firstLine.get(0));
					if(secondLine.size() > 0) tempIndex.add(secondLine.get(0));
					if(thirdLine.size() > 0) tempIndex.add(thirdLine.get(0));

					Collections.sort(tempIndex);
					if(tempIndex.size() > 0)
					previousIndex = tempIndex.get(0) - 35;
					System.out.println("previous index " + previousIndex);

				}

				System.out.println(letters);



				break;
			}*/


			//System.out.println(letters);

			break;




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