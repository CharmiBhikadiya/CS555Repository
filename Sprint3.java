package gedcomParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint3 {
	static Sprint4 s4 = new Sprint4();
	//Sprint 3 - Priya Parmar - User Story 12
	public static String parentsNotTooOld(ArrayList<indi> indArray, ArrayList<fam> famArray){
		String error="";
		for(int i=0;i<famArray.size();i++){
			if(famArray.get(i).getMarriage() != null){
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
						error = "Error US12: INDIVIDUAL: "+mother.getId()+" Mother is too old: Line: "+s4.getLineNumber();
						break;
					}else if(fatherAge-childAge>80){
						error = "Error US12: INDIVIDUAL: "+father.getId()+" Father is too old: Line: "+s4.getLineNumber();
						break;
					}
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
					if(birthDate!= null){
						if(birthDate.before(mrgDate)){
							error = "Error US08: INDIVIDUAL: "+child.getId()+" has birth date before marriage of parents: Line: "+s4.getLineNumber();
							break;
						}
					}			
				}
			}
		}
		return error;
	}
	
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
								
								error = "US34: Family: "+currentfamilyID+" has largest difference, husband age is "+agehusband+" and wife age is "+agewife+"";
								break;
							}
						}
						
					}
					
				}
		}
		return error;
	}
	
	//Sprint 3 - Ruchika Sutariya - User Story 39
	public String listUpcomingAnniversaries(ArrayList<indi> indArray, ArrayList<fam> famArray){
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
										
										Calendar calendar = Calendar.getInstance();
										Date now = new Date();
										//System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(now));
										calendar.add(Calendar.DATE, 30);
										Date nextdate = calendar.getTime();
										//System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(nextdate));
										Date marriagedate=currentfamindex.getMarriage();
										//System.out.println(marriagedate);
										int marrmonth=marriagedate.getMonth();
										int marrdate=marriagedate.getDate();	
										calendar.clear();
										int year = Calendar.getInstance().get(Calendar.YEAR);
										calendar.set(year, marrmonth, marrdate);
										Date marriagedate1=calendar.getTime();	
										if((marriagedate1.after(now)) &&(marriagedate1.before(nextdate)))
										{
											//System.out.println(currentfamilyID);
											error = "US39: FAMILY:"+currentfamilyID+" has upcoming marriage anniversary(In Next 30 Days).";
											break;
										}
							
									}
							}
						}
			}		
		}
		return error;
	}
	
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
