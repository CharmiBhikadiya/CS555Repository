package gedcomParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint2 {
	
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
			Calendar a = 
      (first);
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
