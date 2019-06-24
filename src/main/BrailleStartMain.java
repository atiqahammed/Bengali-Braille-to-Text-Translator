package main;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BrailleStartMain extends Application {

	public static FXMLLoader confirmBoxLoader1;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage confirmWindow = new Stage();


		FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
		FXMLLoader confirmBoxLoader = new FXMLLoader(getClass().getResource("confirm_ui.fxml"));


		Parent root = loader.load();
		Parent confirmBoxRoot = confirmBoxLoader.load();

		System.out.println("ok");
		Button button = (Button) loader.getNamespace().get("close_button");
		button.setOnAction(e -> {

			Label label = (Label) confirmBoxLoader.getNamespace().get("message_confirm");
			label.setText("Test message hjfjg gjhg gjhg jhg  \njhghjg jhgjhg jhg \n jhg jgj jhgu jiuiu jhghjhg jyjgug jguygg");

			Scene confirmBoxScene = new Scene(confirmBoxRoot);
			confirmWindow.setTitle("Braille to Text Translator");
			confirmWindow.setScene(confirmBoxScene);
			confirmWindow.setResizable(false);
			confirmWindow.showAndWait();

			System.out.println("hello");

		});
//		Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
//
		Scene scene = new Scene(root);
		primaryStage.setTitle("Braille to Text Translator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();





	}

}