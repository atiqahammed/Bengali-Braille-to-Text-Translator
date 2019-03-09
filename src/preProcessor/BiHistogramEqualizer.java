package preProcessor;

import java.awt.Color;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class BiHistogramEqualizer extends ImageProcessor{


	private long totalNumberOfPixel;
	private Map<Integer, Long> grayScaleFrequency;
	private Map<Integer, Double> grayScaleCF;
	private Map<Integer, Integer> nextGrayValue;


	@Override
	protected void writePixel(int x, int y) {
		Color c = new Color(image.getRGB(y, x));
		int thisGrayScale = (int) c.getRed();
		int supposedGrayScale = nextGrayValue.get(thisGrayScale);
		Color color = new Color(supposedGrayScale, supposedGrayScale, supposedGrayScale);
		image.setRGB(y, x, color.getRGB());

	}

	@Override
	protected void initializedPreOperation() {
		imageFileName = "Bi_HE_Image_" + imageFileName;
	}

	public File getBiHistogramEqualizedImage(File imageFile, int grayLevel, Map<Integer, Long> grayScaleFrequency) {

		this.grayScaleFrequency = grayScaleFrequency;
		initializeImage(imageFile);
		totalNumberOfPixel  = height * width;

		grayScaleCF = new TreeMap<Integer, Double>();
		nextGrayValue = new TreeMap<>();

		// first calculation
		grayScaleCF.put(0, (double)grayScaleFrequency.get(0));
		for(int i = 1; i < grayLevel; i++) {
			grayScaleCF.put(i, grayScaleCF.get(i - 1) + grayScaleFrequency.get(i));
		}

		grayScaleCF.put(grayLevel, (double)grayScaleFrequency.get(grayLevel));
		for(int i = grayLevel + 1; i<= 255; i++) {
			grayScaleCF.put(i, grayScaleCF.get(i - 1) + grayScaleFrequency.get(i));
		}


		double totalInFirstBoundary = grayScaleCF.get(grayLevel - 1);
		double totalInSecondBoundary = grayScaleCF.get(255);

		for(int i = 0; i < grayLevel; i++) {
			int newValue = (int) Math.ceil((grayLevel - 1) * grayScaleCF.get(i) / totalInFirstBoundary);
			nextGrayValue.put(i, newValue);
		}

		for(int i = grayLevel; i <= 255; i++) {
			int newValue = (int) Math.ceil(255 * grayScaleCF.get(i) / totalInSecondBoundary);
			nextGrayValue.put(i, newValue);
		}

		return convert(imageFile);
	}

}
