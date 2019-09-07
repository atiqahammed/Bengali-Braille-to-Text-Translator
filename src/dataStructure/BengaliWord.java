package dataStructure;

import java.util.ArrayList;
import java.util.Collections;

import util.Utils;

public class BengaliWord {

	private ArrayList<String> letterList;

	public BengaliWord() {
		letterList = new ArrayList<>();
	}

	public void addLetters(String letter) {
		letterList.add(letter);
	}

	public String getBengaliWord() {

		int arraySize = letterList.size();
		for(int i = 0; i < arraySize; i++) {
			if(Utils.LETTERS.isConcateLetter(letterList.get(i)) && i + 1 < arraySize) {
				Collections.swap(letterList, i, i + 1);
				i++;
			}
			//Utils.OUTPUT_LIST.add(letterList.get(i));
			//Utils.OUTPUT_LIST.add("*");
		}


		letterList = Utils.FUNCTIONS.getReadableMergedWord(letterList);

		String word = "";
		for(int i = 0; i < arraySize; i++) {
//			if(Utils.LETTERS.isConcateLetter(letterList.get(i)) && i + 1 < arraySize) {
//				Collections.swap(letterList, i, i + 1);
//				i++;
//			}
			word = word + letterList.get(i);
//			Utils.OUTPUT_LIST.add(letterList.get(i));
//			Utils.OUTPUT_LIST.add("*");
		}

		Utils.OUTPUT_LIST.add(word);

		return null;
	}




}
