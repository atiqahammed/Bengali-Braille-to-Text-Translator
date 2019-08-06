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

class Filter2D_DemoRun {

    public void run(String[] args) {
        // Declare variables
        Mat src, dst = new Mat();

        Mat kernel = new Mat();
        Point anchor;
        double delta;
        int ddepth;
        int kernel_size;
        String window_name = "filter2D Demo";

        //! [load]
//        String imageName = ((args.length > 0) ? args[0] : "../data/lena.jpg");
        String imageName = "a.jpg";

        // Load an image
        src = Imgcodecs.imread(imageName, Imgcodecs.IMREAD_COLOR);

        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default ../data/lena.jpg] \n");
            System.exit(-1);
        }
        //! [load]

        //! [init_arguments]
        // Initialize arguments for the filter
        anchor = new Point( -1, -1);
        delta = 0.0;
        ddepth = -1;
        //! [init_arguments]

        // Loop - Will filter the image with different kernel sizes each 0.5 seconds
        int ind = 0;
        while( true )
        {
            //! [update_kernel]
            // Update kernel size for a normalized box filter
            kernel_size = 3 + 2*( ind%5 );
            Mat ones = Mat.ones( kernel_size, kernel_size, CvType.CV_32F );
            Core.multiply(ones, new Scalar(1/(double)(kernel_size*kernel_size)), kernel);
            //! [update_kernel]

            //! [apply_filter]
            // Apply filter
            Imgproc.filter2D(src, dst, ddepth , kernel, anchor, delta, Core.BORDER_DEFAULT );
            //! [apply_filter]
            HighGui.imshow( window_name, dst );

            int c = HighGui.waitKey(500);
            // Press 'ESC' to exit the program
            if( c == 27 )
            { break; }

            ind++;
        }

        System.exit(0);
    }
}

public class Filter2D_Demo {
    public static void main(String[] args) {

    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    	Mat kernel = Mat.ones(3,3, CvType.CV_32F);

    	String imageName = "braille-data/data_8.jpg";
        Mat src = Imgcodecs.imread(imageName);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 5);
//        Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 5);
        Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_OTSU);
//        Imgproc.medianBlur(dst, dst, 3);

        Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_ERODE, kernel);
        Imgcodecs.imwrite("pre_processed_image.jpg", dst);

        System.out.println("pre processing is completed");

        File image_file = new File("pre_processed_image.jpg");

        image_file = Utils.OPOSITE_BINARY_CONVERTOR.getOpositBinaryImage(image_file);
        new TextProcessorAdvance().getRectangularDottedFile(image_file);

        System.out.println("Execution is completed");


    }
}
