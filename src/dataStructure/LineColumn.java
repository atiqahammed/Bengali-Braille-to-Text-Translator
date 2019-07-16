package dataStructure;

import util.Utils;

public class LineColumn {

	private int averageIndex;
	private String symbol = "";

	public LineColumn(int upperDot, int middleDot, int lowerDot) {

		int count = 0;
		averageIndex = 0;

		if(upperDot >= 0) {
			count++;
			averageIndex += upperDot;
			symbol += "1";
//			Utils.FUNCTIONS.replaceCharUsingCharArray(symbol, '1', 0);
		} else symbol += "0";

		if(middleDot >= 0) {
			count++;
			averageIndex += middleDot;
//			Utils.FUNCTIONS.replaceCharUsingCharArray(symbol, '1', 1);
			symbol += "1";
		} else symbol += "0";

		if(lowerDot >= 0) {
			count++;
			averageIndex += lowerDot;
//			Utils.FUNCTIONS.replaceCharUsingCharArray(symbol, '1', 2);
			symbol += "1";
		} else symbol += "0";

		if(count > 0)
			averageIndex =  averageIndex / count;

	}

	public int getAverageIndex() {
		return averageIndex;
	}

	public String getSymbol() {
		return symbol;
	}

	public void printColumn() {
		System.out.println("this is a column with index = " + averageIndex);
		System.out.println(symbol);
	}
}
