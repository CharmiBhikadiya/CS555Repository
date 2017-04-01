package gedcomParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;
//import gedcomParser.indi;

public class Sprint1 {
	
		
		
		//Sprint 1 - Ruchika Sutariya - User Story 02: Birth before marriage
		public static ArrayList<String> checkBirthBeforeMarriage(ArrayList<indi> indArray,ArrayList<fam> famArray){
			ArrayList<String> errors = new ArrayList<String>();
			for(int fam=0;fam<famArray.size();fam++){
				fam currentfam = famArray.get(fam);
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date marriage = currentfam.getMarriage();
				String husband = currentfam.getHusband(); 
				String wife = currentfam.getWife(); 
				Date hbirth = getBirthDeath(husband, indArray).get(0);
				Date wbirth = getBirthDeath(wife, indArray).get(0);
				String error = ""; 
				if(hbirth != null && marriage != null && hbirth.after(marriage)){
					errors.add("Error US02: FAMILY: " + currentfam.getId() 
					+ ": Married " + dateFormat.format(marriage)
		        	+ " before husband's("+husband+") birth on " + dateFormat.format(hbirth)+"\n");
				}
				if(wbirth != null && marriage != null && wbirth.after(marriage)){
					errors.add("Error US02: FAMILY: " + currentfam.getId() 
					+ ": Married " + dateFormat.format(marriage)
		        	+ " before wife's("+wife+") birth on " + dateFormat.format(wbirth));
				}
			}
			
			return errors;
		}
		//Sprint 1 - Ruchika Sutariya - User Story 09 
		@SuppressWarnings("deprecation")
		public static ArrayList<String> checkBirthBeforeDeathofParents(ArrayList<indi> indArray, ArrayList<fam> famArray){
			ArrayList<String> errors = new ArrayList<String>();
			for(int i=0;i<famArray.size();i++){
				fam currentFamiliy = famArray.get(i);
				String fatherName = currentFamiliy.getHusband();
				String motherName = currentFamiliy.getWife();
				ArrayList<String> children = new ArrayList<String>();
				Date fatherDeath = getDeathDate(fatherName,indArray);
				Date deathMinusNineMonths =null;
				if(fatherDeath!=null){
					Calendar cal = Calendar.getInstance();
					cal.setTime(fatherDeath);
					cal.add(Calendar.MONTH,-9);
					deathMinusNineMonths = cal.getTime();
				}
				
				//fatherDeath.setMonth((fatherDeath.getMonth() - 1 -9) % 12 + 1);;
				Date motherDeath = getDeathDate(motherName, indArray);
				for(int j=0;j<children.size();j++){
					String currentChildId = children.get(j);
					Date currentChildBirth = getBirthDeath(currentChildId, indArray).get(0);
					if(fatherDeath!=null){
						if(currentChildBirth.after(deathMinusNineMonths)){
							errors.add("Error US 09 : FAMILY: Birthdate of child is "+currentChildBirth+" which is before nine months after death of father");
						}
					}else if(motherDeath!=null){
						if(currentChildBirth.after(motherDeath)){
							errors.add("Error US 09 : FAMILY: Birthdate of child is "+currentChildBirth+" which is after death of mother");
						}
					}else{
						continue;
					}
				}
			}
			return errors;
		}
		public static Date getDeathDate(String name, ArrayList<indi> indArray){
			for(int i=0;i<indArray.size();i++){
				indi currentIndi = indArray.get(i);
				if(currentIndi.getName().equals(name)){
					return currentIndi.getDeath();
				}
			}
			return null;
		}
		
		
		public static int getIndexOfChild(String ChildID, ArrayList<indi> individualArray){
			for(int i = 0; i< individualArray.size();i++){
				indi child = individualArray.get(i);
				if(child.getId().equals(ChildID)){
					return i;
				}
			}
			return -1;
		}
		
		
		

		
		public static ArrayList<Date> getBirthDeath(String id, ArrayList<indi> indArray){
			ArrayList<Date> BirthDeath = new ArrayList<Date>();
			Date yourdeath = null;
			Date yourbirth = null;
			for(int i=0; i<indArray.size(); i++){
				indi currentspot = indArray.get(i);
				String current_id = currentspot.getId();
				if(current_id.equals(id)){
					yourdeath= currentspot.getDeath(); 
					yourbirth = currentspot.getBirth(); 
				}
			}
			BirthDeath.add(yourbirth);
			BirthDeath.add(yourdeath);
			return BirthDeath; 
		}
		
}
