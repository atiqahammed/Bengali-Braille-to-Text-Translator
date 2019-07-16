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

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

import dataStructure.BrailleDot;
import dataStructure.Dot;
import dataStructure.LineColumn;
import dataStructure.Point;
import fileManager.FileWithPrintWriter;
import util.Utils;

public class TextProcessorAdvance {

	int height;
	int width;

	BufferedImage inputImage = null;
	BufferedImage outputImage;

	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();
	Map<Integer, ArrayList<Point>> lineIndexToDotListMap = new TreeMap<Integer, ArrayList<Point>>();
	ArrayList<ArrayList<String>> twoDString = null;

	ArrayList<Integer> lineIndex = new ArrayList<Integer>();
	ArrayList<Point> pointInLine = new ArrayList<Point>();


//	public TextProcessorAdvance() {
//		height = Utils.IMAGE_HEIGHT;
//		width = Utils.IMAGE_WEIDTH;
//	}

	public ArrayList<ArrayList<String>> getRectangularDottedFile(File imageFile) {
		ArrayList<ArrayList<String>> text = new ArrayList<ArrayList<String>>();
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


		// merging line which has small distance between
		int differenceLimit = Utils.INITAL_DIFFRENCE_BETWEEN_LINE;

		while(differenceLimit <= Utils.MAXIMUM_DISTANCE) {

			ArrayList<Integer> copyOfLineIndex = (ArrayList<Integer>) lineIndex.clone();
			ArrayList<Integer> newLineIndexList = new ArrayList<>();

			for(int i = 0; i < copyOfLineIndex.size(); i++) {
				int currentLineIndex = lineIndex.get(i);
				ArrayList<Point> dotsOfCurrentLine = lineIndexToDotListMap.get(currentLineIndex);

				int nextLineIndex = -1;
				int secondLineIndex = -1;


				if(i + 2 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
					secondLineIndex = lineIndex.get(i + 2);

				}

				else if (i + 1 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
				}

				if(secondLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit && secondLineIndex - nextLineIndex < differenceLimit) {

					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(secondLineIndex));
					int average = 0;
					for(int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 2;
				}

				else if(nextLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit) {
					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));

					int average = 0;
					for(int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 1;
				}
				else {
					newLineIndexList.add(currentLineIndex);
				}
			}

			lineIndex = newLineIndexList;
			lineIndex = getAverageLineIndexBasedOnDots(lineIndex);
			differenceLimit += Utils.LINE_INDEX_MERGED_UNIT;
		}

		//////////////////////////////////////////////////////////////////////////////
		colorLine(lineIndex, Utils.RED);

		for(int i = 0; i < lineIndex.size(); i++) {

			int currentLineIndexY = lineIndex.get(i);
			ArrayList<Point> allPointsInThisLine = (ArrayList<Point>) lineIndexToDotListMap.get(currentLineIndexY).clone();

			for(int j = 0; j < allPointsInThisLine.size(); j++) {
				Point currentPointInThisLine = allPointsInThisLine.get(j);
				int nearestLineIndex = getNearestLineIndex(currentPointInThisLine.getY(), lineIndex);
				lineIndexToDotListMap.get(currentLineIndexY).remove(currentPointInThisLine);
				lineIndexToDotListMap.get(nearestLineIndex).add(currentPointInThisLine);
			}

		}



		ArrayList<Integer> differenceBetweenLines = new ArrayList<Integer>();
		for(int i = 1; i < lineIndex.size(); i++)
			differenceBetweenLines.add(lineIndex.get(i) - lineIndex.get(i - 1));
		Collections.sort(differenceBetweenLines);
		int lineDistance = differenceBetweenLines.get(differenceBetweenLines.size() / 2);
		int acceptanceOfLineDistance = (int) ((double) lineDistance * 0.50);
		Utils.DIFFERENCE_BETWEEN_WORDS = lineDistance * 3 - (lineDistance / 2);

		Utils.OUTPUT_LIST.add("line distance:: " + lineDistance);


		int traverseIndex = 4;
		while(traverseIndex < 5/*lineIndex.size() - 2*/) {


			int firstLineIndex = lineIndex.get(traverseIndex);
			int secondLineIndex = lineIndex.get(traverseIndex + 1);
			int thirdLineIndex = lineIndex.get(traverseIndex + 2);

			if(isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance, acceptanceOfLineDistance)) {

				// collecting all point of individual lines
				ArrayList<Point> dotListOfFirstLine = lineIndexToDotListMap.get(firstLineIndex);
				ArrayList<Point> dotListOfSecondLine = lineIndexToDotListMap.get(secondLineIndex);
				ArrayList<Point> dotListOfThirdLine = lineIndexToDotListMap.get(thirdLineIndex);

				// collecting all x index from points of an individual lines
				ArrayList<Integer> xIndexsOfFirstLineDots = getXOfDosFromLine(dotListOfFirstLine);
				ArrayList<Integer> xIndexsOfSecondLineDots = getXOfDosFromLine(dotListOfSecondLine);
				ArrayList<Integer> xIndexsOfThirdLineDots = getXOfDosFromLine(dotListOfThirdLine);

				ArrayList<LineColumn> listOfLineColumn = convertLineIntoColumn(xIndexsOfFirstLineDots, xIndexsOfSecondLineDots, xIndexsOfThirdLineDots);
				ArrayList<ArrayList<LineColumn>> wordSegmentList = convertWordSegment(listOfLineColumn);

				for(int i = 0; i < listOfLineColumn.size(); i++) {
					listOfLineColumn.get(i).printColumn();
				}

				Utils.OUTPUT_LIST.add("Size of column :: " + listOfLineColumn.size());

				for(int i = 0; i < 1/*wordSegmentList.size()*/; i++) {

					ArrayList<LineColumn> currentWordSegment = (ArrayList<LineColumn>) wordSegmentList.get(i).clone();
					int sizeOfCurrentLineColumnSegment = currentWordSegment.size();

					// using initial column as 1st one---
					int currentColumnIndex = currentWordSegment.get(0).getAverageIndex();
					int columnCovered = 0;
					int nextColumnLimit = (lineDistance * 5) / 10;
					String symbol1 = currentWordSegment.get(0).getSymbol();
					currentWordSegment.remove(0);

					Utils.OUTPUT_LIST.add("initial size:: " + sizeOfCurrentLineColumnSegment);
					Utils.OUTPUT_LIST.add("lineDistance " + lineDistance);
					Utils.OUTPUT_LIST.add("next column limit " + nextColumnLimit);

					while(currentWordSegment.size() > 0) {

						int nextColumnIndex = currentWordSegment.get(0).getAverageIndex();
						int difference = nextColumnIndex - currentColumnIndex;

						Utils.OUTPUT_LIST.add("current column information :: " + currentColumnIndex +"  -- " + symbol1 + " next column Index :: " + nextColumnIndex);
						Utils.OUTPUT_LIST.add("difference " + difference);
						// next column found
						if(difference >= lineDistance - nextColumnLimit && difference <= lineDistance + nextColumnLimit) {
							String symbol2 = currentWordSegment.get(0).getSymbol();
							String symbol = symbol1 + symbol2;

							if(symbol1.equals("000")) columnCovered += 1;
							else columnCovered += 2;

							String Letter = Utils.LETTERS.getLetters(symbol);
							currentWordSegment.remove(0);

							Utils.OUTPUT_LIST.add(symbol);
							Utils.OUTPUT_LIST.add(Letter);
						}

						else {
							String symbol = symbol1 + "000";
							String Letter = Utils.LETTERS.getLetters(symbol);
							columnCovered += 1;

							Utils.OUTPUT_LIST.add(symbol);
							Utils.OUTPUT_LIST.add(Letter);
						}

						currentColumnIndex = currentColumnIndex + lineDistance * 2;
						symbol1 = "000";
						int tempIndex = currentWordSegment.get(0).getAverageIndex();
						difference = Math.abs(tempIndex - currentColumnIndex);

						Utils.OUTPUT_LIST.add("Actual next index:: " + tempIndex);
						Utils.OUTPUT_LIST.add("Predected column index:: " + currentColumnIndex);
						Utils.OUTPUT_LIST.add("difference for next column index:: " + difference);

						if(difference <= 2 * nextColumnLimit) {
							currentColumnIndex = tempIndex;
							symbol1 = currentWordSegment.get(0).getSymbol();
							currentWordSegment.remove(0);

							Utils.OUTPUT_LIST.add("next line found:: " + currentColumnIndex);
							Utils.OUTPUT_LIST.add("symbol of next:: " + symbol1);
						}

						Utils.OUTPUT_LIST.add("column covered:: " + columnCovered);
					}

					if(columnCovered != sizeOfCurrentLineColumnSegment) {
						String symbol = symbol1 + "000";
						String letter = Utils.LETTERS.getLetters(symbol);

						Utils.OUTPUT_LIST.add("all column is not covered properly...");
						Utils.OUTPUT_LIST.add(letter);
					}


					Utils.OUTPUT_LIST.add("-----------------------// using first column as first letters first column completed //-------------------------");
				}
			}





//			for(int i = 0; i < dotListOfFirstLine.size(); i++) {
//				Point dot= dotListOfFirstLine.get(i);
//				System.out.print("[x: " + dot.getX() + ", Y: " + dot.getY() + "]");
//			}
//			System.out.println();
//
//			for(int i = 0; i < dotListOfSecondLine.size(); i++) {
//				Point dot= dotListOfSecondLine.get(i);
//				System.out.print("[x: " + dot.getX() + ", Y: " + dot.getY() + "]");
//			}
//			System.out.println();
//
//			for(int i = 0; i < dotListOfThirdLine.size(); i++) {
//				Point dot= dotListOfThirdLine.get(i);
//				System.out.print("[x: " + dot.getX() + ", Y: " + dot.getY() + "]");
//			}
//			System.out.println();

//			System.out.println(dotListOfFirstLine);
//			System.out.println(dotListOfSecondLine);
//			System.out.println(dotListOfThirdLine);



			traverseIndex++;
			System.out.println("----------------------------------------------------------");

		}

//
//
//
//		System.out.println(allCenter.size());
//		ArrayList<Integer> differenceBetweenLines = new ArrayList<Integer>();
////
////
//		for(int i = 1; i < lineIndex.size(); i++) {
//			differenceBetweenLines.add(lineIndex.get(i) - lineIndex.get(i - 1));
//		}
//		Collections.sort(differenceBetweenLines);
//
//		int lineDistance = differenceBetweenLines.get(differenceBetweenLines.size() / 2);
//		int acceptanceOfLineDistance = (int) ((double) lineDistance * 0.50);
//
//		System.out.println("line distance " + lineDistance);
////
////
//////////
//////		initializeOutputImage();
//		ArrayList<Integer> selectedLineIndexForLine = new ArrayList<Integer>();
////
//		for(int i = 2; i < 4; i++) {
//
////			System.out.println(i);
//
////			if(i + 3 < lineIndex.size()) {
////
////				int firstLineIndex = lineIndex.get(i);
////				int secondLineIndex = lineIndex.get(i + 1);
////				int thirdLineIndex = lineIndex.get(i + 2);
////				int forthLineIndex = lineIndex.get(i + 3);
////
////
////
////
////				Boolean isFirstThreeLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance, acceptanceOfLineDistance);
////				Boolean isLastThreeLineShouldTake = isPartOfLine(secondLineIndex, thirdLineIndex, forthLineIndex, lineDistance, acceptanceOfLineDistance);
////
////
////
////
////
////				if(isFirstThreeLineShouldTake && isLastThreeLineShouldTake) {
////					System.out.println(" both cann be taken....");
////
////					ArrayList<String> lettersInFirstSequence = getWords(firstLineIndex, secondLineIndex, thirdLineIndex);
////					ArrayList<String> lettersInLastSequence = getWords(secondLineIndex, thirdLineIndex, forthLineIndex);
////
////					int lettersInFirstLine = Utils.FUNCTIONS.getLetterCount(lettersInFirstSequence);
////					int lettersInLastLine = Utils.FUNCTIONS.getLetterCount(lettersInLastSequence);
////
////					if(lettersInFirstLine >= lettersInLastLine) {
////						System.out.println("First " + lettersInFirstSequence);
////						text.add(lettersInFirstSequence);
////						i += 2;
////
////						selectedLineIndexForLine.add(firstLineIndex);
////						selectedLineIndexForLine.add(secondLineIndex);
////						selectedLineIndexForLine.add(thirdLineIndex);
////
////					} else {
////						System.out.println("Last " + lettersInLastSequence);
////						text.add(lettersInLastSequence);
////						i += 3;
////
////
////						selectedLineIndexForLine.add(secondLineIndex);
////						selectedLineIndexForLine.add(thirdLineIndex);
////						selectedLineIndexForLine.add(forthLineIndex);
////
////					}
////
////				}
////
////				else if(isFirstThreeLineShouldTake) {
////
//////					System.out.println("in if function");
////					System.out.println(firstLineIndex + " " + secondLineIndex + " " + thirdLineIndex + " f");
////					ArrayList<String> lettersInFirstSequence = getWords(firstLineIndex, secondLineIndex, thirdLineIndex);
////					System.out.println("single first " + lettersInFirstSequence);
////					text.add(lettersInFirstSequence);
////					i += 2;
////
////					selectedLineIndexForLine.add(firstLineIndex);
////					selectedLineIndexForLine.add(secondLineIndex);
////					selectedLineIndexForLine.add(thirdLineIndex);
////
////				}
//////
////				else if(isLastThreeLineShouldTake) {
////					System.out.println(secondLineIndex + " " + thirdLineIndex + " " + forthLineIndex + " l");
////					ArrayList<String> lettersInLastSequence = getWords(secondLineIndex, thirdLineIndex, forthLineIndex);
////					System.out.println("single first " + lettersInLastSequence);
////					text.add(lettersInLastSequence);
////					i += 3;
////
////					selectedLineIndexForLine.add(secondLineIndex);
////					selectedLineIndexForLine.add(thirdLineIndex);
////					selectedLineIndexForLine.add(forthLineIndex);
////
////				}
////			}
//
//			if(i + 2 < lineIndex.size()) {
//				int firstLineIndex = lineIndex.get(i);
//				int secondLineIndex = lineIndex.get(i + 1);
//				int thirdLineIndex = lineIndex.get(i + 2);
//
//				Boolean isFirstThreeLineShouldTake = isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance, acceptanceOfLineDistance);
//				if(isFirstThreeLineShouldTake) {
//					System.out.println(firstLineIndex + " " + secondLineIndex + " " + thirdLineIndex + " f");
//
//					ArrayList<String> allLetters = getWords(firstLineIndex, secondLineIndex, thirdLineIndex);
//					ArrayList<String> allLettersFromLast = getLettersFromLast(firstLineIndex, secondLineIndex, thirdLineIndex);
//
//
////					System.out.println("single first " + lettersInFirstSequence);
////					text.add(lettersInFirstSequence);
////					i += 2;
////
////					selectedLineIndexForLine.add(firstLineIndex);
////					selectedLineIndexForLine.add(secondLineIndex);
////					selectedLineIndexForLine.add(thirdLineIndex);
//				}
//
//			}
//		}
//
//		System.out.println(selectedLineIndexForLine);
//
//
//		int arraySizeOfSelectedLineIndex = 3;//selectedLineIndexForLine.size();
//		for(int i = 0; i < arraySizeOfSelectedLineIndex; i += 3) {
//			ArrayList<Point> dotsInFirstLine = lineIndexToDotListMap.get(selectedLineIndexForLine.get(i));
//			ArrayList<Point> dotsInSecondLine = lineIndexToDotListMap.get(selectedLineIndexForLine.get(i + 1));
//			ArrayList<Point> dotsInThirdLine = lineIndexToDotListMap.get(selectedLineIndexForLine.get(i + 2));
//
//			System.out.println(dotsInFirstLine.size());
//			System.out.println(dotsInSecondLine.size());
//			System.out.println(dotsInThirdLine.size());
//
//		}
//
//
//
//
////		colorLine(lineIndex, Utils.YELLOW);
////		colorLine(newLineIndexList, Utils.YELLOW);
//		colorLine(selectedLineIndexForLine, Utils.BLUE);
		FileWithPrintWriter printWriter = null;
		File outputfile = new File("dotDetected.jpg");

		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

//		return null;
		return text;
	}

	private ArrayList<ArrayList<LineColumn>> convertWordSegment(ArrayList<LineColumn> listOfLineColumn) {

		ArrayList<ArrayList<LineColumn>> segmentedColumnList = new ArrayList<ArrayList<LineColumn>>();
		ArrayList<LineColumn> currentCulmnList = new ArrayList<LineColumn>();
		LineColumn currentColumn = listOfLineColumn.get(0);
		int currentColumnAverageIndex = currentColumn.getAverageIndex();
		int listSize = listOfLineColumn.size();
		currentCulmnList.add(currentColumn);

		for(int i = 1; i < listSize; i++) {
			currentColumn = listOfLineColumn.get(i);
			int tempAverageIndex = currentColumn.getAverageIndex();

			if(tempAverageIndex - currentColumnAverageIndex >= Utils.DIFFERENCE_BETWEEN_WORDS) {
				segmentedColumnList.add((ArrayList<LineColumn>) currentCulmnList.clone());
				currentCulmnList = new ArrayList<LineColumn>();
			}

			currentCulmnList.add(currentColumn);
			currentColumnAverageIndex = tempAverageIndex;
		}

		segmentedColumnList.add((ArrayList<LineColumn>) currentCulmnList.clone());

		return segmentedColumnList;
	}

	private ArrayList<LineColumn> convertLineIntoColumn(ArrayList<Integer> xIndexsOfFirstLineDots,
		ArrayList<Integer> xIndexsOfSecondLineDots, ArrayList<Integer> xIndexsOfThirdLineDots) {

		ArrayList<LineColumn> columnList = new ArrayList<LineColumn>();
		int limit = Utils.MAXIMUM_DISTANCE;


		while(anyDotExistInThreeLine(xIndexsOfFirstLineDots, xIndexsOfSecondLineDots, xIndexsOfThirdLineDots)) {
			System.out.println(xIndexsOfFirstLineDots.size() + " - " + xIndexsOfSecondLineDots.size() + " - " + xIndexsOfThirdLineDots.size());
			int smallestIndex = width + 100;

			if(xIndexsOfFirstLineDots.size() > 0) smallestIndex = Math.min(smallestIndex, xIndexsOfFirstLineDots.get(0));
			if(xIndexsOfSecondLineDots.size() > 0) smallestIndex = Math.min(smallestIndex, xIndexsOfSecondLineDots.get(0));
			if(xIndexsOfThirdLineDots.size() > 0) smallestIndex = Math.min(smallestIndex, xIndexsOfThirdLineDots.get(0));
			System.out.println(smallestIndex + " small index");

			int upperDot = -1;
			int middledot = -1;
			int lowerDot = -1;

			if(xIndexsOfFirstLineDots.size() > 0 && isDotBetweenLimit(xIndexsOfFirstLineDots.get(0), limit, smallestIndex)) {
				upperDot = xIndexsOfFirstLineDots.get(0);
				xIndexsOfFirstLineDots.remove(0);
			}
			if(xIndexsOfSecondLineDots.size() > 0 && isDotBetweenLimit(xIndexsOfSecondLineDots.get(0), limit, smallestIndex)) {
				middledot = xIndexsOfSecondLineDots.get(0);
				xIndexsOfSecondLineDots.remove(0);
			}
			if(xIndexsOfThirdLineDots.size() > 0 && isDotBetweenLimit(xIndexsOfThirdLineDots.get(0), limit, smallestIndex)) {
				lowerDot = xIndexsOfThirdLineDots.get(0);
				xIndexsOfThirdLineDots.remove(0);
			}

			System.out.println(upperDot + "*" + middledot + "*" + lowerDot);

			columnList.add(new LineColumn(upperDot, middledot, lowerDot));
		}

		return columnList;
	}

	private boolean isDotBetweenLimit(int integer, int limit, int smallestIndex) {
		int difference = integer - smallestIndex;
		return (difference <= (limit + limit / 4)) ;
	}

	private boolean anyDotExistInThreeLine(ArrayList<Integer> xIndexsOfFirstLineDots,
			ArrayList<Integer> xIndexsOfSecondLineDots, ArrayList<Integer> xIndexsOfThirdLineDots) {
		return (xIndexsOfFirstLineDots.size() > 0 || xIndexsOfSecondLineDots.size() > 0 || xIndexsOfThirdLineDots.size() > 0);
	}

	private ArrayList<Integer> getXOfDosFromLine(ArrayList<Point> dotListOfLine) {

		ArrayList<Integer> allXs = new ArrayList<>();
		int dotListSize = dotListOfLine.size();

		// collecting all x from dots
		for(int i = 0; i < dotListSize; i++)
			allXs.add(dotListOfLine.get(i).getX());

		Collections.sort(allXs);

		int previousXIndexOfDot = allXs.get(0);
		ArrayList<Integer> dotsToRemove = new ArrayList<Integer>();

		for(int j = 1; j < allXs.size(); j++) {
			if(allXs.get(j) - previousXIndexOfDot < Utils.SAME_POINT_COVERAGE)
				dotsToRemove.add(previousXIndexOfDot);
			previousXIndexOfDot = allXs.get(j);
		}

		for(int j = 0; j < dotsToRemove.size(); j++)
			allXs.remove(dotsToRemove.get(j));

		return allXs;
	}

	private ArrayList<String> getLettersFromLast(int firstLineIndex, int secondLineIndex, int thirdLineIndex) {

		ArrayList<Integer> firstLine= getDotSequence(firstLineIndex);
		ArrayList<Integer> secondLine = getDotSequence(secondLineIndex);
		ArrayList<Integer> thirdLine= getDotSequence(thirdLineIndex);

		Collections.reverse(firstLine);
		Collections.reverse(secondLine);
		Collections.reverse(thirdLine);

		System.out.println(firstLine);
		System.out.println(secondLine);
		System.out.println(thirdLine);



		ArrayList<String> letters = new ArrayList<String>();

		int initialUpperDot = firstLine.get(0);
		int initialMiddelDot = secondLine.get(0);
		int initialLowerDot = thirdLine.get(0);

		int greatestXIndex = initialUpperDot;
		if(initialMiddelDot > greatestXIndex) greatestXIndex = initialMiddelDot;
		if(initialLowerDot > greatestXIndex) greatestXIndex = initialLowerDot;


		int previousIndex = greatestXIndex + 35;

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
				if(Math.abs(firstLine.get(0) - previousIndex) >= 20 && Math.abs(firstLine.get(0) - previousIndex) <= 45) {
					nextUpperDot = firstLine.get(0);
					firstLine.remove(0);
				}
			}

			if(secondLine.size() > 0) {
				if(Math.abs(secondLine.get(0) - previousIndex) >= 20 && Math.abs(secondLine.get(0) - previousIndex) <= 45) {
					nextMiddelDot = secondLine.get(0);
					secondLine.remove(0);
				}
			}

			if(thirdLine.size() > 0) {
				if(Math.abs(thirdLine.get(0) - previousIndex) >= 20 && Math.abs(thirdLine.get(0) - previousIndex) <= 45) {
					nextLowerDot = thirdLine.get(0);
					thirdLine.remove(0);
				}
			}

			if(nextUpperDot > 0) currentIndex = nextUpperDot;
			if(currentIndex < 0 && nextMiddelDot > 0) currentIndex = nextMiddelDot;
			if(nextMiddelDot > 0 && nextMiddelDot > currentIndex) currentIndex = nextMiddelDot;
			if(currentIndex < 0 && nextLowerDot > 0) currentIndex = nextLowerDot;
			if(nextLowerDot > 0 && nextLowerDot > currentIndex) currentIndex = nextLowerDot;

			System.out.println(nextUpperDot +" "+ nextLowerDot + " "+ nextMiddelDot);
			System.out.println("current Index " + currentIndex);

			Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);

						// second level identification....
			if(firstLine.size() > 0) {
				if(Math.abs(firstLine.get(0) - currentIndex) >= 20 && Math.abs(firstLine.get(0) - currentIndex) <= 45) {
					upperDot = firstLine.get(0);
					firstLine.remove(0);
				}
			}

			if(secondLine.size() > 0) {
				if(Math.abs(secondLine.get(0) - currentIndex) >= 20 && Math.abs(secondLine.get(0) - currentIndex) <= 45) {
					middelDot = secondLine.get(0);
					secondLine.remove(0);
				}
			}

			if(thirdLine.size() > 0) {
				if(Math.abs(thirdLine.get(0) - currentIndex) >= 20 && Math.abs(thirdLine.get(0) - currentIndex) <= 45) {
					lowerDot = thirdLine.get(0);
					thirdLine.remove(0);
				}
			}

			System.out.println(upperDot+" "+ middelDot +" "+ lowerDot+ "  >");
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
					previousIndex = tempIndex.get(0) + 25;
					if(Math.abs(tempIndex.get(0) - currentIndex) >= 90)
						letters.add("    ");

				}
			}

			System.out.println(letters);
		}

		Collections.reverse(letters);

		return letters;







//		return null;
	}

	private ArrayList<Integer> getMergedLineIndex(ArrayList<Integer> list) {

		int differenceLimit = Utils.INITAL_DIFFRENCE_BETWEEN_LINE;

		while(differenceLimit <= Utils.MAXIMUM_DISTANCE) {

			ArrayList<Integer> copyOfLineIndex = (ArrayList<Integer>) list.clone();
			ArrayList<Integer> newLineIndexList = new ArrayList<>();

			for(int i = 0; i < copyOfLineIndex.size(); i++) {
				int currentLineIndex = lineIndex.get(i);
				ArrayList<Point> dotsOfCurrentLine = lineIndexToDotListMap.get(currentLineIndex);

				int nextLineIndex = -1;
				int secondLineIndex = -1;


				if(i + 2 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
					secondLineIndex = lineIndex.get(i + 2);

				}

				else if (i + 1 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
				}

				if(secondLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit && secondLineIndex - nextLineIndex < differenceLimit) {
//					int average = (currentLineIndex + nextLineIndex + secondLineIndex) / 3;
					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(secondLineIndex));
					int average = 0;
					for(int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 2;
				}

				else if(nextLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit) {
//					int average = (currentLineIndex + nextLineIndex) / 2;
					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));

					int average = 0;
					for(int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 1;
				}
				else {
					newLineIndexList.add(currentLineIndex);
				}
			}

			list = newLineIndexList;
			list = getAverageLineIndexBasedOnDots(list);
			differenceLimit += 3;
		}

		return list;
	}

	private ArrayList<Integer> getAverageLineIndexBasedOnDots(ArrayList<Integer> lineIndex2) {
		ArrayList<Integer> primaryLineIndexCopy = (ArrayList<Integer>) lineIndex.clone();
		ArrayList<Integer> allAverage = new ArrayList<Integer>();

		for(int i = 0; i < primaryLineIndexCopy.size(); i++) {

			int index = primaryLineIndexCopy.get(i);
			ArrayList<Point> allDotsInCurrentLine = lineIndexToDotListMap.get(index);
			ArrayList<Integer> allYCoordinateOfCurrentLineDots = new ArrayList<Integer>();

			int sum = 0;
			for(int j = 0; j < allDotsInCurrentLine.size(); j++)
				sum += allDotsInCurrentLine.get(j).getY();

			int average = sum / allDotsInCurrentLine.size();
			lineIndexToDotListMap.put(average, allDotsInCurrentLine);
			allAverage.add(average);
		}
		return allAverage;
	}

	private ArrayList<String> getWords(int firstLineIndex, int secondLineIndex, int thirdLineIndex) {

		ArrayList<Integer> firstLine= getDotSequence(firstLineIndex);
		ArrayList<Integer> secondLine = getDotSequence(secondLineIndex);
		ArrayList<Integer> thirdLine= getDotSequence(thirdLineIndex);

		System.out.println(firstLine);
		System.out.println(secondLine);
		System.out.println(thirdLine);

//		System.out.println("ok ok ok line");


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
			}

			System.out.println(letters);
		}

		return letters;
	}

	private ArrayList<Integer> getDotSequence(int index) {

		ArrayList<Point> pointList = lineIndexToDotListMap.get(index);
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
					lineIndexToDotListMap.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());
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
			lineIndexToDotListMap.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());
		}
	}

	private ArrayList<Point> getAllCenter() {
		ArrayList<Point> allCenter = new ArrayList<>();

		for(int index = 0; index< twoDString.size(); index++) {

			ArrayList<String> xx = twoDString.get(index);
			BrailleDot brailleDot = new BrailleDot(xx);
			Point center = brailleDot.getCenter();
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
