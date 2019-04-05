package util;

import java.util.ArrayList;

public class Functions {

	public void printCurrentLine(ArrayList<Integer> firstLine, ArrayList<Integer> secondLine,
			ArrayList<Integer> thirdLine) {
		System.out.println(firstLine);
		System.out.println(secondLine);
		System.out.println(thirdLine);

	}

	public String replaceCharUsingCharArray(String str, char ch, int index) {
	    char[] chars = str.toCharArray();
	    chars[index] = ch;
	    return String.valueOf(chars);
	}

}
