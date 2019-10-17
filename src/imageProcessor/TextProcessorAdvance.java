package imageProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import dataStructure.BengaliWord;
import dataStructure.BrailleDot;
import dataStructure.BrailleWord;
import dataStructure.Dot;
import dataStructure.LetterInBrailleCode;
import dataStructure.Line;
import dataStructure.LineColumn;
import dataStructure.Point;
import dataStructure.Word;
import fileManager.FileWithPrintWriter;
import util.Constant;
import util.Utils;

public class TextProcessorAdvance {

	int height;
	int width;
	int lineDistance;
	int acceptanceOfLineDistance;

	int distanceBetweenDot = 28;

	BufferedImage inputImage = null;
	BufferedImage outputImage;

	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();
	Map<Integer, ArrayList<Point>> lineIndexToDotListMap = new TreeMap<Integer, ArrayList<Point>>();
	ArrayList<ArrayList<String>> twoDString = null;

	ArrayList<Integer> lineIndex = new ArrayList<Integer>();
	ArrayList<Point> pointInLine = new ArrayList<Point>();

	ArrayList<Word> all_word = new ArrayList<Word>();

	public ArrayList<String> getRectangularDottedFile(File imageFile) {

//		ArrayList<ArrayList<String>> text = new ArrayList<ArrayList<String>>();

		initializeVariables(imageFile);
		findDots();
		initializeOutputImage();
		processUniqueDots();
		ArrayList<Dot> firstStepSelectedDot = selectDotInFirstStep();

		// task completed for finding unique dots...

		ArrayList<Point> allCenter = getAllCenter();
		processLineInformation(allCenter);
		Collections.sort(lineIndex);
		
		
//		for(int t: lineIndex)
//			lineIndexToDotListMap.get(t).clear();
		
		
		lineIndex = mergeLineIndexs(lineIndex);
		
		for(int t: lineIndex)
			lineIndexToDotListMap.put(t, new ArrayList<Point>());
		
		for(Point point: allCenter) {
			int index = getNearestLineIndex(point.getY(), lineIndex);
			lineIndexToDotListMap.get(index).add(point);
		}
		
		ArrayList<Point> temp = new ArrayList<Point>();
		for(int t: lineIndex)
			temp.addAll(lineIndexToDotListMap.get(t));
		
		System.out.println("final list: " + temp.size());
		
		
		
		colorLine(lineIndex, Constant.RED);
		processDistance();

		ArrayList<Line> allSegmentedLines = getAllLine();

		System.out.println(allSegmentedLines.size() + " linesss   ---- ");

		colorSegmentedLine(allSegmentedLines);
		ArrayList<String> Lines = new ArrayList<String>();
		int lineSize = allSegmentedLines.size();
		
		
		
		for (int i = 0; i <  lineSize ; i++) {

			System.out.println("------------------");
			
			Line li = allSegmentedLines.get(i);
			

			ArrayList<Integer> threeIndexs = new ArrayList<Integer>();
			threeIndexs.add(li.getUpperLineIndex());
			threeIndexs.add(li.getMiddleLineIndex());
			threeIndexs.add(li.getLowerLineIndex());
			
//			System.out.println(lineIndexToDotListMap.get(li.getUpperLineIndex()).size());
//			System.out.println(lineIndexToDotListMap.get(li.getMiddleLineIndex()).size());
//			System.out.println(lineIndexToDotListMap.get(li.getLowerLineIndex()).size());
			
			
			ArrayList<Point> allLineDot = new ArrayList<Point>();
			allLineDot.addAll(lineIndexToDotListMap.get(li.getUpperLineIndex()));
			allLineDot.addAll(lineIndexToDotListMap.get(li.getMiddleLineIndex()));
			allLineDot.addAll(lineIndexToDotListMap.get(li.getLowerLineIndex()));
			
//			System.out.println("- " + allLineDot.size());
			
			lineIndexToDotListMap.put(li.getUpperLineIndex(), new ArrayList<Point>());
			lineIndexToDotListMap.put(li.getMiddleLineIndex(), new ArrayList<Point>());
			lineIndexToDotListMap.put(li.getLowerLineIndex(), new ArrayList<Point>());
			
			
			for(int index = 0; index < allLineDot.size(); index++) {
				int selectedLineIndex = getNearestLineIndex(allLineDot.get(index).getY(), threeIndexs);
				lineIndexToDotListMap.get(selectedLineIndex).add(allLineDot.get(index));
			}
			
			
//			System.out.println(lineIndexToDotListMap.get(li.getUpperLineIndex()).size());
//			System.out.println(lineIndexToDotListMap.get(li.getMiddleLineIndex()).size());
//			System.out.println(lineIndexToDotListMap.get(li.getLowerLineIndex()).size());
			
//			System.out.println(threeIndexs);
			
		}
		
		for (int i = 0; i <  lineSize ; i++) {

			Line li = allSegmentedLines.get(i);
			System.out.println("line : " + i + 1 + " -- " + li.getUpperLineIndex() + " " + li.getMiddleLineIndex() + " "
					+ li.getLowerLineIndex());

			ArrayList<Integer> xIndexsOfFirstLineDots = getXOfDosFromLine(lineIndexToDotListMap.get(li.getUpperLineIndex()));
			ArrayList<Integer> xIndexsOfSecondLineDots = getXOfDosFromLine(lineIndexToDotListMap.get(li.getMiddleLineIndex()));
			ArrayList<Integer> xIndexsOfThirdLineDots = getXOfDosFromLine(lineIndexToDotListMap.get(li.getLowerLineIndex()));

//			System.out.println(xIndexsOfFirstLineDots);
//			System.out.println(xIndexsOfSecondLineDots);
//			System.out.println(xIndexsOfThirdLineDots.size()+" ok");

			ArrayList<LineColumn> listOfLineColumn = convertLineIntoColumnVersion_2(xIndexsOfFirstLineDots,
					xIndexsOfSecondLineDots, xIndexsOfThirdLineDots);
			for (LineColumn col : listOfLineColumn) {
				col.printColumn();
			}

			ArrayList<BrailleWord> brailleWords = getBrailleWordsFromLine(listOfLineColumn);

			for (BrailleWord word : brailleWords)
				word.printWord();

			ArrayList<String> bengaliWordList = getBengaliWordListOfLine(brailleWords);

			String Line = "";
			for (int index = 0; index < bengaliWordList.size(); index++) {
				Line += bengaliWordList.get(index) + "   ";
			}

			Lines.add(Line);

		}

		for (int i = 0; i < Lines.size(); i++)
			System.out.println(Lines.get(i) + " ...");

//		FileWithPrintWriter printWriter = null;
		File outputfile = new File(Constant.OUTPUT_IMAGE_FILE_NAME + "." + Constant.OUTPUT_IMAGE_FILE_TYPE);

		try {
			ImageIO.write(outputImage, Constant.OUTPUT_IMAGE_FILE_TYPE, outputfile);
		} catch (IOException e1) {

		}

		return Lines;
	}

	private ArrayList<String> getBengaliWordListOfLine(ArrayList<BrailleWord> brailleWords) {

		ArrayList<String> bengaliWordList = new ArrayList<String>();
		Constant.OUTPUT_LIST.add("all word is identified in this line");

		int cont = 0;
		for (int x = 0; x < brailleWords.size(); x++) {
			cont += brailleWords.get(x).getColList().size();
		}

		Constant.OUTPUT_LIST.add("word size:: " + brailleWords.size());

		for (int j = 0; j < brailleWords.size(); j++) {

			Constant.OUTPUT_LIST.add("=====================================");

			BengaliWord bengaliWord = new BengaliWord();

			BrailleWord word = brailleWords.get(j);

			Constant.OUTPUT_LIST
					.add("word no :: " + (j + 1) + " and number of column in this word :: " + word.getColList().size());

			int coulmnToCovered = word.getColList().size();
			int coveredColSize = 0;

			for (int colIndex = 1; colIndex < coulmnToCovered; colIndex++) {

				LineColumn previousColumn = word.getColList().get(colIndex - 1);
				LineColumn currentColumn = word.getColList().get(colIndex);

				Constant.OUTPUT_LIST.add("previous column:: ");
				previousColumn.printColumn();
				Constant.OUTPUT_LIST.add("current column::  ");
				currentColumn.printColumn();

				Constant.OUTPUT_LIST.add(currentColumn.getAverageIndex() + "  " + previousColumn.getAverageIndex());
				int diff = currentColumn.getAverageIndex() - previousColumn.getAverageIndex();
				Constant.OUTPUT_LIST.add("difference found:: " + diff);

				if (diff < 40) {

					LetterInBrailleCode letter = new LetterInBrailleCode(previousColumn, currentColumn);
					Constant.OUTPUT_LIST.add(
							"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
					colIndex++;
					coveredColSize += 2;

					bengaliWord.addLetters(letter.getLetter());

				}

				else if (diff < 65) {

					LetterInBrailleCode letter = new LetterInBrailleCode("000", previousColumn);
					Constant.OUTPUT_LIST.add(
							"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
					coveredColSize++;
					bengaliWord.addLetters(letter.getLetter());

				}

				else if (diff < 85) {

					Constant.OUTPUT_LIST.add("colIndex :: " + colIndex);

					if (colIndex - 3 >= 0 && word.getColList().get(colIndex - 2).getAverageIndex()
							- word.getColList().get(colIndex - 3).getAverageIndex() < 40) {
////						Utils.OUTPUT_LIST.add("bug is over here " + colIndex);
//
//						Utils.OUTPUT_LIST.add(" ###################### ---- bug ---- ##########################");
//
//						Utils.OUTPUT_LIST.add(" ###################### ---- bug ---- ##########################");

						int currentDistance = previousColumn.getAverageIndex()
								- word.getColList().get(colIndex - 2).getAverageIndex();
						Constant.OUTPUT_LIST.add("**** diff :: " + currentDistance);

						LetterInBrailleCode letter = null;
						if (currentDistance < 65) {
							letter = new LetterInBrailleCode(previousColumn, "000");
						} else
							letter = new LetterInBrailleCode("000", previousColumn);

//						LetterInBrailleCode letter = new LetterInBrailleCode(previousColumn, "000");
						Constant.OUTPUT_LIST.add(
								"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
						coveredColSize++;
						bengaliWord.addLetters(letter.getLetter());
						Constant.OUTPUT_LIST.add("bengali letter:: eith bug " + letter.getLetter());

					}

					else if (colIndex + 1 < coulmnToCovered && word.getColList().get(colIndex + 1).getAverageIndex()
							- currentColumn.getAverageIndex() < 40) {

						Constant.OUTPUT_LIST.add("bug is over here " + colIndex);
						LetterInBrailleCode letter = new LetterInBrailleCode(previousColumn, "000");
						Constant.OUTPUT_LIST.add(
								"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
						coveredColSize++;
						bengaliWord.addLetters(letter.getLetter());

//						int currentDistance = previousColumn.getAverageIndex() - word.getColList().get(colIndex - 2).getAverageIndex();
//						Utils.OUTPUT_LIST.add("**** diff :: "  + currentDistance);
					}

					else {
						Constant.OUTPUT_LIST.add("output is here...");
						LetterInBrailleCode letter = new LetterInBrailleCode(previousColumn, "000");
						Constant.OUTPUT_LIST.add(
								"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
						coveredColSize++;
						bengaliWord.addLetters(letter.getLetter());
					}

				}

				else {

					LetterInBrailleCode letter = new LetterInBrailleCode(previousColumn, "000");
					Constant.OUTPUT_LIST.add(
							"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
					coveredColSize++;
					bengaliWord.addLetters(letter.getLetter());
				}
			}

			if (coveredColSize < coulmnToCovered && coulmnToCovered >= 2) {

				LineColumn previousColumn = word.getColList().get(coulmnToCovered - 2);
				LineColumn currentColumn = word.getColList().get(coulmnToCovered - 1);
				int diff = currentColumn.getAverageIndex() - previousColumn.getAverageIndex();

				if (diff < 65) {

					LetterInBrailleCode letter = new LetterInBrailleCode(currentColumn, "000");
					Constant.OUTPUT_LIST.add(
							"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
					coveredColSize++;
					bengaliWord.addLetters(letter.getLetter());

				} else if (diff < 85) {

					LetterInBrailleCode letter = new LetterInBrailleCode("000", currentColumn);
					Constant.OUTPUT_LIST.add(
							"letter code :: " + letter.getSymbol() + " .. bengali letter :: " + letter.getLetter());
					coveredColSize++;
					bengaliWord.addLetters(letter.getLetter());
					Constant.OUTPUT_LIST.add("bengali letter:: eith bug " + letter.getLetter());

				}
				coveredColSize++;
			}

			Constant.OUTPUT_LIST.add("column covered :: " + coveredColSize);
			Constant.OUTPUT_LIST.add("------------------- * word covered * -------------------");
			String bngWord = bengaliWord.getBengaliWord();
			bengaliWordList.add(bngWord);

		}

		return bengaliWordList;
	}

	private ArrayList<BrailleWord> getBrailleWordsFromLine(ArrayList<LineColumn> listOfLineColumn) {
		ArrayList<BrailleWord> brailleWords = new ArrayList<BrailleWord>();
		ArrayList<LineColumn> tempColList = new ArrayList<LineColumn>();

		LineColumn tempColumn = listOfLineColumn.get(0);
		tempColList.add(tempColumn);

		for (int x = 1; x < listOfLineColumn.size(); x++) {
			LineColumn currentColumn = listOfLineColumn.get(x);
			int difference = currentColumn.getAverageIndex() - tempColumn.getAverageIndex();
			tempColumn = currentColumn;

			if (difference >= 120) {
				brailleWords.add(new BrailleWord(tempColList));
				tempColList = new ArrayList<>();
				tempColList.add(tempColumn);
			}

			else {
				tempColList.add(currentColumn);
			}
		}

		brailleWords.add(new BrailleWord(tempColList));
		return brailleWords;
	}

	private ArrayList<String> getWords(Line line) {
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<Integer> xIndexsOfFirstLineDots = getXOfDosFromLine(
				lineIndexToDotListMap.get(line.getUpperLineIndex()));
		ArrayList<Integer> xIndexsOfSecondLineDots = getXOfDosFromLine(
				lineIndexToDotListMap.get(line.getMiddleLineIndex()));
		ArrayList<Integer> xIndexsOfThirdLineDots = getXOfDosFromLine(
				lineIndexToDotListMap.get(line.getLowerLineIndex()));

		ArrayList<LineColumn> listOfLineColumn = convertLineIntoColumn(xIndexsOfFirstLineDots, xIndexsOfSecondLineDots,
				xIndexsOfThirdLineDots);
		ArrayList<ArrayList<LineColumn>> wordSegmentList = convertWordSegment(listOfLineColumn);

		ArrayList<String> selectedWords = new ArrayList<String>();

		for (int j = 0; j < wordSegmentList.size(); j++) {
			ArrayList<LineColumn> wordWithColumnSegment = wordSegmentList.get(j);
			String probableWord1 = firstProbableWord(wordWithColumnSegment);
			String probableWord2 = secondProbableWord(wordWithColumnSegment);

			String word = Constant.BANGLA_DICTIONARY.getWordWithLessEditDistance(probableWord1, probableWord2);
			words.add(word);
		}
		return words;
	}

	private String secondProbableWord(ArrayList<LineColumn> wordWithColumnSegment) {
		ArrayList<String> letters = new ArrayList<String>();
		int nextColumnLimit = (lineDistance * 5) / 10;
		ArrayList<LineColumn> currentWordSegment = (ArrayList<LineColumn>) wordWithColumnSegment.clone();
		int currentColumnIndex = currentWordSegment.get(0).getAverageIndex();
		Constant.OUTPUT_LIST.add("initial current column index as 2nd column of word is -> " + currentColumnIndex);
		int sizeOfCurrentLineColumnSegment = currentWordSegment.size();

		String symbol1 = currentWordSegment.get(0).getSymbol();
		String symbol = "000" + symbol1;
		String letter = Constant.LETTERS.getLetters(symbol);
		letters.add(letter);
		currentWordSegment.remove(0);
		int columnCovered = 1;

		Constant.OUTPUT_LIST.add(symbol);
		Constant.OUTPUT_LIST.add(letter);

//		Utils.OUTPUT_LIST.add("initial size:: " + sizeOfCurrentLineColumnSegment);
//		Utils.OUTPUT_LIST.add("lineDistance " + lineDistance);
//		Utils.OUTPUT_LIST.add("next column limit " + nextColumnLimit);

		int estimatedNextColumnIndex = currentColumnIndex + lineDistance;
		if (columnCovered == sizeOfCurrentLineColumnSegment) {
			Constant.OUTPUT_LIST.add("ding ding not found any thing...");
			return getwordInString(letters);
		}

		int tempIndex = currentWordSegment.get(0).getAverageIndex();
		int difference = Math.abs(estimatedNextColumnIndex - tempIndex);

		Constant.OUTPUT_LIST.add("estimated next column :: " + estimatedNextColumnIndex);
		Constant.OUTPUT_LIST.add("actual next column :: " + tempIndex);

		if (difference <= nextColumnLimit * 2) {
			Constant.OUTPUT_LIST.add("next column is found immediate column");

			currentColumnIndex = tempIndex;
			symbol1 = currentWordSegment.get(0).getSymbol();
			currentWordSegment.remove(0);

		}

		else {
			Constant.OUTPUT_LIST.add("after next column is found immediate column");

			difference = Math.abs((estimatedNextColumnIndex + lineDistance) - tempIndex);
			Constant.OUTPUT_LIST.add("after next difference " + difference);

			if (difference <= nextColumnLimit * 2) {
				Constant.OUTPUT_LIST.add("next column is found next column of the letter column");

				currentColumnIndex = estimatedNextColumnIndex;
				symbol1 = "000";
//				currentWordSegment.remove(0);
			}

			else {
				Constant.OUTPUT_LIST.add("not found eny thing :( ******");
				return getwordInString(letters);
			}
		}

		while (currentWordSegment.size() > 0) {

			int nextColumnIndex = currentWordSegment.get(0).getAverageIndex();
			difference = nextColumnIndex - currentColumnIndex;

			Constant.OUTPUT_LIST.add("current column information :: " + currentColumnIndex + "  -- " + symbol1
					+ " next column Index :: " + nextColumnIndex);
			Constant.OUTPUT_LIST.add("difference " + difference);
			// next column found
			if (difference >= lineDistance - nextColumnLimit && difference <= lineDistance + nextColumnLimit) {
				String symbol2 = currentWordSegment.get(0).getSymbol();
				symbol = symbol1 + symbol2;

				if (symbol1.equals("000"))
					columnCovered += 1;
				else
					columnCovered += 2;

				letter = Constant.LETTERS.getLetters(symbol);
				letters.add(letter);
				currentWordSegment.remove(0);

				Constant.OUTPUT_LIST.add(symbol);
				Constant.OUTPUT_LIST.add(letter);
			}

			else {
				symbol = symbol1 + "000";
				String Letter = Constant.LETTERS.getLetters(symbol);
				letters.add(letter);
				columnCovered += 1;

				Constant.OUTPUT_LIST.add(symbol);
				Constant.OUTPUT_LIST.add(Letter);
			}

			currentColumnIndex = currentColumnIndex + lineDistance * 2;
			symbol1 = "000";
			if (columnCovered >= sizeOfCurrentLineColumnSegment)
				break;
			tempIndex = currentWordSegment.get(0).getAverageIndex();
			difference = Math.abs(tempIndex - currentColumnIndex);

			Constant.OUTPUT_LIST.add("Actual next index:: " + tempIndex);
			Constant.OUTPUT_LIST.add("Predected column index:: " + currentColumnIndex);
			Constant.OUTPUT_LIST.add("difference for next column index:: " + difference);

			if (difference <= 2 * nextColumnLimit) {
				currentColumnIndex = tempIndex;
				symbol1 = currentWordSegment.get(0).getSymbol();
				currentWordSegment.remove(0);

				Constant.OUTPUT_LIST.add("next line found:: " + currentColumnIndex);
				Constant.OUTPUT_LIST.add("symbol of next:: " + symbol1);
			}

			Constant.OUTPUT_LIST.add("column covered:: " + columnCovered);
		}

		letters = Constant.FUNCTIONS.getReadableMergedWord(letters);
		return getwordInString(letters);

	}

	private String firstProbableWord(ArrayList<LineColumn> arrayList) {
		ArrayList<LineColumn> currentWordSegment = (ArrayList<LineColumn>) arrayList.clone();
		ArrayList<String> letters = new ArrayList<String>();

		// using initial column as 1st one---
		int currentColumnIndex = currentWordSegment.get(0).getAverageIndex();
		int columnCovered = 0;
		int nextColumnLimit = (lineDistance * 5) / 10;
		int sizeOfCurrentLineColumnSegment = currentWordSegment.size();
		String symbol1 = currentWordSegment.get(0).getSymbol();
		currentWordSegment.remove(0);

		Constant.OUTPUT_LIST.add("initial size:: " + sizeOfCurrentLineColumnSegment);
		Constant.OUTPUT_LIST.add("lineDistance " + lineDistance);
		Constant.OUTPUT_LIST.add("next column limit " + nextColumnLimit);

		while (currentWordSegment.size() > 0) {

			int nextColumnIndex = currentWordSegment.get(0).getAverageIndex();
			int difference = nextColumnIndex - currentColumnIndex;

			Constant.OUTPUT_LIST.add("current column information :: " + currentColumnIndex + "  -- " + symbol1
					+ " next column Index :: " + nextColumnIndex);
			Constant.OUTPUT_LIST.add("difference " + difference);
			// next column found
			if (difference >= lineDistance - nextColumnLimit && difference <= lineDistance + nextColumnLimit) {
				String symbol2 = currentWordSegment.get(0).getSymbol();
				String symbol = symbol1 + symbol2;

				if (symbol1.equals("000"))
					columnCovered += 1;
				else
					columnCovered += 2;

				String letter = Constant.LETTERS.getLetters(symbol);
				currentWordSegment.remove(0);
				letters.add(letter);

				Constant.OUTPUT_LIST.add(symbol);
				Constant.OUTPUT_LIST.add(letter);
			}

			else {
				String symbol = symbol1 + "000";
				String letter = Constant.LETTERS.getLetters(symbol);
				columnCovered += 1;
				letters.add(letter);

				Constant.OUTPUT_LIST.add(symbol);
				Constant.OUTPUT_LIST.add(letter);
			}

			currentColumnIndex = currentColumnIndex + lineDistance * 2;
			symbol1 = "000";
			if (columnCovered >= sizeOfCurrentLineColumnSegment)
				break;
			int tempIndex = currentWordSegment.get(0).getAverageIndex();
			difference = Math.abs(tempIndex - currentColumnIndex);

			Constant.OUTPUT_LIST.add("Actual next index:: " + tempIndex);
			Constant.OUTPUT_LIST.add("Predected column index:: " + currentColumnIndex);
			Constant.OUTPUT_LIST.add("difference for next column index:: " + difference);

			if (difference <= 2 * nextColumnLimit) {
				currentColumnIndex = tempIndex;
				symbol1 = currentWordSegment.get(0).getSymbol();
				currentWordSegment.remove(0);

				Constant.OUTPUT_LIST.add("next line found:: " + currentColumnIndex);
				Constant.OUTPUT_LIST.add("symbol of next:: " + symbol1);
			}

			Constant.OUTPUT_LIST.add("column covered:: " + columnCovered);
		}

		if (columnCovered != sizeOfCurrentLineColumnSegment) {
			String symbol = symbol1 + "000";
			String letter = Constant.LETTERS.getLetters(symbol);

			Constant.OUTPUT_LIST.add("all column is not covered properly...");
			Constant.OUTPUT_LIST.add(letter);
			letters.add(letter);
		}

		letters = Constant.FUNCTIONS.getReadableMergedWord(letters);

		String stringWord1 = getwordInString(letters);
		return stringWord1;
	}

	private void colorSegmentedLine(ArrayList<Line> allSegmentedLines) {
		ArrayList<Integer> allSelectedSingleLine = new ArrayList<Integer>();
		for (int i = 0; i < allSegmentedLines.size(); i++) {
			allSelectedSingleLine.add(allSegmentedLines.get(i).getUpperLineIndex());
			allSelectedSingleLine.add(allSegmentedLines.get(i).getMiddleLineIndex());
			allSelectedSingleLine.add(allSegmentedLines.get(i).getLowerLineIndex());
		}
		colorLine(allSelectedSingleLine, Constant.YELLOW);
	}

	private ArrayList<Line> getAllLine() {
		ArrayList<Line> lines = new ArrayList<Line>();

//		System.out.println(lineIndex);
		int traverseIndex = 0;
		while (traverseIndex < lineIndex.size() - 2) {

			if (traverseIndex + 4 < lineIndex.size()) {

				int firstLineIndex = lineIndex.get(traverseIndex);
				int secondLineIndex = lineIndex.get(traverseIndex + 1);
				int thirdLineIndex = lineIndex.get(traverseIndex + 2);
				int forthLineIndex = lineIndex.get(traverseIndex + 3);
				int fifthLineIndex = lineIndex.get(traverseIndex + 4);

				int firstLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(firstLineIndex)).size();
				int secondLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(secondLineIndex)).size();
				int thirdLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(thirdLineIndex)).size();
				int forthLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(forthLineIndex)).size();
				int fifthLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(fifthLineIndex)).size();

				boolean takeFirstThreeLine = isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance,
						acceptanceOfLineDistance);
				boolean takeSecondThreeLine = isPartOfLine(secondLineIndex, thirdLineIndex, forthLineIndex,
						lineDistance, acceptanceOfLineDistance);
				boolean takeThirdThreeLine = isPartOfLine(thirdLineIndex, forthLineIndex, fifthLineIndex, lineDistance,
						acceptanceOfLineDistance);

//				System.out.println(firstLineIndex + " " + secondLineIndex + " " + thirdLineIndex + " " + forthLineIndex
//						+ " " + fifthLineIndex);
//				System.out.println(takeFirstThreeLine + " " + secondLineIndex + " " + thirdLineIndex);

				if (takeFirstThreeLine && takeSecondThreeLine && takeThirdThreeLine) {

					int firstThreeCount = firstLineDotCount + secondLineDotCount + thirdLineDotCount;
					int secondThreeCount = secondLineDotCount + thirdLineDotCount + forthLineDotCount;
					int thirdThreeCount = thirdLineDotCount + forthLineDotCount + fifthLineDotCount;

					if (firstThreeCount >= secondThreeCount && firstThreeCount >= thirdThreeCount) {
						lines.add(new Line(firstLineIndex, secondLineIndex, thirdLineIndex));
						traverseIndex += 3;
						continue;
					}

					if (secondThreeCount >= firstThreeCount && secondThreeCount >= thirdThreeCount) {
						lines.add(new Line(secondLineIndex, thirdLineIndex, forthLineIndex));
						traverseIndex += 4;
						continue;
					}

					if (thirdThreeCount >= firstThreeCount && thirdThreeCount >= secondThreeCount) {
						lines.add(new Line(thirdLineIndex, forthLineIndex, fifthLineIndex));
						traverseIndex += 5;
						continue;
					}

				}

				if (takeFirstThreeLine && takeSecondThreeLine) {

					int firstThreeCount = firstLineDotCount + secondLineDotCount + thirdLineDotCount;
					int secondThreeCount = secondLineDotCount + thirdLineDotCount + forthLineDotCount;

//					System.out.println(firstLineIndex + " " + secondLineIndex);
//					System.out.println("co: " + firstThreeCount + " " + secondThreeCount);

					if (firstThreeCount >= secondThreeCount) {
						lines.add(new Line(firstLineIndex, secondLineIndex, thirdLineIndex));
						traverseIndex += 3;
						continue;
					}

					if (secondThreeCount >= firstThreeCount) {
						lines.add(new Line(secondLineIndex, thirdLineIndex, forthLineIndex));
						traverseIndex += 4;
						continue;
					}

				}

				if (takeFirstThreeLine && takeThirdThreeLine) {

					int firstThreeCount = firstLineDotCount + secondLineDotCount + thirdLineDotCount;
					int thirdThreeCount = thirdLineDotCount + forthLineDotCount + fifthLineDotCount;

					if (firstThreeCount >= thirdThreeCount) {
						lines.add(new Line(firstLineIndex, secondLineIndex, thirdLineIndex));
						traverseIndex += 3;
						continue;
					}

					if (thirdThreeCount >= firstThreeCount) {
						lines.add(new Line(thirdLineIndex, forthLineIndex, fifthLineIndex));
						traverseIndex += 5;
						continue;
					}

				}

				if (takeSecondThreeLine && takeThirdThreeLine) {

					int secondThreeCount = secondLineDotCount + thirdLineDotCount + forthLineDotCount;
					int thirdThreeCount = thirdLineDotCount + forthLineDotCount + fifthLineDotCount;

//					System.out.println("ok");

					if (secondThreeCount >= thirdThreeCount) {
//						System.out.println("ok1");
						lines.add(new Line(secondLineIndex, thirdLineIndex, forthLineIndex));
						traverseIndex += 4;
						continue;
					}

					if (thirdThreeCount >= secondThreeCount) {
//						System.out.println("ok2");
						lines.add(new Line(thirdLineIndex, forthLineIndex, fifthLineIndex));
						traverseIndex += 5;
						continue;
					}

				}

			}

			if (traverseIndex + 3 < lineIndex.size()) {

				int firstLineIndex = lineIndex.get(traverseIndex);
				int secondLineIndex = lineIndex.get(traverseIndex + 1);
				int thirdLineIndex = lineIndex.get(traverseIndex + 2);
				int forthLineIndex = lineIndex.get(traverseIndex + 3);

				int firstLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(firstLineIndex)).size();
				int secondLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(secondLineIndex)).size();
				int thirdLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(thirdLineIndex)).size();
				int forthLineDotCount = getXOfDosFromLine(lineIndexToDotListMap.get(forthLineIndex)).size();

				boolean takeFirstThreeLine = isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance,
						acceptanceOfLineDistance);
				boolean takeSecondThreeLine = isPartOfLine(secondLineIndex, thirdLineIndex, forthLineIndex,
						lineDistance, acceptanceOfLineDistance);

				if (takeFirstThreeLine && takeSecondThreeLine) {

					int firstThreeCount = firstLineDotCount + secondLineDotCount + thirdLineDotCount;
					int secondThreeCount = secondLineDotCount + thirdLineDotCount + forthLineDotCount;

					if (firstThreeCount >= secondThreeCount) {
						lines.add(new Line(firstLineIndex, secondLineIndex, thirdLineIndex));
						traverseIndex += 3;
						continue;
					}

					if (secondThreeCount >= firstThreeCount) {
						lines.add(new Line(secondLineIndex, thirdLineIndex, forthLineIndex));
						traverseIndex += 4;
						continue;
					}

				}

			}

			int firstLineIndex = lineIndex.get(traverseIndex);
			int secondLineIndex = lineIndex.get(traverseIndex + 1);
			int thirdLineIndex = lineIndex.get(traverseIndex + 2);

			if (isPartOfLine(firstLineIndex, secondLineIndex, thirdLineIndex, lineDistance, acceptanceOfLineDistance)) {
				lines.add(new Line(firstLineIndex, secondLineIndex, thirdLineIndex));
				traverseIndex += 3;
				continue;
			}

			traverseIndex++;
		}

		return lines;
	}

	private void processDistance() {
		ArrayList<Integer> differenceBetweenLines = new ArrayList<Integer>();
		for (int i = 1; i < lineIndex.size(); i++)
			differenceBetweenLines.add(lineIndex.get(i) - lineIndex.get(i - 1));
		Collections.sort(differenceBetweenLines);
		lineDistance = differenceBetweenLines.get(differenceBetweenLines.size() / 2);
		Constant.DIFFERENCE_BETWEEN_LINE = lineDistance;
		acceptanceOfLineDistance = (int) ((double) lineDistance * 0.50);
		Constant.DIFFERENCE_BETWEEN_WORDS = lineDistance * 3 - (lineDistance / 4);

		Constant.OUTPUT_LIST.add("line distance:: " + lineDistance);

	}

	private ArrayList<Integer> mergeLineIndexs(ArrayList<Integer> lineIndex) {

//		Utils.OUTPUT_LIST.add("merging line indexes");
//		for(int i = 0; i < lineIndex.size(); i++) {
//			Utils.OUTPUT_LIST.add("line index:: " + lineIndex.get(i) + " size of dot in this line:: " +lineIndexToDotListMap.get(lineIndex.get(i)).size());
//		}
//
//		System.out.println("-- " + lineIndex.size());

		int differenceLimit = Constant.INITAL_DIFFRENCE_BETWEEN_LINE;
		ArrayList<Integer> tempLineIndex = new ArrayList<>();

		while (differenceLimit <= Constant.MAXIMUM_DISTANCE) {

			ArrayList<Integer> copyOfLineIndex = (ArrayList<Integer>) lineIndex.clone();
			ArrayList<Integer> newLineIndexList = new ArrayList<>();

			for (int i = 0; i < copyOfLineIndex.size(); i++) {
				int currentLineIndex = lineIndex.get(i);
				int nextLineIndex = -1;
				int secondLineIndex = -1;

				if (i + 2 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
					secondLineIndex = lineIndex.get(i + 2);
				}

				else if (i + 1 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
				}

				if (secondLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit
						&& secondLineIndex - nextLineIndex < differenceLimit) {

					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(secondLineIndex));
					int average = 0;
					for (int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 2;

//					System.out.println(newLineIndexList.size());

				}

				else if (nextLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit) {
					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));

					int average = 0;
					for (int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 1;
				} else {
					newLineIndexList.add(currentLineIndex);
				}
			}

//			System.out.println(lineIndex.size());
//			System.out.println(newLineIndexList.size());

			lineIndex = newLineIndexList;
			tempLineIndex = newLineIndexList;
//			System.out.println(lineIndex.size());
			lineIndex = getAverageLineIndexBasedOnDots(lineIndex);
//			System.out.println(lineIndex.size());
			differenceLimit += Constant.LINE_INDEX_MERGED_UNIT;
		}

		if (tempLineIndex.size() >= 0) {
			lineIndex = new ArrayList<Integer>();
			lineIndex.addAll(tempLineIndex);
		}

		return lineIndex;
	}

	private String getwordInString(ArrayList<String> letters) {
		String stringWord1 = "";
		for (int x = 0; x < letters.size(); x++)
			stringWord1 = stringWord1 + letters.get(x);
		return stringWord1;
	}

	private ArrayList<ArrayList<LineColumn>> convertWordSegment(ArrayList<LineColumn> listOfLineColumn) {

		ArrayList<ArrayList<LineColumn>> segmentedColumnList = new ArrayList<ArrayList<LineColumn>>();
		ArrayList<LineColumn> currentCulmnList = new ArrayList<LineColumn>();
		LineColumn currentColumn = listOfLineColumn.get(0);
		int currentColumnAverageIndex = currentColumn.getAverageIndex();
		int listSize = listOfLineColumn.size();
		currentCulmnList.add(currentColumn);

		for (int i = 1; i < listSize; i++) {
			currentColumn = listOfLineColumn.get(i);
			int tempAverageIndex = currentColumn.getAverageIndex();

			if (tempAverageIndex - currentColumnAverageIndex >= Constant.DIFFERENCE_BETWEEN_WORDS) {
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
		int limit = Constant.DIFFERENCE_BETWEEN_LINE / 2;

		while (anyDotExistInThreeLine(xIndexsOfFirstLineDots, xIndexsOfSecondLineDots, xIndexsOfThirdLineDots)) {
			Constant.OUTPUT_LIST.add(xIndexsOfFirstLineDots.size() + " - " + xIndexsOfSecondLineDots.size() + " - "
					+ xIndexsOfThirdLineDots.size());
			int smallestIndex = width + 100;

			if (xIndexsOfFirstLineDots.size() > 0)
				smallestIndex = Math.min(smallestIndex, xIndexsOfFirstLineDots.get(0));
			if (xIndexsOfSecondLineDots.size() > 0)
				smallestIndex = Math.min(smallestIndex, xIndexsOfSecondLineDots.get(0));
			if (xIndexsOfThirdLineDots.size() > 0)
				smallestIndex = Math.min(smallestIndex, xIndexsOfThirdLineDots.get(0));
			Constant.OUTPUT_LIST.add(smallestIndex + " small index");

			int upperDot = -1;
			int middledot = -1;
			int lowerDot = -1;

			if (xIndexsOfFirstLineDots.size() > 0
					&& isDotBetweenLimit(xIndexsOfFirstLineDots.get(0), limit, smallestIndex)) {
				upperDot = xIndexsOfFirstLineDots.get(0);
				xIndexsOfFirstLineDots.remove(0);
			}
			if (xIndexsOfSecondLineDots.size() > 0
					&& isDotBetweenLimit(xIndexsOfSecondLineDots.get(0), limit, smallestIndex)) {
				middledot = xIndexsOfSecondLineDots.get(0);
				xIndexsOfSecondLineDots.remove(0);
			}
			if (xIndexsOfThirdLineDots.size() > 0
					&& isDotBetweenLimit(xIndexsOfThirdLineDots.get(0), limit, smallestIndex)) {
				lowerDot = xIndexsOfThirdLineDots.get(0);
				xIndexsOfThirdLineDots.remove(0);
			}

			Constant.OUTPUT_LIST.add(upperDot + "*" + middledot + "*" + lowerDot);

			columnList.add(new LineColumn(upperDot, middledot, lowerDot));
		}

		return columnList;
	}

	private ArrayList<LineColumn> convertLineIntoColumnVersion_2(ArrayList<Integer> xIndexsOfFirstLineDots,
			ArrayList<Integer> xIndexsOfSecondLineDots, ArrayList<Integer> xIndexsOfThirdLineDots) {

		ArrayList<LineColumn> columnList = new ArrayList<LineColumn>();
		int limit = distanceBetweenDot / 2;

		while (anyDotExistInThreeLine(xIndexsOfFirstLineDots, xIndexsOfSecondLineDots, xIndexsOfThirdLineDots)) {
			Constant.OUTPUT_LIST.add(xIndexsOfFirstLineDots.size() + " - " + xIndexsOfSecondLineDots.size() + " - "
					+ xIndexsOfThirdLineDots.size());
			int smallestIndex = width + 100;

			if (xIndexsOfFirstLineDots.size() > 0)
				smallestIndex = Math.min(smallestIndex, xIndexsOfFirstLineDots.get(0));
			if (xIndexsOfSecondLineDots.size() > 0)
				smallestIndex = Math.min(smallestIndex, xIndexsOfSecondLineDots.get(0));
			if (xIndexsOfThirdLineDots.size() > 0)
				smallestIndex = Math.min(smallestIndex, xIndexsOfThirdLineDots.get(0));
			Constant.OUTPUT_LIST.add(smallestIndex + " small index");

			int upperDot = -1;
			int middledot = -1;
			int lowerDot = -1;

			if (xIndexsOfFirstLineDots.size() > 0
					&& isDotBetweenLimit(xIndexsOfFirstLineDots.get(0), limit, smallestIndex)) {
				upperDot = xIndexsOfFirstLineDots.get(0);
				xIndexsOfFirstLineDots.remove(0);
			}
			if (xIndexsOfSecondLineDots.size() > 0
					&& isDotBetweenLimit(xIndexsOfSecondLineDots.get(0), limit, smallestIndex)) {
				middledot = xIndexsOfSecondLineDots.get(0);
				xIndexsOfSecondLineDots.remove(0);
			}
			if (xIndexsOfThirdLineDots.size() > 0
					&& isDotBetweenLimit(xIndexsOfThirdLineDots.get(0), limit, smallestIndex)) {
				lowerDot = xIndexsOfThirdLineDots.get(0);
				xIndexsOfThirdLineDots.remove(0);
			}

			Constant.OUTPUT_LIST.add(upperDot + "*" + middledot + "*" + lowerDot);

			columnList.add(new LineColumn(upperDot, middledot, lowerDot));
		}

		return columnList;
	}

	private boolean isDotBetweenLimit(int integer, int limit, int smallestIndex) {
		int difference = integer - smallestIndex;
		return (difference <= (limit + limit / 4));
	}

	private boolean anyDotExistInThreeLine(ArrayList<Integer> xIndexsOfFirstLineDots,
			ArrayList<Integer> xIndexsOfSecondLineDots, ArrayList<Integer> xIndexsOfThirdLineDots) {
		return (xIndexsOfFirstLineDots.size() > 0 || xIndexsOfSecondLineDots.size() > 0
				|| xIndexsOfThirdLineDots.size() > 0);
	}

	private ArrayList<Integer> getXOfDosFromLine(ArrayList<Point> dotListOfLine) {

		ArrayList<Integer> allXs = new ArrayList<>();
		int dotListSize = dotListOfLine.size();

		// collecting all x from dots
		for (int i = 0; i < dotListSize; i++)
			allXs.add(dotListOfLine.get(i).getX());

		Collections.sort(allXs);
//		System.out.println(allXs);

		int previousXIndexOfDot = allXs.get(0);
		ArrayList<Integer> dotsToRemove = new ArrayList<Integer>();

		for (int j = 1; j < allXs.size(); j++) {
			if (allXs.get(j) - previousXIndexOfDot < Constant.SAME_POINT_COVERAGE)
				dotsToRemove.add(previousXIndexOfDot);
			previousXIndexOfDot = allXs.get(j);
		}

		/* this part is not so good product at all ==================================================
//		for(int j = 1; j < allXs.size(); j++) {
//			if(allXs.get(j) - previousXIndexOfDot < Utils.SAME_POINT_COVERAGE) {
//				dotsToRemove.add(allXs.get(j));
//			}
//			
//			else 
//				previousXIndexOfDot = allXs.get(j);
//		}
 
		 =============================================================================================*/
		
		/*
		ArrayList<Integer> selectedDots = new ArrayList<Integer>(); 
		int previousXIndexOfDot = allXs.get(0);
		ArrayList<Integer> dotsToRemove = new ArrayList<Integer>();
		ArrayList<Integer> dotSegment = new ArrayList<Integer>();
		dotSegment.add(previousXIndexOfDot);
		
		
		
		
		for (int j = 1; j < allXs.size(); j++) {
			if (allXs.get(j) - previousXIndexOfDot < Constant.SAME_POINT_COVERAGE) {
				dotSegment.add(allXs.get(j));
			}
			
			else {
				
				if(dotSegment.size() > 0) {
					int average = calculateAverage(dotSegment);
					selectedDots.add(average);
				}
				
				dotSegment = new ArrayList<Integer>();
				
				
			}
				dotsToRemove.add(previousXIndexOfDot);
			previousXIndexOfDot = allXs.get(j);
		}
		
		if(dotSegment.size() > 0) {
			int average = calculateAverage(dotSegment);
			selectedDots.add(average);
		}
		*/
		

		for (int j = 0; j < dotsToRemove.size(); j++)
			allXs.remove(dotsToRemove.get(j));

		return allXs;
	}
	
	private int calculateAverage(ArrayList<Integer> marks) {
	      int sum = 0;
	      for (int i=0; i< marks.size(); i++) {
	            sum += i;
	      }
	      return sum / marks.size();
	  }

	private ArrayList<String> getLettersFromLast(int firstLineIndex, int secondLineIndex, int thirdLineIndex) {

		ArrayList<Integer> firstLine = getDotSequence(firstLineIndex);
		ArrayList<Integer> secondLine = getDotSequence(secondLineIndex);
		ArrayList<Integer> thirdLine = getDotSequence(thirdLineIndex);

		Collections.reverse(firstLine);
		Collections.reverse(secondLine);
		Collections.reverse(thirdLine);

//		System.out.println(firstLine);
//		System.out.println(secondLine);
//		System.out.println(thirdLine);

		ArrayList<String> letters = new ArrayList<String>();

		int initialUpperDot = firstLine.get(0);
		int initialMiddelDot = secondLine.get(0);
		int initialLowerDot = thirdLine.get(0);

		int greatestXIndex = initialUpperDot;
		if (initialMiddelDot > greatestXIndex)
			greatestXIndex = initialMiddelDot;
		if (initialLowerDot > greatestXIndex)
			greatestXIndex = initialLowerDot;

		int previousIndex = greatestXIndex + 35;

		while (firstLine.size() > 0 || secondLine.size() > 0 || thirdLine.size() > 0) {
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
			if (firstLine.size() > 0) {
				if (Math.abs(firstLine.get(0) - previousIndex) >= 20
						&& Math.abs(firstLine.get(0) - previousIndex) <= 45) {
					nextUpperDot = firstLine.get(0);
					firstLine.remove(0);
				}
			}

			if (secondLine.size() > 0) {
				if (Math.abs(secondLine.get(0) - previousIndex) >= 20
						&& Math.abs(secondLine.get(0) - previousIndex) <= 45) {
					nextMiddelDot = secondLine.get(0);
					secondLine.remove(0);
				}
			}

			if (thirdLine.size() > 0) {
				if (Math.abs(thirdLine.get(0) - previousIndex) >= 20
						&& Math.abs(thirdLine.get(0) - previousIndex) <= 45) {
					nextLowerDot = thirdLine.get(0);
					thirdLine.remove(0);
				}
			}

			if (nextUpperDot > 0)
				currentIndex = nextUpperDot;
			if (currentIndex < 0 && nextMiddelDot > 0)
				currentIndex = nextMiddelDot;
			if (nextMiddelDot > 0 && nextMiddelDot > currentIndex)
				currentIndex = nextMiddelDot;
			if (currentIndex < 0 && nextLowerDot > 0)
				currentIndex = nextLowerDot;
			if (nextLowerDot > 0 && nextLowerDot > currentIndex)
				currentIndex = nextLowerDot;

//			System.out.println(nextUpperDot + " " + nextLowerDot + " " + nextMiddelDot);
//			System.out.println("current Index " + currentIndex);

			Constant.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);

			// second level identification....
			if (firstLine.size() > 0) {
				if (Math.abs(firstLine.get(0) - currentIndex) >= 20
						&& Math.abs(firstLine.get(0) - currentIndex) <= 45) {
					upperDot = firstLine.get(0);
					firstLine.remove(0);
				}
			}

			if (secondLine.size() > 0) {
				if (Math.abs(secondLine.get(0) - currentIndex) >= 20
						&& Math.abs(secondLine.get(0) - currentIndex) <= 45) {
					middelDot = secondLine.get(0);
					secondLine.remove(0);
				}
			}

			if (thirdLine.size() > 0) {
				if (Math.abs(thirdLine.get(0) - currentIndex) >= 20
						&& Math.abs(thirdLine.get(0) - currentIndex) <= 45) {
					lowerDot = thirdLine.get(0);
					thirdLine.remove(0);
				}
			}

//			System.out.println(upperDot + " " + middelDot + " " + lowerDot + "  >");
			if (nextUpperDot == -1 && nextMiddelDot == -1 && nextLowerDot == -1) {
				if (upperDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
				if (middelDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
				if (lowerDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

//				System.out.println(letter);
				letters.add(Constant.LETTERS.getLetters(letter));

				ArrayList<Integer> tempIndex = new ArrayList<Integer>();
				if (firstLine.size() > 0)
					tempIndex.add(firstLine.get(0));
				if (secondLine.size() > 0)
					tempIndex.add(secondLine.get(0));
				if (thirdLine.size() > 0)
					tempIndex.add(thirdLine.get(0));

				Collections.sort(tempIndex);
				if (tempIndex.size() > 0)
					previousIndex = tempIndex.get(0) - 25;
				if (tempIndex.size() > 0 && tempIndex.get(0) - currentIndex >= 90)
					letters.add("    ");
//				System.out.println("previous index " + previousIndex);

			}

			else {
				if (upperDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
				if (middelDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
				if (lowerDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

				if (nextUpperDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 3);
				if (nextMiddelDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 4);
				if (nextLowerDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 5);

//				System.out.println(letter);
				letters.add(Constant.LETTERS.getLetters(letter));

				ArrayList<Integer> tempIndex = new ArrayList<Integer>();
				if (firstLine.size() > 0)
					tempIndex.add(firstLine.get(0));
				if (secondLine.size() > 0)
					tempIndex.add(secondLine.get(0));
				if (thirdLine.size() > 0)
					tempIndex.add(thirdLine.get(0));

				Collections.sort(tempIndex);
				if (tempIndex.size() > 0) {
					previousIndex = tempIndex.get(0) + 25;
					if (Math.abs(tempIndex.get(0) - currentIndex) >= 90)
						letters.add("    ");

				}
			}

//			System.out.println(letters);
		}

		Collections.reverse(letters);

		return letters;

//		return null;
	}

	private ArrayList<Integer> getMergedLineIndex(ArrayList<Integer> list) {

		int differenceLimit = Constant.INITAL_DIFFRENCE_BETWEEN_LINE;

		while (differenceLimit <= Constant.MAXIMUM_DISTANCE) {

			ArrayList<Integer> copyOfLineIndex = (ArrayList<Integer>) list.clone();
			ArrayList<Integer> newLineIndexList = new ArrayList<>();

			for (int i = 0; i < copyOfLineIndex.size(); i++) {
				int currentLineIndex = lineIndex.get(i);
				ArrayList<Point> dotsOfCurrentLine = lineIndexToDotListMap.get(currentLineIndex);

				int nextLineIndex = -1;
				int secondLineIndex = -1;

				if (i + 2 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
					secondLineIndex = lineIndex.get(i + 2);

				}

				else if (i + 1 < copyOfLineIndex.size()) {
					nextLineIndex = lineIndex.get(i + 1);
				}

				if (secondLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit
						&& secondLineIndex - nextLineIndex < differenceLimit) {
//					int average = (currentLineIndex + nextLineIndex + secondLineIndex) / 3;
					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(secondLineIndex));
					int average = 0;
					for (int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 2;
				}

				else if (nextLineIndex >= 0 && nextLineIndex - currentLineIndex < differenceLimit) {
//					int average = (currentLineIndex + nextLineIndex) / 2;
					ArrayList<Point> newDotList = new ArrayList<>();
					newDotList.addAll(lineIndexToDotListMap.get(currentLineIndex));
					newDotList.addAll(lineIndexToDotListMap.get(nextLineIndex));

					int average = 0;
					for (int k = 0; k < newDotList.size(); k++)
						average += newDotList.get(k).getY();
					average = average / newDotList.size();

					newLineIndexList.add(average);
					lineIndexToDotListMap.put(average, newDotList);
					i += 1;
				} else {
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

		for (int i = 0; i < primaryLineIndexCopy.size(); i++) {

			int index = primaryLineIndexCopy.get(i);
			ArrayList<Point> allDotsInCurrentLine = lineIndexToDotListMap.get(index);
			ArrayList<Integer> allYCoordinateOfCurrentLineDots = new ArrayList<Integer>();

			int sum = 0;
			for (int j = 0; j < allDotsInCurrentLine.size(); j++)
				sum += allDotsInCurrentLine.get(j).getY();

			int average = sum / allDotsInCurrentLine.size();
			lineIndexToDotListMap.put(average, allDotsInCurrentLine);
			allAverage.add(average);
		}
		return allAverage;
	}

	private ArrayList<String> getWords(int firstLineIndex, int secondLineIndex, int thirdLineIndex) {

		ArrayList<Integer> firstLine = getDotSequence(firstLineIndex);
		ArrayList<Integer> secondLine = getDotSequence(secondLineIndex);
		ArrayList<Integer> thirdLine = getDotSequence(thirdLineIndex);

//		System.out.println(firstLine);
//		System.out.println(secondLine);
//		System.out.println(thirdLine);

//		System.out.println("ok ok ok line");

		ArrayList<String> letters = new ArrayList<String>();

		int initialUpperDot = firstLine.get(0);
		int initialMiddelDot = secondLine.get(0);
		int initialLowerDot = thirdLine.get(0);

		int smallestXIndex = initialUpperDot;
		if (initialMiddelDot < smallestXIndex)
			smallestXIndex = initialMiddelDot;
		if (initialLowerDot < smallestXIndex)
			smallestXIndex = initialLowerDot;

//		Utils.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);
		int previousIndex = smallestXIndex - 35;

		while (firstLine.size() > 0 || secondLine.size() > 0 || thirdLine.size() > 0) {
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
			if (firstLine.size() > 0) {
				if (firstLine.get(0) - previousIndex >= 20 && firstLine.get(0) - previousIndex <= 45) {
					upperDot = firstLine.get(0);
					firstLine.remove(0);
				}
			}

			if (secondLine.size() > 0) {
				if (secondLine.get(0) - previousIndex >= 20 && secondLine.get(0) - previousIndex <= 45) {
					middelDot = secondLine.get(0);
					secondLine.remove(0);
				}
			}

			if (thirdLine.size() > 0) {
				if (thirdLine.get(0) - previousIndex >= 20 && thirdLine.get(0) - previousIndex <= 45) {
					lowerDot = thirdLine.get(0);
					thirdLine.remove(0);
				}
			}

			if (upperDot > 0)
				currentIndex = upperDot;
			if (currentIndex < 0 && middelDot > 0)
				currentIndex = middelDot;
			if (middelDot > 0 && middelDot < currentIndex)
				currentIndex = middelDot;
			if (currentIndex < 0 && lowerDot > 0)
				currentIndex = lowerDot;
			if (lowerDot > 0 && lowerDot < currentIndex)
				currentIndex = lowerDot;

//			System.out.println(upperDot + " " + middelDot + " " + lowerDot);
//			System.out.println("current Index " + currentIndex);

			Constant.FUNCTIONS.printCurrentLine(firstLine, secondLine, thirdLine);

			// second level identification....
			if (firstLine.size() > 0) {
				if (firstLine.get(0) - currentIndex >= 20 && firstLine.get(0) - currentIndex <= 45) {
					nextUpperDot = firstLine.get(0);
					firstLine.remove(0);
				}
			}

			if (secondLine.size() > 0) {
				if (secondLine.get(0) - currentIndex >= 20 && secondLine.get(0) - currentIndex <= 45) {
					nextMiddelDot = secondLine.get(0);
					secondLine.remove(0);
				}
			}

			if (thirdLine.size() > 0) {
				if (thirdLine.get(0) - currentIndex >= 20 && thirdLine.get(0) - currentIndex <= 45) {
					nextLowerDot = thirdLine.get(0);
					thirdLine.remove(0);
				}
			}

//			System.out.println(nextUpperDot + " " + nextMiddelDot + " " + nextLowerDot + "  >");
			if (nextUpperDot == -1 && nextMiddelDot == -1 && nextLowerDot == -1) {
				if (upperDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
				if (middelDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
				if (lowerDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

//				System.out.println(letter);
				letters.add(Constant.LETTERS.getLetters(letter));

				ArrayList<Integer> tempIndex = new ArrayList<Integer>();
				if (firstLine.size() > 0)
					tempIndex.add(firstLine.get(0));
				if (secondLine.size() > 0)
					tempIndex.add(secondLine.get(0));
				if (thirdLine.size() > 0)
					tempIndex.add(thirdLine.get(0));

				Collections.sort(tempIndex);
				if (tempIndex.size() > 0)
					previousIndex = tempIndex.get(0) - 25;
				if (tempIndex.size() > 0 && tempIndex.get(0) - currentIndex >= 90)
					letters.add("    ");
//				System.out.println("previous index " + previousIndex);

			}

			else {
				if (upperDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 0);
				if (middelDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 1);
				if (lowerDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 2);

				if (nextUpperDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 3);
				if (nextMiddelDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 4);
				if (nextLowerDot != -1)
					letter = Constant.FUNCTIONS.replaceCharUsingCharArray(letter, '1', 5);

//				System.out.println(letter);
				letters.add(Constant.LETTERS.getLetters(letter));

				ArrayList<Integer> tempIndex = new ArrayList<Integer>();
				if (firstLine.size() > 0)
					tempIndex.add(firstLine.get(0));
				if (secondLine.size() > 0)
					tempIndex.add(secondLine.get(0));
				if (thirdLine.size() > 0)
					tempIndex.add(thirdLine.get(0));

				Collections.sort(tempIndex);
				if (tempIndex.size() > 0) {
					previousIndex = tempIndex.get(0) - 25;
					if (tempIndex.get(0) - currentIndex >= 90)
						letters.add("    ");

				}
			}

//			System.out.println(letters);
		}

		return letters;
	}

	private ArrayList<Integer> getDotSequence(int index) {

		ArrayList<Point> pointList = lineIndexToDotListMap.get(index);
		ArrayList<Integer> dots = new ArrayList<Integer>();

		for (int i = 0; i < pointList.size(); i++)
			dots.add(pointList.get(i).getX());

		Collections.sort(dots);

		int previousXIndexOfDot = dots.get(0);
		ArrayList<Integer> dotsToRemove = new ArrayList<Integer>();

		for (int j = 1; j < dots.size(); j++) {
			if (dots.get(j) - previousXIndexOfDot < Constant.SAME_POINT_COVERAGE)
				dotsToRemove.add(previousXIndexOfDot);
			previousXIndexOfDot = dots.get(j);
		}

		for (int j = 0; j < dotsToRemove.size(); j++)
			dots.remove(dotsToRemove.get(j));

		return dots;
	}

	private int getNearestLineIndex(int y, ArrayList<Integer> newLineIndex) {
		int selectedIndex = newLineIndex.get(0);
		int initialDistance = Math.abs(y - selectedIndex);

		for (int i = 1; i < newLineIndex.size(); i++) {
			int currentIndex = newLineIndex.get(i);
			int currentDistance = Math.abs(y - currentIndex);
			if (initialDistance > currentDistance) {
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

		if (distance <= lineDistance + limit && distance >= lineDistance - limit)
			secondLineFound = true;
		if (distance2 <= lineDistance + limit && distance2 >= lineDistance - limit)
			thirdLineFound = true;

		if (secondLineFound && thirdLineFound)
			return true;
		return false;
	}

	private void colorLine(ArrayList<Integer> arrayOfLineIndex, Color color) {
		for (int i = 0; i < arrayOfLineIndex.size(); i++) {

			for (int x = 0; x < width; x++) {
				outputImage.setRGB(x, arrayOfLineIndex.get(i), color.getRGB());
			}
		}

	}

	private void processLineInformation(ArrayList<Point> allCenter) {
		int iniTialPoint = 1;
		int count = 0;
		int maxH = Constant.INITAL_DIFFRENCE_BETWEEN_LINE;

		for (int i = 0; i < allCenter.size(); i++) {

			int centerY = allCenter.get(i).getY();

			if (centerY - iniTialPoint >= maxH) {
				if (count > 0) {
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

		if (count > 0) {
			lineIndex.add(iniTialPoint);
			lineIndexToDotListMap.put(iniTialPoint, (ArrayList<Point>) pointInLine.clone());
		}
	}

	private ArrayList<Point> getAllCenter() {
		ArrayList<Point> allCenter = new ArrayList<>();

		for (int index = 0; index < twoDString.size(); index++) {

			ArrayList<String> xx = twoDString.get(index);
			BrailleDot brailleDot = new BrailleDot(xx);
			Point center = brailleDot.getCenter();
			allCenter.add(center);

			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					int tempX = center.getX() + x;
					int tempY = center.getY() + y;

					if (tempY >= 0 && tempY < height && tempX >= 0 && tempX < width)
						outputImage.setRGB(tempX, tempY, Color.WHITE.getRGB());
				}
			}
		}
		return allCenter;
	}

	private ArrayList<Dot> selectDotInFirstStep() {
		ArrayList<Dot> FirstStepSelectedDot = new ArrayList<>();
		twoDString = new ArrayList<>();

		for (int ii = 0; ii < uniqueDots.size(); ii++) {
			Map<String, Boolean> pixelCounted = new TreeMap<String, Boolean>();
			Dot dot = uniqueDots.get(ii);
			ArrayList<String> pixelList = new ArrayList<String>();
			ArrayList<String> oneDot = new ArrayList<>();

			boolean flag = false;

			for (int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
				for (int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {

					String pixel = getStringIndex(x, y);
					if (!isColor(y, x, Constant.WHITE, outputImage))
						flag = true;
					pixelList.add(pixel);
					oneDot.add(pixel);
					pixelCounted.put(pixel, true);
				}
			}

			if (flag)
				continue;

			while (pixelList.size() > 0) {
				String currentPixel = pixelList.get(0);
				pixelList.remove(0);

				String[] pixVal = currentPixel.split("-");
				int x = Integer.parseInt(pixVal[0]);
				int y = Integer.parseInt(pixVal[1]);

				for (int neighbourY = -1; neighbourY <= 1; neighbourY++) {
					for (int neighbourX = -1; neighbourX <= 1; neighbourX++) {
						int tempY = y + neighbourY;
						int tempX = x + neighbourX;

						String tempPixelString = getStringIndex(tempX, tempY);

						if (tempY < height && tempY >= 0 && tempX < width && tempX >= 0) {
							if (isColor(tempY, tempX, Constant.WHITE, outputImage)
									&& !pixelCounted.containsKey(tempPixelString)) {
								pixelList.add(tempPixelString);
								pixelCounted.put(tempPixelString, true);
								oneDot.add(tempPixelString);
							}
						}

					}
				}
			}

			ArrayList<String> newString = new ArrayList<>();
			for (int i = 0; i < oneDot.size(); i++) {
				String s = oneDot.get(i);
				newString.add(s);

				String[] arr = s.split("-");
				int x = Integer.parseInt(arr[0]);
				int y = Integer.parseInt(arr[1]);

				outputImage.setRGB(x, y, Constant.RED.getRGB());

			}
			twoDString.add(newString);
		}

		for (int i = 0; i < twoDString.size(); i++) {
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
					outputImage.setRGB(x, y, Constant.WHITE.getRGB());
				}
			}
		}

	}

	private void initializeOutputImage() {
		outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				outputImage.setRGB(x, y, Constant.BLACK.getRed());
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

		int netghborDotSize = Constant.NEIGHBOUR_DOT_SIZE_FOR_PART_OF_DOT_SELECTION;

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
