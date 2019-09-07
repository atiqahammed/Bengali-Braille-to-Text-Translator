package dataStructure;

import java.util.ArrayList;

public class BrailleWord {

	private ArrayList<LineColumn> colList;

	public BrailleWord(ArrayList<LineColumn> colList) {
		this.colList = colList;
	}

	public ArrayList<LineColumn> getColList(){
		return colList;
	}

}
