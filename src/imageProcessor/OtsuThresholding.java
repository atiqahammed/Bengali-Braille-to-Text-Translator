package imageProcessor;

import java.awt.Color;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class OtsuThresholding extends ImageProcessor{

	private Map<Integer, Long> grayScaleFrequency;
	private Map<Integer, Double> backgroundWeight;
	private Map<Integer, Double> foregroundWeight;
	private Map<Integer, Double> backgroudMean;
	private Map<Integer, Double> foregroudMean;
	private Map<Integer, Double> backgroundVariane;
	private Map<Integer, Double> foregroundVariane;
	private Map<Integer, Double> withinClassVariance;

	private long totalNumberOfPixel;


	@Override
	protected void operatePixel(int indexOfRow, int indexOfColumn) {
		Color c = new Color(inputImage.getRGB(indexOfColumn, indexOfRow));
		int grayLevel = (int) c.getRed();
		grayScaleFrequency.put(grayLevel, grayScaleFrequency.get(grayLevel) + 1);

	}

	public int getThresholdGrayLevel(File imageFile) {
		process(imageFile);

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

	@Override
	protected void init() {
		totalNumberOfPixel = imageHeight * imageWidth;
		grayScaleFrequency = new TreeMap<>();
		backgroundWeight = new TreeMap<>();
		foregroundWeight = new TreeMap<>();
		backgroudMean = new TreeMap<>();
		foregroudMean = new TreeMap<>();
		backgroundVariane = new TreeMap<>();
		foregroundVariane = new TreeMap<>();
		withinClassVariance = new TreeMap<>();

		long initialValue = 0;
		for (int i = 0; i < 256; i++) {
			grayScaleFrequency.put(i, initialValue);
		}

	}

	private void calculateWithInClassVariance() {
		for (int i = 0; i <= 255; i++) {
			double wClsVariance = backgroundWeight.get(i) * backgroundVariane.get(i)
					+ foregroundWeight.get(i) * foregroundVariane.get(i);
			withinClassVariance.put(i, wClsVariance);
		}

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

}
