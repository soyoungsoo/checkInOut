package com.sys.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class utilDay {

	public String getDateDay(String date, String dateType) throws Exception {

		String day = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
		Date nDate = dateFormat.parse(date);

		Calendar cal = Calendar.getInstance();
		cal.setTime(nDate);

		int dayNum = cal.get(Calendar.DAY_OF_WEEK);
		
		switch (dayNum) {
		
			case 1:
				day = "일";
				break;
			case 2:
				day = "월";
				break;
			case 3:
				day = "화";
				break;
			case 4:
				day = "수";
				break;
			case 5:
				day = "목";
				break;
			case 6:
				day = "금";
				break;
			case 7:
				day = "토";
				break;
		}

		return day;
	}

}
