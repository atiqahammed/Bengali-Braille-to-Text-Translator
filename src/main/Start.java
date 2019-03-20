package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.GrayFilter;

import preProcessor.BinaryImageProcessor2;
import preProcessor.Dilation;
import preProcessor.DotProcess;
import preProcessor.GrayScale;
import preProcessor.MedianFilter;
import preProcessor.OtsuThresholding;

public class Start {
	static Desktop desktop = Desktop.getDesktop();

	public static void main(String[] args) {
		File dataFile = new File("1-1.jpg");
		System.out.println("file path:: " + dataFile.getAbsolutePath());

		GrayScale grayScale = new GrayScale();
		File blackAndWhiteImage = grayScale.convert(dataFile);
		System.out.println("black and white convert converted...");

		OtsuThresholding otsuThresholding = new OtsuThresholding();
		int grayLevel = otsuThresholding.getThresholdGrayLevel(blackAndWhiteImage);
		System.out.println("otsu threshold :: " + grayLevel);

		File binaryImage = new BinaryImageProcessor2().getBinaryImage(blackAndWhiteImage, grayLevel);
		System.out.println("binary image conversion is completed...");

		File dImage = new Dilation().getImage(binaryImage);
		System.out.println("Dilation is completed...");

		File dotFile = new DotProcess().getRectangularDottedFile(dImage);
		System.out.println("Dot detected...");

		File MFile = new MedianFilter().getFilteredImage(dotFile, 1);
		System.out.println("median filter is done...");

		File dotFile2 = new DotProcess().getRectangularDottedFile(MFile);
		System.out.println("Dot detected...");

		// GrayFilter filter = new GrayFilter(b, p)

	}

}
