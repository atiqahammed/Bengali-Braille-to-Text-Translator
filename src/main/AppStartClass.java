package main;

import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import util.Constant;

public class AppStartClass extends Application{


	public static Stage STAGE;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("test_home_ui_v2.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Constant.BRAILLE_TO_TEXT_TRANSLATOR);
		primaryStage.setResizable(false);
		primaryStage.show();
		STAGE = primaryStage;
		
		
		primaryStage.setOnCloseRequest( event ->
	    {
	    	
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Braille to Text Translator Close");
			alert.setHeaderText("Do you want to close the application?");
			alert.setContentText("Choose your option.");

			ButtonType buttonTypeOne = new ButtonType("No");
			ButtonType buttonTypeTwo = new ButtonType("Yes");

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne){
				alert.close();
				event.consume();
			}  else if(result.get() == buttonTypeTwo){
				primaryStage.close();
				
			}
	    });

	}

	public static void main(String[] args) {
		
		// Application with user interface
		launch(args);
		
		// application with console output
//		Constant.FUNCTIONS.getBengaliText("braille-data/data_06.jpg");
		
		System.out.println("end");
		
		
	}

}