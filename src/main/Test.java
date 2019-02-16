package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;



public class Test extends Application{


	private double xOffset = 0;
	private double yOffset = 0;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("okk");
		// TODO Auto-generated method stub


		FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
		Parent root = loader.load();

		Button button = (Button)loader.getNamespace().get("button_file_chooser");

		button.setOnAction(e -> {
			System.out.println("hello");
		});

		// button_file_chooser
		//TextField foo = (TextField)loader.getNamespace().get("exampleFxId");


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







		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println("ok");

	}

}
