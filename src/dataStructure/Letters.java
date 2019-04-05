package dataStructure;

import java.util.Map;
import java.util.TreeMap;

public class Letters {

	Map<String, String> symbolToLetter;

	public Letters() {
		symbolToLetter = new TreeMap<String, String>();
		symbolToLetter.put("110000", "ব");
		symbolToLetter.put("111010", "র");
		symbolToLetter.put("001110", "আ");
	}


	public String getLetters(String symbol) {
		//System.out.println(symbol);
		//System.out.println(symbolToLetter.keySet());
		if(symbolToLetter.containsKey(symbol)) {
			return symbolToLetter.get(symbol);
		}

		return "-";
	}

}
