package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class Controler implements Initializable{

	@FXML
	private BorderPane brailleborderpane;

	@FXML
	private TextField default_template_ui_file_path_textfield;

	final FileChooser fileChooser = new FileChooser();
	private File choosedFile;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		System.out.println("init function");
//		loadUI("home_anchorpane_ui");
	}

	@FXML
	private void main_ui_home_button(MouseEvent mouseEvent) {
		loadUI("home_anchorpane_ui");
	}

	@FXML
	private void main_iu_default_template_button(MouseEvent mouseEvent) {
		loadUI("default_file_chooser_anchorpane_ui");
	}

	@FXML
	private void default_file_chooser_ui_choose_button(MouseEvent mouseEvent) {
		File file = fileChooser.showOpenDialog(TempTest.STAGE);
		if (file != null) {
			default_template_ui_file_path_textfield.setText(file.getAbsolutePath());
			choosedFile = file;
		}
	}

	@FXML
	private void default_file_chooser_ui_done_button(MouseEvent mouseEvent) {
		Desktop desktop = Desktop.getDesktop();
        if(choosedFile.exists())
			try {
				desktop.open(choosedFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void loadUI(String ui) {

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		brailleborderpane.setCenter(root);

	}

}


