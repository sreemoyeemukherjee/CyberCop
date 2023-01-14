// Name: Sreemoyee Mukherjee
// Andrew ID: sreemoym

package hw3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**DeleteCaseView class builds the GUI for deleting a case
 * @author Name: Sreemoyee Mukherjee, Andrew ID: sreemoym
 */
public class DeleteCaseView extends CaseView{

	/**DeleteCaseView() constructor to pass the stage title
	 */
	DeleteCaseView(String header) {
		super(header);
	}

	/**buildView() creates the GUI stage to delete a case
	 */
	@Override
	Stage buildView() {
		updateButton.setText("Delete Case");
		//Setting GUI controls with currentCase properties
		titleTextField.setText(CyberCop.currentCase.getCaseTitle());
		caseTypeTextField.setText(CyberCop.currentCase.getCaseType());
		caseDatePicker.setValue(LocalDate.parse(CyberCop.currentCase.getCaseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		caseNumberTextField.setText(CyberCop.currentCase.getCaseNumber());
		categoryTextField.setText(CyberCop.currentCase.getCaseCategory());
		caseLinkTextField.setText(CyberCop.currentCase.getCaseLink());
		caseNotesTextArea.setText(CyberCop.currentCase.getCaseNotes());

		Scene scene = new Scene(updateCaseGridPane);
		stage.setScene(scene);
		return stage;
	}

}
