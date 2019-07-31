package dictonary;


import util.Utils;

public class Test {

	public static void main(String[] args) {
		BanglaDictonary dictonary = new BanglaDictonary();

//		int diff = dictonary.calculateDP("à¦�à¦†à¦®à¦¿", "à¦†à¦®à¦¿");
//		System.out.println(diff);

		int editDistance = Utils.BANGLA_DICTIONARY.getEditDistance("আমার");
		System.out.println(editDistance);
		
//		LevenshteinDistance
		String word = Utils.BANGLA_DICTIONARY.getWordWithLessEditDistance("আমা:", "আমার");
		System.out.println(word);


	}

}
