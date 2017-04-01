package gedcomParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint3 {

	//Sprint 3 - Ruchika Sutariya - User Story 34
	public static void listLargeAgeDifferences(ArrayList<indi> indArray, ArrayList<fam> famArray){
		ArrayList<fam> listcouple = new ArrayList<>();
		for(int i=0;i<famArray.size();i++)
		{
			fam currentfamindex=famArray.get(i);
			String currentfamilyID=famArray.get(i).getId();
			if(currentfamindex.getMarriage()!=null)
			{
				String husband=currentfamindex.getHusband();
				String husbandname= new gedcomParser().getName(husband, indArray);
			//	System.out.println(husbandname);
				int agehusband= indArray.get(i).getAge();
			//	System.out.println(agehusband);
				String wife=currentfamindex.getWife();
			//	System.out.println(wife);
					for(i=0;i<indArray.size();i++)
					{
						
						String wifename= new gedcomParser().getName(wife, indArray);
						String currentID=indArray.get(i).getId();
						//System.out.println(currentID);
						if(currentID.equals(wife))
						{
							int agewife=indArray.get(i).getAge();
							
							//System.out.println(agewife);
							if((agewife>=(2*agehusband)) ||(agehusband>=(2*agewife)) )
								System.out.println(currentfamilyID);
								listcouple.add(currentfamindex);
								
						}
					}
				}
			
			for( i=0;i<listcouple.size();i++){
				System.out.println(listcouple.get(i).getId());
			}
		}
	}
	
	//Sprint 3 - Ruchika Sutariya - User Story 39
	public static void listUpcomingAnniversaries(ArrayList<indi> indArray, ArrayList<fam> famArray){
		
	}
	
	
	
	
}
