package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import userInterface.Home;
import utility.Utility;

public class Main extends Application{

	public static void main(String[] args) {
		System.out.println("Braille to Text Translator");
		//Home home = Home.getHomeUserInterfaceInstance();
		//home.name();
		//home.launch(args);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(Utility.BRAILLE_TO_TEXT_TRANSLATOR);
		Button button = new Button();

		button.setText("Click Me");

		StackPane layout = new StackPane();
		layout.getChildren().add(button);

		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
