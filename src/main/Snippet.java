package main;

public class Snippet {
	imageFile = Utils.GRAY_SCALE_IMAGE_PROCESSOR.process(imageFile);
			System.out.println("Gray Scale Conversion completed...");
	
			int thresholdingLavel = Utils.OTSU_SHRESHOLDER.getThresholdGrayLevel(imageFile);
			System.out.println("Otsu thresholding : " + thresholdingLavel);
	
			imageFile = Utils.BINARY_IMAGE_CONVERTOR.getBinaryImage(imageFile, thresholdingLavel);
			System.out.println("binary image conversion is completed...");
	
	//		imageFile = Utils.MEDIAN_FILTER.getFilteredImage(imageFile, 1);
	//		System.out.println("median filter is done...");
	
	//		imageFile = new Dilation().getImage(imageFile);
	//		System.out.println("Dilation is completed...");
}

