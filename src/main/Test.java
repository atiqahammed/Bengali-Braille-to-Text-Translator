package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import com.sun.javafx.logging.Logger;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import preProcessor.BiHistogramEqualizer;
import preProcessor.BinaryImageProcessor;
import preProcessor.GrayScale;
import preProcessor.OtsuThresholding;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

public class Test extends Application {

	private Desktop desktop = Desktop.getDesktop();
	private double xOffset = 0;
	private double yOffset = 0;

	final FileChooser fileChooser = new FileChooser();

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
		Parent root = loader.load();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		// primaryStage.initStyle(StageStyle.UNDECORATED);

		// grab your root here
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});

		// move around here
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		Button button = (Button) loader.getNamespace().get("button_file_chooser");
		button.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {

				// String s =
				// FileSystemView.getFileSystemView().getSystemTypeDescription(file);
				// .button.System.out.println(s.toLowerCase());
				// System.out.println("System Type description of " +
				// file.getName() + " is " +
				// FileSystemView.getFileSystemView().getSystemTypeDescription(file));
				openFile(file);
			}

			File test = new GrayScale().convert(file);
			if (test != null)
				openFile(test);



			// MID POINT FOR BI HISTOGRAM EQUALIZATION
			OtsuThresholding otsuThresholding = new OtsuThresholding();
			int grayLevel = otsuThresholding.getThresholdGrayLevel(test);
			System.out.println(grayLevel);
			Map<Integer, Long> grayScaleFrequency = otsuThresholding.getGrayScaleFrequency();

			BiHistogramEqualizer equalizer = new BiHistogramEqualizer();
			equalizer.getBiHistogramEqualizedImage(test, grayLevel, grayScaleFrequency);

/*
			File biImg = new BinaryImageProcessor().getBinaryImage(test, grayLevel);
			if(biImg != null)
				openFile(biImg);
*/

			//Map<Integer, Long> grayScaleFrequency = new OtsuThresholding().getGrayScaleFrequency(test);

			/*
			for(int i = 0; i <=  255; i++) {
				System.out.println(i + " >> " + grayScaleFrequency.get(i));
			}*/



		});



		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("ok");

	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {

		}
	}

}