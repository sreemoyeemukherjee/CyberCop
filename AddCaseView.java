// Name: Sreemoyee Mukherjee
// Andrew ID: sreemoym

package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**AddCaseView class builds the GUI for adding a case
 * @author Name: Sreemoyee Mukherjee, Andrew ID: sreemoym
 */
public class AddCaseView extends CaseView{

	/**AddCaseView() constructor to pass the stage title
	 */
	AddCaseView(String header) {
		super(header);
	}

	/**buildView() creates the GUI stage to add a case
	 */
	@Override
	Stage buildView() {
		caseDatePicker.setValue(LocalDate.now()); 
		updateButton.setText("Add Case");
		Scene scene = new Scene(updateCaseGridPane);
		stage.setScene(scene);
		return stage;
	}

}
