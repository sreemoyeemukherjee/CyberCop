package cyberCopApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**CCModel class stores the backend data manipulation functions
 * @author Name: Sreemoyee Mukherjee
 */
public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//write your code here
		CaseReaderFactory crf = new CaseReaderFactory();
		CaseReader cr = crf.createReader(filename);
		List<Case> temp = cr.readCases();
		//Sorting cases, starting with most recent case date
		Collections.sort(temp);		
		caseList.addAll(temp);
		for(Case c: caseList) {
			caseMap.put(c.getCaseNumber(), c);
		}
	}

	// new method added in HW3
	/** writeCases() writes the caseList to a new file as per the file format.
	 * The file format is checked by checking if the filename contains '.csv' extension or '.tsv'
	 * extension. Lastly, this method returns true on successfully writing to the file and false otherwise.
	 * @param filename
	 */
	boolean writeCases(String filename) {
		boolean flag = true;
		Scanner input = new Scanner(System.in);
		try (
				BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
				){
			for(Case c: caseList) {
				if(filename.contains(".csv"))
				{
					bw.write(c.getCaseDate()+","+c.getCaseTitle()+","+c.getCaseType()+
							","+c.getCaseNumber()+","+c.getCaseLink()+","+c.getCaseCategory()+","+c.getCaseNotes()+" \n");
				}
				else if(filename.contains(".tsv")) {
					bw.write(c.getCaseDate()+"\t"+c.getCaseTitle()+"\t"+c.getCaseType()+
							"\t"+c.getCaseNumber()+"\t"+c.getCaseLink()+"\t"+c.getCaseCategory()+"\t"+c.getCaseNotes() +" \n");
				}
			}
		} catch (IOException e) {
			flag = false;
		}
		return flag;
	}
	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
		for(Case c: caseList) {
			String year = c.getCaseDate().substring(0, 4);
			List<Case> temp;
			if(yearMap.containsKey(year)) {
				temp = yearMap.get(year);
				temp.add(c);
				yearMap.put(year, temp);
			}
			else {
				temp = new ArrayList<>();
				temp.add(c);
				yearMap.put(year, temp);
			}
		}
		yearList.addAll(yearMap.keySet());
		Collections.sort(yearList);
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//write your code here
		List<Case> matches = new ArrayList<>();
		for(Case c: caseList) {
			if(title != null && caseType != null && year != null && caseNumber != null && 
					c.getCaseTitle().toLowerCase().contains(title.toLowerCase())
					&& c.getCaseType().toLowerCase().equals(caseType.toLowerCase())
					&& c.getCaseDate().contains(year)
					&& c.getCaseNumber().equals(caseNumber)) {
				matches.add(c);
				break;
			}
			if(caseNumber != null) {
				if(c.getCaseNumber().contains(caseNumber)) {
					matches.add(c);
					continue;
				}
			}
			if(title != null && caseNumber == null) {
				if(year != null) {
					if(caseType != null) {
						if(c.getCaseTitle().toLowerCase().contains(title.toLowerCase())
								&& c.getCaseType().toLowerCase().contains(caseType.toLowerCase())
								&& c.getCaseDate().contains(year)) {
							matches.add(c);
							continue;
						}
					}
					else {
						if(c.getCaseTitle().toLowerCase().contains(title.toLowerCase())
								&& c.getCaseDate().contains(year)) {
							matches.add(c);
							continue;
						}
					}
				}
				else {
					if(caseType != null) {
						if(c.getCaseTitle().toLowerCase().contains(title.toLowerCase())
								&& c.getCaseType().toLowerCase().contains(caseType.toLowerCase())) {
							matches.add(c);
							continue;	
						}
					}
					else {
						if(c.getCaseTitle().toLowerCase().contains(title.toLowerCase())) {
							matches.add(c);
							continue;
						}
					}
				}
			}
			else if(caseNumber == null){
				if(year != null) {
					if(caseType != null) {
						if(c.getCaseType().toLowerCase().contains(caseType.toLowerCase())
								&& c.getCaseDate().contains(year)) {
							matches.add(c);
							continue;
						}
					}
					else {
						if(c.getCaseDate().contains(year)) {
							matches.add(c);
							continue;
						}
					}
				}
			}
			if(title == null && caseType == null && year == null && caseNumber == null) {
				matches.add(c);
			}
		}
		return matches;
	}
}
