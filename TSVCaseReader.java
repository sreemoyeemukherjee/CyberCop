package cyberCopApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**TSVCaseReader class reads the TSV file and stores all the cases in a list
 * @author Name: Sreemoyee Mukherjee
 */
public class TSVCaseReader extends CaseReader {

	/**TSVCaseReader() constructor to set the filename
	 */
	TSVCaseReader(String filename) {
		super(filename);
	}

	/**readCases() reads the TSV file and stores and returns data from the file as a list
	 */
	@Override
	List<Case> readCases() {
		List<Case> caseList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			Scanner input = new Scanner(new File(filename));
			while (input.hasNextLine()) {
				sb.append(input.nextLine() + "\n");
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] data = sb.toString().split("\n");
		int errorCount = 0; // added in HW3 - to store number of error cases
		for (String s : data) {
			Case c = new Case(s.split("\t")[0], s.split("\t")[1], s.split("\t")[2],
					s.split("\t")[3], s.split("\t")[4], s.split("\t")[5], s.split("\t")[6]);
			if(c.getCaseDate().isBlank() || c.getCaseTitle().isBlank() || c.getCaseType().isBlank() || c.getCaseNumber().isBlank()) {
				errorCount++;
				continue; // skips adding erroneous cases
			}
			caseList.add(c);
		}
		// try-catch-finally block to handle data consistency exceptions
		// and to return the correct list of cases
		try {
			if(errorCount > 0) {
				throw new DataException(errorCount+" cases rejected.");
			}
		}
		catch(DataException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Data Error");
			alert.setHeaderText("Error");
			alert.setContentText(e.getMessage()+
					"\nThe file must have cases with\ntab separated date, title, type, and case number!");
			alert.showAndWait();
		}
		finally {
			return caseList;
		}
	}
}
