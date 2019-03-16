package dataStructure;

public class Point {

	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double getDistance(Point point) {

		return Math.sqrt(Math.pow((x-point.getX()), 2) + Math.pow((y-point.getY()),2));

	}

}
