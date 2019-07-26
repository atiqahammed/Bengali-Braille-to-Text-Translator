package util;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import dataStructure.Letters;
import dictonary.BanglaDictonary;
import fileManager.FileWithPrintWriter;
import imageProcessor.BinaryImageConvertor;
import imageProcessor.GrayScale;
import imageProcessor.ImageProcessor;
import imageProcessor.MedianFilter;
import preProcessor.OtsuThresholding;
import test.FileReadWriter;

public class Utils {

	public static String BRAILLE_TO_TEXT_TRANSLATOR = "Braille to Text Translator";
	public static String OUTPUT_FILE_NAME = "output.txt";
	public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|jpeg))$)";

	public static int NEIGHBOUR_DOT_SIZE_FOR_PART_OF_DOT_SELECTION = 9;
	public static int INITAL_DIFFRENCE_BETWEEN_LINE = 10;
	public static int SAME_POINT_COVERAGE = 20;
	public static int MAXIMUM_DISTANCE = 16;
	public static int LINE_INDEX_MERGED_UNIT = 3;
	public static int DIFFERENCE_BETWEEN_WORDS = 100;
	public static int DIFFERENCE_BETWEEN_LINE = 10;

	public static Color WHITE = new Color(255, 255, 255);
	public static Color BLACK = new Color(0, 0, 0);
	public static Color RED = new Color(255, 0, 0);
	public static Color GREEN = new Color(0, 255, 0);
	public static Color BLUE = new Color(0, 0, 255);
	public static Color YELLOW = new Color(255, 255, 0);

	public static ImageProcessor GRAY_SCALE_IMAGE_PROCESSOR = new GrayScale();
	public static OtsuThresholding OTSU_SHRESHOLDER = new OtsuThresholding();
	public static BinaryImageConvertor BINARY_IMAGE_CONVERTOR = new BinaryImageConvertor();
	public static MedianFilter MEDIAN_FILTER = new MedianFilter();

	public static Functions FUNCTIONS = new Functions();
	public static Letters LETTERS = new Letters();

	public static FileWithPrintWriter FILE_WRITER;

	public static int IMAGE_HEIGHT = 0;
	public static int IMAGE_WEIDTH = 0;
	public static String IMAGE_FILE_NAME = "image";
	public static String IMAGE_FILE_TYPE = "abc";

	public static ArrayList<ArrayList<Color>> IMAGE_ARRAY_OF_PIXEL = null;
	public static ArrayList<String> OUTPUT_LIST = new ArrayList<String>();
	public static FileReadWriter FILE_READ_WRITER = new FileReadWriter();
	public static BanglaDictonary BANGLA_DICTIONARY = new BanglaDictonary();

}
