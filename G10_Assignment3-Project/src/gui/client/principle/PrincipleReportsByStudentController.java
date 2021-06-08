package gui.client.principle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientUI;
import common.CommonMethodsHandler;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import logic.exam.ExamResults;

public class PrincipleReportsByStudentController implements Initializable {
	// JAVAFX INSTANCES ******************************************************
	@FXML
	private BarChart<?, ?> sbHistogramBc;

	@FXML
	private CategoryAxis sbCategoryAxis;

	@FXML
	private NumberAxis sbNumberAxis;

	@FXML
	private Label sbReportsByLbl;

	@FXML
	private Label sbExamIDLbl;

	@FXML
	private Label sbMedianLbl;

	@FXML
	private Label sbAverageLbl;

	@FXML
	private Button sbNextRepBtn;

	@FXML
	private Button sbPreviousRepBtn;

	@FXML
	private Button sbBackToViewReportsBtn;

	@FXML
	private ChoiceBox<String> sbCourcesCb;

	@FXML
	private Button sbShowReportsByCourseBtn;

	@FXML
	private Label sbStudentIDLbl;

	// STATIC JAVAFX INSTANCES **********************************************
	private static BarChart<?, ?> histogramBc;
	private static CategoryAxis categoryAxis;
	private static NumberAxis numberAxis;
	private static Label reportsByLbl;
	private static Label examIDLbl;
	private static Label medianLbl;
	private static Label averageLbl;
	private static Button nextRepBtn;
	private static Button previousRepBtn;
	private static ChoiceBox<String> courcesCb;
	private static Button showReportsByCourseBtn;
	private static Button backToViewReportsBtn;
	private static Label studentIDLbl;

	// STATIC  INSTANCES ****************************************************
	public static ObservableList<String> coursesList = FXCollections.observableArrayList();
	public static List <ExamResults> examResultsList;
	private int index=0;
	private static Series series;

	// STATIC CONTROLLER INSTANCES ******************************************
	public static PrincipleReportsByStudentController prbsController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			prbsController=new PrincipleReportsByStudentController();
			histogramBc = sbHistogramBc;
			categoryAxis = sbCategoryAxis;
			numberAxis = sbNumberAxis;
			reportsByLbl = sbReportsByLbl;
			examIDLbl = sbExamIDLbl;
			medianLbl = sbMedianLbl;
			averageLbl = sbAverageLbl;
			nextRepBtn = sbNextRepBtn;
			previousRepBtn = sbPreviousRepBtn;
			backToViewReportsBtn=sbBackToViewReportsBtn;
			studentIDLbl=sbStudentIDLbl;
			previousRepBtn.setDisable(true);
			courcesCb = sbCourcesCb;
			showReportsByCourseBtn = sbShowReportsByCourseBtn;
			studentIDLbl.setText(" "+PrincipleViewReportsController.insertedValue);
			coursesList.clear();
			coursesList.add("----------");

			// set "----------" as the first value of the choice box
			courcesCb.setValue("----------");


			// set the choice box to get it's items from 'coursesList'
			courcesCb.setItems(coursesList);


			// set up a listener that sets the disable value of
			// 'showReportsByCourseBtn' according to the selected value
			courcesCb.getSelectionModel().selectedItemProperty()
			.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				if (newValue != null) {
					if (newValue.equals("----------"))
						showReportsByCourseBtn.setDisable(true);
					else
						showReportsByCourseBtn.setDisable(false);
				}
			});

			showReportsByCourseBtn.setDisable(true);

			System.out.println(PrincipleViewReportsController.insertedValue);
			ClientUI.chat.accept(new String[] { "GetCourses", PrincipleViewReportsController.insertedValue,"S"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	void BtnPressPrevousRep(ActionEvent event) {
		// TODO show the next report
		System.out.println("PrincipleReports::BtnPressPrevousRep");
		index--;
		if(index==examResultsList.size()-2)
			nextRepBtn.setDisable(false);
		if(index==0)
			previousRepBtn.setDisable(true);
		histogramBc.getData().removeAll(series);
		setExamResultData();
	}

	@FXML
	void btnPressBackToViewReports(ActionEvent event) {
		PrincipleMenuBarController.mainPaneBp.setCenter(CommonMethodsHandler.getInstance().getPane("principle", "PrincipleViewReports"));
	}

	@FXML
	void btnPressNextRep(ActionEvent event) {
		index++;
		if(index==1)
			previousRepBtn.setDisable(false);
		if(index==examResultsList.size()-1)
			nextRepBtn.setDisable(true);
		histogramBc.getData().removeAll(series);
		setExamResultData();
	}

	@FXML
	void btnPressShowReportsByCourse(ActionEvent event) {
		//System.out.println(PrincipleViewReportsController.insertedValue);
		ClientUI.chat.accept(new String[] { "GetExamDetails",courcesCb.getValue(), PrincipleViewReportsController.insertedValue,"S" });
		setExamResultData();
	}

	// EXTERNAL METHODS *******************************************************
	/**
	 *
	 * @param list
	 */
	public void setCoursesCoiseBox(List<String> list) {
		System.out.println(list);
		coursesList.addAll(list);
	}

	/**
	 *
	 * @param examResultsList
	 */
	public void setExamResultsDetails(List<ExamResults> examResultsList) {
		PrincipleReportsByStudentController.examResultsList=examResultsList;
	}

	/**
	 *
	 */
	public void setExamResultData() {
		ExamResults er=examResultsList.get(index);
		examIDLbl.setText("ExamID: #" + er.getExamID());
		medianLbl.setText("Median: "+er.getMedian());
		averageLbl.setText("Average: " + er.getAverage());
		series= er.getGraph();
		histogramBc.getData().add(series);
	}

}
