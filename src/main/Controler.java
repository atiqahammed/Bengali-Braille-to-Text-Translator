package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import imageProcessor.TextProcessor;
import imageProcessor.TextProcessorAdvance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import preProcessor.Dilation;
import util.Utils;

public class Controler implements Initializable {

	@FXML
	private CheckBox gaussian_blur_checkbox, median_blur_checkbox;

	@FXML
	private BorderPane brailleborderpane;

	@FXML
	private TextArea output_textarea;

	@FXML
	private TextField default_template_ui_file_path_textfield;

	final FileChooser fileChooser = new FileChooser();
	private File choosedFile;
	private String outputText = "default output";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	private void exit(MouseEvent mouseEvent) {
		System.exit(0);
	}

	@FXML
	private void main_ui_home_button(MouseEvent mouseEvent) {
		loadUI("home_anchorpane_ui");
	}

	@FXML
	private void setting_button_pressed(MouseEvent mouseEvent) {
//		loadUI("home_anchorpane_ui");
		System.out.println("setting pressed");
		loadUI("setting_anchorpane_ui");
	}

//	setting_button_released
	@FXML
	private void setting_button_released(MouseEvent mouseEvent) {
//		loadUI("home_anchorpane_ui");
//		System.out.println("setting pressed");
//		loadUI("setting_anchorpane_ui");

		if (gaussian_blur_checkbox != null)
			System.out.println("rrr not null");
		else
			System.out.println("rrr null");

	}

	// mouse_exit
	@FXML
	private void mouse_exit(MouseEvent mouseEvent) {

		System.out.println("exit ... ");

		if (gaussian_blur_checkbox != null)
			System.out.println("not null");
		else
			System.out.println("null");

//		loadUI("default_file_chooser_anchorpane_ui");
	}

	@FXML
	private void main_iu_default_template_button(MouseEvent mouseEvent) {
		loadUI("default_file_chooser_anchorpane_ui");
	}

	@FXML
	private void default_file_chooser_ui_choose_button(MouseEvent mouseEvent) {
		File file = fileChooser.showOpenDialog(AppStartClass.STAGE);
		if (file != null) {
			default_template_ui_file_path_textfield.setText(file.getAbsolutePath());
			choosedFile = file;
		}
	}

	// main_iu_settings_button
	// main_iu_user_manual_button

	@FXML
	private void main_iu_user_manual_button(MouseEvent mouseEvent) {
		System.out.println("user manual button");
		loadUI("user_man_anchorpane_ui");
	}

	@FXML
	private void main_iu_settings_button(MouseEvent mouseEvent) {
//		System.out.println("ok ok ok");
//		loadUI("setting_anchorpane_ui");

		if (gaussian_blur_checkbox != null)
			System.out.println("not null");
		else
			System.out.println("null");

		if (Utils.GAUSSIAN_BLUR) {
			// gaussian_blur_checkbox.setSelected(true);

//			gaussian_blur_checkbox.setSelected(true);

			System.out.println("g");
		}
		if (Utils.MEDIAN_BLUR) {
//			median_blur_checkbox.setText("hello");
//			median_blur_checkbox.setSelected(true);
			System.out.println("M");
		}

	}

	@FXML
	private void setting_save_button(MouseEvent mouseEvent) {
		System.out.println("this is save button.. on setting ui");

//		if(gaussian_blur_checkbox != null) System.out.println("not null");

		if (gaussian_blur_checkbox != null)
			System.out.println(" save not null");
		else
			System.out.println("null");

		if (!gaussian_blur_checkbox.isSelected() && !median_blur_checkbox.isSelected()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Setting Error!");
			alert.setHeaderText("Please select noise reduction algorithm.");
			alert.setContentText(
					"There are two types of noise reduction algorithm algorithm is here. Please select at least one of them.");
			alert.showAndWait();

		}

		else {

			if (gaussian_blur_checkbox.isSelected()) {
				Utils.GAUSSIAN_BLUR = true;
			} else {
				Utils.GAUSSIAN_BLUR = false;
			}

			if (median_blur_checkbox.isSelected()) {
				Utils.MEDIAN_BLUR = true;
			} else {
				Utils.MEDIAN_BLUR = false;
			}

		}

		ArrayList<String> settings = new ArrayList<>();

		if (Utils.GAUSSIAN_BLUR) {
			settings.add("gaussian_blur+true");
		} else {
			settings.add("gaussian_blur+false");
		}

		if (Utils.MEDIAN_BLUR) {
			settings.add("median_blur+true");
		} else {
			settings.add("median_blur+false");
		}

		Utils.FILE_READ_WRITER.writeOutput(settings, Utils.SEETING_FILE_NAME);

	}

	@FXML
	private void default_file_chooser_ui_image_view_button(MouseEvent mouseEvent) {

		File file = new File(Utils.OUTPUT_IMAGE_FILE_NAME + "." + Utils.OUTPUT_IMAGE_FILE_TYPE);

		// first check if Desktop is supported by Platform or not
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported");
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (file.exists()) {
			try {
				desktop.open(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void default_file_chooser_ui_save_button(MouseEvent mouseEvent) {
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(AppStartClass.STAGE);

		if (file != null) {
			saveTextToFile(output_textarea.getText(), file);
		}
	}

	@FXML
	private void default_file_chooser_ui_done_button(MouseEvent mouseEvent) {

		String filePathInTextField = default_template_ui_file_path_textfield.getText();
		boolean validFile = Utils.FUNCTIONS.validateImageFileType(filePathInTextField);
		validFile = validFile && Utils.FUNCTIONS.isSelectedFileValid(choosedFile, filePathInTextField);

		if (validFile) {

			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			System.out.println("here");

			String imageName = filePathInTextField;
			System.out.println(imageName);
			Mat src = Imgcodecs.imread(imageName);
			Mat dst = new Mat();
			Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
			Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 5);
			Imgproc.medianBlur(dst, dst, 3);

			Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_OTSU);
//	        Imgproc.medianBlur(dst, dst, 3);

//	        Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_ERODE, kernel);
			Imgcodecs.imwrite("pre_processed_image.jpg", dst);

			System.out.println("pre processing is completed");

			File image_file = new File("pre_processed_image.jpg");

			image_file = Utils.OPOSITE_BINARY_CONVERTOR.getOpositBinaryImage(image_file);

			ArrayList<String> lines = new TextProcessorAdvance().getRectangularDottedFile(image_file);
//
			Utils.FILE_READ_WRITER.writeOutput(Utils.OUTPUT_LIST, Utils.OUTPUT_FILE_NAME);
			System.out.println("Execution is completed");

			output_textarea.setText("");

			for (int i = 0; i < lines.size(); i++) {
				output_textarea.appendText(lines.get(i));
				output_textarea.appendText("\n");

			}

		}

		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid File Type");
			alert.setHeaderText("Please select a valid image file.");
			alert.setContentText(
					"Selected file is invalid. This application supports PNG and JPG type image. Please select valid image for translation.");
			alert.showAndWait();
			default_template_ui_file_path_textfield.setText("");
			choosedFile = null;
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

	private void saveTextToFile(String content, File file) {
		try {
			PrintWriter writer;
			writer = new PrintWriter(file);
			writer.println(content);
			writer.close();
		} catch (IOException ex) {
			System.out.println("file not found to show output");
		}
	}

}
