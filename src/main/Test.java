package main;


import java.io.File;
import java.util.ArrayList;

import imageProcessor.TextProcessor;
import imageProcessor.TextProcessorAdvance;
import preProcessor.Dilation;
import util.Utils;


public class Test {

	public static void main(String[] args) {

		File imageFile = new File("data/data1.jpg");
//		File imageFile = new File("data/data5.jpg");
//		File imageFile = new File("data/data6.png");
//		File imageFile = new File("data/data7.jpg");


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

		ArrayList<ArrayList<String>>text = new TextProcessorAdvance().getRectangularDottedFile(imageFile);

		Utils.FILE_READ_WRITER.writeOutput(Utils.OUTPUT_LIST, Utils.OUTPUT_FILE_NAME);
		System.out.println("execution is completed");

	}

}