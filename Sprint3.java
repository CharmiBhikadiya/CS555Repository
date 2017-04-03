package gedcomParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint3 {
	
	//Sprint 3 - Charmi Bhikadiya - User Story 30
	public static void listLivingMarried(ArrayList<indi> indArray, ArrayList<fam> famArray){
		System.out.println("US30: List of all people who are living and married:");
		ArrayList<indi> finalList = new ArrayList<>();
		for(int i=0;i<indArray.size();i++){
			indi currentIndi = indArray.get(i);
			if(!currentIndi.getFams().isEmpty()){
				if(currentIndi.getDeath() == null){
					finalList.add(currentIndi);
				}
			}
		}
		for(int i=0;i<finalList.size();i++){
			System.out.println(finalList.get(i).getName());
		}
	}
  
  //Sprint 3 - Charmi Bhikadiya - User Story 33
	public static String listOrphans(ArrayList<indi> indArray, ArrayList<fam> famArray){
		String note = ""; 
		for(int k=0;k<famArray.size();k++){
			fam currentfam = famArray.get(k);
			String husband = currentfam.getHusband(); 
			String wife = currentfam.getWife(); 
			ArrayList<Date> chk = new gedcomParser(). getBirthDeath(husband, indArray);
			ArrayList<Date> chk1 = new gedcomParser(). getBirthDeath(wife, indArray);
			Date hdeath = new gedcomParser(). getBirthDeath(husband, indArray).get(1);
			Date wdeath = new gedcomParser().getBirthDeath(wife, indArray).get(1);
			
			if(hdeath != null && wdeath != null){
				ArrayList<String> children = currentfam.getChildren();
				for(int i = 0; i < children.size(); i++){
					for(int j = 0; j < indArray.size(); j++){
						if(indArray.get(j).id.equals(children.get(i))){
							int age = indArray.get(j).age;
							if (age < 18){
								note = "US33: Child " + children.get(i) + " in family " + currentfam.id + " is orphaned.\n";
								break;
							}
						}
					}
				}
				
			}
		}
		return note;
	}
	
}
