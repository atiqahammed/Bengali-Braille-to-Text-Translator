package util;

import java.util.ArrayList;

public class InfoUtils {
	
	public static final String ROOT_UI_VIEW = "test_home_ui_v2.fxml";
	
	public static final String HOME_UI_VIEW = "home_anchorpane_ui";
	public static final String HOME_UI_SUB_VIEW = "default_file_chooser_anchorpane_ui";
	public static final String USER_MANUAL_VIEW = "user_man_anchorpane_ui";
	public static final String TRANSLATE_FOLDER_VIEW = "translate_folder_ui";
	
	public static final String PREPROCESSED_FILE_NAME = "pre_processed_image.jpg";
	
	public static final String OPTION_CHOOSE_MESSAGE = "Choose your option.";
	public static final String APP_CLOSE_QUESTION_MESSAGE = "Do you want to close the application?";
	public static final String APP_CLOSE_WINDOW_TITLE = "Closing Application.";
	public static final String APP_CLOSE_CONSOLE_MESSAGE = "EXIT PROGRAM SUCCESSFULLY";	
	public static final String APP_NOT_SUPPORT_ERROR = "This file is not supported by desktop to open.";
	public static final String INVALID_FILE_TYPE_ERROR = "Invalid File Type";
	public static final String INVALID_FOLDER_TYPE_ERROR = "Invalid Folder Type";
	public static final String INVALID_FILE_TYPE_ERROR2 = "Please select a valid image file.";
	public static final String INVALID_FILE_TYPE_ERROR3 = "Selected file is invalid. This application supports PNG and JPG type image. Please select valid image for translation.";
	public static final String INVALID_FILE_TYPE_ERROR4 = "Please select a valid direcroty.";
	public static final String INVALID_FILE_TYPE_ERROR5 = "Selected folder is invalid. This application supports PNG and JPG type image. Please select valid folder in where all files has PNG or JPG extension.";
	public static final String INVALID_FILE_TYPE_ERROR6 = "Selected folder is invalid for output. Please select a valid folder.";
	public static final String FILE_SAVE_WARNING = "OUTPUT FILE NOT FOUND. DATA CANNOT BE SAVE.";
	
	public static final String SAVE_FILE_REGEX = "\"TXT files (*.txt)\", \"*.txt\""; 
	
	public static final String NO_BUTTON_TEXT = "No";
	public static final String YES_BUTTON_TEXT = "Yes";
	
	public static final ArrayList<String> LANGUAGES = new ArrayList<String>()
			{ 
	            { 
	                add("Bengali"); 
	                add("English"); 
	            } 
			};
	
}
