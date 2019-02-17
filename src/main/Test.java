package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.sun.javafx.logging.Logger;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import preProcessor.GrayScale;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;



public class Test extends Application{

	 private Desktop desktop = Desktop.getDesktop();
	private double xOffset = 0;
	private double yOffset = 0;

	final FileChooser fileChooser = new FileChooser();

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("okk");
		// TODO Auto-generated method stub



		FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
		Parent root = loader.load();







		//Parent root = FXMLLoader.load(getClass().getResource("temp.fxml"));
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		//primaryStage.initStyle(StageStyle.UNDECORATED);





		 //grab your root here
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
       @Override
       public void handle(MouseEvent event) {
           xOffset = event.getSceneX();
           yOffset = event.getSceneY();
       }
   });

   //move around here
   root.setOnMouseDragged(new EventHandler<MouseEvent>() {
       @Override
       public void handle(MouseEvent event) {
           primaryStage.setX(event.getScreenX() - xOffset);
           primaryStage.setY(event.getScreenY() - yOffset);
       }
   });



   Button button = (Button)loader.getNamespace().get("button_file_chooser");
   button.setOnAction(e->{
	   System.out.println("hello");
	   File file = fileChooser.showOpenDialog(primaryStage);
       if (file != null) {
           openFile(file);
       }

       File test = new GrayScale().convert(file);
       if(test != null)
    	   openFile(test);


   });

	if(button != null)
		System.out.println("ok :)");




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