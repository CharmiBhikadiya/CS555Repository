/*
 * Team submission:
 * 
 * Priya Parmar
 * Ruchika Sutariya
 * Charmi Bhikadiya
 */
import java.io.*;
import java.util.*;
import java.text.*;

public class gedcomParser {
	
	static class indi {
		String id; //0
		String name; //1 
		String sex; //1 
		Date birth; //1&2 
		Date death; //1&2 
		String family_child; //1 
		ArrayList<String> family_spouse = new ArrayList<String>(); //1
		
		public indi(String id){
			this.id = id; 
		}
		public String getId(){
			return id; 
		}
		public void setName(String name){
			this.name = name;
		}
		public String getName(){
			return name; 
		}
		public void setSex(String sex){
			this.sex = sex;
		}
		public String getSex(){
			return sex; 
		}
		public void setBirth(Date birth){
			this.birth = birth;
		}
		public Date getBirth(){
			return birth; 
		}
		public void setDeath(Date death){
			this.death = death;
		}
		public Date getDeath(){
			return death; 
		}
		public void setFamc(String famc){
			this.family_child = famc;
		}
		public String getFamc(){
			return family_child; 
		}
		public void setFams(String fams){
			this.family_spouse.add(fams);
		}
		public ArrayList<String> getFams(){
			return family_spouse; 
		}
	}
	
	static class fam {
		String id; //0 
		Date marriage; //1&2
		String husband; //1
		String wife; //1
		ArrayList<String> children = new ArrayList<String>(); //1
		Date divorce; //1&2 
		
		public fam(String id){
			this.id = id; 
		}
		public String getId(){
			return id; 
		}
		public void setMarriage(Date marriage){
			this.marriage = marriage;
		}
		public Date getMarriage(){
			return marriage; 
		}
		public void setHusband(String husband){
			this.husband = husband; 
		}
		public String getHusband(){
			return husband; 
		}
		public void setWife(String wife){
			this.wife = wife; 
		}
		public String getWife(){
			return wife; 
		}
		public void setChildren(String child){
			this.children.add(child);
		}
		public ArrayList<String> getChildren(){
			return children; 
		}
		public void setDivorce(Date divorce){
			this.divorce = divorce;
		}
		public Date getDivorce(){
			return divorce; 
		}
	}
	
	static Date stringToDate(String sDate){
		SimpleDateFormat dt = new SimpleDateFormat("dd MMM yyyy"); 
		Date date; 
		try{
			date = dt.parse(sDate);
			return date; 
		}
		catch (ParseException e){
			return null;
		}
	}
	
	//array of valid tags 
	static String[] valid0tags = {"INDI","FAM","HEAD","TRLR", "NOTE"};
	static String[] valid1tags = {"INDI","NAME","SEX","BIRT","DEAT","FAMC","FAMS","FAM","MARR",
			"HUSB","WIFE","CHIL","DIV","HEAD","TRLR", "NOTE"};
	
	public static boolean isValidTag(String tag, int level){
		boolean valid = false;
		if (level == 0){
			for(String s: valid0tags){
				if (s.equals(tag)){
					valid= true; 
				} 
			}
		}
		if (level == 1){
			for(String s: valid1tags){
				if (s.equals(tag)){
					valid= true; 
				} 
			}
		}
		if (level == 2 && tag.equals("DATE")){
				valid= true; 
			} 
		return valid; 
	}
	/*
	
	public static void printLineLevelTag(String line, int level, String tag){
		System.out.println("Line: "+line); 
		System.out.println("Level: "+level);
		if (isValidTag(tag, level)){
			System.out.println("Tag: "+tag+"\n");
		} else System.out.println("Tag: Invalid tag\n");
	}*/
	
	public static void parseFile(String filename, ArrayList<indi> indArray, ArrayList<fam> famArray) throws IOException{
		//open file 
				FileReader fr = new FileReader(filename);
				try(BufferedReader br= new BufferedReader(fr)){
					 
					String line; 
					int level; 
					String tag; 
					String thirdArg; 
					
					//read each line of the file until end of file
					while ((line =br.readLine()) != null) {
						
						//split line on spaces
						String split[] = line.split(" ");
						
						//set level to the first string, parse to integer 
						level = Integer.parseInt(split[0]);
						
						//for all non-0 level lines, the tag always follows the level number, so set tag to the second string
						if (level != 0){
							tag=split[1];
							if(split.length >3){
								String[] split2 = split.clone();
								/*split2[0] = "";
								split2[1]="";*/
								split2 = Arrays.copyOfRange(split2, 2, split2.length);
								StringBuilder builder = new StringBuilder();
								for(String s : split2) {
								    builder.append(s+" ");
								}
								thirdArg = builder.toString();
							}
							else if (split.length == 3){
								thirdArg = split[2];
							}
							else {thirdArg = "";}
						} 
						//when level is 0, either second or last part can be tag, check if second part is a valid tag, if not, set third part as tag
						else{ 
							if(isValidTag(split[1], 0)){
								tag = split[1]; 
								thirdArg = "";
							} else { 
								tag= split[2];
								thirdArg= split[1];
							}
						}
						if (isValidTag(tag, level)){
							storeData(level,tag,thirdArg, indArray, famArray);
						}
						
						//print data 
						//printLineLevelTag(line,level,tag);
					}//end while 
				}//end try 
	}
	
	static boolean birthcheck = false; 
	static boolean deathcheck = false; 
	static boolean marriagecheck = false; 
	static boolean divorcecheck = false; 
	
	public static void storeData(int level, String tag, String arg, ArrayList<indi> individualArray, ArrayList<fam> familyArray){
		if(level ==0){
			if (tag.equals("INDI")){
				indi newindividual = new indi(arg); 
				individualArray.add(newindividual);
			}
			else if (tag.equals("FAM")){
				fam newfamily = new fam(arg); 
				familyArray.add(newfamily);
			}
			else {} //do nothing, don't care about it
		}
		if(level == 1){
				int lastIndiElem1;
				int lastFamElem1;
				indi currentIndi1;
				fam currentFam1;
				//Indi stuff 
				if(tag.equals("NAME")){ 
					lastIndiElem1 = individualArray.size()-1;
					currentIndi1 = individualArray.get(lastIndiElem1);
					currentIndi1.setName(arg);
				}
				if(tag.equals("SEX")){ 
					lastIndiElem1 = individualArray.size()-1;
					currentIndi1 = individualArray.get(lastIndiElem1);
					currentIndi1.setSex(arg);
				}
				if(tag.equals("BIRT")){ birthcheck = true; }
				if(tag.equals("DEAT")){ deathcheck = true; }
				if(tag.equals("FAMC")){ 
					lastIndiElem1 = individualArray.size()-1;
					currentIndi1 = individualArray.get(lastIndiElem1);
					currentIndi1.setFamc(arg);
				}
				if(tag.equals("FAMS")){ 
					lastIndiElem1 = individualArray.size()-1;
					currentIndi1 = individualArray.get(lastIndiElem1);
					currentIndi1.setFams(arg);
				}
				//Fam stuff 
				if(tag.equals("MARR")){ marriagecheck = true; } 
				if(tag.equals("HUSB")){ 
					lastFamElem1 = familyArray.size()-1;
					currentFam1 = familyArray.get(lastFamElem1);
					currentFam1.setHusband(arg);
				}
				if(tag.equals("WIFE")){ 
					lastFamElem1 = familyArray.size()-1;
					currentFam1 = familyArray.get(lastFamElem1);
					currentFam1.setWife(arg);
				}
				if(tag.equals("CHIL")){ 
					lastFamElem1 = familyArray.size()-1;
					currentFam1 = familyArray.get(lastFamElem1);
					currentFam1.setChildren(arg);
				}
				if(tag.equals("DIV")){  divorcecheck = true; }
		}
		if(level == 2){//tag will always be date 
				Date thisDate = stringToDate(arg);
				int lastIndiElem;
				int lastFamElem;
				indi currentIndi;
				fam currentFam;
				if(birthcheck == true){
					lastIndiElem = individualArray.size()-1;
					currentIndi = individualArray.get(lastIndiElem);
					currentIndi.setBirth(thisDate);
					birthcheck = false; 
				}
				if(deathcheck == true){
					lastIndiElem = individualArray.size()-1;
					currentIndi = individualArray.get(lastIndiElem);
					currentIndi.setDeath(thisDate);
					deathcheck = false; 
				}
				if(marriagecheck == true){
					lastFamElem = familyArray.size()-1;
					currentFam = familyArray.get(lastFamElem);
					currentFam.setMarriage(thisDate);
					marriagecheck = false; 
				}
				if(divorcecheck == true){
					lastFamElem = familyArray.size()-1;
					currentFam = familyArray.get(lastFamElem);
					currentFam.setDivorce(thisDate);
					divorcecheck = false; 
				}
		}
	}
	
	public static void printIndiAndFamData(ArrayList<indi> indArray, ArrayList<fam> famArray){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		System.out.println("People");
		System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-11s  %n", 
				"ID", "Name", "Sex", "Birthday", "Death", "Child", "Spouse");
		for(int i=0; i<indArray.size(); i++){
			indi currentspot = indArray.get(i);
			Date birth = currentspot.getBirth(); 
			Date death = currentspot.getDeath();
			if (birth != null && death != null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-11s  %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), dateFormat.format(birth), 
					dateFormat.format(death), currentspot.getFamc(), currentspot.getFams());
			}
			if (birth != null && death == null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-11s  %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), dateFormat.format(birth), 
					death, currentspot.getFamc(), currentspot.getFams());
			}
			if (birth == null && death != null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-11s  %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), birth, 
					dateFormat.format(death), currentspot.getFamc(), currentspot.getFams());
			}
			if (birth == null && death == null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-11s  %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), birth, 
					death, currentspot.getFamc(), currentspot.getFams());
			}
		}
			
		System.out.println("\nFamilies");
		System.out.printf("%-11s  %-12s   %-12s  %-30s   %-30s  %-30s  %n", 
				"ID", "Marriage", "Divorce", "Husband", "Wife", "Children");
		for(int i=0; i<famArray.size(); i++){
			fam currentspot = famArray.get(i);
			Date mar = currentspot.getMarriage();
			Date div = currentspot.getDivorce();
			if (mar != null && div != null){
				System.out.printf("%-11s  %-12s   %-12s  %-30s   %-30s  %-30s  %n", 
					currentspot.getId(), dateFormat.format(mar), dateFormat.format(div), getName(currentspot.getHusband(),indArray), 
					getName(currentspot.getWife(),indArray), currentspot.getChildren());
			}
			if (mar != null && div == null){
				System.out.printf("%-11s  %-12s   %-12s  %-30s   %-30s  %-30s  %n", 
					currentspot.getId(), dateFormat.format(mar), div, getName(currentspot.getHusband(),indArray), 
					getName(currentspot.getWife(),indArray), currentspot.getChildren());
			}
			if (mar == null && div != null){
				System.out.printf("%-11s  %-12s   %-12s  %-30s   %-30s  %-30s  %n", 
					currentspot.getId(), mar, dateFormat.format(div), getName(currentspot.getHusband(),indArray), 
					getName(currentspot.getWife(),indArray), currentspot.getChildren());
			}
			if (mar == null && div == null){
				System.out.printf("%-11s  %-12s   %-12s  %-30s   %-30s  %-30s  %n", 
					currentspot.getId(), mar, div, getName(currentspot.getHusband(),indArray), 
					getName(currentspot.getWife(),indArray), currentspot.getChildren());
			}
		}
	}
	
	public static String getName(String id, ArrayList<indi> indArray){
		String yourname = "";
		for(int i=0; i<indArray.size(); i++){
			indi currentspot = indArray.get(i);
			String current_id = currentspot.getId();
			if(current_id.equals(id)){
				yourname= currentspot.getName(); 
			}
		}
		return yourname; 
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
	
	
	//Sprint 2 - Ruchika Sutariya -US 17 - Parents should not marry any of their descendants
	public static void ChkNoMarriageDescendent(ArrayList<indi> indArray, ArrayList<fam> famArray){
	for(int i=0;i<famArray.size();i++)
	{
		
		Date marriagedate1=famArray.get(i).getMarriage();
		//System.out.println(marriagedate1);
		
		for(int j=i+1;j<famArray.size();j++)
		{
			Date marriagedate2=famArray.get(j).getMarriage();
			//System.out.println(marriagedate2);
			 if(marriagedate1!=null && marriagedate2!=null)
			 if(marriagedate1.before(marriagedate2))
			 {
				 String familyID1=famArray.get(j).getId();
				 String familyID=famArray.get(i).getId();
				 System.out.println(familyID+" should not marry to descendant"+familyID1);
			 }
			 
			
		}
		
	}
	
}
	
	//Sprint 2 - Ruchika Sutariya -US 18 - Siblings should not marry one another
	public static void SiblingShouldnotMarry(ArrayList<indi> indArray, ArrayList<fam> famArray){
		
		for(int i=0;i<famArray.size();i++){
			ArrayList<String> children = famArray.get(i).getChildren();
			
			if(famArray.get(i).getChildren()!=null)
			{
				String familyID=famArray.get(i).getId();
				
				if(children.size()>=2)
				{
					indi husband = getIndividualById(famArray.get(i).getHusband(), indArray);
					indi wife = getIndividualById(famArray.get(i).getWife(),indArray);
					String mothername=wife.getName();
					String fathername=husband.getName();
					
					
				System.out.println(children+" are Siblings who has same mother" + mothername + "and father" + fathername + "so they should not marry with each other.");
				
				}
				else
					System.out.println("There is only one child");
				
			}
		}
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
	
	public static void main(String[] args) throws IOException{
		ArrayList<indi> individualArray = new ArrayList<indi>();
		ArrayList<fam> familyArray = new ArrayList<fam>();
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the gedcom file path with '\\' you wish evaluate: ");
		String file = in.nextLine();
		in.close();
		parseFile(file, individualArray, familyArray);
		//parseFile("D:\\Stevens\\Semester 3\\Agile\\CS555 Agile\\New folder\\gedcomParser\\src\\Project3.ged", individualArray, familyArray);
		printIndiAndFamData(individualArray, familyArray);
	}
	
}
