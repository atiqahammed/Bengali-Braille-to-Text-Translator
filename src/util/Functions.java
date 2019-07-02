package util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

	ArrayList<String> letters = new ArrayList<>();

	public Functions() {
		letters.add("à¦…");
		letters.add("à¦†");

		letters.add("à¦‡");
		letters.add("à¦ˆ");

		letters.add("à¦‰");
		letters.add("à¦Š");

		letters.add("à¦�");
		letters.add("à¦�");

		letters.add("à¦“");
		letters.add("à¦”");

		letters.add("à¦•");
		letters.add("à¦–");
		letters.add("à¦—");
		letters.add("à¦˜");
		letters.add("à¦™");

		letters.add("à¦š");
		letters.add("à¦›");
		letters.add("à¦œ");
		letters.add("à¦�");
		letters.add("à¦ž");

		letters.add("à¦Ÿ");
		letters.add("à¦ ");
		letters.add("à¦¡");
		letters.add("à¦¢");
		letters.add("à¦£");

		letters.add("à¦¤");
		letters.add("à¦¥");
		letters.add("à¦¦");
		letters.add("à¦§");
		letters.add("à¦¨");

		letters.add("à¦ª");
		letters.add("à¦«");
		letters.add("à¦¬");
		letters.add("à¦­");
		letters.add("à¦®");

		letters.add("à¦¯");
		letters.add("à¦°");
		letters.add("à¦²");

		letters.add("à¦¶");
		letters.add("à¦¸");
		letters.add("à¦·");
		letters.add("à¦¹");
		letters.add("à¦•à§�à¦·");

		letters.add("à¦œà§�à¦ž");
		letters.add("à§œ");
		letters.add("à§�");
		letters.add("à§Ÿ");
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

	public boolean isSelectedFileValid(File file, String filePath) {
		return filePath.equals(file.getAbsolutePath());
	}

}
