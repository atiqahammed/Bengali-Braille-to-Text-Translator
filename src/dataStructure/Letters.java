package dataStructure;

import java.util.Map;
import java.util.TreeMap;

public class Letters {

	Map<String, String> symbolToLetter;

	public Letters() {

		symbolToLetter = new TreeMap<String, String>();

		symbolToLetter.put("100000", "অ");
		symbolToLetter.put("001110", "আ");

		symbolToLetter.put("010100", "ই");
		symbolToLetter.put("001010", "ঈ");

		symbolToLetter.put("101001", "উ");
		symbolToLetter.put("110011", "ঊ");

		symbolToLetter.put("100010", "এ");
		symbolToLetter.put("001100", "ঐ");

		symbolToLetter.put("101010", "ও");
		symbolToLetter.put("010101", "ঔ");


		symbolToLetter.put("101000", "ক");
		symbolToLetter.put("101101", "খ");
		symbolToLetter.put("110110", "গ");
		symbolToLetter.put("110001", "ঘ");
		symbolToLetter.put("001101", "ঙ");

		symbolToLetter.put("100100", "চ");
		symbolToLetter.put("100001", "ছ");
		symbolToLetter.put("010110", "জ");
		symbolToLetter.put("101011", "ঝ");
		symbolToLetter.put("010010", "ঞ");

		symbolToLetter.put("011111", "ট");
		symbolToLetter.put("010111", "ঠ");
		symbolToLetter.put("110101", "ড");
		symbolToLetter.put("111111", "ঢ");
		symbolToLetter.put("001111", "ণ");

		symbolToLetter.put("011110", "ত");
		symbolToLetter.put("100111", "থ");
		symbolToLetter.put("100110", "দ");
		symbolToLetter.put("011101", "ধ");
		symbolToLetter.put("101110", "ন");

		symbolToLetter.put("111100", "প");
		symbolToLetter.put("011010", "ফ");
		symbolToLetter.put("110000", "ব");
		symbolToLetter.put("111001", "ভ");
		symbolToLetter.put("101100", "ম");

		symbolToLetter.put("101111", "য");
		symbolToLetter.put("111010", "র");
		symbolToLetter.put("111000", "ল");

		symbolToLetter.put("100101", "শ");
		symbolToLetter.put("011100", "স");
		symbolToLetter.put("111101", "ষ");
		symbolToLetter.put("110101", "হ");
		symbolToLetter.put("111110", "ক্ষ");

		symbolToLetter.put("100011", "জ্ঞ");
		symbolToLetter.put("110111", "ড়");
		symbolToLetter.put("111011", "ঢ়");
		symbolToLetter.put("010001", "য়");

		symbolToLetter.put("000100", "্‌");
		symbolToLetter.put("000011", "ং");
		symbolToLetter.put("000001", "ঃ");
		symbolToLetter.put("001000", "ঁ");
		symbolToLetter.put("010011", "।");

	}

	public String getLetters(String symbol) {

		if(symbolToLetter.containsKey(symbol))
			return symbolToLetter.get(symbol);

		return "-";
	}

}
