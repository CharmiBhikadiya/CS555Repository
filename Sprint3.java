package gedcomParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint3 {
	//Sprint 3 - Priya Parmar - User Story 12
	public static String parentsNotTooOld(ArrayList<indi> indArray, ArrayList<fam> famArray){
		String error="";
		for(int i=0;i<famArray.size();i++){
			fam currentFam = famArray.get(i);
			indi mother = new gedcomParser().getIndividualById(currentFam.getWife(), indArray);
			indi father = new gedcomParser().getIndividualById(currentFam.getHusband(), indArray);
			int motherAge = mother.getAge();
			int fatherAge = father.getAge();
			ArrayList<String> children = currentFam.getChildren();
			for(int j=0;j<children.size();j++){
				indi child = new gedcomParser().getIndividualById(children.get(j), indArray);
				int childAge = child.getAge();
				if(motherAge-childAge>60){
					error = "US12: INDIVIDUAL: "+mother.getId()+" Mother is too old";
					break;
				}else if(fatherAge-childAge>80){
					error = "US12: INDIVIDUAL: "+father.getId()+" Father is too old";
					break;
				}
			}
		}
		return error;
	}
	//Sprint 3 - Priya Parmar - User Story 08
	public static String birthBeforeMarriageOfParents(ArrayList<indi> indArray, ArrayList<fam> famArray){
		String error="";
		for(int i=0;i<famArray.size();i++){
			fam currentFam = famArray.get(i);
			Date mrgDate = currentFam.getMarriage();
			if(mrgDate !=null){
				ArrayList<String> children = currentFam.getChildren();
				for(int j=0;j<children.size();j++){
					indi child = new gedcomParser().getIndividualById(children.get(j), indArray);
					Date birthDate = child.getBirth();
					if(birthDate.before(mrgDate)){
						error = "US08: INDIVIDUAL: "+child.getId()+" has birth date before marriage of parents";
						break;
					}
				}
			}
		}
		return error;
	}
	
	//Sprint 3 - Ruchika Sutariya - User Story 34
	public static void listLargeAgeDifferences(ArrayList<indi> indArray, ArrayList<fam> famArray){
		
	}
	
	//Sprint 3 - Ruchika Sutariya - User Story 39
	public static void listUpcomingAnniversaries(ArrayList<indi> indArray, ArrayList<fam> famArray){
		
	}
	
	//Sprint 3 - Charmi Bhikadiya - User Story 30
	public static void listLivingMarried(ArrayList<indi> indArray, ArrayList<fam> famArray){
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
	public static void listOrphans(ArrayList<indi> indArray, ArrayList<fam> famArray){
		for(int k=0;k<famArray.size();k++){
			fam currentfam = famArray.get(k);
			String husband = currentfam.getHusband(); 
			String wife = currentfam.getWife(); 
			Date hdeath = new gedcomParser(). getBirthDeath(husband, indArray).get(1);
			Date wdeath = new gedcomParser().getBirthDeath(wife, indArray).get(1);
			String note = ""; 
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
	}
}
