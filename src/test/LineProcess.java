package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import dataStructure.Dot;
import dataStructure.Point;

public class LineProcess {


	int height;
	int width;
	BufferedImage testImg = null;
	ArrayList<Dot> uniqueDots = new ArrayList<>();
	Map<String, Boolean> isAPartOfADot = new TreeMap<String, Boolean>();
	Map<String, Dot> indexStringToDotMapping = new TreeMap<String, Dot>();
	ArrayList<Integer> pixelSize = new ArrayList<Integer>();
	Map<Dot, Boolean> dotTaken = new TreeMap<Dot, Boolean>();
	ArrayList<Integer> lineIndex = new ArrayList<Integer>();

	public File run(File imageFile) {


		findDots(imageFile);

		double totalDotPixel = 0;
		int maxH = -1;
		double avRadious = 0;

		for(int i = 0; i < uniqueDots.size(); i++) {
			Dot dot = uniqueDots.get(i);
			dot.processDot();
			totalDotPixel += dot.getPixelSIze();
			pixelSize.add(dot.getPixelSIze());
			avRadious += dot.getRadious();
			maxH = Math.max(maxH, dot.getEndingY()-dot.getStartingY());
		}

		System.out.println(maxH + "=mxh");

		int radious = (int) (avRadious/uniqueDots.size());
		System.out.println("rad " + radious);

		Collections.sort(pixelSize);

		double averagePixelSize = totalDotPixel/uniqueDots.size();

		//System.out.println("Mean: " + pixelSize.get(pixelSize.size()/2));
		System.out.println("AVG : "+ averagePixelSize);

		System.out.println(uniqueDots.get(0).getRadious() + " > " + uniqueDots.get(0).getCenter().getX() +" " +uniqueDots.get(0).getCenter().getY());


		Collections.sort(uniqueDots, new DotSorter());
		int iniTialPoint = -1;
		int count = 0;

		for(int i = 0; i < uniqueDots.size(); i++) {
			System.out.println(uniqueDots.get(i).getCenter().getX() +"--"+ uniqueDots.get(i).getCenter().getY());
			Dot dot = uniqueDots.get(i);
			int centerY = dot.getCenter().getY();

			if(centerY - iniTialPoint >= maxH) {
				if(count > 3) {
					lineIndex.add(iniTialPoint);

				}

				count = 1;
				iniTialPoint = centerY;

				System.out.println("---------------------");

			} else {
				count++;
			}


		}

		if(count > 3) {
			lineIndex.add(iniTialPoint);

		}




		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				outputImage.setRGB(x, y, 0);
			}
		}


		Color myWhite = new Color(255, 255, 255);
		Color red = new Color(255, 0, 0);
		Color black = new Color(0, 0, 0);
		int rgb = myWhite.getRGB();


		for(int i = 0; i< uniqueDots.size(); i++) {

			Dot dot = uniqueDots.get(i);
			dot.processDot();

			for(int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
				for(int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
					outputImage.setRGB(x, y, rgb);
				}
			}
		}

		for(int i = 0; i < lineIndex.size(); i++) {
			for(int x = 0; x < width; x++) {
				outputImage.setRGB(x, lineIndex.get(i), rgb);
			}
		}



		File outputfile = new File("efg.jpg");
		try {
			ImageIO.write(outputImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		System.out.println("done");

		return outputfile;






//		System.out.println("line index " + lineIndex.size() );


	//	return null;
	}


	private boolean isDot(Point point) {


		for(int index = 0; index < uniqueDots.size(); index++) {

			Dot dot = uniqueDots.get(index);

			if(dotTaken.containsKey(dot))
				return false;

			int startX = dot.getStartingX();
			int endX = dot.getEndingX();
			int startY = dot.getStartingY();
			int endY = dot.getEndingY();

			for(int i = -9; i <= 9; i++) {
				for(int j = -9; j <= 9; j ++) {

					int x = j + point.getX();
					int y = i + point.getY();

					if(x >= startX && x<= endX && y>=startY && y<= endY)
					{
						dotTaken.put(dot, true);
						return true;
					}




				}
			}



		}


		return false;
	}


	private void findDots(File imageFile) {
		try {
			testImg = ImageIO.read(imageFile);

		} catch (IOException e) {
			System.out.println("no input file");

		}

		width = testImg.getWidth();
		height = testImg.getHeight();


		System.out.println("image h: " + height);
		System.out.println("image w: " + width);


		int count = 0;

		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {

				Point point = new Point(x, y);

				Color color = new Color(testImg.getRGB(x, y));
				int grayLevel = (int) color.getRed();


				//System.out.println(grayLevel);


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
