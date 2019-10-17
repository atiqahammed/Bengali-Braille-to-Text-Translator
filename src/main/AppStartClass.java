package main;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import util.Constant;
import util.InfoUtils;

public class AppStartClass extends Application{


	
	public static Stage STAGE;
	public final static Logger LOG = Logger.getLogger(AppStartClass.class.getName());
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		

		Parent root = FXMLLoader.load(getClass().getResource(InfoUtils.ROOT_UI_VIEW));
		
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Constant.BRAILLE_TO_TEXT_TRANSLATOR);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
		STAGE = primaryStage;
		
		
		
		primaryStage.setOnCloseRequest( event -> {
	    	
			
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(InfoUtils.APP_CLOSE_WINDOW_TITLE);
			alert.setHeaderText(InfoUtils.APP_CLOSE_QUESTION_MESSAGE);
			alert.setContentText(InfoUtils.OPTION_CHOOSE_MESSAGE);

			
			ButtonType buttonTypeOne = new ButtonType(InfoUtils.NO_BUTTON_TEXT);
			ButtonType buttonTypeTwo = new ButtonType(InfoUtils.YES_BUTTON_TEXT);

			
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

			
			Optional<ButtonType> result = alert.showAndWait();
			
			
			// application close condition with confirmation...
			if (result.get() == buttonTypeOne){
				alert.close();
				event.consume();
			}  
			
			else if(result.get() == buttonTypeTwo) {
				
				primaryStage.close();
				
			}
			
	    });

	}

	public static void main(String[] args) {
		
		// Application with user interface
		launch(args);
		
		
		// application with console output
//		Constant.FUNCTIONS.getBengaliText("braille-data/data_06.jpg");

		
		LOG.info(InfoUtils.APP_CLOSE_CONSOLE_MESSAGE);		
	
	}

}