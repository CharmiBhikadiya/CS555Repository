package gedcomParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint2 {
	
		
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
		
		
}
