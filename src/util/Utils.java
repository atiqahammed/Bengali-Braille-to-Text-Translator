package util;

import java.awt.Color;
import java.io.File;

import dataStructure.Letters;
import imageProcessor.BinaryImageConvertor;
import imageProcessor.GrayScale;
import imageProcessor.ImageProcessor;
import imageProcessor.MedianFilter;
import preProcessor.OtsuThresholding;

public class Utils {

	public static String BRAILLE_TO_TEXT_TRANSLATOR = "Braille to Text Translator";
	public static int APPLICATION_HEIGHT = 500;
	public static int APPLICATION_WIDTH = 840;
	public static int NEIGHBOUR_DOT_SIZE_FOR_PART_OF_DOT_SELECTION = 9;

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


}
