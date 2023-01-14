// Name: Sreemoyee Mukherjee
// Andrew ID: sreemoym

package hw3;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**CyberCop class is the main entry into the application
 * @author Name: Sreemoyee Mukherjee, Andrew ID: sreemoym
 */
public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//write your code here
		/**Binding disableProperty of menu items and text-fields 
		 * with ccView.isFileOpen so that they are enabled as needed
		 */
		ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
		ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.yearComboBox.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseNumberTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.searchButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.clearButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.saveFileMenuItem.disableProperty().bind(ccView.isFileOpen.not()); // added in HW3


		//Event Handlers for open, close, exit, save menuitems
		ccView.openFileMenuItem.setOnAction(new OpenFileMenuItemHandler());
		ccView.closeFileMenuItem.setOnAction(new CloseFileMenuItemHandler());
		ccView.exitMenuItem.setOnAction(new ExitMenuItemHandler());
		ccView.saveFileMenuItem.setOnAction(new SaveFileMenuItemHandler ()); // added in HW3

		//Event Handlers for search, clear buttons
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		ccView.clearButton.setOnAction(new ClearButtonHandler());

		//Event Handlers for add, modify, delete menuitems
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());

		//Event Handlers for chart menuitem
		ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler()); // added in HW3

		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldVlaue, newValue) ->
		{
			currentCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
			if(currentCase != null) {
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				String year = currentCase.getCaseDate().substring(0, 4);
				ccView.yearComboBox.getSelectionModel().select(year);

				//The following is some helper code to display web-pages. It is commented out to start with
				//Uncomment it to plug it in your code as needed.  
				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
					if (url != null) ccView.webEngine.load(url.toExternalForm());
				}
			}
		});



	}
	/** OpenFileMenuItemHandler inner class comes into play when we click open to open a CSV or TSV file
	 */
	private class OpenFileMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			ccView.isFileOpen.setValue(true);
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File file = null;
			if ((file = fileChooser.showOpenDialog(stage)) != null){
				String filename = file.getAbsoluteFile().toString().substring(file.getAbsoluteFile().toString().lastIndexOf("\\")+1);
				ccModel.readCases(file.getAbsoluteFile().toString());
				ccModel.buildYearMapAndList();
				ccView.caseTableView.setItems(ccModel.caseList);
				ccView.yearComboBox.setItems(ccModel.yearList);
				ccView.caseTableView.getSelectionModel().selectFirst();
				stage.setTitle("Cyber Cop"+filename);
				ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
			}
		}
	}

	/** CloseFileMenuItemHandler inner class comes into play when we click close to close an opened file
	 */
	private class CloseFileMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			ccView.isFileOpen.setValue(false);
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.yearComboBox.valueProperty().set(null);
			ccView.caseTableView.getItems().clear();
			ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
			ccView.messageLabel.setText("");
		}
	}

	/** ExitMenuItemHandler inner class comes into play when we click exit to exit the application
	 */
	private class ExitMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			Platform.exit();
		}
	}

	/** SearchButtonHandler inner class comes into play when we want to search cases as per our criteria
	 */
	private class SearchButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			//Setting empty text fields to null
			if(ccView.titleTextField.getText().isBlank()) {
				ccView.titleTextField.setText(null);
			}
			if(ccView.caseTypeTextField.getText().isBlank()) {
				ccView.caseTypeTextField.setText(null);
			}
			if(ccView.caseNumberTextField.getText().isBlank()) {
				ccView.caseNumberTextField.setText(null);
			}
			List<Case> matches = ccModel.searchCases(ccView.titleTextField.getText(), ccView.caseTypeTextField.getText(), ccView.yearComboBox.getSelectionModel().getSelectedItem(), ccView.caseNumberTextField.getText());
			ObservableList<Case> matchesObservable = FXCollections.observableArrayList();
			matchesObservable.addAll(matches);
			ccView.caseTableView.setItems(matchesObservable);
			ccView.messageLabel.setText(matchesObservable.size()+" cases");
			ccView.caseNotesTextArea.clear();
			ccView.caseTableView.getSelectionModel().selectFirst();
		}
	}

	/** ClearButtonHandler inner class comes into play when we want to clear all our filter criteria
	 */
	private class ClearButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.yearComboBox.valueProperty().set(null);
			ccView.messageLabel.setText("");
		}
	}

	/** CaseMenuItemHandler inner class comes into play when we want to add/modify/delete a case
	 */
	private class CaseMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			MenuItem selection = (MenuItem) event.getSource();
			String userSelectionText = selection.getText();
			if(userSelectionText.equals("Add case")) {
				caseView = new AddCaseView("Add Case");
				caseView.buildView().show();
				caseView.updateButton.setOnAction(new AddButtonHandler());
			}
			else if(userSelectionText.equals("Modify case")) {
				caseView = new ModifyCaseView("Modify Case");
				caseView.buildView().show();
				caseView.updateButton.setOnAction(new ModifyButtonHandler());
			}
			else if(userSelectionText.equals("Delete case")) {
				caseView = new DeleteCaseView("Delete Case");
				caseView.buildView().show();
				caseView.updateButton.setOnAction(new DeleteButtonHandler());
			}
			caseView.closeButton.setOnAction(actionEvent -> caseView.stage.close());
			caseView.clearButton.setOnAction(actionEvent -> {caseView.titleTextField.clear();
			caseView.caseTypeTextField.clear();
			caseView.caseDatePicker.setValue(null);
			caseView.caseNumberTextField.clear();
			caseView.categoryTextField.clear();
			caseView.caseLinkTextField.clear();
			caseView.caseNotesTextArea.clear();});
		}
	}

	/** AddButtonHandler inner class comes into play when we want to add a case
	 */
	private class AddButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			String caseDate = caseView.caseDatePicker.getValue().format(
					DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			Case newCase = new Case(caseDate, caseView.titleTextField.getText(),
					caseView.caseTypeTextField.getText(), caseView.caseNumberTextField.getText(),
					caseView.caseLinkTextField.getText(), caseView.categoryTextField.getText(),
					caseView.caseNotesTextArea.getText());
			// added in HW3
			// DataException handling try-catch block for checking data consistency
			try {
				if(newCase.getCaseDate().isBlank() || newCase.getCaseTitle().isBlank() || newCase.getCaseType().isBlank() || newCase.getCaseNumber().isBlank()) {
					throw new DataException("Case must have date, title, type and number");
				}
				for(Case c: ccModel.caseList) {
					if(c.getCaseNumber().equals(newCase.getCaseNumber())) {
						throw new DataException("Duplicate case number");
					}	
				}
				ccModel.caseList.add(newCase);
				ccView.caseTableView.setItems(ccModel.caseList);
				ccModel.buildYearMapAndList();
				ccView.yearComboBox.setItems(ccModel.yearList);
				ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
			}
			catch(DataException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Data Error");
				alert.setHeaderText("Error");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
	}

	/** CaseMenuItemHandler inner class comes into play when we want to modify a case
	 */
	private class ModifyButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			String caseDate = caseView.caseDatePicker.getValue().format(
					DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			Case newCase = new Case(caseDate, caseView.titleTextField.getText(),
					caseView.caseTypeTextField.getText(), caseView.caseNumberTextField.getText(),
					caseView.caseLinkTextField.getText(), caseView.categoryTextField.getText(),
					caseView.caseNotesTextArea.getText());
			// added in HW3
			// DataException handling try-catch block for checking data consistency
			try {
				if(newCase.getCaseDate().isBlank() || newCase.getCaseTitle().isBlank() || newCase.getCaseType().isBlank() || newCase.getCaseNumber().isBlank()) {
					throw new DataException("Case must have date, title, type and number");
				}
				if(!currentCase.getCaseNumber().equals(newCase.getCaseNumber())){
					for(Case c: ccModel.caseList) {
						if(c.getCaseNumber().equals(newCase.getCaseNumber())) {
							throw new DataException("Duplicate case number");
						}	
					}
				}
				//Modifying currentCase
				currentCase.setCaseTitle(newCase.getCaseTitle());
				currentCase.setCaseDate(newCase.getCaseDate());
				currentCase.setCaseType(newCase.getCaseType());
				currentCase.setCaseNumber(newCase.getCaseNumber());
				currentCase.setCaseLink(newCase.getCaseLink());
				currentCase.setCaseCategory(newCase.getCaseCategory());
				currentCase.setCaseNotes(newCase.getCaseNotes());

				ccView.caseTableView.setItems(ccModel.caseList);
			}
			catch(DataException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Data Error");
				alert.setHeaderText("Error");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
	}

	/** CaseMenuItemHandler inner class comes into play when we want to delete a case
	 */
	private class DeleteButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			//Deleting currentCase
			ccModel.caseList.removeAll(currentCase);
			ccModel.caseMap.remove(currentCase.getCaseNumber());
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
		}
	}

	/** CaseCountChartMenuItemHandler inner class comes into play when we want to display the Case Count Chart
	 */
	private class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			ccView.showChartView(ccModel.yearMap);
		}
	}

	/** SaveFileMenuItemHandler inner class comes into play when we want to save current table view to a file
	 */
	private class SaveFileMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File file = null;
			if ((file = fileChooser.showSaveDialog(stage)) != null){
				String filename = file.getAbsoluteFile().toString();
				if(ccModel.writeCases(filename)) {
					// if successfully written to file, display appropriate message
					ccView.messageLabel.setText(filename.substring(file.getAbsoluteFile().toString().lastIndexOf("\\")+1) + " saved.");
				}
			}
		}
	}
}

