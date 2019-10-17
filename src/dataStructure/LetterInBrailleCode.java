package dataStructure;

import util.Constant;

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
		if(Constant.IS_BENGALI_SELECTED)
			return Constant.LETTERS.getLetters(symbol);
		return Constant.LETTERS.getEnglishLetters(symbol);
	}

	public void print() {
		Constant.OUTPUT_LIST.add("symbol :: " + symbol + "  ,  letter :: " + getLetter());
	}

}
