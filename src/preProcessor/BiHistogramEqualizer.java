package preProcessor;

import java.io.File;
import java.util.Map;

public class BiHistogramEqualizer extends ImageProcessor{


	private long totalNumberOfPixel;
	private Map<Integer, Long> grayScaleFrequency;

	@Override
	protected void writePixel(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initializedPreOperation() {
		imageFileName = "Bi_HE_Image_" + imageFileName;
	}

	public File getBiHistogramEqualizedImage(File imageFile, int grayLevel, Map<Integer, Long> grayScaleFrequency) {

		this.grayScaleFrequency = grayScaleFrequency;
		initializeImage(imageFile);
		totalNumberOfPixel  = height * width;
		System.out.println(totalNumberOfPixel);



		return null;
	}

}
