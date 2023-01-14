// Name: Sreemoyee Mukherjee
// Andrew ID: sreemoym

package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**Case class creates all the properties of case to store and interpret each row of data file as a Case
 * @author Name: Sreemoyee Mukherjee, Andrew ID: sreemoym
 */
public class Case implements Comparable<Case>{
	private StringProperty caseDate = new SimpleStringProperty();
	private StringProperty caseTitle = new SimpleStringProperty();
	private StringProperty caseType = new SimpleStringProperty();
	private StringProperty caseNumber = new SimpleStringProperty();
	private StringProperty caseLink = new SimpleStringProperty();
	private StringProperty caseCategory = new SimpleStringProperty();
	private StringProperty caseNotes = new SimpleStringProperty();

	/**Case() constructor to set all the properties of a case
	 */
	Case(String caseDate, String caseTitle, String caseType, String caseNumber,
			String caseLink, String caseCategory, String caseNotes) {
		this.caseDate.set(caseDate);
		this.caseTitle.set(caseTitle);
		this.caseType.set(caseType);
		this.caseNumber.set(caseNumber);
		this.caseLink.set(caseLink);
		this.caseCategory.set(caseCategory);
		this.caseNotes.set(caseNotes);
	}

	// All getters to get each property value
	public String getCaseDate() {
		return caseDate.get();
	}
	public String getCaseTitle() {
		return caseTitle.get();
	}
	public String getCaseType() {
		return caseType.get();
	}
	public String getCaseNumber() {
		return caseNumber.get();
	}
	public String getCaseLink() {
		return caseLink.get();
	}
	public String getCaseCategory() {
		return caseCategory.get();
	}
	public String getCaseNotes() {
		return caseNotes.get();
	}

	// All setters to set each property value
	public void setCaseDate(String date) {
		caseDate.set(date);
	}
	public void setCaseTitle(String title) {
		caseTitle.set(title);
	}
	public void setCaseType(String type) {
		caseType.set(type);
	}
	public void setCaseNumber(String number) {
		caseNumber.set(number);
	}
	public void setCaseLink(String link) {
		caseLink.set(link);
	}
	public void setCaseCategory(String category) {
		caseCategory.set(category);
	}
	public void setCaseNotes(String notes) {
		caseNotes.set(notes);
	}

	//Returns each property value as a StringProperty
	public StringProperty caseDateProperty() {
		return caseDate;
	}
	public StringProperty caseTitleProperty() {
		return caseTitle;
	}
	public StringProperty caseTypeProperty() {
		return caseType;
	}
	public StringProperty caseNumberProperty() {
		return caseNumber;
	}
	public StringProperty caseLinkProperty() {
		return caseLink;
	}
	public StringProperty caseCategoryProperty() {
		return caseCategory;
	}
	public StringProperty caseNotesProperty() {
		return caseNotes;
	}

	//Helps in sorting cases; starting with most recent to least recent case date
	@Override
	public int compareTo(Case c) {
		return c.caseDate.get().compareTo(caseDate.get());
	}

	//Returns case number when we want to use Case as a string
	@Override
	public String toString() {
		return caseNumber.get();
	}
}
