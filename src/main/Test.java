package main;


import java.io.File;
import java.util.ArrayList;

import imageProcessor.TextProcessor;
import imageProcessor.TextProcessorAdvance;
import preProcessor.Dilation;
import util.Utils;


public class Test {

	public static void main(String[] args) {

//		File imageFile = new File("braille-data/data_0.png");
//		File imageFile = new File("braille-data/data_1.jpg");
		File imageFile = new File("braille-data/data_02.jpg");
//		File imageFile = new File("braille-data/data_3.jpg");
//		File imageFile = new File("braille-data/data_4.jpg");
//		File imageFile = new File("braille-data/data_5.jpg");
//		File imageFile = new File("braille-data/data_6.jpg");
//		File imageFile = new File("braille-data/data_7.jpg");
//		File imageFile = new File("braille-data/data_8.jpg");
//		File imageFile = new File("braille-data/data_9.jpg");


		imageFile = Utils.GRAY_SCALE_IMAGE_PROCESSOR.process(imageFile);
		System.out.println("Gray Scale Conversion completed...");

		int thresholdingLavel = Utils.OTSU_SHRESHOLDER.getThresholdGrayLevel(imageFile);
		System.out.println("Otsu thresholding : " + thresholdingLavel);

		imageFile = Utils.BINARY_IMAGE_CONVERTOR.getBinaryImage(imageFile, thresholdingLavel);
		System.out.println("binary image conversion is completed...");

		imageFile = Utils.MEDIAN_FILTER.getFilteredImage(imageFile, 1);
		System.out.println("median filter is done...");

		imageFile = new Dilation().getImage(imageFile);
		System.out.println("Dilation is completed...");

		ArrayList<String>text = new TextProcessorAdvance().getRectangularDottedFile(imageFile);

		System.out.println("execution completed...");
		Utils.FILE_READ_WRITER.writeOutput(Utils.OUTPUT_LIST, Utils.OUTPUT_FILE_NAME);
//		System.out.println("execution is completed");

	}

}