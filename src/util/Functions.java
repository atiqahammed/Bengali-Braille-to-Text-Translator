package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import imageProcessor.TextProcessorAdvance;

import org.opencv.core.*;
import util.Utils;

public class Functions {

	ArrayList<String> letters = new ArrayList<>();

	private Map<Integer, Long> grayScaleFrequency;
	private Map<Integer, Double> backgroundWeight;
	private Map<Integer, Double> foregroundWeight;
	private Map<Integer, Double> backgroudMean;
	private Map<Integer, Double> foregroudMean;
	private Map<Integer, Double> backgroundVariane;
	private Map<Integer, Double> foregroundVariane;
	private Map<Integer, Double> withinClassVariance;
	private long totalNumberOfPixel;


	protected void init() {
		totalNumberOfPixel = Utils.IMAGE_HEIGHT * Utils.IMAGE_WEIDTH;
		grayScaleFrequency = new TreeMap<>();
		backgroundWeight = new TreeMap<>();
		foregroundWeight = new TreeMap<>();
		backgroudMean = new TreeMap<>();
		foregroudMean = new TreeMap<>();
		backgroundVariane = new TreeMap<>();
		foregroundVariane = new TreeMap<>();
		withinClassVariance = new TreeMap<>();

		long initialValue = 0;
		for (int i = 0; i <= 256; i++) {
			grayScaleFrequency.put(i, initialValue);
		}

	}

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

	public ArrayList<ArrayList<Color>> convertInGrayScale() {
		ArrayList<ArrayList<Color>> imageArray = Utils.IMAGE_ARRAY_OF_PIXEL;
		int imageHeight = Utils.IMAGE_HEIGHT;
		int imageWidth = Utils.IMAGE_WEIDTH;

		for(int i = 0; i < imageHeight; i++) {
			for(int j = 0 ; j < imageWidth; j ++) {
				Color currentPixelColor = imageArray.get(i).get(j);

				int red = (int) currentPixelColor.getRed();
				int green = (int) currentPixelColor.getGreen();
				int blue = (int) currentPixelColor.getBlue();

				int averageValue = (red + green + blue) / 3;
				Color newColor = new Color(averageValue, averageValue, averageValue);
				imageArray.get(i).set(j, newColor);
			}
		}

		return imageArray;
	}

	public int getOtsuThreshold() {
		init();
		ArrayList<ArrayList<Color>> imageArray = Utils.IMAGE_ARRAY_OF_PIXEL;

		for(int i = 0; i < Utils.IMAGE_HEIGHT; i++) {
			for(int j = 0; j < Utils.IMAGE_WEIDTH; j++) {
				int grayLevel = (int) imageArray.get(i).get(j).getRed();
				grayScaleFrequency.put(grayLevel,grayScaleFrequency.get(grayLevel) + 1);
			}
		}

		for (int i = 0; i <= 255; i++) {
			calculateBackgroundPixels(i);
			calculateForegroundPixels(i);
		}

		calculateWithInClassVariance();


		int smallestIndex = 0;

		for(int i =1; i <=255; i++) {
			if(withinClassVariance.get(i) < withinClassVariance.get(smallestIndex)) {
				smallestIndex = i;
			}
		}

		return smallestIndex;
	}

	private void calculateWithInClassVariance() {
		for (int i = 0; i <= 255; i++) {
			double wClsVariance = backgroundWeight.get(i) * backgroundVariane.get(i)
					+ foregroundWeight.get(i) * foregroundVariane.get(i);
			withinClassVariance.put(i, wClsVariance);
		}

	}
	
	
	public ArrayList<ArrayList<String>> getBengaliText(String filePath) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    	Mat kernel = Mat.ones(3,3, CvType.CV_32F);
		
		Mat src = Imgcodecs.imread(filePath);
      Mat dst = new Mat();
      
      Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
      Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 5);
      Imgproc.medianBlur(dst, dst, 3);

      Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_OTSU);
      
//      Imgproc.medianBlur(dst, dst, 3);
//      Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_ERODE, kernel);
      
      Imgcodecs.imwrite("pre_processed_image.jpg", dst);
      System.out.println("pre processing is completed");

      File image_file = new File("pre_processed_image.jpg");

      image_file = Utils.OPOSITE_BINARY_CONVERTOR.getOpositBinaryImage(image_file);
      new TextProcessorAdvance().getRectangularDottedFile(image_file);

      Utils.FILE_READ_WRITER.writeOutput(Utils.OUTPUT_LIST, Utils.OUTPUT_FILE_NAME);
      System.out.println("Execution is completed");
		
		
		return null;
	}
	

	private void calculateBackgroundPixels(int grayLevel) {
		long totalBackgroungFrequency = 0;
		double bgMean = 0;
		double variance = 0;
		for (int i = 0; i < grayLevel; i++) {
			totalBackgroungFrequency += grayScaleFrequency.get(i);
			bgMean += i * grayScaleFrequency.get(i);
		}

		if (bgMean != 0)
			bgMean = bgMean / totalBackgroungFrequency;
		double weight = totalBackgroungFrequency / (double) totalNumberOfPixel;

		for (int i = 0; i < grayLevel; i++) {
			double diff = (double) i - bgMean;
			variance += diff * diff * grayScaleFrequency.get(i);
		}

		if (variance != 0)
			variance = variance / totalBackgroungFrequency;

		backgroundWeight.put(grayLevel, weight);
		backgroudMean.put(grayLevel, bgMean);
		backgroundVariane.put(grayLevel, variance);

	}

	private void calculateForegroundPixels(int grayLevel) {
		long totalForegroungFrequency = 0;
		double fgMean = 0;
		double variance = 0;
		for (int i = grayLevel; i <= 255; i++) {
			totalForegroungFrequency += grayScaleFrequency.get(i);
			fgMean += i * grayScaleFrequency.get(i);
		}

		if (fgMean != 0)
			fgMean = fgMean / totalForegroungFrequency;
		double weight = totalForegroungFrequency / (double) totalNumberOfPixel;

		for (int i = grayLevel; i <= 255; i++) {
			double diff = (double) i - fgMean;
			variance += diff * diff * grayScaleFrequency.get(i);
		}

		if (variance != 0)
			variance = variance / totalForegroungFrequency;

		foregroundWeight.put(grayLevel, weight);
		foregroudMean.put(grayLevel, fgMean);
		foregroundVariane.put(grayLevel, variance);

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

	public void convertBinaryImage(int threshold) {
		for(int i = 0; i < Utils.IMAGE_HEIGHT; i++) {
			for(int j = 0; j < Utils.IMAGE_WEIDTH; j++) {
				int colorValue = (int) Utils.IMAGE_ARRAY_OF_PIXEL.get(i).get(j).getRed();
				if(colorValue < threshold)
					Utils.IMAGE_ARRAY_OF_PIXEL.get(i).set(j, Utils.WHITE);
				else
					Utils.IMAGE_ARRAY_OF_PIXEL.get(i).set(j, Utils.BLACK);
			}
		}
	}

	public ArrayList<ArrayList<Color>> getMedianFilteredArray(int kernelSize) {

		ArrayList<ArrayList<Color>> newImageArray = new ArrayList<ArrayList<Color>>();
		for(int i = 0; i < Utils.IMAGE_HEIGHT; i++) {

			ArrayList<Color> arrayOfARow = new ArrayList<>();
			for(int j = 0; j < Utils.IMAGE_WEIDTH; j++) {
				ArrayList<Integer> colorValue = new ArrayList<Integer>();

				for (int a = -1 * kernelSize; a <= kernelSize; a++) {
					for (int b = -1 * kernelSize; b <= kernelSize; b++) {

						int tempX = j + a;
						int tempY = i + b;

						if (tempX >= 0 && tempX <  Utils.IMAGE_WEIDTH && tempY >= 0 && tempY < Utils.IMAGE_HEIGHT) {
							Color c = Utils.IMAGE_ARRAY_OF_PIXEL.get(tempY).get(tempX);
							int grayScale = (int) c.getRed();
							colorValue.add(grayScale);
						}
					}
				}

				Collections.sort(colorValue);
				int medianValue = colorValue.get(colorValue.size()/2);
				Color color;
				if(medianValue == 0) color = Utils.BLACK;
				else color = Utils.WHITE;
				arrayOfARow.add(color);

			}

			newImageArray.add(arrayOfARow);
		}

		return newImageArray;
	}

	public ArrayList<ArrayList<Color>> getDialutedImageArray() {
		ArrayList<ArrayList<Color>> newImageArray = new ArrayList<ArrayList<Color>>();

		int row[] = {-1, 0, 1, 0};
		int col[] = {0, 1, 0, -1};

		for(int i = 0; i < Utils.IMAGE_HEIGHT; i++) {
			ArrayList<Color> arrayOfARow = new ArrayList<Color>();

			for(int j = 0; j < Utils.IMAGE_WEIDTH; j++) {

				Color c = Utils.IMAGE_ARRAY_OF_PIXEL.get(i).get(j);
				int grayScale = (int) c.getRed();
				int count = 0;

				for(int a = 0; a < 4; a++) {
					int tempX = j + col[a];
					int tempY = i + row[a];

					if(tempX>= 0 && tempX < Utils.IMAGE_WEIDTH && tempY >= 0 && tempY < Utils.IMAGE_HEIGHT) {
						Color color = Utils.IMAGE_ARRAY_OF_PIXEL.get(tempY).get(tempX);
						int grayValue = (int) color.getRed();
						if(grayValue == 255) count++;
					}

				}

				Color color;
				if (count >= 1) color = Utils.WHITE;
				else color = Utils.BLACK;

				arrayOfARow.add(color);
			}

			newImageArray.add(arrayOfARow);
		}

		return newImageArray;

	}

	public ArrayList<String> getReadableMergedWord(ArrayList<String> letters) {

		
		
		
		ArrayList<Integer> indexsOfLetterToRemove = new ArrayList<Integer>();
		
		
		
		for(int i = 1; i < letters.size(); i++) {
			String previousLetter = letters.get(i - 1);
			String currentLetter = letters.get(i);
			
			if(previousLetter.equals("$") && currentLetter.equals("র")) {
				System.out.println("bug is here to resolve");
				letters.set(i - 1, "ঋ");
				indexsOfLetterToRemove.add(i);
				
				letters.remove(i);
				i--;
				
			}
		}
		
		
		int wordLength = letters.size();
		for(int i = 1; i < wordLength; i++) {
			String previousLetter = letters.get(i - 1);
			String currentLetter = letters.get(i);

			if(Utils.LETTERS.isShoroBorno(currentLetter) && Utils.LETTERS.isBanjonBorno(previousLetter)) {
				currentLetter = Utils.LETTERS.getRepresentativeKarSymbol(currentLetter);
				letters.set(i, currentLetter);
			}
		}

		return letters;
	}
}
