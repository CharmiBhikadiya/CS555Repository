package gedcomParser;
import static org.junit.Assert.*;

import junit.framework.TestCase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import gedcomParser.gedcomParser.indi;
import gedcomParser.gedcomParser.fam;
public class US01TestCases extends TestCase{
	private indi i1; 
	private indi i2; 
	private fam f1; 
	ArrayList<indi> individualArray = new ArrayList<indi>();
	ArrayList<fam> familyArray = new ArrayList<fam>();
	protected void setUp(){
		i1 = new indi("one");
		i2 = new indi("two");
		f1 = new fam("one");
		i1.setBirth(gedcomParser.stringToDate("17 JUN 2018"));
		i1.setDeath(gedcomParser.stringToDate("17 JUN 2017"));
		individualArray.add(i1); 
		individualArray.add(i2);
		f1.setHusband(i1.id);
		f1.setWife(i2.id);
		f1.setMarriage(gedcomParser.stringToDate("13 MAR 2018"));
		familyArray.add(f1);
	}
	

	@Test
	public void testMarriageBeforeDeath(){
		ArrayList<String> result = gedcomParser.checkFamMarriageBeforeDate(familyArray);
		Assert.assertEquals(result.get(0), "Error US01: FAMILY: one has a marriage date 03/13/2018 before 02/27/2017");
				
	}
	public void testindiBirthBeforeDeath(){
		ArrayList<String> result= gedcomParser.checkIndiDateBeforeCurrentDate(individualArray); 
		Assert.assertEquals(result.get(0), "Error US01: INDIVIDUAL: one has a birth date 06/17/2018 before 02/27/2017");
	}
	
	public void testBirthBeforeDeath(){
		ArrayList<String> result = gedcomParser.checkBirthBeforeDeath(individualArray);
		Assert.assertEquals(result.get(0), "Error US03: INDIVIDUAL: one died on 06/17/2017 before birth on 06/17/2018");
	}
}