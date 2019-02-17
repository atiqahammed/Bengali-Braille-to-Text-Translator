package main;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import userInterface.ConfirmBox;
import userInterface.Home;
import utility.Utility;

public class Main extends Application implements EventHandler<ActionEvent>{

	// https://www.youtube.com/watch?v=hFAsvYz2oeE
	//https://www.youtube.com/watch?v=jtdTHAQSb6c&list=PL383vhYFx-7owa-bxRJixFfdxDF3HnAkt

	FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
	Parent root = loader.load();
	Button fileChooserButton = (Button) loader.getNamespace().get("button_file_chooser");

	fileChooserButton.setOnAction(e -> {
		System.out.println("hello");
	});

	Stage window;

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

		Parent root = FXMLLoader.load(getClass().getResource("../fxmlFiles/t.fxml"));

		window = primaryStage;
		//window.setTitle(Utility.BRAILLE_TO_TEXT_TRANSLATOR);
		window.initStyle(StageStyle.TRANSPARENT);
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});



		button = new Button();

		button.setText("Click Me");
		button.setOnAction(this);

		StackPane layout = new StackPane();
		layout.getChildren().add(button);

		Scene scene = new Scene(layout, Utility.APPLICATION_WIDTH, Utility.APPLICATION_HEIGHT);
		scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
		window.setScene(new Scene(root, Utility.APPLICATION_WIDTH, Utility.APPLICATION_HEIGHT));
		window.show();

	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == button) {
			System.out.println("clicked");
		}

	}

	private void closeProgram() {

		boolean decision = ConfirmBox.display("Closing program", "Sure you want to close the program?");
		if(decision)
			window.close();
	}

}
