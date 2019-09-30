package dataStructure;

import java.util.ArrayList;

import util.Constant;

public class BrailleWord {

	private ArrayList<LineColumn> colList;

	public BrailleWord(ArrayList<LineColumn> colList) {
		this.colList = colList;
	}

	public ArrayList<LineColumn> getColList(){
		return colList;
	}
	
	public void printWord() {
		Constant.OUTPUT_LIST.add("----- Print a braille words column -----");
		for(LineColumn column: colList)
			column.printColumn();
		Constant.OUTPUT_LIST.add("----- ............................ -----");
	}

}
