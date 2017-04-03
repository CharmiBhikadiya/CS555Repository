package gedcomParser;
import java.beans.FeatureDescriptor;
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
import java.time.LocalDate;
import java.time.Period;

import javax.swing.text.ChangedCharSetException;

import gedcomParser.gedcomParser.indi;

public class gedcomParser {
	
	public static class indi {
		String id; //0
		String name; //1 
		String sex; //1 
		Date birth; //1&2 
		Date death; //1&2 
		String family_child; //1 
		ArrayList<String> family_spouse = new ArrayList<String>(); //1
		int age = 0;
		
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
			indi_spouse = fams;
			this.family_spouse.add(fams);
		}
		public ArrayList<String> getFams(){
			return family_spouse; 
		}
		public void setAge(int age) {
			this.age = age;
		}
		public int getAge() {
	        	return age;
		}
		String indi_spouse;
		public String getFamsp(){	
			return indi_spouse; 
		}
	}
	class AgeComparator implements Comparator<indi> {
		@Override
		public int compare(indi o1, indi o2) {
		    if (o1.getAge() > o2.getAge()) {
		        return -1;
		    } else if (o1.getAge() < o2.getAge()) {
		        return 1;
		    }
		    return 0;
		}
	}
	
	public static class fam {
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
				int Age = getIndiAge(arg);
				int lastIndiElem;
				int lastFamElem;
				indi currentIndi;
				fam currentFam;
					if(birthcheck == true){
						lastIndiElem = individualArray.size()-1;
						currentIndi = individualArray.get(lastIndiElem);
						currentIndi.setBirth(thisDate);
						currentIndi.setAge(Age);
						birthcheck = false;
					}
					if(deathcheck == true){
						lastIndiElem = individualArray.size()-1;
						currentIndi = individualArray.get(lastIndiElem);
						currentIndi.setDeath(thisDate);
						deathcheck = false; 
					}
					if(marriagecheck == true){
						if(arg != null){
							lastFamElem = familyArray.size()-1;
							currentFam = familyArray.get(lastFamElem);
							currentFam.setMarriage(thisDate);
							marriagecheck = false; 
						}
						
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
		System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-15s  %-11s %n", 
				"ID", "Name", "Sex", "Birthday", "Death", "Child", "Spouse","Age");
		for(int i=0; i<indArray.size(); i++){
			indi currentspot = indArray.get(i);
			Date birth = currentspot.getBirth(); 
			Date death = currentspot.getDeath();
			String indiAge=Integer.toString(currentspot.getAge());
			if (birth != null && death != null && birth!= death){
				long difference = death.getTime() - birth.getTime();
                long hours = difference / (60 * 60 * 1000);
                long days = hours / 24; 
                double year = days / 365;
                int yr = (int) year;
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-15s  %-11s  %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), dateFormat.format(birth), 
					dateFormat.format(death), currentspot.getFamc(), currentspot.getFams(), yr);
			}
			if (birth != null && death == null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-15s  %-11s %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), dateFormat.format(birth), 
					death, currentspot.getFamc(), currentspot.getFams(), indiAge);
			}
			if (birth == null && death != null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-11s  %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), birth, 
					dateFormat.format(death), currentspot.getFamc(), currentspot.getFams());
			}
			if (birth == null && death == null){
				System.out.printf("%-11s  %-30s   %-11s  %-12s   %-12s  %-11s  %-15s  %-11s %n", 
					currentspot.getId(), currentspot.getName(), currentspot.getSex(), birth, 
					death, currentspot.getFamc(), currentspot.getFams(), indiAge="0");
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
		System.out.println("Sprint 1:");
		Sprint1 s1 = new Sprint1();
		//Priya Parmar - User Story 01 - Sprint 1 for individual
		ArrayList<String> errorsIndividual = s1.checkIndiDateBeforeCurrentDate(indArray);
		for(int j=0; j<errorsIndividual.size();j++){
			System.out.println(errorsIndividual.get(j));
		}
		
		//Priya Parmar - User Story 01 - Sprint 1 for family
		ArrayList<String> errorsFamily = s1.checkFamMarriageBeforeDate(famArray);
		for(int j=0;j<errorsFamily.size();j++){
			System.out.println(errorsFamily.get(j));
		}
		//Priya Parmar - User Story 03 - Sprint 1
		ArrayList<String> errorsIndiBirth = s1.checkBirthBeforeDeath(indArray);
		for(int j=0;j<errorsIndiBirth.size();j++){
			System.out.println(errorsIndiBirth.get(j));
		}
		//Ruchika Sutariya - User Story 02 - Sprint 1
		ArrayList<String> errorsBirthBeforeMrg = s1.checkBirthBeforeMarriage(indArray, famArray);
		System.out.println(errorsBirthBeforeMrg.get(0));
		//Ruchika Sutariya  - User Story 09 - Sprint 1
		ArrayList<String> errorsBirthBeforeDeathOfParents = s1.checkBirthBeforeDeathofParents(indArray, famArray);
		for(int j=0;j<errorsBirthBeforeDeathOfParents.size();j++){
			System.out.println(errorsBirthBeforeDeathOfParents.get(j));
		}
		//Charmi Bhikadiya -User Story 13 - Sprint 1
		ArrayList<String> errorsSiblingSpacing = s1.compareSiblingSpacing(indArray,famArray);
		System.out.println(errorsSiblingSpacing.get(0));
		//Charmi Bhikadiya - User Story 16 - Sprint 1
		ArrayList<String> errorsMaleNames = s1.checkMaleLastNames(indArray, famArray);
		for(int j=0;j<errorsMaleNames.size();j++){
			System.out.println(errorsMaleNames.get(j));
		}
		System.out.println("Sprint 2:");
		Sprint2 s2 = new Sprint2();
		//Priya Parmar - User Story 07 - Sprint 2
		ArrayList<String> errorsOver150 = s2.checkIfAgeOver150(indArray);
		for(int j=0;j<errorsOver150.size();j++){
			System.out.println(errorsOver150.get(j));
		}
		//Priya Parmar - User Story 10 - Sprint 2
		ArrayList<String> errorsMrgless14 = s2.checkMarriageAfter14(famArray, indArray);
		System.out.println(errorsMrgless14.get(3));
		//Ruchika Sutariya - User Story 17 - Sprint 2
		System.out.println(s2.checkNoMarriageDescendent(indArray, famArray));
		//Ruchika Sutariya - User Story 18 - Sprint 2
		System.out.println(s2.checkSiblingShouldnotMarry(indArray, famArray));
		//Charmi Bhikadiya - User Story 19 - Sprint 2
		s2.checkCousinShouldNotMarry(indArray, famArray);
		//Charmi Bhikadiya - User Story 28 - Sprint 2
		System.out.println("US28: Order Siblings by Age for each family");
		s2.orderSiblingsByAge(famArray, indArray);
		
		System.out.println("\nSprint 3:");
		Sprint3 s3 = new Sprint3();
		System.out.println(s3.parentsNotTooOld(indArray, famArray));
		System.out.println(s3.birthBeforeMarriageOfParents(indArray, famArray));
		System.out.println(s3.listLargeAgeDifferences(indArray, famArray)); 
		s3.listLivingMarried(indArray, famArray);
		System.out.println(s3.listOrphans(indArray, famArray)); 
	}
	
	
	
	
	//US 27
	static int getIndiAge(String sDate) {
        SimpleDateFormat dt = new SimpleDateFormat("dd MMM yyyy"); 
	    Date date; 
	    try{
            date = dt.parse(sDate);
            //System.out.println(date);
            Date currentDate = new Date();
            //if (deathcheck=false) {
            long difference = currentDate.getTime() - date.getTime();
            long Hours = difference / (60 * 60 * 1000);
            long Days = Hours / 24; 
            double Year = Days / 365;
            int year = (int) Year;
            //  System.out.println(year);
            return year;
         }
         catch (Exception ex) {}
        return 0;
    }
	public static fam getFamilyById(String id, ArrayList<fam> famArray){
		fam family = null;
		for(int i=0;i<famArray.size();i++){
			String famId = famArray.get(i).getId();
			if(famId.equals(id)){
				family = famArray.get(i);
				break;
			}
		}
		return family;
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
				break;
			}
		}
		BirthDeath.add(yourbirth);
		BirthDeath.add(yourdeath);
		return BirthDeath; 
	}
	public static void main(String[] args) throws IOException{
		ArrayList<indi> individualArray = new ArrayList<indi>();
		ArrayList<fam> familyArray = new ArrayList<fam>();
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the gedcom file path with '\\' you wish evaluate: ");
		//String file = in.nextLine();
		String file ="D:\\Stevens\\Semester 3\\Agile\\Sprint 3\\gedcomParser\\src\\gedcomParser\\sprint3-4.ged";
		in.close();
		parseFile(file, individualArray, familyArray);
		printIndiAndFamData(individualArray, familyArray);
	}
}
