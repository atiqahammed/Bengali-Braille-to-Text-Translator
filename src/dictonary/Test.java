package dictonary;


import java.util.ArrayList;

import util.Utils;

public class Test {

	public static void main(String[] args) {
//		BanglaDictonary dictonary = new BanglaDictonary();

//		int diff = dictonary.calculateDP("à¦�à¦†à¦®à¦¿", "à¦†à¦®à¦¿");
//		System.out.println(diff);

//		int editDistance = Utils.BANGLA_DICTIONARY.getEditDistance("আমার");
//		System.out.println(editDistance);
//
////		LevenshteinDistance
//		String word = Utils.BANGLA_DICTIONARY.getWordWithLessEditDistance("আমা:", "আমার");
//		System.out.println(word);


		/*
		 *
		 *
		 * আমার লজটেয় চররল
ঘুঘু
দকিম দিল তোকাকে
সেই আআব হিমের আজব ঁ
আজব শিশক খাস ্‌দিইইইইইতে
অ , ঁ
থাকে
ইওন শিশকর পরিচয়     অংশগ্রহণকারী     অগ্নিপুরাণ   অত্যাদর
		 */


		String test = "জনসংখ্‌যার";
		ArrayList<Integer> inderToRemove = new ArrayList<>();

		for(int i = 0; i < test.length(); i++) {

			if((int)test.charAt(i) == 8204)
				inderToRemove.add(i);
//				System.out.println("ok");
			System.out.println(test.charAt(i) + " "+ (int)test.charAt(i));
		}

		String te = "";
		for(int i = 0; i < test.length(); i++) {
			if(!inderToRemove.contains(i))
				te += test.charAt(i);
		}

		System.out.println(te);

		System.out.println(inderToRemove);


//		System.out.println(Utils.BANGLA_DICTIONARY.getWord("তোকাকে") );

	}

}
