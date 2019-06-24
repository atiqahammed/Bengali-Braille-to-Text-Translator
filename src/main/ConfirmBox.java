package main;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmBox {

	public static void display() throws IOException {

		Stage confirmWindow = new Stage();

//		FXMLLoader loader = new FXMLLoader(BrailleStartMain.class.getClass().getResource("confirm_ui.fxml"));
		Parent root = BrailleStartMain.confirmBoxLoader1.load();



		Scene scene = new Scene(root);
		confirmWindow.setTitle("Braille to Text Translator");
		confirmWindow.setScene(scene);
		confirmWindow.setResizable(false);
		confirmWindow.showAndWait();

		System.out.println("ok2");
	}

}
