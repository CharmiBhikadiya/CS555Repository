package gedcomParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint3 {

	//Sprint 3 - Ruchika Sutariya - User Story 34
	public static String  listLargeAgeDifferences(ArrayList<indi> indArray, ArrayList<fam> famArray){
		String error="";
		for(int i=0;i<famArray.size();i++)
		{
			fam currentfamindex=famArray.get(i);
			String currentfamilyID=famArray.get(i).getId();
			//System.out.println(currentfamilyID);
			if(currentfamindex.getMarriage()!=null)
			{
				
				String husband=currentfamindex.getHusband();
				String husbandname= new gedcomParser().getName(husband, indArray);
				int agehusband= indArray.get(i).getAge();
				//System.out.println(agehusband);
				String wife=currentfamindex.getWife();
					for(int j=0;j<indArray.size();j++)
					{
						String wifename= new gedcomParser().getName(wife, indArray);	
						String currentID=indArray.get(j).getId();
						if(currentID.equals(wife))
						{
							int agewife=indArray.get(j).getAge();
							//System.out.println(agewife);
							if((agewife>=(2*agehusband)) ||(agehusband>=(2*agewife)) )
							{
								
								error = "US34: Family: "+currentfamilyID+" has largest difference, husband age is"+agehusband+" and wife age is"+agewife+"";
								break;
							}
						}
						
					}
					
				}
		}
		return error;
	}
	
	//Sprint 3 - Ruchika Sutariya - User Story 39
	public static String listUpcomingAnniversaries(ArrayList<indi> indArray, ArrayList<fam> famArray){
		String error="";
		for(int i=0;i<famArray.size();i++)
		{
			fam currentfamindex=famArray.get(i);
			String currentfamilyID=famArray.get(i).getId();
			if(currentfamindex.getMarriage()!=null)
			{
				
					String husband=currentfamindex.getHusband();
					String wife =currentfamindex.getWife();
					Date deathhusband=indArray.get(i).getDeath();
					if(deathhusband==null)
						for(int j=0;j<indArray.size();j++)
						{
							String wifeid=indArray.get(j).getId();
							if(wifeid.equals(wife))
							{
								Date deathwife=indArray.get(j).getDeath();
									if(deathwife==null)
									{	
										Date now = new Date();
										Calendar calendar = new GregorianCalendar(/* remember about timezone! */);
										calendar.setTime(now);
										calendar.add(Calendar.DATE, 30);
										Date newdate = calendar.getTime();
										System.out.println(newdate);
				
										Date marriagedate=currentfamindex.getMarriage();
										calendar.setTime(marriagedate);
										System.out.println(marriagedate);
										long difference = Math.abs(marriagedate.getTime()-newdate.getTime()); 
										System.out.println(difference);
									}
							}
						}
			}		
		}
		return error;
	}
	
	
	
	
	
	
}
