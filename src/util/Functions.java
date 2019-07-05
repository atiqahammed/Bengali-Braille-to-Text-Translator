package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;

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

	public String getImageType(File imageFile) {
		String srting = FileSystemView.getFileSystemView().getSystemTypeDescription(imageFile);
		String[] splited = srting.toLowerCase().split(" ");
		String type = splited[0];
		return type;
	}

	public String getName(File imageFile) {
		String nameWithExtension = stripExtension(imageFile.getName());
		return nameWithExtension;
	}

	public String stripExtension(String str) {
		if (str == null)
			return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1)
			return str;
		return str.substring(0, pos);
	}

	public File writeInImageFile() {
		BufferedImage outputImage = new BufferedImage(Utils.IMAGE_WEIDTH, Utils.IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now);

		String imagePath = Utils.IMAGE_FILE_NAME + /*"_" + time*/ "." + Utils.IMAGE_FILE_TYPE;
		File ouptutFile = new File(imagePath);

		for(int i = 0; i < Utils.IMAGE_HEIGHT; i++) {
			for(int j = 0; j < Utils.IMAGE_WEIDTH; j++) {

				outputImage.setRGB(j, i, Utils.IMAGE_ARRAY_OF_PIXEL.get(i).get(j).getRGB());
			}
		}

		System.out.println(Utils.IMAGE_FILE_TYPE + "..." + imagePath);


		try {
			ImageIO.write(outputImage, Utils.IMAGE_FILE_TYPE, ouptutFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ouptutFile;
	}


	public ArrayList<ArrayList<Color>> getImageIn2DArray(File imageFile) {
		try {
			BufferedImage inputImage = ImageIO.read(imageFile);
			Utils.IMAGE_HEIGHT = inputImage.getHeight();
			Utils.IMAGE_WEIDTH = inputImage.getWidth();
			Utils.IMAGE_FILE_NAME = getName(imageFile);
			Utils.IMAGE_FILE_TYPE = getImageType(imageFile);

			ArrayList<ArrayList<Color>> imageArray = new ArrayList<ArrayList<Color>>();

			for(int i = 0;  i < Utils.IMAGE_HEIGHT; i++) {
				ArrayList<Color> arrayOfARow = new ArrayList<Color>();
				for(int j = 0; j < Utils.IMAGE_WEIDTH; j++)
					arrayOfARow.add(new Color(inputImage.getRGB(j, i)));
				imageArray.add(arrayOfARow);
			}

			return imageArray;

		} catch (IOException e) {
			System.out.println("error in Functions.java getImageIn2DArray. Image not found");
		}

		return null;
	}
}
