package filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @file Filter2D_demo.java
 * @brief Sample code that shows how to implement your own linear filters by using filter2D function
 */

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import imageProcessor.TextProcessorAdvance;
import util.Utils;

public class Filter2D_Demo {
    public static void main(String[] args) {

    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    	Mat kernel = Mat.ones(3,3, CvType.CV_32F);

    	String imageName = "braille-data/data_06.jpg";
        Mat src = Imgcodecs.imread(imageName);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 5);
        Imgproc.medianBlur(dst, dst, 3);

        Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_OTSU);
//        Imgproc.medianBlur(dst, dst, 3);

//        Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_ERODE, kernel);
        Imgcodecs.imwrite("pre_processed_image.jpg", dst);

        System.out.println("pre processing is completed");

        File image_file = new File("pre_processed_image.jpg");

        image_file = Utils.OPOSITE_BINARY_CONVERTOR.getOpositBinaryImage(image_file);
        new TextProcessorAdvance().getRectangularDottedFile(image_file);

        Utils.FILE_READ_WRITER.writeOutput(Utils.OUTPUT_LIST, Utils.OUTPUT_FILE_NAME);
        System.out.println("Execution is completed");
    }
}
