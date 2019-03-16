package dataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.sun.corba.se.impl.interceptors.PINoOpHandlerImpl;

public class Dot {

	private Point center;
	private int redious;

	private int startingX;
	private int endingX;
	private int startingY;
	private int endingY;


	private ArrayList<Point> indexsOfPixels;


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

	public Dot() {
		indexsOfPixels = new ArrayList<>();
	}

	public void processDot() {
		ArrayList<Integer> allXIndexs = new ArrayList<Integer>();
		ArrayList<Integer> allYIndexs = new ArrayList<Integer>();

		for (int i = 0; i < indexsOfPixels.size(); i++) {
			Point point = indexsOfPixels.get(i);
			//String arr[] = indexOfPixel.split("-");

			allYIndexs.add(point.getY());
			allXIndexs.add(point.getX());

		}

		Collections.sort(allXIndexs);
		Collections.sort(allXIndexs);

		startingX = allXIndexs.get(0);
		endingX = allXIndexs.get(allXIndexs.size() - 1);

		startingY = allYIndexs.get(0);
		endingY = allYIndexs.get(allYIndexs.size() - 1);

		Point leftUpperPixel = new Point(startingX, startingY);
		Point rightUpperPixel = new Point(endingX, startingY);
		Point leftLowerPixel = new Point(startingX, endingY);
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

	public void addPixels(Point point) {
		indexsOfPixels.add(point);
	}

	public int getPixelSIze() {
		return indexsOfPixels.size();
	}

}
