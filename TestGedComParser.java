package gedcomParser;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TestGedComParser {

	@Test
	public void testchk1()
	{
		long diffTime=100000;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);	
			assertTrue(diffDays < 2 || diffDays > 243);
			System.out.println("difference between two child should be greater than 8 month or less than 2 days ");
		
		
	}
	
	@Test
	
	public void testchk2()
	{
		gedcomParser ged=new gedcomParser();
		assertNotNull("Parse Tree should not be null",ged);
		
		
	}
	
	@Test
	
	public void testchk3()
	{
		Date childBirthday1 = new Date();
		assertNotNull("Birthdate is not Null",childBirthday1 );
	}
	
	@Test
	public void testchk4()
	{
		long chiltime1=100000;
		long chiltime2 = 50000;
		assertNotEquals("BirthTime of child1 and child 2 should be different.",chiltime2 , chiltime1);
	}
	
	@Test
	public void testchk5()
	{
		SimpleDateFormat BirthDate1=new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat BirthDate2=new SimpleDateFormat("dd MMM yyyy");
		
		assertEquals("Birthdate Format should be same.",BirthDate1, BirthDate2);
		
	}

}
