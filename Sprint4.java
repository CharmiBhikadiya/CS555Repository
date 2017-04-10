package gedcomParser;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

import java.util.ArrayList;
import java.util.Date;

public class Sprint4 {
	public static void checkUniqueIndiId(indi i, ArrayList<indi> individuals) {
        String indId = i.getId();
        
        for (int j = 0; j < individuals.size(); j++) {
            String id = individuals.get(j).getId();
            if (id.equals(indId) != individuals.get(j).equals(j)) {
                System.out.println("Error US22: Check Unique ID: Individual "+ i.getName().replace("/", "") +" (" +id +") " +"has duplicate ID");
            }
        }//System.out.println("every Individual IDs are Unique!!"); 
        
        
    }
	public static void checkUniqueFamId(fam f, ArrayList<fam> families) {
		String famId = f.getId();
		for (int j = 0; j < families.size(); j++) {
            String id = families.get(j).getId();
            if (families.get(j).equals(f) != id.equals(famId)) {
                System.out.println("Error US22: Check Unique ID: Family with Husband "+  f.getHusband() + "and Wife  " + f.getWife() +" (" +id +") " +"has duplicate ID");
            } 
        }//System.out.println("every Family IDs are Unique!!");
	}
	public static String checkUniqueChildren(ArrayList<indi> indArray, fam currentfam){
			
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
					break;
				}
				i +=2; 
			}
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
	
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}
