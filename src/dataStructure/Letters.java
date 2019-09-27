package dataStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Letters {

	Map<String, String> symbolToLetter;
	Map<String, String> symbolToKar;

	ArrayList<String> shoroBorno;
	ArrayList<String> banjonBorno;

	public Letters() {

		symbolToLetter = new TreeMap<String, String>();
		symbolToKar =  new TreeMap<String, String>();

		shoroBorno = new ArrayList<>(Arrays.asList("অ", "আ", "ই", "ঈ", "উ", "ঊ", "এ", "ঐ", "ও", "ঔ", "ঋ"));
		banjonBorno = new ArrayList<>(Arrays.asList("ক", "খ", "গ", "ঘ", "ঙ", "চ", "ছ", "জ", "ঝ", "ঞ", "ট", "ঠ", "ড",
				"ঢ","ণ", "ত", "থ", "দ", "ধ", "ন", "প", "ফ", "ব", "ভ", "ম", "য", "র", "ল", "শ", "স", "ষ", "হ", "ক্ষ", "জ্ঞ",
				"ড়", "ঢ়", "য়"/* ,"ং", "ঃ", "ঁ"*/));

		//this map if for symbol to kar symbol
		symbolToKar.put("অ", "");
		symbolToKar.put("আ", "া");
		symbolToKar.put("ই", "ি");
		symbolToKar.put("ঈ", "ী");
		symbolToKar.put("উ", "ু");
		symbolToKar.put("ঊ", "ূ");
		symbolToKar.put("এ", "ে");
		symbolToKar.put("ঐ", "ৈ");
		symbolToKar.put("ও", "ো");
		symbolToKar.put("ঔ", "ৌ");
		symbolToKar.put("ঋ", "ৃ");

		// symbol map
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
		symbolToLetter.put("110010", "হ");
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

		symbolToLetter.put("010000", ",");
		symbolToLetter.put("011000", ";");
		symbolToLetter.put("011001", "?");
		symbolToLetter.put("001001", "-");
		
		symbolToLetter.put("000010", "$");

		//symbolToLetter.put("011010", "!");
		//symbolToLetter.put("010010", ":");

	}

	public boolean isConcateLetter(String letter) {
		return letter.equals("্‌");
	}

	public String getRepresentativeKarSymbol(String shoroBornoLetter) {
		return symbolToKar.get(shoroBornoLetter);
	}

	public boolean isBanjonBorno(String letter) {
		return banjonBorno.contains(letter);
	}

	public boolean isShoroBorno(String letter) {
		return shoroBorno.contains(letter);
	}

	public String getLetters(String symbol) {

		if(symbolToLetter.containsKey(symbol))
			return symbolToLetter.get(symbol);

		return "-";
	}

}