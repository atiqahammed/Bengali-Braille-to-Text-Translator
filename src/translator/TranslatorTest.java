package translator;

import java.io.File;

//import com.sun.org.apache.bcel.internal.classfile.Field;

import imageProcessor.DotProcessor;
import preProcessor.Dilation;
import util.Utils;

public class TranslatorTest {

	public static void main(String[] args) {

		System.out.println("Braille to text translator");
		File ImageFile = new File("data/data1"+ ".jpg");
		File grayScaleImage = Utils.GRAY_SCALE_IMAGE_PROCESSOR.process(ImageFile);
		System.out.println("Gray Scale Conversion completed...");

		int thresholdingLavel = Utils.OTSU_SHRESHOLDER.getThresholdGrayLevel(grayScaleImage);
		System.out.println("Otsu thresholding : " + thresholdingLavel);

		File binaryImage = Utils.BINARY_IMAGE_CONVERTOR.getBinaryImage(grayScaleImage, thresholdingLavel);
		System.out.println("binary image conversion is completed...");

		File medianImmage = Utils.MEDIAN_FILTER.getFilteredImage(binaryImage, 1);
		System.out.println("median filter is done...");

		File dImage = new Dilation().getImage(medianImmage);
		System.out.println("Dilation is completed...");

		new DotProcessor().getRectangularDottedFile(dImage);


		System.out.println("execution completed...");




	}

}
