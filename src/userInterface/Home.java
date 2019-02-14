package userInterface;

import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utility.Utility;

public class Home extends Application{

	private static Home home;

	public Home() {

	}

	/*
	public static Home getHomeUserInterfaceInstance() {
		if(home == null) {
			synchronized (Home.class) {
				if(home == null) {
					home = new Home();
				}
				return home;
			}
		}
		return home;
	}

	public void name() {
		System.out.println("test");
	}*/

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
