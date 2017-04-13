package gedcomParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gedcomParser.gedcomParser.fam;
import gedcomParser.gedcomParser.indi;

public class Sprint4 {

	// Sprint 4 - Ruchika Sutariya - User Story 29 : List deceased
	public static void listdeceased(ArrayList<indi> indArray, ArrayList<fam> famArray) {
		ArrayList<indi> finalList = new ArrayList<>();
		for (int i = 0; i < indArray.size(); i++) {

			indi currentindiindex = indArray.get(i);
			String currentindiID = indArray.get(i).getId();
			Date deathdate = indArray.get(i).getDeath();
			String name = indArray.get(i).getName();
			if (deathdate != null) {
				finalList.add(currentindiindex);
			}
		}
		System.out.println("List out deceased Individuals");
		for (int i = 0; i < finalList.size(); i++) {
			
			System.out.println(finalList.get(i).getId() + " - " + finalList.get(i).getName());
		}
	}

	// Sprint 4 - Ruchika Sutariya - User Story 35: List recent births
	public static void listrecentbirths(ArrayList<indi> indArray, ArrayList<fam> famArray) {
		ArrayList<indi> finalList = new ArrayList<>();
		for (int i = 0; i < indArray.size(); i++) {

			indi currentindiindex = indArray.get(i);
			String currentindiID = indArray.get(i).getId();
			Calendar calendar = Calendar.getInstance();
			Date now = new Date();
			calendar.add(Calendar.DATE, -30);
			Date predate = calendar.getTime();
			Date birthdate = currentindiindex.getBirth();
			int marrmonth = birthdate.getMonth();
			int marrdate = birthdate.getDate();
			calendar.clear();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			calendar.set(year, marrmonth, marrdate);
			Date birthdate1 = calendar.getTime();
			if ((birthdate1.after(predate)) && (birthdate1.before(now))) {
				finalList.add(currentindiindex);
				break;
			}
		}
		System.out.println("List out Recent birthday of Individuals");
		for (int i = 0; i < finalList.size(); i++) {
			System.out.println(finalList.get(i).getId() + " - " + finalList.get(i).getName());
		}
	}
}
