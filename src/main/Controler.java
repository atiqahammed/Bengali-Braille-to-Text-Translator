package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import preProcessor.Dilation;
import util.Constant;
import util.InfoUtils;

public class Controler implements Initializable {

	private boolean inputFolderFileValidation;
	
	@FXML
	private CheckBox gaussian_blur_checkbox, median_blur_checkbox;

	@FXML
	private BorderPane brailleborderpane;
	
	@FXML
	private RadioButton bengali_radio_button;

	@FXML
	private TextArea output_textarea;

	@FXML
	private TextField default_template_ui_file_path_textfield, folder_ui_input_folder_textbox, folder_ui_output_folder_textbox1;
	
	@FXML
	private ComboBox language_combo;

	private FileChooser fileChooser;// = new FileChooser();
	private File choosedFile;
	private String outputText = "default output";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	private void exit(MouseEvent mouseEvent) {
		
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(InfoUtils.APP_CLOSE_WINDOW_TITLE);
		alert.setHeaderText(InfoUtils.APP_CLOSE_QUESTION_MESSAGE);
		alert.setContentText(InfoUtils.OPTION_CHOOSE_MESSAGE);

		
		ButtonType buttonTypeOne = new ButtonType(InfoUtils.NO_BUTTON_TEXT);
		ButtonType buttonTypeTwo = new ButtonType(InfoUtils.YES_BUTTON_TEXT);

		
		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
		Optional<ButtonType> result = alert.showAndWait();
		
		
		if (result.get() == buttonTypeOne){
			alert.close();
		}  else if(result.get() == buttonTypeTwo){
			AppStartClass.LOG.info(InfoUtils.APP_CLOSE_CONSOLE_MESSAGE);
			System.exit(0);
		}
		
	}
	
	
	
	@FXML
	private void input_folder_button(MouseEvent mouseEvent) {
		
		DirectoryChooser dir_chooser = new DirectoryChooser();
        File folder = dir_chooser.showDialog(AppStartClass.STAGE); 
        inputFolderFileValidation = true;
        
        if (folder != null) { 
        	
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                	inputFolderFileValidation = Constant.FUNCTIONS.validateImageFileType(file.getAbsolutePath());
                	if(!inputFolderFileValidation) break;
                } else {
                	inputFolderFileValidation = false;
                }
            }
            
            if(inputFolderFileValidation) {
            	folder_ui_input_folder_textbox.setText(folder.getAbsolutePath());
            } else {
            	Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle(InfoUtils.INVALID_FOLDER_TYPE_ERROR);
    			alert.setHeaderText(InfoUtils.INVALID_FILE_TYPE_ERROR4);
    			alert.setContentText(InfoUtils.INVALID_FILE_TYPE_ERROR5);
    			alert.showAndWait();
    		
            }
            
        } 
		
	}
	
	@FXML
	private void output_folder_button(MouseEvent mouseEvent) {
		System.out.println("output folder button");
		
		DirectoryChooser dir_chooser = new DirectoryChooser();
        File folder = dir_chooser.showDialog(AppStartClass.STAGE); 
        inputFolderFileValidation = true;
        
        if (folder != null) {
        	folder_ui_output_folder_textbox1.setText(folder.getAbsolutePath());
        } else {
        	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(InfoUtils.INVALID_FOLDER_TYPE_ERROR);
			alert.setHeaderText(InfoUtils.INVALID_FILE_TYPE_ERROR4);
			alert.setContentText(InfoUtils.INVALID_FILE_TYPE_ERROR6);
			alert.showAndWait();
        }
        
		
		
	}
	
	@FXML
	private void translate_folder(MouseEvent mouseEvent) {
		System.out.println("translate folder button");
	}
	
	@FXML
	private void main_iu_default_translate_folder(MouseEvent mouseEvent) {
		loadUI(InfoUtils.TRANSLATE_FOLDER_VIEW);
	}
	
	
	@FXML
	private void main_ui_home_button(MouseEvent mouseEvent) {
		loadUI(InfoUtils.HOME_UI_VIEW);
	}
	

	@FXML
	private void main_iu_default_template_button(MouseEvent mouseEvent) {
		loadUI(InfoUtils.HOME_UI_SUB_VIEW);
	}

	
	@FXML
	private void default_file_chooser_ui_choose_button(MouseEvent mouseEvent) {
		fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(AppStartClass.STAGE);
		
		if (file != null) {
			default_template_ui_file_path_textfield.setText(file.getAbsolutePath());
			choosedFile = file;
		}
		
		
		if(language_combo.getItems().size() < 2) {
			language_combo.getItems().addAll(InfoUtils.LANGUAGES);
			language_combo.setValue(InfoUtils.LANGUAGES.get(0));
		}
	}


	@FXML
	private void main_iu_user_manual_button(MouseEvent mouseEvent) {
		loadUI(InfoUtils.USER_MANUAL_VIEW);
	}


	@FXML
	private void default_file_chooser_ui_image_view_button(MouseEvent mouseEvent) {
		
		File file = new File(Constant.OUTPUT_IMAGE_FILE_NAME + "." + Constant.OUTPUT_IMAGE_FILE_TYPE);

		// first check if Desktop is supported by Platform or not
		if (!Desktop.isDesktopSupported()) {
			AppStartClass.LOG.warning(InfoUtils.APP_NOT_SUPPORT_ERROR);
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (file.exists()) {
			try {
				desktop.open(file);
			} catch (IOException e) {
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
		
		
		//System.out.println("test");
		
	}

	
	@FXML
	private void default_file_chooser_ui_done_button(MouseEvent mouseEvent) {

		String filePathInTextField = default_template_ui_file_path_textfield.getText();
		boolean validFile = Constant.FUNCTIONS.validateImageFileType(filePathInTextField);
		validFile = validFile && Constant.FUNCTIONS.isSelectedFileValid(choosedFile, filePathInTextField);

		if (validFile) {

			if(language_combo.getValue().equals(InfoUtils.LANGUAGES.get(1)))
				Constant.IS_BENGALI_SELECTED = false;
			
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			String imageName = filePathInTextField;
			System.out.println(imageName);
			Mat src = Imgcodecs.imread(imageName);
			Mat dst = new Mat();
			Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
			Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 5);
			Imgproc.medianBlur(dst, dst, 3);

			Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_OTSU);
			Imgcodecs.imwrite(InfoUtils.PREPROCESSED_FILE_NAME, dst);
			File image_file = new File(InfoUtils.PREPROCESSED_FILE_NAME);

			image_file = Constant.OPOSITE_BINARY_CONVERTOR.getOpositBinaryImage(image_file);
			ArrayList<String> lines = new TextProcessorAdvance().getRectangularDottedFile(image_file);
			Constant.FILE_READ_WRITER.writeOutput(Constant.OUTPUT_LIST, Constant.OUTPUT_FILE_NAME);
			
			output_textarea.setText("");

			for (int i = 0; i < lines.size(); i++) {
				output_textarea.appendText(lines.get(i));
				output_textarea.appendText("\n");

			}

		}

		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(InfoUtils.INVALID_FILE_TYPE_ERROR);
			alert.setHeaderText(InfoUtils.INVALID_FILE_TYPE_ERROR2);
			alert.setContentText(InfoUtils.INVALID_FILE_TYPE_ERROR3);
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
			AppStartClass.LOG.warning(InfoUtils.FILE_SAVE_WARNING);
		}
	}
	
}
