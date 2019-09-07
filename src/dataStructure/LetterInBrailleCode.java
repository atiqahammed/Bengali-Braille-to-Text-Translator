package dataStructure;

import util.Utils;

public class LetterInBrailleCode {

	private String symbol = "";

	public LetterInBrailleCode(LineColumn column1,LineColumn column2) {
		symbol = column1.getSymbol() + column2.getSymbol();
	}

	public LetterInBrailleCode(LineColumn column2) {
		symbol = "000" + column2.getSymbol();
	}

	public LetterInBrailleCode(String string, LineColumn column2) {
		symbol = "000" + column2.getSymbol();
	}

	public LetterInBrailleCode(LineColumn column, String string) {
		symbol = column.getSymbol() + "000";
	}

	public String getSymbol() {
		return symbol;
	}

	public String getLetter() {
		return Utils.LETTERS.getLetters(symbol);
	}

	public void print() {
		Utils.OUTPUT_LIST.add("symbol :: " + symbol + "  ,  letter :: " + getLetter());
	}

}
