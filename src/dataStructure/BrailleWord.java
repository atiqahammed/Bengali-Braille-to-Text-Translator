package dataStructure;

import java.util.ArrayList;

import util.Utils;

public class BrailleWord {

	private ArrayList<LineColumn> colList;

	public BrailleWord(ArrayList<LineColumn> colList) {
		this.colList = colList;
	}

	public ArrayList<LineColumn> getColList(){
		return colList;
	}
	
	public void printWord() {
		Utils.OUTPUT_LIST.add("----- Print a braille words column -----");
		for(LineColumn column: colList)
			column.printColumn();
		Utils.OUTPUT_LIST.add("----- ............................ -----");
	}

}
