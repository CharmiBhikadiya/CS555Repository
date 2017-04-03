package gedcomParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;
//import gedcomParser.indi;

public class Sprint1 {
//Sprint 1 - Charmi Bhikadiya - User Story 16 : Male Members lastnames
		public static ArrayList<String> checkMaleLastNames(ArrayList<indi> indArray, ArrayList<fam> famArray){
			ArrayList<String> errors = new ArrayList<String>();
			for(int i=0;i<famArray.size();i++){
				String husbandId = famArray.get(i).getHusband();
				String lastName="";
				ArrayList<String> children = new ArrayList<String>();
				children = famArray.get(i).getChildren();
				for(int j=0;j<indArray.size();j++){
					if(indArray.get(j).getId().equals(husbandId)){
						lastName=indArray.get(j).getName().substring(indArray.get(j).getName().indexOf("/")+1, indArray.get(j).getName().length()-2);
						break;
						//System.out.println(lastName);
					}
				}
				for(int j=0;j<children.size();j++){
					for(int k=0;k<indArray.size();k++){
						if(children.get(j).equals(indArray.get(k).getId())){
							String sex = indArray.get(k).getSex();
							String indiName = indArray.get(k).getName();
							String indiLName = indiName.substring(indiName.indexOf("/")+1,indiName.length()-2);
							if(!lastName.equals(indiLName)
									&& indArray.get(k).getSex().equals("M")){
								errors.add("Error US16: FAMILY:"+famArray.get(i).getId()+" INDIVIDUAL:"+ indArray.get(k).getId()+"The last name of all male members should be "+lastName);
							}
						}
					}
				}
				
			}
			return errors;
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
		//Sprint 1 - Charmi Bhikadiya - User Story 13:Sibling Spacing
		public class dateAndId {            //class to help get 2 array of Id and birth date for children
			String Id;
			Date date;
			
			private dateAndId (String Id, Date date) {
				this.Id = Id;
				this.date = date;
			}
			
			private Date getDate() { return this.date; }
			
			private String getId() { return this.Id; }
		}
		
		public static ArrayList<String> compareSiblingSpacing(ArrayList<indi> indArray, ArrayList<fam> famArray){
			String error = "";
			fam famspot = null;
			indi currentspot = null;
			ArrayList<String> errors = new ArrayList<String>();
			ArrayList<dateAndId> siblingBirthDates = new ArrayList<>();
			for (fam famArray1 : famArray){
				famspot = famArray1;
				for (indi indArray1 : indArray){
					 currentspot = indArray1;
	                 String spot = currentspot.getId();
	                 ArrayList<String> children = famspot.getChildren();
	                 if (children.contains(spot) && currentspot.getBirth() != null) {
	            		siblingBirthDates.add(new Sprint1().new dateAndId(currentspot.getId(), currentspot.getBirth()));	
	                }
				}
			}
				
			for (dateAndId sibling1 : siblingBirthDates) {
				for (dateAndId sibling2 : siblingBirthDates) {
					long diff = Math.abs(sibling1.getDate().getTime() - sibling2.getDate().getTime());
					long Hours = diff / (60 * 60 * 1000);
			            long diffDays = Hours / 24;
			            long diffMonths = diffDays / 30;
			            
			            //System.out.println(sibling1.getId() + " " + sibling2.getId());
			            //System.out.println(sibling1.getDate() + " " + sibling2.getDate());
			            
			            if (diffDays > 2  && sibling1.getId() != sibling2.getId()){
			            	//errors.add("Error US13: Sibling " + sibling1.getId() + " and " + sibling2.getId() + " have birthdays more than 2 days apart");		            
			            	}
			            if (diffMonths < 9 && sibling1.getId() != sibling2.getId()){
			            	errors.add("Error US13: Sibling " + sibling1.getId() + " and " + sibling2.getId() + " have birthdays less than 9 months apart");		            }
				}
			}
			
			return errors;
		}

		
		 
		 
		 
		 
		 
		 
		 
	

}  
