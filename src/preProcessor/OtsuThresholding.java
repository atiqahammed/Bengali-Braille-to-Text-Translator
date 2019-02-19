package preProcessor;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;


public class OtsuThresholding extends ImageProcessor{

	private int smallestValue = 5000;
	private int highestValue = -9;
	private Map<Integer, Integer> grayScaleFrequency;// = new TreeMap<>();

	public void test() {
		System.out.println(smallestValue);
		System.out.println(highestValue);
		System.out.println("-------------------------------------------");
		for(int i = 0; i <= 255; i++) {
			System.out.println(i + "    " + grayScaleFrequency.get(i));
		}
	}

	@Override
	protected void writePixel(int x, int y) {
		Color c = new Color(image.getRGB(y, x));
		int grayLevel = (int)c.getRed();
		grayScaleFrequency.put(grayLevel, grayScaleFrequency.get(grayLevel) + 1);
		smallestValue = Math.min(smallestValue, grayLevel);
		highestValue = Math.max(highestValue, grayLevel);

	}

	@Override
	protected void initializedPreOperation() {
		grayScaleFrequency = new TreeMap<>();
		for(int i = 0; i <= 255; i++)
			grayScaleFrequency.put(i, 0);

	}

}
