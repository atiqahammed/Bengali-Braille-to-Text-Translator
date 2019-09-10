package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import imageProcessor.TextProcessor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private void default_file_chooser_ui_image_view_button(MouseEvent mouseEvent) {
		System.out.println("image viewer button");
	}

	@FXML
	private void default_file_chooser_ui_save_button(MouseEvent mouseEvent) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(TempTest.STAGE);

        if (file != null) {
            saveTextToFile(output_textarea.getText(), file);
        }
	}

	@FXML
	private void default_file_chooser_ui_done_button(MouseEvent mouseEvent) {

		String filePathInTextField = default_template_ui_file_path_textfield.getText();
		boolean validFile = Utils.FUNCTIONS
				.validateImageFileType(filePathInTextField);
		validFile = validFile && Utils.FUNCTIONS.isSelectedFileValid(choosedFile, filePathInTextField);

		if (validFile) {
			System.out.println("valid\nGo to next page with output");
			File grayScaleImage = Utils.GRAY_SCALE_IMAGE_PROCESSOR.process(choosedFile);
			System.out.println("Gray Scale Conversion completed...");

			int thresholdingLavel = Utils.OTSU_SHRESHOLDER.getThresholdGrayLevel(grayScaleImage);
			System.out.println("Otsu thresholding : " + thresholdingLavel);

			File binaryImage = Utils.BINARY_IMAGE_CONVERTOR.getBinaryImage(grayScaleImage, thresholdingLavel);
			System.out.println("binary image conversion is completed...");

			File medianImmage = Utils.MEDIAN_FILTER.getFilteredImage(binaryImage, 1);
			System.out.println("median filter is done...");

			File dImage = new Dilation().getImage(medianImmage);
			System.out.println("Dilation is completed...");

//			Utils.IMAGE_ARRAY_OF_PIXEL = Utils.FUNCTIONS.getImageIn2DArray(choosedFile);
//			Utils.IMAGE_ARRAY_OF_PIXEL = Utils.FUNCTIONS.convertInGrayScale();
//			int threshold = Utils.FUNCTIONS.getOtsuThreshold();
//			Utils.FUNCTIONS.convertBinaryImage(threshold);
//			Utils.IMAGE_ARRAY_OF_PIXEL = Utils.FUNCTIONS.getMedianFilteredArray(1);
//			Utils.IMAGE_ARRAY_OF_PIXEL = Utils.FUNCTIONS.getDialutedImageArray();
//			Utils.FUNCTIONS.writeInImageFile();

			ArrayList<ArrayList<String>>text = new TextProcessor().getRectangularDottedFile(dImage);
			System.out.println(text.size() + " size");
			for(int i = 0; i < text.size(); i++) {
				for(int j = 0; j < text.get(i).size(); j++) {
//					System.out.print(text.get(i).get(j));
					output_textarea.appendText(text.get(i).get(j));
				}
				output_textarea.appendText("\n");
			}

		}

		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid File Type");
			alert.setHeaderText("Please select a valid image file.");
			alert.setContentText("Selected file is invalid. This application supports PNG and JPG type image. Please select valid image for translation.");
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
