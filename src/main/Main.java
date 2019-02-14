package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import userInterface.Home;
import utility.Utility;

public class Main extends Application implements EventHandler<ActionEvent>{

	Button button;
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

		button = new Button();

		button.setText("Click Me");
		button.setOnAction(this);

		StackPane layout = new StackPane();
		layout.getChildren().add(button);

		Scene scene = new Scene(layout, Utility.APPLICATION_WIDTH, Utility.APPLICATION_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == button) {
			System.out.println("clicked");
		}

	}

}
