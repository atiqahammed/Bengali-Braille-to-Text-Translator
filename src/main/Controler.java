package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;


public class Controler implements Initializable{

	@FXML
	private BorderPane brailleborderpane;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		System.out.println("init function");
	}

	@FXML
	private void main_ui_home_button(MouseEvent mouseEvent) {
////		loadUI("u1");
//		System.out.println("ok");
//
//		Parent root = null;
//		try {
//			root = FXMLLoader.load(getClass().getResource("u1.fxml"));
//		} catch (IOException e) {
//
//			System.out.println("error");
//		}
//
//		main_border_pane.setCenter(root);
//		if(root == null) {
//			System.out.println("r");
//		}
//
//		if (main_border_pane == null) {
//			System.out.println("b");
//		}
		
		System.out.println("home button");

	}

//	@FXML
//	private void btn2(MouseEvent mouseEvent) {
//		loadUI("u2");
//	}
//
//	@FXML
//	private void btn3(MouseEvent mouseEvent) {
//		loadUI("u3");
//	}
//
//	@FXML
//	private void btn4(MouseEvent mouseEvent) {
//		loadUI("u4");
//	}

//	private void loadUI(String ui) {
//
//		Parent root = null;
//		try {
//			root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		main_border_pane.setCenter(root);
//
//	}

}


