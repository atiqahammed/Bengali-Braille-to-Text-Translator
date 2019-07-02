package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

	ArrayList<String> letters = new ArrayList<>();

	public Functions() {
		letters.add("অ");
		letters.add("আ");

		letters.add("ই");
		letters.add("ঈ");

		letters.add("উ");
		letters.add("ঊ");

		letters.add("এ");
		letters.add("ঐ");

		letters.add("ও");
		letters.add("ঔ");

		letters.add("ক");
		letters.add("খ");
		letters.add("গ");
		letters.add("ঘ");
		letters.add("ঙ");

		letters.add("চ");
		letters.add("ছ");
		letters.add("জ");
		letters.add("ঝ");
		letters.add("ঞ");

		letters.add("ট");
		letters.add("ঠ");
		letters.add("ড");
		letters.add("ঢ");
		letters.add("ণ");

		letters.add("ত");
		letters.add("থ");
		letters.add("দ");
		letters.add("ধ");
		letters.add("ন");

		letters.add("প");
		letters.add("ফ");
		letters.add("ব");
		letters.add("ভ");
		letters.add("ম");

		letters.add("য");
		letters.add("র");
		letters.add("ল");

		letters.add("শ");
		letters.add("স");
		letters.add("ষ");
		letters.add("হ");
		letters.add("ক্ষ");

		letters.add("জ্ঞ");
		letters.add("ড়");
		letters.add("ঢ়");
		letters.add("য়");
	}

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

	public int getLetterCount(ArrayList<String> lettersInline) {
		int count = 0;
		for (int i = 0; i < lettersInline.size(); i++)
			if (letters.contains(lettersInline.get(i)))
				count++;

		return count;
	}

	public boolean validateImageFileType(final String image) {
		Pattern pattern = Pattern.compile(Utils.IMAGE_PATTERN);
		Matcher matcher = pattern.matcher(image);
		return matcher.matches();
	}

}
