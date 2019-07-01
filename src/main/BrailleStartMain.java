package main;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BrailleStartMain extends Application {

//	public static  confirmBoxLoader1;
	public static FXMLLoader MAIN_UI_FXML_LOADER;
	public static FXMLLoader CONFIRM_BOX_UI_FXML_LOADER;
	public static Parent MAIN_ROOT;
	public static Parent CONFIRM_BOX_ROOT;


	public Button APPLICATION_CLOSE_BUTTON;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		Stage confirmWindow = new Stage();


//		FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
//		FXMLLoader confirmBoxLoader = new FXMLLoader(getClass().getResource("confirm_ui.fxml"));

		loadUI();
//		Parent root = loader.load();
//		Parent confirmBoxRoot = confirmBoxLoader.load();

		//home_ui_anchor_pane

		System.out.println("ok");
		Button button = (Button) MAIN_UI_FXML_LOADER.getNamespace().get("test_button");


		AnchorPane p1 = (AnchorPane) MAIN_UI_FXML_LOADER.getNamespace().get("home_ui_anchor_pane");
		AnchorPane p = (AnchorPane) MAIN_UI_FXML_LOADER.getNamespace().get("root_anchoe_pane");
		AnchorPane p2 = (AnchorPane) FXMLLoader.load(getClass().getResource("test_temp.fxml"));
		
		
		button.setOnAction(e -> {

			System.out.println("test button");
			BorderPane.
			p1.getChildren().clear();
			//AnchorPane.setRightAnchor(p1, 0.0);
//			System.out.println(AnchorPane.getRightAnchor(p1).doubleValue());

//			p1.set

		});

		APPLICATION_CLOSE_BUTTON.setOnAction(e -> {

			closeApplication(primaryStage);
			System.out.println("hello");

		});
//
		Scene scene = new Scene(MAIN_ROOT);
		primaryStage.setTitle("Braille to Text Translator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	private void closeApplication(Stage primaryStage) {
		System.out.println("application has been closed");
		primaryStage.close();
	}

	private void loadUI() throws IOException {

		MAIN_UI_FXML_LOADER = new FXMLLoader(getClass().getResource("ui.fxml"));
		CONFIRM_BOX_UI_FXML_LOADER = new FXMLLoader(getClass().getResource("confirm_ui.fxml"));

		MAIN_ROOT = MAIN_UI_FXML_LOADER.load();
		CONFIRM_BOX_ROOT = CONFIRM_BOX_UI_FXML_LOADER.load();

		APPLICATION_CLOSE_BUTTON = (Button) MAIN_UI_FXML_LOADER.getNamespace().get("close_button");


	}

}