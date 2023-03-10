package cyberCopApplication;

/**CaseReaderFactory class creates CSV or TSV object as needed by checking the file extension
 * @author Name: Sreemoyee Mukherjee
 */
public class CaseReaderFactory {

	/**createReader() creates CSVCaseReader or TSVCaseReader object as required as per the file extension
	 */
	CaseReader createReader(String filename) {
		CaseReader cr;
		if(filename.contains(".csv")) {
			cr = new CSVCaseReader(filename);
		}
		else {
			cr = new TSVCaseReader(filename);
		}
		return cr;
	}
}
