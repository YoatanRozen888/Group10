package gui.client.teacher;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import common.CommonMethodsHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.question.Question;

public class TeacherCreateExamController implements Initializable {
	public static TeacherCreateExamController tceController;

	// JAVAFX INSTNCES ******************************************************

	@FXML
	private AnchorPane sbTopPanelAp;

	@FXML
	private Button sbContinue1Btn;

	@FXML
	private AnchorPane sbBotPanelAp;

	@FXML
	private ChoiceBox<String> sbChooseCourseCb;

	@FXML
	private ChoiceBox<String> sbExamBankCb;

	@FXML
	private TableView<Question> sbAvailableQuestionsTv;

	@FXML
	private TableColumn<Question, String> sbQuestionID1Tc;

	@FXML
	private TableColumn<Question, Void> sbPreview1Tc;

	@FXML
	private TableColumn<Question, Void> sbAddToExamTc;

	@FXML
	private TableView<Question> sbCurrentQuestionsTable;

	@FXML
	private TableColumn<Question, String> sbQuestionID2Tc;

	@FXML
	private TableColumn<Question, Void> sbPreview2Tc;

	@FXML
	private TableColumn<Question, Void> sbRemoveFromExamTc;

	@FXML
	private Button sbChangeBankBtn;

	@FXML
	private Button sbContinue2Btn;

	// STATIC JAVAFX INSTANCES **********************************************
	private static AnchorPane topPanelAp;
	private static ChoiceBox<String> examBankCb;
	private static Button continue1Btn;
	private static AnchorPane botPanelAp;
	private static ChoiceBox<String> chooseCourseCb;
	private static TableView<Question> availableQuestionsTv;
	private static TableColumn<Question, String> questionID1Tc;
	private static TableColumn<Question, Void> preview1Tc;
	private static TableColumn<Question, Void> addToExamTc;
	private static TableView<Question> currentQuestionsTable;
	private static TableColumn<Question, String> questionID2Tc;
	private static TableColumn<Question, Void> preview2Tc;
	private static TableColumn<Question, Void> removeFromExamTc;
	private static Button changeBankBtn;
	private static Button continue2Btn;

	// STATIC INSTANCES *****************************************************
	public static ObservableList<String> bankList = FXCollections.observableArrayList("----------");
	public static ObservableList<String> CourseList = FXCollections.observableArrayList("----------");
	private static List<Question> questionList;
	private static List<Question> questionInExam;
	private ObservableList<Question> questionObservableList = FXCollections.observableArrayList();
	private static HashMap<Question,TableRow<Question>> locateRow = new HashMap<>();
	private static String msg;
	public static TableView<Question> xx;

	// INITIALIZE METHOD ****************************************************
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tceController = new TeacherCreateExamController();

		/**** First panel ****/
		topPanelAp = sbTopPanelAp;

		examBankCb = sbExamBankCb;
		// set "----------" as the first value of the choice box
		examBankCb.setValue("----------");
		// set the choice box to get it's items from 'bankList'
		examBankCb.setItems(bankList);

		continue1Btn = sbContinue1Btn;
		botPanelAp = sbBotPanelAp;
		botPanelAp.setDisable(true);
		chooseCourseCb = sbChooseCourseCb;
		availableQuestionsTv = sbAvailableQuestionsTv;
		questionID1Tc = sbQuestionID1Tc;
		preview1Tc = sbPreview1Tc;
		addToExamTc = sbAddToExamTc;
		currentQuestionsTable = sbCurrentQuestionsTable;
		questionID2Tc = sbQuestionID2Tc;
		preview2Tc = sbPreview2Tc;
		removeFromExamTc = sbRemoveFromExamTc;
		changeBankBtn = sbChangeBankBtn;
		continue2Btn = sbContinue2Btn;

		if (bankList.size() == 1) { // add banks only once
			ClientUI.chat.accept(new String[] { "GetBanks", ChatClient.user.getUsername(), "1" });
		}
	}

	// ACTION METHODS *******************************************************
//	@FXML
//	void btnPressCancelCreation(ActionEvent event) {
//		System.out.println("TeacherCreateExam::btnPressCancelCreation");
//	}

	@FXML
	void btnPressChangeBank(ActionEvent event) {
		System.out.println("TeacherCreateExam::btnPressChangeBank");
		sbTopPanelAp.setDisable(false);
		sbBotPanelAp.setDisable(true);
		examBankCb.setValue("----------");

		availableQuestionsTv.setItems(null);
		currentQuestionsTable.setItems(null);
		questionInExam.clear();
	}

	@FXML
	void btnPressContinue1(ActionEvent event) {
		System.out.println("TeacherCreateExam::btnPressContinue1");

		if (examBankCb.getValue() != "----------") {

			CourseList.clear(); // clear list

			sbTopPanelAp.setDisable(true);
			sbBotPanelAp.setDisable(false);
//		chooseCourseCb.setValue("----------");
			ClientUI.chat.accept(
					new String[] { "GetCourseBySubject", examBankCb.getValue(), ChatClient.user.getUsername(), "1" });

			chooseCourseCb.setItems(CourseList);

			ClientUI.chat.accept(new String[] { "btnPressShowQuestionsBySubject", examBankCb.getValue(), "2",
					ChatClient.user.getUsername() });

			// set up table view
			questionID1Tc.setCellValueFactory(new PropertyValueFactory<Question, String>("questionID"));
			questionID1Tc.setStyle("-fx-alignment: CENTER; -fx-font-weight: Bold;");

			// set preview col
			Callback<TableColumn<Question, Void>, TableCell<Question, Void>> btnCellFactory = new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {
				@Override
				public TableCell<Question, Void> call(final TableColumn<Question, Void> param) {
					final TableCell<Question, Void> cell1 = new TableCell<Question, Void>() {

						private final Button btn = new Button();
						private final ImageView previewEye = new ImageView(new Image("/previewEye.png"));

						@Override
						public void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							btn.setStyle("-fx-background-color: transparent;");
							btn.setPrefSize(40, 20);
							previewEye.setPreserveRatio(true);
							previewEye.setFitHeight(20);
							previewEye.setFitWidth(40);
							btn.setGraphic(previewEye);
							if (empty) {
								setGraphic(null);
							} else {
								btn.setOnAction(e -> {
									Question question = getTableRow().getItem();
									TeacherMenuBarController.mainPaneBp.setDisable(true);
									TeacherMenuBarController.menuBarAp.setDisable(true);
									chooseQuestionToPreview(question);
								});
								setGraphic(btn);
							}
						}
					};
					cell1.setAlignment(Pos.CENTER);
					return cell1;
				}
			};
			preview1Tc.setCellFactory(btnCellFactory);

			// set button cells for the 'Update Time' Column
			Callback<TableColumn<Question, Void>, TableCell<Question, Void>> btnCellFactory2 = new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {

				@Override
				public TableCell<Question, Void> call(final TableColumn<Question, Void> param2) {
					final TableCell<Question, Void> cell2 = new TableCell<Question, Void>() {

						private final Button btn = new Button();
						private final ImageView addicon = new ImageView(new Image("/icon_add.png"));

						@Override
						public void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							btn.setStyle("-fx-background-color: transparent;");
							btn.setPrefSize(40, 20);
							addicon.setPreserveRatio(true);
							addicon.setFitHeight(20);
							addicon.setFitWidth(40);
							btn.setGraphic(addicon);
							if (empty) {
								setGraphic(null);
							} else {
								btn.setOnAction(e -> {
									Question question = getTableRow().getItem();
									locateRow.put(question, getTableRow()); //TODO <-->
									getTableRow().setDisable(true);
									addQuestionToCurrentQuestions(question);
								});
								setGraphic(btn);
							}
						}
					};
					cell2.setAlignment(Pos.CENTER);
					return cell2;
				}
			};
			addToExamTc.setCellFactory(btnCellFactory2);

			// set up current table view
			questionID2Tc.setCellValueFactory(new PropertyValueFactory<Question, String>("questionID"));
			questionID2Tc.setStyle("-fx-alignment: CENTER; -fx-font-weight: Bold;");

			// set preview col
			Callback<TableColumn<Question, Void>, TableCell<Question, Void>> btnCellFactory3 = new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {
				@Override
				public TableCell<Question, Void> call(final TableColumn<Question, Void> param3) {
					final TableCell<Question, Void> cell3 = new TableCell<Question, Void>() {

						private final Button btn = new Button();
						private final ImageView previewEye = new ImageView(new Image("/previewEye.png"));

						@Override
						public void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							btn.setStyle("-fx-background-color: transparent;");
							btn.setPrefSize(40, 20);
							previewEye.setPreserveRatio(true);
							previewEye.setFitHeight(20);
							previewEye.setFitWidth(40);
							btn.setGraphic(previewEye);
							if (empty) {
								setGraphic(null);
							} else {
								btn.setOnAction(e -> {
									Question question = getTableRow().getItem();
									TeacherMenuBarController.mainPaneBp.setDisable(true);
									TeacherMenuBarController.menuBarAp.setDisable(true);
									chooseQuestionToPreview(question);
								});
								setGraphic(btn);
							}
						}
					};
					cell3.setAlignment(Pos.CENTER);
					return cell3;
				}
			};
			preview2Tc.setCellFactory(btnCellFactory3);

			// set button cells for the 'Update Time' Column
			Callback<TableColumn<Question, Void>, TableCell<Question, Void>> btnCellFactory4 = new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {

				@Override
				public TableCell<Question, Void> call(final TableColumn<Question, Void> param4) {
					final TableCell<Question, Void> cell4 = new TableCell<Question, Void>() {

						private final Button btn = new Button();
						private final ImageView removeicon = new ImageView(new Image("/icon_remove.png"));

						@Override
						public void updateItem(Void item, boolean empty) {
							super.updateItem(item, empty);
							btn.setStyle("-fx-background-color: transparent;");
							btn.setPrefSize(40, 20);
							removeicon.setPreserveRatio(true);
							removeicon.setFitHeight(20);
							removeicon.setFitWidth(40);
							btn.setGraphic(removeicon);
							if (empty) {
								setGraphic(null);
							} else {
								btn.setOnAction(e -> {
									Question question = getTableRow().getItem();
									locateRow.get(question).setDisable(false); //TODO <-->
									questionInExam.remove(question);
									removeQuestionFromCurrentQuestions(question);
								});
								setGraphic(btn);
							}
						}
					};
					cell4.setAlignment(Pos.CENTER);
					return cell4;
				}
			};
			removeFromExamTc.setCellFactory(btnCellFactory4);

		} else {
			CommonMethodsHandler.getInstance().getNewAlert(AlertType.ERROR, "Error message",
					"Missing Exam Bank/Subject Name", "Must to choose Subject name/bank").showAndWait();
		}
	}

	@FXML
	void btnPressContinue2(ActionEvent event) throws Exception {
		System.out.println("TeacherCreateExam::btnPressContinue2");
		String correctAnswer, author = ChatClient.user.getFirstname() + " " + ChatClient.user.getLastname();

		if (chooseCourseCb.getValue() != null) {
			if (!questionObservableList.isEmpty()) {
				TeacherMenuBarController.mainPaneBp.setCenter(
						CommonMethodsHandler.getInstance().getPane("teacher", "TeacherComputerizedExamDefinitions"));

				ClientUI.chat.accept(new String[] { "btnPressContinue2CreateExam", chooseCourseCb.getValue(),
						examBankCb.getValue(), author, ChatClient.user.getUsername() });
			} else {
				CommonMethodsHandler.getInstance().getNewAlert(AlertType.ERROR, "Error message",
						"Missing question in exam", "Must to choose question").showAndWait();
			}
		} else {
			CommonMethodsHandler.getInstance()
					.getNewAlert(AlertType.ERROR, "Error message", "Missing Course Name", "Must to choose course name")
					.showAndWait();
		}
	}

	// EXTERNAL USE METHODS **************************************************
	public void setBankChoiceBox(List<String> msg) {
		System.out.println(msg.toString());
		bankList.addAll(msg);
	}

	public void setCourseChoiceBox(List<String> msg) {
		System.out.println(msg.toString());
		CourseList.addAll(msg);
		System.out.println(CourseList);
	}

	public void chooseQuestionToPreview(Question question) {
		try {
			new TeacherPreviewQuestionController().start(new Stage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(question);
		TeacherPreviewQuestionController.tpqController.setQuestion(question);
	}

	public void addQuestionToCurrentQuestions(Question question) {
		questionObservableList.add(question);
		currentQuestionsTable.setItems(questionObservableList);
		questionInExam = currentQuestionsTable.getItems();
	}

	public void removeQuestionFromCurrentQuestions(Question question) {
		questionObservableList.remove(question);
		currentQuestionsTable.setItems(questionObservableList);
		questionInExam = currentQuestionsTable.getItems();
	}

	public void setQuestionTableView(List<Question> questions) {
		questionList = questions;
		questionObservableList.clear();
		questionObservableList.addAll(questions);
		availableQuestionsTv.setItems(questionObservableList);
	}

	public void successfulCreateExam(String Msg) {
		msg = Msg;
	}

	public ObservableList<Question> getCurrentObservableList() {
		return questionObservableList;
	}

	public List<Question> getCurrentList() {
		return questionInExam;
	}

}
