package dataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Dot {

	private Point center;
	private int redious;

	private int startingX;
	private int endingX;
	private int startingY;
	private int endingY;

	public Dot(ArrayList<String> indexsOfPixels) {

		ArrayList<Integer> allXIndexs = new ArrayList<Integer>();
		ArrayList<Integer> allYIndexs = new ArrayList<Integer>();

		for (int i = 0; i < indexsOfPixels.size(); i++) {
			String indexOfPixel = indexsOfPixels.get(i);
			String arr[] = indexOfPixel.split("-");

			allYIndexs.add(Integer.parseInt(arr[0]));
			allXIndexs.add(Integer.parseInt(arr[1]));

		}

		Collections.sort(allXIndexs);
		Collections.sort(allXIndexs);

		startingX = allXIndexs.get(0);
		endingX = allXIndexs.get(allXIndexs.size() - 1);

		startingY = allYIndexs.get(0);
		endingY = allYIndexs.get(allYIndexs.size() - 1);

		Point leftUpperPixel = new Point(startingX, startingY);
		Point rightUpperPixel = new Point(startingX, endingY);
		Point leftLowerPixel = new Point(endingX, startingY);
		Point rightLowerPixel = new Point(endingX, endingY);

		double sumOfAllDistance = leftUpperPixel.getDistance(rightUpperPixel)
				+ leftUpperPixel.getDistance(leftLowerPixel) + leftUpperPixel.getDistance(rightLowerPixel)
				+ leftLowerPixel.getDistance(rightLowerPixel) + rightLowerPixel.getDistance(rightUpperPixel);

		redious = (int) (sumOfAllDistance / 6);

		center = new Point((startingX + endingX) / 2, (startingY + endingY) / 2);

	}

	public Point getCenter() {
		return center;
	}

	public int getRadious() {
		return redious;
	}

	public int getStartingX() {
		return startingX;
	}

	public int getEndingX() {
		return endingX;
	}

	public int getStartingY() {
		return startingY;
	}

	public int getEndingY() {
		return endingY;
	}

	public int compareTo(Dot o) {
		return center.getX() < o.getCenter().getX() ? -1
				: center.getX() > o.getCenter().getX() ? 1 : doSecodaryOrderSort(o);
	}

	public int doSecodaryOrderSort(Dot o) {
		return center.getY() < o.getCenter().getX() ? -1 : center.getY() > o.getCenter().getX() ? 1 : 0;
	}

}
