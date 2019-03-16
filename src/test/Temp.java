package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.sun.javafx.scene.traversal.WeightedClosestCorner;

import dataStructure.Dot;
import dataStructure.Point;

public class Temp {

	private int x[] = { -1, -1, -1, 0, 0, 1, 1, 1 };
	private int y[] = { -1, 0, 1, -1, 1, -1, 0, 1 };
	private Map<String, Boolean> indexTaken = new TreeMap<String, Boolean>();
	private ArrayList<ArrayList<String>> allDot = new ArrayList<ArrayList<String>>();
	private Map<String, ArrayList<String>> pixelDotMap = new TreeMap<String, ArrayList<String>>();

	public void run(File imageFile) {

		ArrayList<ArrayList<String>> dotList = getAllDots(imageFile);
		ArrayList<ArrayList<String>> selection1 = new ArrayList<ArrayList<String>>();

		System.out.println(dotList.size());

		BufferedImage testImg = null;
		try {
			testImg = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("no input file");
		}
		int widthTest = testImg.getWidth();
		int heightTest = testImg.getHeight();

		Color myWhite = new Color(255, 255, 255);
		Color red = new Color(255, 0, 0);
		Color blue = new Color(0, 200, 0);
		int rgb = myWhite.getRGB();

		BufferedImage theImage = new BufferedImage(widthTest, heightTest, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < widthTest; i++) {
			for (int j = 0; j < heightTest; j++) {
				theImage.setRGB(i, j, rgb);
			}
		}

		for (int i = 0; i < allDot.size(); i++) {
			ArrayList<String> currentDot = allDot.get(i);

			if (currentDot.size() > 10 && currentDot.size() < 100) {

				selection1.add(currentDot);
			}
		}

		for (int i = 0; i < selection1.size(); i++) {
			ArrayList<String> currentDot = selection1.get(i);

			for (int j = 0; j < currentDot.size(); j++) {
				colorThisPoint(currentDot.get(j), theImage, 0);
			}
		}

		// selection1.remove(0);

		/*
		 *
		 * for (int index = 0; index < 16; index++) {
		 *
		 * for (int i = 0; i < selection1.get(index).size(); i++) {
		 * colorThisPoint(selection1.get(index).get(i), theImage, red.getRGB());
		 * }
		 *
		 * }
		 *
		 */

		/*
		 * for (int i = 0; i < selection1.get(18).size(); i++) {
		 * colorThisPoint(selection1.get(18).get(i), theImage, red.getRGB()); }
		 */

		/*
		 * Dot dot = new Dot(selection1.get(18));
		 *
		 * System.out.println(dot.getRadious());
		 * System.out.println(dot.getCenter().getX() + " " +
		 * dot.getCenter().getY());
		 *
		 *
		 * for(int i = dot.getStartingY(); i<= dot.getEndingY(); i++) { for(int
		 * j = dot.getStartingX(); j <= dot.getEndingX(); j++) {
		 * theImage.setRGB(i, j, Color.BLUE.getRed()); } }
		 */

		ArrayList<Dot> dots = new ArrayList<Dot>();

		for (int selectedDotsIndex = 0; selectedDotsIndex < selection1.size(); selectedDotsIndex++) {
			dots.add(new Dot(selection1.get(selectedDotsIndex)));
		}

		/*
		 * for(int dotsIndex=0; dotsIndex<dots.size(); dotsIndex++) { Dot dot =
		 * dots.get(dotsIndex);
		 *
		 * for(int i = dot.getStartingY(); i<= dot.getEndingY(); i++) { for(int
		 * j = dot.getStartingX(); j <= dot.getEndingX(); j++) {
		 * theImage.setRGB(i, j, Color.BLUE.getRed()); } }
		 *
		 * }
		 */

		ArrayList<Dot> selectedDots = new ArrayList<Dot>();

		int indexToTest = 18;
		Dot testDot = dots.get(indexToTest);
		int maxLimit = testDot.getRadious() * 15;

		for (int dotListIndex = 0; dotListIndex < dots.size(); dotListIndex++) {

			if (dotListIndex != indexToTest) {
				int distance = (int) testDot.getCenter().getDistance(dots.get(dotListIndex).getCenter());
				if (distance <= maxLimit) {
					selectedDots.add(dots.get(dotListIndex));
				}
			}
		}

		System.out.println("selected dots = " + selectedDots.size());

		System.out.println(".." + testDot.getCenter().getX() + "  " + testDot.getCenter().getY());
		for (int i = 0; i < selectedDots.size(); i++) {
			System.out.println(selectedDots.get(i).getCenter().getX() + "  " + selectedDots.get(i).getCenter().getY());
		}

		selectedDots.add(testDot);
		System.out.println("pppp");

		// Collections.sort(selectedDots, (Dot a1, Dot a2) ->
		// a1.getCenter().getX() - a2.getCenter().getX().thenComparing
		// ((ActiveAlarm a1, ActiveAlarm a2) -> a1.timeEnded-a2.timeEnded))

		Collections.sort(selectedDots, new DotComparator(testDot));

		for (int i = 0; i < selectedDots.size(); i++) {
			System.out.println(selectedDots.get(i).getCenter().getX() + "  " + selectedDots.get(i).getCenter().getY());
		}


		System.out.println("dots are sorted based on distance");


		int arr[] = {0, 0, 0, 0, 0, 0};

		for (int dotTraverseIndex = 0; dotTraverseIndex < 6/*
															 * selectedDots.size(
															 * )
															 */; dotTraverseIndex++) {

			Dot currentDot = selectedDots.get(dotTraverseIndex);
			//System.out.println(currentDot.getRadious());

			double minDisTance = (double) (currentDot.getRadious() * 100);
			double maxDistance = 0;

			for (int i = 0; i < selectedDots.size(); i++) {
				if (i != dotTraverseIndex) {
					double distance = currentDot.getCenter().getDistance(selectedDots.get(i).getCenter());

					minDisTance = Math.min(minDisTance, distance);
					maxDistance = Math.max(maxDistance, distance);
				}
			}

			System.out.println(minDisTance +" minimum distance");
			System.out.println(maxDistance + " mx distance");


			Map<Integer, Integer> count = new TreeMap<Integer, Integer>();
			for (int i = 1; i < 7; i++)
				count.put(i, 0);

			for (int i = 0; i < 6/* selectedDots.size() */; i++) {

				if (i != dotTraverseIndex) {
					//System.out.println("ht " + i);
					Point A = currentDot.getCenter();
					Point B = selectedDots.get(i).getCenter();
					Point C = new Point(A.getX(), B.getY());

					Double hyp = A.getDistance(B);
					Double opp = B.getDistance(C);

					System.out.println(hyp);
					System.out.println(opp+"l");

					System.out.println(A.getX() + " " + A.getY());
					System.out.println(B.getX() + " " + B.getY());
					System.out.println(C.getX() + " " + C.getY());
					Double Degree = Math.abs(Math.toDegrees(Math.asin(opp / hyp)) % 90);

					System.out.println(Degree + " degree");
					Double dis = A.getDistance(B);

					if (Degree <= 15.0) {
						System.out.println("ei khane");

						if (Math.abs(dis - maxDistance) > Math.abs(dis - minDisTance)) {

							System.out.println("neighbour dot");

							if(Math.abs(A.getX()-B.getX()) <= Math.abs(A.getY()-B.getY())) {
								System.out.println("pasapasi");

								if (A.getY() < B.getY()) {
									count.put(1, count.get(1) + 1);
									count.put(3, count.get(3) + 1);
									count.put(5, count.get(5) + 1);
								} else {
									count.put(2, count.get(2) + 1);
									count.put(4, count.get(4) + 1);
									count.put(6, count.get(6) + 1);
								}



							}

							else {

								if (A.getX() < B.getX()) {
									count.put(1, count.get(1) + 1);
									count.put(2, count.get(2) + 1);
									count.put(3, count.get(3) + 1);
									count.put(4, count.get(4) + 1);
								} else {
									count.put(3, count.get(3) + 1);
									count.put(4, count.get(4) + 1);
									count.put(5, count.get(5) + 1);
									count.put(6, count.get(6) + 1);
								}

							}
						}

						else {
							System.out.println(" not neighbour dot");
							if(A.getX() < B.getX()) {

								System.out.println(" this is upper point");
								count.put(1, count.get(1) + 1);
								count.put(2, count.get(2) + 1);

							}

							else {
								count.put(5, count.get(5) + 1);
								count.put(6, count.get(6) + 1);
							}

						}
					}

					else if (Degree > 15.0 && Degree <= 45.0) {
						System.out.println("arpound 45 degree");
						if (Math.abs(dis - maxDistance) > Math.abs(dis - minDisTance)) {
							System.out.println("neighbour diagonal dot");

							if(A.getX() < B.getX() && A.getY() < B.getY()) {
								count.put(1, count.get(1) +1);
								count.put(3, count.get(3) +1);
							}

							else if(A.getX() < B.getX() && A.getY() > B.getY()) {
								count.put(2, count.get(2) +1);
								count.put(4, count.get(4) +1);
							}

							else if(A.getX() > B.getX() && A.getY() < B.getY()) {
								count.put(3, count.get(3) +1);
								count.put(5, count.get(5) +1);
							}

							else {
								count.put(4, count.get(4) +1);
								count.put(6, count.get(6) +1);
							}

						}

						else {
							System.out.println("diagonal last dot");

							if(A.getX() < B.getX() && A.getY() < B.getY()) {
								count.put(1, count.get(1) +1);
								//count.put(3, count.get(3) +1);
							}

							else if(A.getX() < B.getX() && A.getY() > B.getY()) {
								count.put(2, count.get(2) +1);
								//count.put(4, count.get(4) +1);
							}

							else if(A.getX() > B.getX() && A.getY() < B.getY()) {
								//count.put(3, count.get(3) +1);
								count.put(5, count.get(5) +1);
							}

							else {
								//count.put(4, count.get(4) +1);
								count.put(6, count.get(6) +1);
							}



						}
					}

					else if (Degree > 45.0 && Degree <= 65.0) {
						System.out.println("arpound 60 degree");
						if (Math.abs(dis - maxDistance) > Math.abs(dis - minDisTance)) {
							System.out.println("neighbour diagonal dot");

							if(A.getX() < B.getX() && A.getY() < B.getY()) {
								count.put(1, count.get(1) +1);
								count.put(3, count.get(3) +1);
							}

							else if(A.getX() < B.getX() && A.getY() > B.getY()) {
								count.put(2, count.get(2) +1);
								count.put(4, count.get(4) +1);
							}

							else if(A.getX() > B.getX() && A.getY() < B.getY()) {
								count.put(3, count.get(3) +1);
								count.put(5, count.get(5) +1);
							}

							else {
								count.put(4, count.get(4) +1);
								count.put(6, count.get(6) +1);
							}

						}

						else {
							System.out.println("diagonal last dot");

							if(A.getX() < B.getX() && A.getY() < B.getY()) {
								count.put(1, count.get(1) +1);
								//count.put(3, count.get(3) +1);
							}

							else if(A.getX() < B.getX() && A.getY() > B.getY()) {
								count.put(2, count.get(2) +1);
								//count.put(4, count.get(4) +1);
							}

							else if(A.getX() > B.getX() && A.getY() < B.getY()) {
								//count.put(3, count.get(3) +1);
								count.put(5, count.get(5) +1);
							}

							else {
								//count.put(4, count.get(4) +1);
								count.put(6, count.get(6) +1);
							}



						}
					}

					else {
						System.out.println("around 90 degree");

						if (Math.abs(dis - maxDistance) > Math.abs(dis - minDisTance)) {
							System.out.println("neighbour dot");

							if(Math.abs(A.getX()-B.getX()) <= Math.abs(A.getY()-B.getY())) {
								System.out.println("pasapasi");

								if (A.getY() < B.getY()) {
									count.put(1, count.get(1) + 1);
									count.put(3, count.get(3) + 1);
									count.put(5, count.get(5) + 1);
								} else {
									count.put(2, count.get(2) + 1);
									count.put(4, count.get(4) + 1);
									count.put(6, count.get(6) + 1);
								}

							}

							else {
								System.out.println("upor nich");

								if (A.getX() < B.getX()) {
									count.put(1, count.get(1) + 1);
									count.put(2, count.get(2) + 1);
									count.put(3, count.get(3) + 1);
									count.put(4, count.get(4) + 1);
								} else {
									count.put(3, count.get(3) + 1);
									count.put(4, count.get(4) + 1);
									count.put(5, count.get(5) + 1);
									count.put(6, count.get(6) + 1);
								}


							}

						}

						else {
							System.out.println("not neighbour");


							if(A.getX() < B.getX()) {

								System.out.println(" this is upper point");
								count.put(1, count.get(1) + 1);
								count.put(2, count.get(2) + 1);

							}

							else {
								count.put(5, count.get(5) + 1);
								count.put(6, count.get(6) + 1);
							}
						}

					}
				}

				System.out.println("-------------");
			}

			int dotOfLetter = -1;
			int maxValue = 0;


			for (int i = 1; i < 7; i++) {
				if(count.get(i) > maxValue) {
					maxValue = count.get(i);
					dotOfLetter = i;
				}
				System.out.println(count.get(i));
			}

			if(dotOfLetter > 0) {
				arr[dotOfLetter-1] = 1;
			}
		}

		for(int i = 0; i < arr.length; i++)
			System.out.print(arr[i] +" ");
		System.out.println();

		File outputfile = new File("abc.jpg");
		try {
			ImageIO.write(theImage, "jpg", outputfile);
		} catch (IOException e1) {

		}

		System.out.println("done");

	}

	private void colorThisPoint(String index, BufferedImage theImage, int rgb) {
		String[] arr = index.split("-");
		int yy = Integer.parseInt(arr[0]);
		int xx = Integer.parseInt(arr[1]);

		theImage.setRGB(yy, xx, rgb);

	}

	private ArrayList<ArrayList<String>> getAllDots(File imageFile) {
		BufferedImage testImg = null;

		try {
			testImg = ImageIO.read(imageFile);

		} catch (IOException e) {
			System.out.println("no input file");

		}

		int widthTest = testImg.getWidth();
		int heightTest = testImg.getHeight();

		int count = 0;
		ArrayList<String> strings = new ArrayList<>();

		for (int i = 0; i < widthTest; i++) {
			for (int j = 0; j < heightTest; j++) {
				Color c1 = new Color(testImg.getRGB(i, j));
				String index = getStringIndex(i, j);

				int value = (int) c1.getBlue();
				if (value == 0) {
					count++;

					boolean neighbourNotListed = true;
					for (int k = 0; k < 8; k++) {

						int tempY = i + y[k];
						int tempX = j + x[k];

						if (tempX >= 0 && tempX < heightTest && tempY >= 0 && tempY < widthTest) {
							Color c = new Color(testImg.getRGB(i, j));
							int colorValue = c.getBlue();

							if (colorValue == 0) {

								String neighbourIndex = getStringIndex(tempY, tempX);
								if (indexTaken.containsKey(neighbourIndex)) {
									neighbourNotListed = false;
									ArrayList<String> currentDot = pixelDotMap.get(neighbourIndex);
									currentDot.add(index);
									pixelDotMap.put(index, currentDot);
									break;
								}
								// break;
							}
						}
					}

					if (neighbourNotListed) {
						ArrayList<String> dot = new ArrayList<String>();

						dot.add(index);

						pixelDotMap.put(index, dot);

						allDot.add(dot);
					}

					indexTaken.put(index, true);

				}
			}
		}

		return allDot;
	}

	private String getStringIndex(int i, int j) {
		return Integer.toString(i) + "-" + Integer.toString(j);
	}

}
