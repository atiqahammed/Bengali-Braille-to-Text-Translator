package imageProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
	ArrayList<ArrayList<String>> twoDString = null;



	// line information
	ArrayList<Integer> lineIndex = new ArrayList<Integer>();
	ArrayList<Integer> dotsInLine = new ArrayList<Integer>();
	ArrayList<Integer> dotsInLinexx = new ArrayList<Integer>();
	ArrayList<Point> pointInLine = new ArrayList<Point>();

	public File getRectangularDottedFile(File imageFile) {

		System.out.println("processing dot...");

		BufferedImage image;
		initializeVariables(imageFile);

		findDots();
		initializeOutputImage();
//		System.out.println("number of unique dot :: " + uniqueDots.size());
		processUniqueDots();
		ArrayList<Dot> firstStepSelectedDot = selectDotInFirstStep();
//		System.out.println(firstStepSelectedDot.size());
		initializeOutputImage();
		ArrayList<Point> allCenter = getAllCenter();






		//System.out.println(twoDString);

//		System.out.println("dot test");
//		for(int i =0; i < 10; i++)
//			System.out.println(allCenter.get(i).getX() + " " + allCenter.get(i).getY());


		processLineInformation(allCenter);

//////

		for(int i = 0; i < lineIndex.size(); i++) {
			int key = lineIndex.get(i);
			ArrayList<Integer> listX = lineMappedXX.get(key);
			Collections.sort(listX);
			lineMappedXX.put(key, listX);
			//lineMappedXX.put(lineIndex.get(i), Collections.sort(lineMappedXX.get(lineIndex.get(i))));
		}


//		System.out.println(lineIndex.size());

		Collections.sort(lineIndex);


		//colorLine(lineIndex);


//		System.out.println("size of lines:: " + lineIndex.size());
		ArrayList<Integer> differenceBetweenLines = new ArrayList<Integer>();


//		for(int i = 0; i < lineIndex.size(); i++) {
//			System.out.println(lineIndex.get(i));
//		}

		for(int i = 1; i < lineIndex.size(); i++) {
			differenceBetweenLines.add(lineIndex.get(i) - lineIndex.get(i - 1));
		}


//		System.out.println(differenceBetweenLines);

		Collections.sort(differenceBetweenLines);
//		System.out.println(differenceBetweenLines);


		int lineDistance = differenceBetweenLines.get(differenceBetweenLines.size() / 2);
//		System.out.println(lineDistance);
		int limit = (int) ((double) lineDistance * 0.30);
//		System.out.println(limit);

//		for(int i = 0; i < lineIndex.size(); i++) {
//			ArrayList<Integer> currentLine = lineMappedXX.get(lineIndex.get(i));
//			System.out.println(lineIndex.get(i) + "    ...  " + currentLine.size());
//		}


		ArrayList<Integer> newLineIndex = new ArrayList<Integer>();

		for(int i = 0; i < lineIndex.size(); i++) {

			if(i + 3 < lineIndex.size()) {
				int firstLineIndex = lineIndex.get(i);
				int secondLineIndex = lineIndex.get(i + 1);
				int ThirdLineIndex = lineIndex.get(i + 2);
				int forthLineIndex = lineIndex.get(i + 3);

				Boolean isFirstThreeLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, ThirdLineIndex, lineDistance, limit);
				Boolean isLastThreeLineShouldTake = isPartOfLine(secondLineIndex, ThirdLineIndex, forthLineIndex, lineDistance, limit);

				if(isFirstThreeLineShouldTake && isLastThreeLineShouldTake) {

					double firstAverage = (lineMappedXX.get(firstLineIndex).size() + lineMappedXX.get(secondLineIndex).size() + lineMappedXX.get(ThirdLineIndex).size()) / 3;
					double lastAverage = (lineMappedXX.get(forthLineIndex).size() + lineMappedXX.get(secondLineIndex).size() + lineMappedXX.get(ThirdLineIndex).size()) / 3;

					System.out.println(firstAverage + "  fi");
					System.out.println(lastAverage + "  la");

					if(firstAverage > lastAverage) {
						newLineIndex.add(firstLineIndex);
						newLineIndex.add(secondLineIndex);
						newLineIndex.add(ThirdLineIndex);

						i += 2;
					} else {
						newLineIndex.add(secondLineIndex);
						newLineIndex.add(ThirdLineIndex);
						newLineIndex.add(forthLineIndex);

						i += 3;
					}
				}

				else if(isFirstThreeLineShouldTake && !isLastThreeLineShouldTake) {
					newLineIndex.add(firstLineIndex);
					newLineIndex.add(secondLineIndex);
					newLineIndex.add(ThirdLineIndex);

					i += 2;
				}

				else if(!isFirstThreeLineShouldTake && isLastThreeLineShouldTake) {
					newLineIndex.add(secondLineIndex);
					newLineIndex.add(ThirdLineIndex);
					newLineIndex.add(forthLineIndex);

					i += 3;
				}



			}

			else if(i + 2 < lineIndex.size()) {
				int firstLineIndex = lineIndex.get(i);
				int secondLineIndex = lineIndex.get(i + 1);
				int ThirdLineIndex = lineIndex.get(i + 2);

				Boolean isLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, ThirdLineIndex, lineDistance, limit);
				if(isLineShouldTake) {
					newLineIndex.add(firstLineIndex);
					newLineIndex.add(secondLineIndex);
					newLineIndex.add(ThirdLineIndex);

					i += 2;
				}
			}
		}

		colorLine(newLineIndex);

		System.out.println("new line Index:  "+ newLineIndex);
		System.out.println("previous line diff :" + lineIndex.size() + lineIndex);
		System.out.println("previous line diff :" + newLineIndex.size() + newLineIndex);
		ArrayList<Integer> missedLineIndex = getMissedLineIndex(lineIndex, newLineIndex);
		System.out.println("missed line index : " + missedLineIndex);

		for(int i = 0; i < missedLineIndex.size(); i++) {
			int indexOfAMissedLine = missedLineIndex.get(i);

			ArrayList<Point> dotlistOfMissedLine = lineMapped2.get(indexOfAMissedLine);
			for(int j = 0; j < dotlistOfMissedLine.size(); j++) {
				Point point = dotlistOfMissedLine.get(j);
				int nearestLineIndex = getNearestLineIndex(point.getY(), newLineIndex);


				if(Math.abs(point.getY() - nearestLineIndex) <= Math.abs(point.getY() - indexOfAMissedLine)) {
//					System.out.println("nearest index of line:: " + nearestLineIndex);
//					System.out.println("point info X . Y :: " + point.getX() + " . " + point.getY());

					lineMappedXX.get(nearestLineIndex).add(point.getX());
					Collections.sort(lineMappedXX.get(nearestLineIndex));

				}
			}
		}




//		ArrayList<Integer> allHorizontalDifference = new ArrayList<Integer>();
//		for(int i = 0; i < newLineIndex.size(); i++) {
//
//			ArrayList<Integer> currentLineXArray = lineMappedXX.get(newLineIndex.get(i));
//			System.out.println("line : " + currentLineXArray);
//			System.out.println("si " + currentLineXArray.size());
//
//			for(int j = 1; j <  currentLineXArray.size(); j++) {
//				allHorizontalDifference.add(currentLineXArray.get(j) - currentLineXArray.get(j-1));
//			}
//
//		}
//		Collections.sort(allHorizontalDifference);
//		int horizontalMedianDistance = allHorizontalDifference.get(allHorizontalDifference.size()/2);
//		System.out.println("horizontal median distance :: " + horizontalMedianDistance);

		//System.out.println(allHorizontalDifference);


		// removing noice in a single line

		for(int i = 0; i < newLineIndex.size(); i++) {
			ArrayList<Integer> currentLine = lineMappedXX.get(newLineIndex.get(i));
			int previousXIndexOfDot = currentLine.get(0);

			ArrayList<Integer> indexsToRemove = new ArrayList<Integer>();
			for(int j = 1; j < currentLine.size(); j++) {
				if(currentLine.get(j) - previousXIndexOfDot < Utils.SAME_POINT_COVERAGE) {
					indexsToRemove.add(previousXIndexOfDot);
				}

				previousXIndexOfDot = currentLine.get(j);
			}

			for(int j = 0; j < indexsToRemove.size(); j++) {
				currentLine.remove(indexsToRemove.get(j));
			}
			//System.out.println(previousXIndexOfDot);
		}

////////////////////////

		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();

		for(int i = 0; i < newLineIndex.size(); i = i+3) {

			ArrayList<Integer> firstLine = lineMappedXX.get(newLineIndex.get(i + 0));
			ArrayList<Integer> secondLine = lineMappedXX.get(newLineIndex.get(i + 1));
			ArrayList<Integer> thirdLine = lineMappedXX.get(newLineIndex.get(i + 2));

			//Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);


			ArrayList<String> letters = new ArrayList<String>();


			int initialUpperDot = firstLine.get(0);
			int initialMiddelDot = secondLine.get(0);
			int initialLowerDot = thirdLine.get(0);

			int smallestXIndex = initialUpperDot;
			if(initialMiddelDot < smallestXIndex) smallestXIndex = initialMiddelDot;
			if(initialLowerDot < smallestXIndex) smallestXIndex = initialLowerDot;

			Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);


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
					if(firstLine.get(0) - previousIndex >= 20 && firstLine.get(0) - previousIndex <= 45) {
						upperDot = firstLine.get(0);
						firstLine.remove(0);
					}
				}

				if(secondLine.size() > 0) {
					if(secondLine.get(0) - previousIndex >= 20 && secondLine.get(0) - previousIndex <= 45) {
						middelDot = secondLine.get(0);
						secondLine.remove(0);
					}
				}

				if(thirdLine.size() > 0) {
					if(thirdLine.get(0) - previousIndex >= 20 && thirdLine.get(0) - previousIndex <= 45) {
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





				System.out.println("current Index " + currentIndex);

				Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);


				// second level identification....
				if(firstLine.size() > 0) {
					if(firstLine.get(0) - currentIndex >= 20 && firstLine.get(0) - currentIndex <= 45) {
						nextUpperDot = firstLine.get(0);
						firstLine.remove(0);
					}
				}

				if(secondLine.size() > 0) {
					if(secondLine.get(0) - currentIndex >= 20 && secondLine.get(0) - currentIndex<= 45) {
						nextMiddelDot = secondLine.get(0);
						secondLine.remove(0);
					}
				}

				if(thirdLine.size() > 0) {
					if(thirdLine.get(0) - currentIndex >= 20 && thirdLine.get(0) - currentIndex <= 45) {
						nextLowerDot = thirdLine.get(0);
						thirdLine.remove(0);
					}
				}

				System.out.println(nextUpperDot+" "+ nextMiddelDot +" "+ nextLowerDot+ "  >");
				if(nextUpperDot == -1 && nextMiddelDot == -1 && nextLowerDot == -1) {
					if(upperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
					if(middelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
					if(lowerDot!= -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

					System.out.println(letter);
					letters.add(Utils.LETTERS.getLetters(letter));

					ArrayList<Integer> tempIndex = new ArrayList<Integer>();
					if(firstLine.size() > 0) tempIndex.add(firstLine.get(0));
					if(secondLine.size() > 0) tempIndex.add(secondLine.get(0));
					if(thirdLine.size() > 0) tempIndex.add(thirdLine.get(0));

					Collections.sort(tempIndex);
					if(tempIndex.size()> 0)
					previousIndex = tempIndex.get(0) - 25;
					if(tempIndex.size() > 0 && tempIndex.get(0) - currentIndex >= 90)
						letters.add("    ");
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
					if(tempIndex.size() > 0) {
						previousIndex = tempIndex.get(0) - 25;
						if(tempIndex.get(0) - currentIndex >= 90)
							letters.add("    ");

					}
					System.out.println("previous index " + previousIndex);

				}

				System.out.println(letters);



				//break;
			}

			//break;

			output.add(letters);


		}



		for(int i = 0; i < output.size(); i++) {

			for(int j = 0; j < output.get(i).size(); j++)
				System.out.print(output.get(i).get(j));

			System.out.println();
		}



		File outputfile = new File("dotDetected.jpg");

		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		return outputfile;
	}

	private int getNearestLineIndex(int y, ArrayList<Integer> newLineIndex) {
		int selectedIndex = newLineIndex.get(0);
		int initialDistance = Math.abs(y - selectedIndex);

		for(int i = 1; i < newLineIndex.size(); i++) {
			int currentIndex = newLineIndex.get(i);
			int currentDistance = Math.abs(y - currentIndex);
			if(initialDistance > currentDistance) {
				initialDistance = currentDistance;
				selectedIndex = currentIndex;
			}
		}

		return selectedIndex;
	}

	private ArrayList<Integer> getMissedLineIndex(ArrayList<Integer> lineIndex, ArrayList<Integer> newLineIndex) {
		ArrayList<Integer> missedIndexes = new ArrayList<Integer>();

		for(int i = 0; i < lineIndex.size(); i++) {
			int currentIndex = lineIndex.get(i);
			if(!newLineIndex.contains(currentIndex))
				missedIndexes.add(currentIndex);
		}
		return missedIndexes;
	}

	private Boolean isPartOfLine(int firstLineIndex, int secondLineIndex, int thirdLineIndex, int lineDistance,
			int limit) {


		boolean secondLineFound = false;
		boolean thirdLineFound = false;


		int distance = secondLineIndex - firstLineIndex;
//		System.out.println(distance + " dis 1");
		if(distance <= lineDistance + limit && distance >= lineDistance - limit) {
			secondLineFound = true;
		}


		int distance2 = thirdLineIndex - secondLineIndex;
//		System.out.println(distance2 + " dis 2");
		if(distance2 <= lineDistance + limit && distance2 >= lineDistance - limit) {
			thirdLineFound = true;
		}

		if(secondLineFound && thirdLineFound)
			return true;
		return false;
	}

	private void colorLine(ArrayList<Integer> arrayOfLineIndex) {
		for(int i = 0; i < arrayOfLineIndex.size(); i++) {

			for(int x = 0; x < width; x++) {
				outputImage.setRGB(x, arrayOfLineIndex.get(i), Utils.RED.getRGB());
			}
		}

	}

	private ArrayList<Integer> getHeightDistance(ArrayList<Point> allCenter) {
		ArrayList<Integer> allY = new ArrayList<Integer>();
		for (int i = 0; i < allCenter.size(); i++) {
			allY.add(allCenter.get(i).getY());
		}

		System.out.println(allY);
		Collections.sort(allY);
		System.out.println(allY);
		ArrayList<Integer> allHeightDistance = new ArrayList<>();
		for(int i = 1; i < allY.size(); i++) {
			int distance = allY.get(i) - allY.get(i-1);
			if(distance != 0)
			allHeightDistance.add(allY.get(i) - allY.get(i-1));
		}

		System.out.println(allHeightDistance);
		Collections.sort(allHeightDistance);

		System.out.println(allHeightDistance);

		return null;
	}

	private void processLineInformation(ArrayList<Point> allCenter) {
		int iniTialPoint = 1;
		int count = 0;
		int maxH = Utils.INITAL_DIFFRENCE_BETWEEN_LINE;

		for(int i = 0; i < allCenter.size(); i++) {

			int centerY = allCenter.get(i).getY();

//			if(centerY == 179 && allCenter.get(i).getX() == 671) {
//				System.out.println("dsfsdfsdfsdfsdfsdf" + iniTialPoint);
//			}

			if(centerY - iniTialPoint >= maxH) {
				if(count > 0) {
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

		if(count > 0) {
			lineIndex.add(iniTialPoint);
			lineMapped.put(iniTialPoint, (ArrayList<Integer>) dotsInLine.clone());
			lineMappedXX.put(iniTialPoint, (ArrayList<Integer>) dotsInLinexx.clone());
			lineMapped2.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());

		}


	}

	private ArrayList<Point> getAllCenter() {
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

					if(tempY >= 0 && tempY < height && tempX >= 0 && tempX < width)
						outputImage.setRGB(tempX, tempY, Color.WHITE.getRGB());
				}
			}
		}
		return allCenter;
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
					if(!isColor(y, x, Utils.WHITE, outputImage))
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

						if(tempY < height && tempY >= 0 && tempX < width && tempX >= 0) {
							if(isColor(tempY, tempX, Utils.WHITE, outputImage) && !pixelCounted.containsKey(tempPixelString)) {
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

				outputImage.setRGB(x, y, Utils.RED.getRGB());

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

			for (int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
				for (int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
					outputImage.setRGB(x, y, Utils.WHITE.getRGB());
				}
			}
		}

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