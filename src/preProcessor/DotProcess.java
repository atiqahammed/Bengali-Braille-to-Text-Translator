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

import com.sun.javafx.scene.traversal.WeightedClosestCorner;

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

			double height = dot.getEndingY() - dot.getStartingY();
			double width = dot.getEndingY() - dot.getStartingY();
			double area = height * width;
			totalArea += area;

			allArea.add(area);
		}

		//// area
		System.out.println("area information");
		System.out.println(allArea.size());

		Collections.sort(allArea);

		System.out.println(allArea.get(0));
		System.out.println(allArea.get(allArea.size() - 1));
		System.out.println(allArea.get(allArea.size() / 2));
		System.out.println(totalArea / allArea.size());
		int radious = (int) Math.sqrt(allArea.get(allArea.size() - 1));
		System.out.println("radious " + radious);
		System.out.println("end of area information");

		int count = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (isColor(y, x, white, outputImage)) {
					count++;
				}

			}
		}

		System.out.println("total white color = " + count);

		for (int uniqueDotIndex = 1; uniqueDotIndex < 2/* uniqueDots.size() */; uniqueDotIndex++) {

			Dot dot = uniqueDots.get(uniqueDotIndex);
			dot.processDot();

			System.out.println(dot.getStartingY() + "-" + dot.getStartingX());
			System.out.println(dot.getEndingY() + "-" + dot.getEndingX());

			for (int x = dot.getStartingX(); x <= dot.getEndingX(); x++) {
				for (int y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
					// outputImage.setRGB(x, y, yellow.getRGB());

					Color tempColor = new Color(outputImage.getRGB(x, y));

					if (isColorEquals(tempColor, white)) {
						ArrayList<Point> list = new ArrayList<>();
						list.add(new Point(x, y));

						while (list.size() > 0) {
							Point currentPoint = list.get(0);
							System.out.println("queue size = " +list.size());



							for (int treverseIndexY = -1; treverseIndexY <= 1; treverseIndexY++) {
								for (int treverseIndexX = -1; treverseIndexX <= 1; treverseIndexX++) {
									int tempY = y + treverseIndexY;
									int tempX = x + treverseIndexX;

									if (isAPixelIndex(x, y)) {



										if(currentPoint.getX() > 2347 || currentPoint.getX() < 2329 || currentPoint.getY() < 151 || currentPoint.getY() > 160)
										System.out.println(tempX + " ooooo " + tempY);
										if(isColor(tempY, tempX, white, outputImage)) {
											list.add(new Point(tempX, tempY));
											System.out.println("white color ");
										} else {
											if(isColor(tempY, tempX, black, outputImage))
												System.out.println("black color");
											else if(isColor(tempY, tempX, yellow, outputImage))
												System.out.println("yello color");
											else System.out.println("undefined color");
										}


									}

								}
							}

							outputImage.setRGB(currentPoint.getX(), currentPoint.getY(), yellow.getRGB());
							if(currentPoint.getX() > 2347 || currentPoint.getX() < 2329 || currentPoint.getY() < 151 || currentPoint.getY() > 160) {
								System.out.println(currentPoint.getX()+"____"+ currentPoint.getY());
							}


							list.remove(0);
						}

					}

					// list.add(new Point(x, y));

				}
			}

		}

		// ArrayList<Dot> connectedDots = new ArrayList<Dot>();

		/*
		 * for(int dotIndex = 0; dotIndex < uniqueDots.size(); dotIndex++) { Dot
		 * dot = uniqueDots.get(dotIndex);
		 *
		 *
		 * Point dotCenter = dot.getCenter(); System.out.println("dot center = "
		 * + dotCenter.getX() + " " + dotCenter.getY());
		 *
		 *
		 *
		 * boolean flag = false; for(int i = dot.getStartingY(); i <=
		 * dot.getEndingY(); i++) { for(int j = dot.getStartingX(); j <=
		 * dot.getEndingX(); j++) {
		 *
		 * for(int y = -radious; y <= radious; y++) { for(int x = -radious; x <=
		 * radious; x++) { if(x>= 0 && x<width && y>=0 && y<height){ Color c =
		 * new Color(outputImage.getRGB(x, y)); if(c.getRed() == 255 &&
		 * c.getGreen() == 255 && c.getBlue() == 0) { flag = true; break; } } }
		 * }
		 *
		 *
		 *
		 *
		 * }
		 *
		 * if(flag) break; }
		 *
		 *
		 *
		 * if(flag) {
		 *
		 * for(int i = dot.getStartingY(); i <= dot.getEndingY(); i++) { for(int
		 * j = dot.getStartingX(); j <= dot.getEndingX(); j++) {
		 *
		 *
		 *
		 * Color c = new Color(outputImage.getRGB(j, i)); if(c.getRed() == 255
		 * && c.getGreen() == 255 && c.getBlue() == 255) { outputImage.setRGB(j,
		 * i, black.getRed()); }
		 *
		 *
		 * }
		 *
		 *
		 * }
		 *
		 *
		 * } else {
		 *
		 * int centerX = dot.getCenter().getX(); int centerY =
		 * dot.getCenter().getY();
		 *
		 * for(int i = - radious; i <= radious; i++) { for(int j = -radious; j
		 * <= radious; j++) { int x = centerX + j; int y = centerY + i;
		 *
		 *
		 * if(x >= 0 && x < width && y >= 0 && y <height) {
		 * outputImage.setRGB(x, y, yellow.getRGB()); }
		 *
		 *
		 * } }
		 *
		 *
		 *
		 * } }
		 */

		/*
		 *
		 *
		 *
		 * Dot dot = uniqueDots.get(0); dot.processDot();
		 *
		 *
		 *
		 *
		 * for(int x = dot.getStartingX(); x <= dot.getEndingX(); x++) { for(int
		 * y = dot.getStartingY(); y <= dot.getEndingY(); y++) {
		 * //outputImage.setRGB(x, y, red.getRGB());
		 *
		 *
		 *
		 *
		 * } }
		 *
		 * System.out.println("starting X: " + dot.getStartingX());
		 * System.out.println("starting Y: " + dot.getStartingY());
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 * Color clr = new Color(outputImage.getRGB(dot.getStartingX()-1,
		 * dot.getStartingY())); System.out.println(clr.getRed() + " " +
		 * clr.getGreen() +" "+clr.getBlue());
		 *
		 *
		 *
		 *
		 *
		 *
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
