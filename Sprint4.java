package gedcomParser;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Sprint4 {
	//Sprint 4 - Priya Parmar - User Story 22: Unique IDs
	public static void checkUniqueIndiId(indi i, ArrayList<indi> individuals) {
        String indId = i.getId();
        
        for (int j = 0; j < individuals.size(); j++) {
            String id = individuals.get(j).getId();
            if (id.equals(indId) != individuals.get(j).equals(j)) {
                System.out.println("Error US22: Check Unique ID: Individual "+ i.getName().replace("/", "") +" (" +id +") " +"has duplicate ID: Line: "+getLineNumber());
                break;
            }
        }
    }
	public static void checkUniqueFamId(fam f, ArrayList<fam> families) {
		String famId = f.getId();
		for (int j = 0; j < families.size(); j++) {
            String id = families.get(j).getId();
            if (families.get(j).equals(f) != id.equals(famId)) {
                System.out.println("Error US22: Check Unique ID: Family with Husband "+  f.getHusband() + "and Wife  " + f.getWife() +" (" +id +") " +"has duplicate ID: Line: "+getLineNumber());
                break;
            } 
        }
	}
	//Sprint 4 - Priya Parmar - User Story 25: Unique first names in families
	public static String checkUniqueChildren(ArrayList<indi> indArray, fam currentfam){
		int flag=0;
		ArrayList<String> children = currentfam.getChildren();
		ArrayList<String> childrenNames = new ArrayList<String>();
		ArrayList<Date> childrenBdays = new ArrayList<Date>();
		
		String error = "";
		if (children.size() !=0){
			for (int i=0; i < children.size(); i++){
				String currentChildId = children.get(i); 
				indi childIndi = new gedcomParser().getIndividualById(currentChildId, indArray);
				childrenNames.add(childIndi.getName()); 
				childrenBdays.add(childIndi.getBirth()); 
			}
		}
		ArrayList<Integer> duplicateNames = distinctValues(childrenNames); 
		
		if (duplicateNames.size() >0){
			int i = 0; 
			while(i < duplicateNames.size()){
				int index1 = duplicateNames.get(i); 
				int index2 = duplicateNames.get(i+1);
				if (childrenBdays.get(index1).equals(childrenBdays.get(index2))){
					error = "Error US25: FAMILY: " + currentfam.getId() + " There are multiple children with same name and birthdate in family: Line: "+getLineNumber();
					flag=1;
					break;
				}
				i +=2; 
			}
		}else{
			error = "US25: All families have unique first names";
		}
		
		return error;
	}
	public static ArrayList<Integer> distinctValues(ArrayList<String> arr){
		ArrayList<Integer> duplicateIndexes = new ArrayList<Integer>(); 
	    for (int i = 0; i < arr.size()-1; i++) {
	        for (int j = i+1; j < arr.size(); j++) {
	             if (arr.get(i).equals(arr.get(j))) {
	                 duplicateIndexes.add(i); 
	                 duplicateIndexes.add(j); 
	             }
	        }
	    }              
	    return duplicateIndexes;         
	}
	// Sprint 4 - Ruchika Sutariya - User Story 29 : List deceased
	public static void listdeceased(ArrayList<indi> indArray, ArrayList<fam> famArray) {
		ArrayList<indi> finalList = new ArrayList<>();
		for (int i = 0; i < indArray.size(); i++) {

			indi currentindiindex = indArray.get(i);
			String currentindiID = indArray.get(i).getId();
			Date deathdate = indArray.get(i).getDeath();
			String name = indArray.get(i).getName();
			if (deathdate != null) {
				finalList.add(currentindiindex);
			}
		}
		System.out.println("US 29: List out deceased Individuals");
		for (int i = 0; i < finalList.size(); i++) {
			
			System.out.println(finalList.get(i).getId() + " - " + finalList.get(i).getName());
		}
	}
	// Sprint 4 - Ruchika Sutariya - User Story 35: List recent births
	public static void listrecentbirths(ArrayList<indi> indArray, ArrayList<fam> famArray) {
		ArrayList<indi> finalList = new ArrayList<>();
		for (int i = 0; i < indArray.size(); i++) {

			indi currentindiindex = indArray.get(i);
			String currentindiID = indArray.get(i).getId();
			Calendar calendar = Calendar.getInstance();
			Date now = new Date();
			calendar.add(Calendar.DATE, -30);
			Date predate = calendar.getTime();
			Date birthdate = currentindiindex.getBirth();
			int marrmonth = birthdate.getMonth();
			int marrdate = birthdate.getDate();
			calendar.clear();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			calendar.set(year, marrmonth, marrdate);
			Date birthdate1 = calendar.getTime();
			if ((birthdate1.after(predate)) && (birthdate1.before(now))) {
				finalList.add(currentindiindex);
				break;
			}
		}
		System.out.println("US 35: List out Recent birthday of Individuals");
		for (int i = 0; i < finalList.size(); i++) {
			System.out.println(finalList.get(i).getId() + " - " + finalList.get(i).getName());
		}
	}
	//Sprint 4 - Charmi Bhikadiya - User Story 40: Include input line numbers
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	//Sprint 4 - Charmi Bhikadiya - User Story 27: Include individual ages
	public static int getIndiAge(String sDate) {
        SimpleDateFormat dt = new SimpleDateFormat("dd MMM yyyy"); 
	    Date date; 
	    try{
            date = dt.parse(sDate);
            //System.out.println(date);
            Date currentDate = new Date();
            //if (deathcheck=false) {
            long difference = currentDate.getTime() - date.getTime();
            long Hours = difference / (60 * 60 * 1000);
            long Days = Hours / 24; 
            double Year = Days / 365;
            int year = (int) Year;
            //  System.out.println(year);
            return year;
         }
         catch (Exception ex) {}
        return 0;
    }
}
