package gedcomParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import gedcomParser.gedcomParser.AgeComparator;
import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint2 {
	//Sprint 2 - Priya Parmar - US - 07 - Less then 150 years old
		public static ArrayList<String> checkIfAgeOver150(ArrayList<indi> indArray){	
			ArrayList<String> errors = new ArrayList<String>();
			for (int j=0; j < indArray.size(); j++) {
	            indi currentspot = indArray.get(j);
	            int age = currentspot.getAge();
	            //System.out.println(age);
	            if (age > 150){
	            	errors.add("Error US07: INDIVIDUAL: " + currentspot.getId() + " has an age " + age + " over 150!") ;
	             }
			}
			return errors;
		}
		
		//Sprint 2 - Priya Parmar - US - 10 - Marriage after 14
		public static ArrayList<String> checkMarriageAfter14(ArrayList<fam> famArray, ArrayList<indi> indiArray){
			ArrayList<String> errors = new ArrayList<>();
			for(int i=0;i<famArray.size();i++){
				Date a = famArray.get(i).getMarriage();
				if(famArray.get(i).getMarriage() != null){
					indi husband = getIndividualById(famArray.get(i).getHusband(), indiArray);
					indi wife = getIndividualById(famArray.get(i).getWife(),indiArray);
					
					int wifeAge =0;
					if(wife!=null){
						wifeAge = getAgeWhenMarried(wife.getBirth(),famArray.get(i).getMarriage());
					}
					int husbandAge = getAgeWhenMarried(husband.getBirth(),famArray.get(i).getMarriage());
					
					if(husbandAge<14){
						errors.add("Error US10: INDIVIDUAL: " + husband.getId() + " was an age " + husbandAge + " when he got married which is less than 14!");
					}
					if(wifeAge<14 && wife!=null){ 
						errors.add("Error US10: INDIVIDUAL: " + wife.getId() + " was an age " + wifeAge + " when she got married which is less than 14!");
					}
				}
				
			}
			return errors;
		}
		
		//Sprint 2 - Ruchika Sutariya -US 18 - Siblings should not marry one another
		public static String checkSiblingShouldnotMarry(ArrayList<indi> indArray, ArrayList<fam> famArray){
			String error ="";
			for(int i=0;i<famArray.size();i++){
				ArrayList<String> children = famArray.get(i).getChildren();
				if(famArray.get(i).getChildren()!=null)
				{
					String familyID=famArray.get(i).getId();	
					if(children.size()>=2)
					{
						for(int j=0;j<children.size();j++){
							indi currentChild = getIndividualById(children.get(j), indArray);
							String name = currentChild.getName();
							for(int k=0;k<famArray.size();k++){
								String gethusband = getIndividualById(famArray.get(k).getHusband(), indArray).getName();
								//String getwife = getIndividualById(famArray.get(k).getWife(), indArray).getName();
								if(name.equals(gethusband)){
									if(famArray.get(k).getWife()!=null){
										String wife = getIndividualById(famArray.get(k).getWife(),indArray).getName();
										for(int l=0;l<children.size();l++){
											String currentName = getIndividualById(children.get(l), indArray).getName();
											if(wife!=null){
												if(wife.equals(currentName)){
													error = "Error US18: FAMILY:"+familyID+" Sibblings shouldn't marry one another ("+famArray.get(k).getHusband()+")";
													return error;
												}
											}
										}
									}
									
								}
							}
						}
					}
				}
			}
			return error;
		}

		//Sprint 2 - Ruchika Sutariya -US 17 - Parents should not marry any of their descendants
		public static String checkNoMarriageDescendent(ArrayList<indi> indArray, ArrayList<fam> famArray){
			String error = "";
			for(int i=0;i<famArray.size();i++)
			{ 		
				 String familyID=famArray.get(i).getId();
				 for(int j=0;j<indArray.size();j++)
				 {
					 String Child = indArray.get(j).getFamc();
					 if(Child!=null){
						 if(Child.equals(familyID))
						 { 
							 String indiID = indArray.get(j).getId();
							 //System.out.println(familyID+"should not marry to descendent"+indiID);
							 error = "Error US17: FAMILY:"+familyID+" Individual should not marry to descendent INDIVIDUAL:"+ indiID;
							 return error;
						 }
					 }
						 
					  
					  
				 }					 
			}
			return error;
		}
		// Sprint 2 - Charmi Bhikadiya - US 19 - First Cousins should not marry
		public static void checkCousinShouldNotMarry(ArrayList<indi> indArray, ArrayList<fam> famArray){
			String lastName1="";
			String lastName2="";
			//System.out.println("These all are cousins of Same Family so should not be marry with each other.");
			for(int i=0;i<indArray.size();i++){
				String indiID=indArray.get(i).getId();
					
				//System.out.println(indArray.get(i).getId());
				for(i=0;i<indArray.size();i++)
				{
					lastName1=indArray.get(i).getName().substring(indArray.get(i).getName().indexOf("/")+1, indArray.get(i).getName().length()-2);
					
					for(int j=i+1;j<indArray.size();j++)
					{
						lastName2=indArray.get(j).getName().substring(indArray.get(j).getName().indexOf("/")+1, indArray.get(j).getName().length()-2);
						
						if(lastName2.equals(lastName1))
						{
							
							indiID=indArray.get(j).getId();
							//System.out.println(indiID);
						}
						
							
					}
					break;
				}
				//System.out.println("These all are cousins of Same Family so should not be marry with each other.");
				
						for(int j=i+1;j<indArray.size();j++)
						{
							//System.out.println(lastName1);
							lastName2=indArray.get(j).getName().substring(indArray.get(j).getName().indexOf("/")+1, indArray.get(j).getName().length()-2);
							//System.out.println(lastName2);
							if(!lastName1.equals(lastName2))
							{
								indiID=indArray.get(j).getId();
								//System.out.println(indiID);
							}
							
						}
						break;	
				}
			for(int i=0;i<famArray.size();i++)
			{
				String indiID1 = "";
				String familyID = famArray.get(i).getId();
				for(i=0;i<indArray.size();i++)
				{
					
					String spouse=indArray.get(i).getFamsp();
					if(familyID.equals(spouse))
					{
						indiID1=indArray.get(i).getId();
						lastName1 = indArray.get(i).getName().substring(indArray.get(i).getName().indexOf("/")+1, indArray.get(i).getName().length()-2);
					}
					break;
				}
				
				String indiID2="";
				for(i=2;i<indArray.size();i++)
				{
					String spouse=indArray.get(i).getFamsp();
					if(familyID.equals(spouse))
					{
						indiID2=indArray.get(i).getId();
						lastName2 = indArray.get(i).getName().substring(indArray.get(i).getName().indexOf("/")+1, indArray.get(i).getName().length()-2);
					}
					
					
				}
				
				if(lastName1.equals(lastName2))
				{
					System.out.println("Error US19:INDIVIDUAL: "+indiID1+" should not marry first cousins "+indiID2);
				}
			}
			
		}
		
		
		//Sprint 2 - Charmi Bhikadiya - US 28 - Order Siblings by age
		public static void orderSiblingsByAge(ArrayList<fam> famArray, ArrayList<indi> indArray){
			
			for(int i=0;i<famArray.size();i++){
				if(!famArray.get(i).getChildren().isEmpty() && famArray.get(i).getChildren().size()>=2){
					ArrayList<indi> orderedSiblings = new ArrayList<>();
					ArrayList<String> siblings = famArray.get(i).getChildren();
					for(int j =0 ;j<siblings.size();j++){
						orderedSiblings.add(getIndividualById(siblings.get(j), indArray));
					}
					gedcomParser ged = new gedcomParser();
					Collections.sort(orderedSiblings,ged.new AgeComparator());
					System.out.println("\nSiblings of family "+famArray.get(i).getId()+":");
					for(int j=0;j<orderedSiblings.size();j++){
						System.out.println(orderedSiblings.get(j).getName()+"has age "+orderedSiblings.get(j).getAge());
					}
				}		
			}
			
		}
		public static indi getIndividualById(String id, ArrayList<indi> indArray){
			indi individual=null;
			for(int i=0;i<indArray.size();i++){
				String indiId = indArray.get(i).getId();
				if(indiId.equals(id)){
					individual = indArray.get(i);
					break;
				}
			}
			return individual;
		}
		static int getAgeWhenMarried(Date first, Date last) {
			Calendar a = getCalendar(first);
		    Calendar b = getCalendar(last);
		    int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || 
		        (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
		        diff--;
		    }
		    return diff;
		}
		public static Calendar getCalendar(Date date) {
		    Calendar cal = Calendar.getInstance(Locale.US);
		    cal.setTime(date);
		    return cal;
		}
}
