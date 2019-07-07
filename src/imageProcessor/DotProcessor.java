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

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import dataStructure.BrailleDor;
import dataStructure.Dot;
import dataStructure.Point;
import fileManager.FileWithPrintWriter;
import util.Utils;

public class DotProcessor {

	int height;
	int width;

	BufferedImage inputImage = null;
	BufferedImage outputImage;

	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();
	Map<Integer, ArrayList<Point>> lineMapped2 = new TreeMap<Integer, ArrayList<Point>>();
	ArrayList<ArrayList<String>> twoDString = null;



	// line information
	ArrayList<Integer> lineIndex = new ArrayList<Integer>();
	ArrayList<Point> pointInLine = new ArrayList<Point>();

	public File getRectangularDottedFile(File imageFile) {

		System.out.println("processing dot...");

		BufferedImage image;
		initializeVariables(imageFile);

		findDots();
		initializeOutputImage();
		processUniqueDots();
		ArrayList<Dot> firstStepSelectedDot = selectDotInFirstStep();
		initializeOutputImage();
		ArrayList<Point> allCenter = getAllCenter();

		processLineInformation(allCenter);

		Collections.sort(lineIndex);

		// in this section I am trying to swap dots between lines if there esists any issue of nearest line for any dots.
		for(int i = 0; i < lineIndex.size(); i++) {
			int currentLineIndexY = lineIndex.get(i);
			ArrayList<Point> allPointsInThisLine = (ArrayList<Point>) lineMapped2.get(currentLineIndexY).clone();

//			System.out.println("line number: " + i + " .. > indexY : " + currentLineIndexY + "  dot size: " + allPointsInThisLine.size());
			for(int j = 0; j < allPointsInThisLine.size(); j++) {
				Point currentPointInThisLine = allPointsInThisLine.get(j);
				int nearestLineIndex = getNearestLineIndex(currentPointInThisLine.getY(), lineIndex);
				lineMapped2.get(currentLineIndexY).remove(currentPointInThisLine);
				lineMapped2.get(currentLineIndexY).add(currentPointInThisLine);
			}

		}



//		int allDot = 0;

		ArrayList<Integer> primaryLineIndexCopy = (ArrayList<Integer>) lineIndex.clone();
		ArrayList<Integer> allAverage = new ArrayList<Integer>();

		for(int i = 0; i < primaryLineIndexCopy.size(); i++) {

			int index = primaryLineIndexCopy.get(i);
			ArrayList<Point> allDotsInCurrentLine = lineMapped2.get(index);
			ArrayList<Integer> allYCoordinateOfCurrentLineDots = new ArrayList<Integer>();

			int sum = 0;
			for(int j = 0; j < allDotsInCurrentLine.size(); j++)
				sum += allDotsInCurrentLine.get(j).getY();

			int average = sum / allDotsInCurrentLine.size();
			lineMapped2.put(average, allDotsInCurrentLine);
			allAverage.add(average);
		}

		lineIndex = allAverage;

		ArrayList<Integer> allDistanceBetweenLine = new ArrayList<Integer>();

		for(int i = 1; i < lineIndex.size(); i++) {
			allDistanceBetweenLine.add(lineIndex.get(i) - lineIndex.get(i-1));
		}

		Collections.sort(allDistanceBetweenLine);
		int median = allDistanceBetweenLine.get(allDistanceBetweenLine.size() / 2);

		int limit = 15;

		ArrayList<Integer> copyOfLineIndex = (ArrayList<Integer>) lineIndex.clone();
		ArrayList<Integer> newLineIndexList = new ArrayList<>();
		for(int i = 0; i < copyOfLineIndex.size(); i++) {
			int currentLineIndex = lineIndex.get(i);
			ArrayList<Point> dotsOfCurrentLine = lineMapped2.get(currentLineIndex);

			int nextLineIndex = -1;
			int secondLineIndex = -1;


			if(i + 2 < copyOfLineIndex.size()) {
				nextLineIndex = lineIndex.get(i + 1);
				secondLineIndex = lineIndex.get(i + 2);

			}

			else if (i + 1 < copyOfLineIndex.size()) {
				nextLineIndex = lineIndex.get(i + 1);
			}

			if(secondLineIndex >= 0 && nextLineIndex - currentLineIndex < limit && secondLineIndex - nextLineIndex < limit) {
//				int average = (currentLineIndex + nextLineIndex + secondLineIndex) / 3;
				ArrayList<Point> newDotList = new ArrayList<>();
				newDotList.addAll(lineMapped2.get(currentLineIndex));
				newDotList.addAll(lineMapped2.get(nextLineIndex));
				newDotList.addAll(lineMapped2.get(secondLineIndex));
				int average = 0;
				for(int k = 0; k < newDotList.size(); k++)
					average += newDotList.get(k).getY();
				average = average / newDotList.size();

				newLineIndexList.add(average);
				lineMapped2.put(average, newDotList);
				i += 2;
			}

			else if(nextLineIndex >= 0 && nextLineIndex - currentLineIndex < limit) {
//				int average = (currentLineIndex + nextLineIndex) / 2;
				ArrayList<Point> newDotList = new ArrayList<>();
				newDotList.addAll(lineMapped2.get(currentLineIndex));
				newDotList.addAll(lineMapped2.get(nextLineIndex));

				int average = 0;
				for(int k = 0; k < newDotList.size(); k++)
					average += newDotList.get(k).getY();
				average = average / newDotList.size();

				newLineIndexList.add(average);
				lineMapped2.put(average, newDotList);
				i += 1;
			}
			else {
				newLineIndexList.add(currentLineIndex);
			}
		}

		lineIndex = newLineIndexList;


		int cout = 0;
		for(int i = 0; i < lineIndex.size(); i++)
			cout += lineMapped2.get(lineIndex.get(i)).size();

//		System.out.println(cout + "   " + allDot);

//		System.out.println(lineIndex);

		colorLine(lineIndex, Utils.RED);


		ArrayList<Integer> differenceBetweenLines = new ArrayList<Integer>();


		for(int i = 1; i < lineIndex.size(); i++) {
			differenceBetweenLines.add(lineIndex.get(i) - lineIndex.get(i - 1));
		}


//		System.out.println(differenceBetweenLines);

		Collections.sort(differenceBetweenLines);
//		System.out.println(differenceBetweenLines);


		int lineDistance = differenceBetweenLines.get(differenceBetweenLines.size() / 2);
//		System.out.println("Line distance : " + lineDistance);
		int acceptanceOfLineDistance = (int) ((double) lineDistance * 0.50);
//		System.out.println("Limit : " + acceptanceOfLineDistance);



		for(int i = 0; i < lineIndex.size(); i++) {

			System.out.println(i);

			if(i + 3 < lineIndex.size()) {

				int firstLineIndex = lineIndex.get(i);
				int secondLineIndex = lineIndex.get(i + 1);
				int thirdLineIndex = lineIndex.get(i + 2);
				int forthLineIndex = lineIndex.get(i + 3);

				Boolean isFirstThreeLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance, acceptanceOfLineDistance);
				Boolean isLastThreeLineShouldTake = isPartOfLine(secondLineIndex, thirdLineIndex, forthLineIndex, lineDistance, acceptanceOfLineDistance);


				if(isFirstThreeLineShouldTake && isLastThreeLineShouldTake) {
					System.out.println(" b");

					ArrayList<String> lettersInFirstSequence = getWords(firstLineIndex, secondLineIndex, thirdLineIndex);
					ArrayList<String> lettersInLastSequence = getWords(secondLineIndex, thirdLineIndex, forthLineIndex);

					int lettersInFirstLine = Utils.FUNCTIONS.getLetterCount(lettersInFirstSequence);
					int lettersInLastLine = Utils.FUNCTIONS.getLetterCount(lettersInLastSequence);

					if(lettersInFirstLine >= lettersInLastLine) {
						System.out.println("First " + lettersInFirstSequence);
						i += 2;
					} else {
						System.out.println("Last " + lettersInLastSequence);
						i += 3;
					}

				}



				else if(isFirstThreeLineShouldTake) {
					System.out.println(firstLineIndex + " " + secondLineIndex + " " + thirdLineIndex + " f");
					ArrayList<String> lettersInFirstSequence = getWords(firstLineIndex, secondLineIndex, thirdLineIndex);
					System.out.println("single first " + lettersInFirstSequence);
					i += 2;
				}

				else if(isLastThreeLineShouldTake) {
					System.out.println(secondLineIndex + " " + thirdLineIndex + " " + forthLineIndex + " l");
					ArrayList<String> lettersInLastSequence = getWords(secondLineIndex, thirdLineIndex, forthLineIndex);
					System.out.println("single first " + lettersInLastSequence);
					i += 3;

				}





			}

			else if(i + 2 < lineIndex.size()) {
				int firstLineIndex = lineIndex.get(i);
				int secondLineIndex = lineIndex.get(i + 1);
				int thirdLineIndex = lineIndex.get(i + 2);

				Boolean isFirstThreeLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance, acceptanceOfLineDistance);
				if(isFirstThreeLineShouldTake) {
					System.out.println(firstLineIndex + " " + secondLineIndex + " " + thirdLineIndex + " f");
					ArrayList<String> lettersInFirstSequence = getWords(firstLineIndex, secondLineIndex, thirdLineIndex);
					System.out.println("single first " + lettersInFirstSequence);
					i += 2;
				}

			}


		}



//		ArrayList<Integer> newLineIndex = new ArrayList<Integer>();
//
//		for(int i = 0; i < lineIndex.size(); i++) {
//
//			if(i + 3 < lineIndex.size()) {
//				int firstLineIndex = lineIndex.get(i);
//				int secondLineIndex = lineIndex.get(i + 1);
//				int ThirdLineIndex = lineIndex.get(i + 2);
//				int forthLineIndex = lineIndex.get(i + 3);
//
//				Boolean isFirstThreeLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, ThirdLineIndex, lineDistance, limit);
//				Boolean isLastThreeLineShouldTake = isPartOfLine(secondLineIndex, ThirdLineIndex, forthLineIndex, lineDistance, limit);
//
//				if(isFirstThreeLineShouldTake && isLastThreeLineShouldTake) {
//
//					double firstAverage = (lineMapped2.get(firstLineIndex).size() + lineMapped2.get(secondLineIndex).size() + lineMapped2.get(ThirdLineIndex).size()) / 3;
//					double lastAverage = (lineMapped2.get(forthLineIndex).size() + lineMapped2.get(secondLineIndex).size() + lineMapped2.get(ThirdLineIndex).size()) / 3;
//
//					System.out.println(firstAverage + "  fi");
//					System.out.println(lastAverage + "  la");
//
//					if(firstAverage > lastAverage) {
//						newLineIndex.add(firstLineIndex);
//						newLineIndex.add(secondLineIndex);
//						newLineIndex.add(ThirdLineIndex);
//
//						i += 2;
//					} else {
//						newLineIndex.add(secondLineIndex);
//						newLineIndex.add(ThirdLineIndex);
//						newLineIndex.add(forthLineIndex);
//
//						i += 3;
//					}
//				}
//
//				else if(isFirstThreeLineShouldTake && !isLastThreeLineShouldTake) {
//					newLineIndex.add(firstLineIndex);
//					newLineIndex.add(secondLineIndex);
//					newLineIndex.add(ThirdLineIndex);
//
//					i += 2;
//				}
//
//				else if(!isFirstThreeLineShouldTake && isLastThreeLineShouldTake) {
//					newLineIndex.add(secondLineIndex);
//					newLineIndex.add(ThirdLineIndex);
//					newLineIndex.add(forthLineIndex);
//
//					i += 3;
//				}
//
//
//
//			}
//
//			else if(i + 2 < lineIndex.size()) {
//				int firstLineIndex = lineIndex.get(i);
//				int secondLineIndex = lineIndex.get(i + 1);
//				int ThirdLineIndex = lineIndex.get(i + 2);
//
//				Boolean isLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, ThirdLineIndex, lineDistance, limit);
//				if(isLineShouldTake) {
//					newLineIndex.add(firstLineIndex);
//					newLineIndex.add(secondLineIndex);
//					newLineIndex.add(ThirdLineIndex);
//
//					i += 2;
//				}
//			}
//		}

		colorLine(newLineIndexList, Utils.YELLOW);

		FileWithPrintWriter printWriter = null;

//		try {
//			printWriter = new FileWithPrintWriter(Utils.OUTPUT_FILE_NAME);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//		for(int i = 0; i < output.size(); i++) {
//
//			for(int j = 0; j < output.get(i).size(); j++)
//				printWriter.writeInfile(output.get(i).get(j));
//				//System.out.print(output.get(i).get(j));
//
//			printWriter.writeInfile();
//		}
//		printWriter.closeFile();


		File outputfile = new File("dotDetected.jpg");

		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		return outputfile;
	}

	private ArrayList<String> getWords(int firstLineIndex, int secondLineIndex, int thirdLineIndex) {

		ArrayList<Integer> firstLine= getDotSequence(firstLineIndex);
		ArrayList<Integer> secondLine = getDotSequence(secondLineIndex);
		ArrayList<Integer> thirdLine= getDotSequence(thirdLineIndex);

		ArrayList<String> letters = new ArrayList<String>();

		int initialUpperDot = firstLine.get(0);
		int initialMiddelDot = secondLine.get(0);
		int initialLowerDot = thirdLine.get(0);

		int smallestXIndex = initialUpperDot;
		if(initialMiddelDot < smallestXIndex) smallestXIndex = initialMiddelDot;
		if(initialLowerDot < smallestXIndex) smallestXIndex = initialLowerDot;

//		Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);
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

//			System.out.println(upperDot +" "+ middelDot+ " "+ lowerDot);
//			System.out.println("current Index " + currentIndex);

//			Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);

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

//			System.out.println(nextUpperDot+" "+ nextMiddelDot +" "+ nextLowerDot+ "  >");
			if(nextUpperDot == -1 && nextMiddelDot == -1 && nextLowerDot == -1) {
				if(upperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
				if(middelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
				if(lowerDot!= -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

//				System.out.println(letter);
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
//					System.out.println("previous index " + previousIndex);

			}

			else {
				if(upperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
				if(middelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
				if(lowerDot!= -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

				if(nextUpperDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 3);
				if(nextMiddelDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 4);
				if(nextLowerDot != -1) letter = Utils.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 5);

//				System.out.println(letter);
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
			}
		}

//		System.out.println(letters);
		return letters;
	}

	private ArrayList<Integer> getDotSequence(int index) {

		ArrayList<Point> pointList = lineMapped2.get(index);
		ArrayList<Integer> dots = new ArrayList<Integer>();

		for(int i = 0; i < pointList.size(); i++)
			dots.add(pointList.get(i).getX());

		Collections.sort(dots);

		int previousXIndexOfDot = dots.get(0);
		ArrayList<Integer> dotsToRemove = new ArrayList<Integer>();

		for(int j = 1; j < dots.size(); j++) {
			if(dots.get(j) - previousXIndexOfDot < Utils.SAME_POINT_COVERAGE)
				dotsToRemove.add(previousXIndexOfDot);
			previousXIndexOfDot = dots.get(j);
		}

		for(int j = 0; j < dotsToRemove.size(); j++)
			dots.remove(dotsToRemove.get(j));

		return dots;
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

	private Boolean isPartOfLine(int firstLineIndex, int secondLineIndex, int thirdLineIndex, int lineDistance,
			int limit) {

		boolean secondLineFound = false;
		boolean thirdLineFound = false;
		int distance = secondLineIndex - firstLineIndex;
		int distance2 = thirdLineIndex - secondLineIndex;

		if(distance <= lineDistance + limit && distance >= lineDistance - limit)
			secondLineFound = true;
		if(distance2 <= lineDistance + limit && distance2 >= lineDistance - limit)
			thirdLineFound = true;

		if(secondLineFound && thirdLineFound)
			return true;
		return false;
	}

	private void colorLine(ArrayList<Integer> arrayOfLineIndex, Color color) {
		for(int i = 0; i < arrayOfLineIndex.size(); i++) {

			for(int x = 0; x < width; x++) {
				outputImage.setRGB(x, arrayOfLineIndex.get(i), color.getRGB());
			}
		}

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