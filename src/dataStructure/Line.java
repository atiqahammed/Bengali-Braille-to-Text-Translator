package dataStructure;

public class Line {

	private int upperLineIndex;
	private int middleLineIndex;
	private int	lowerLineIndex;
	
	public Line(int upperLineIndex, int middleLineIndex, int	lowerLineIndex) {
		this.upperLineIndex = upperLineIndex;
		this.middleLineIndex = middleLineIndex;
		this.lowerLineIndex = lowerLineIndex;
	}

	public int getUpperLineIndex() {
		return upperLineIndex;
	}

	public int getMiddleLineIndex() {
		return middleLineIndex;
	}

	public int getLowerLineIndex() {
		return lowerLineIndex;
	}
	
}
