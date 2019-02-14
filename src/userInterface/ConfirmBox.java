package userInterface;

import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

	static boolean decision;
	public static boolean display(String title, String message) {

		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(300);

		Label label = new Label();
		label.setText(message);

		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");

		yesButton.setOnAction(e -> {
			decision = true;
			window.close();
		});

		noButton.setOnAction(e -> {
			decision = false;
			window.close();
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, noButton, yesButton);
		layout.setAlignment(Pos.CENTER);
		window.setScene(new Scene(layout));
		window.showAndWait();

		return decision;
	}

}
