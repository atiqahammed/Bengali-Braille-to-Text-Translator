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
import preProcessor.Dilation;
import preProcessor.Erosion;
import preProcessor.GrayScale;
import preProcessor.HistogramEqualizer;
import preProcessor.OtsuThresholding;
import test.Temp;
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
				openFile(file);
			}



			File test = new GrayScale().convert(file);
			if (test != null)
				openFile(test);
			System.out.println("gray conversion completed");


			OtsuThresholding otsuThresholding = new OtsuThresholding();
			int grayLevel = otsuThresholding.getThresholdGrayLevel(test);
			Map<Integer, Long> getThMap = otsuThresholding.getGrayScaleFrequency();


			//File biHiFile = new BiHistogramEqualizer().getBiHistogramEqualizedImage(test, grayLevel, getThMap);
			//openFile(biHiFile);





			File binaryImage = new BinaryImageProcessor().getBinaryImage(test, grayLevel + 30);
			if(binaryImage != null)
				openFile(binaryImage);




			File deFile = new Dilation().getImage(binaryImage);
			openFile(deFile);





			new Temp().run(deFile);

















			/*
			File el = new Erosion().getImage(deFile);
			openFile(el);

			File deFile2 = new Dilation().getImage(el);
			openFile(deFile2);
			*/


/*

			File deFile = new Dilation().getImage(binaryImage);
			openFile(deFile);
			File deFile2 = new Dilation().getImage(deFile);
			openFile(deFile2);

*/




			//System.out.println(grayLevel + "level");
			//Map<Integer, Long> grayScaleFrequency = otsuThresholding.getGrayScaleFrequency();





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