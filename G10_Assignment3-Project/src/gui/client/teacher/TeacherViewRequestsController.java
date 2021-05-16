package gui.client.teacher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class TeacherViewRequestsController implements Initializable {

	// JAVAFX INSTANCES ******************************************************
	@FXML
	private TableView<?> sbRequestsTv;

	@FXML
	private TableColumn<?, String> sbTeacherTc;

	@FXML
	private TableColumn<?, String> sbExamIdTc;

	@FXML
	private TableColumn<?, Integer> sbOrigTimeTc;

	@FXML
	private TableColumn<?, Integer> sbNewTimeTc;

	@FXML
	private Button sbAcceptRequestBtn;

	@FXML
	private Button sbDeclineRequestBtn;

	// STATIC JAVAFX INSTANCES **********************************************
	private TableView<?> requestsTv;
	private TableColumn<?, String> teacherTc;
	private TableColumn<?, String> examIdTc;
	private TableColumn<?, Integer> origTimeTc;
	private TableColumn<?, Integer> newTimeTc;
	private Button acceptRequestBtn;
	private Button declineRequestBtn;

	// INITIALIZE METHOD ****************************************************
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		requestsTv = sbRequestsTv;
		teacherTc = sbTeacherTc;
		examIdTc = sbExamIdTc;
		origTimeTc = sbOrigTimeTc;
		newTimeTc = sbNewTimeTc;
		acceptRequestBtn = sbAcceptRequestBtn;
		declineRequestBtn = sbDeclineRequestBtn;
	}

	// ACTION METHODS *******************************************************
	@FXML
	void BtnPressAcceptRequest(ActionEvent event) {
		// TODO send change allocated time request to server
	}

	@FXML
	void BtnPressDeclineRequest(ActionEvent event) {
		// TODO send message "request declined" to teacher
	}
}
