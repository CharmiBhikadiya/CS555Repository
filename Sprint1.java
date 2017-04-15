package gedcomParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;
//import gedcomParser.indi;

public class Sprint1 {
	static Sprint4 s4 = new Sprint4();
	//Sprint 1 - Priya Parmar - User Story 01
		public static ArrayList<String> checkIndiDateBeforeCurrentDate(ArrayList<indi> indArray){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			ArrayList<String> errors = new ArrayList<String>(); 
			for (int j=0; j < indArray.size(); j++) {
	            indi currentspot = indArray.get(j);
	            Date birth = currentspot.getBirth();
	            Date death = currentspot.getDeath();
	            Date currentDate = new Date();
				  if( birth != null && currentDate.before(birth)){
	            	
	            	errors.add("Error US01: INDIVIDUAL: " + currentspot.getId()+ " has a birth date " + dateFormat.format(birth) + " before " + dateFormat.format(currentDate)+" : Line : "+s4.getLineNumber());
	            }
				  else if(death != null && currentDate.before(death)){
					  
		            	errors.add("Error US01: INDIVIDUAL: " + currentspot.getId()+ " has a death date " + dateFormat.format(death) + " before " + dateFormat.format(currentDate)+" : Line : "+s4.getLineNumber()) ;
		            }
		            
	        }
			return errors;
		}
		//Sprint 1 - Priya Parmar - User Story 01 
		public static ArrayList<String> checkFamMarriageBeforeDate( ArrayList<fam> famArray){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			ArrayList< String> errors = new ArrayList<String>();
			int errorCount = 0;
			for(int j=0;j<famArray.size();j++){
				fam currentFam = famArray.get(j);		
				Date marriage = currentFam.getMarriage();
				Date divorce = currentFam.getDivorce();         
		         Date currentDate = new Date();
					  if(marriage != null && currentDate.before(marriage)){
		         	
						  errors.add("Error US01: FAMILY: " + currentFam.getId()+ " has a marriage date "+ dateFormat.format(marriage) + " before " + dateFormat.format(currentDate)+" : Line : "+s4.getLineNumber()) ;
						 
					  	}
					  else if( divorce != null && currentDate.before(divorce)){
				         	
				         	errors.add("Error US01: FAMILY: " + currentFam.getId()+ " has a divorce date "+ dateFormat.format(divorce) + " before " + dateFormat.format(currentDate)+" : Line : "+s4.getLineNumber());
				         	    	
					  }			  
			}

			return errors;
			
		}
		//Sprint 1 - Priya Parmar - UserStory 03 : Birth Before Death 
		public static ArrayList<String> checkBirthBeforeDeath(ArrayList<indi> indArray){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			ArrayList<String> errors = new ArrayList<String>();
			for (int j=0; j < indArray.size(); j++) {
	            indi currentspot = indArray.get(j);
	            Date birth = currentspot.getBirth();
	            Date death = currentspot.getDeath(); 
	            if(death != null && birth != null && death.before(birth)){
	            	//System.out.println("Error US03: INDIVIDUAL: " + currentspot.getId()
	            	//+ " died on " + dateFormat.format(death) + " before birth on " + dateFormat.format(birth));
	            	errors.add("Error US03: INDIVIDUAL: " + currentspot.getId()+ " died on " + dateFormat.format(death) + " before birth on " + dateFormat.format(birth)+" : Line : "+s4.getLineNumber()) ;
	            }
	        }
			return errors;
		}
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
		        	+ " before husband's("+husband+") birth on " + dateFormat.format(hbirth)+" : Line : "+s4.getLineNumber()+"\n");
				}
				if(wbirth != null && marriage != null && wbirth.after(marriage)){
					errors.add("Error US02: FAMILY: " + currentfam.getId() 
					+ ": Married " + dateFormat.format(marriage)
		        	+ " before wife's("+wife+") birth on " + dateFormat.format(wbirth)+" : Line : "+s4.getLineNumber());
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
							errors.add("Error US 09 : FAMILY: Birthdate of child is "+currentChildBirth+" which is before nine months after death of father : Line : "+s4.getLineNumber());
						}
					}else if(motherDeath!=null){
						if(currentChildBirth.after(motherDeath)){
							errors.add("Error US 09 : FAMILY: Birthdate of child is "+currentChildBirth+" which is after death of mother : Line : "+s4.getLineNumber());
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
								errors.add("Error US16: FAMILY:"+famArray.get(i).getId()+" INDIVIDUAL:"+ indArray.get(k).getId()+"The last name of all male members should be "+lastName+" : Line : "+s4.getLineNumber());
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
			            	errors.add("Error US13: Sibling " + sibling1.getId() + " and " + sibling2.getId() + " have birthdays less than 9 months apart : Line : "+s4.getLineNumber());		            }
				}
			}
			
			return errors;
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
