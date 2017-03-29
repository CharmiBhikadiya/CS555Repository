package gedcomParser;

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
}
