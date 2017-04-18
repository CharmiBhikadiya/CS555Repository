


//Sprint 4 - Charmi Bhikadiya - User Story 40: Include input line numbers
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	//Sprint 4 - Charmi Bhikadiya - User Story 27: Include individual ages
	public static int getIndiAge(String sDate) {
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
