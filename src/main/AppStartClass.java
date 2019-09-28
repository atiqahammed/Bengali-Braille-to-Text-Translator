package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Utils;

public class AppStartClass extends Application{


	public static Stage STAGE;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("test_home_ui_v2.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Utils.BRAILLE_TO_TEXT_TRANSLATOR);
		primaryStage.setResizable(false);
		primaryStage.show();
		STAGE = primaryStage;

	}

	public static void main(String[] args) {
		
		// Application with user interface
//		launch(args);
		
		// application with console output
		Utils.FUNCTIONS.getBengaliText("braille-data/data_05.jpg");
		
		
	}

}