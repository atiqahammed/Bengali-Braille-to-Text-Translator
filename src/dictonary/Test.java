package dictonary;


import util.Utils;

public class Test {

	public static void main(String[] args) {
		BanglaDictonary dictonary = new BanglaDictonary();

//		int diff = dictonary.calculateDP("ঁআমি", "আমি");
//		System.out.println(diff);

//		LevenshteinDistance
		String word = Utils.BANGLA_DICTIONARY.getWordWithLessEditDistance("আম", "আদ");
		System.out.println(word);


	}

}
