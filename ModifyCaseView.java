// Name: Sreemoyee Mukherjee
// Andrew ID: sreemoym

package hw3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**ModifyCaseView class builds the GUI for modifying a case
 * @author Name: Sreemoyee Mukherjee, Andrew ID: sreemoym
 */
public class ModifyCaseView extends CaseView{

	/**ModifyCaseView() constructor to pass the stage title
	 */
	ModifyCaseView(String header) {
		super(header);
	}

	/**buildView() creates the GUI stage to modify a case
	 */
	@Override
	Stage buildView() { 
		updateButton.setText("Modify Case");
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
