package com.sys.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCheck {

	private String lateCheck = "지각";	
	private String inTime= "09:00";
	private String outTime = "18:00";	
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private long diff = 0;
	private long overHour = 0;
	private long overMinute = 0;
	private String defaultTime = "00:00";
	private String result = "";
	private Date standardInTime = null;
	private Date standardOutTime = null;
	
	public TimeCheck(){
		try {
			this.standardInTime = sdf.parse(inTime);
			this.standardOutTime = sdf.parse(outTime);
		} catch (ParseException e) {		
			e.printStackTrace();
		}
	}
	public boolean Check(String hour, String minute, String exp) throws ParseException{
		
		Date checkTime = exp.equals(lateCheck) ? standardInTime : standardOutTime;
		if (hour == null || minute == null) return false;
		
		Date time = sdf.parse(hour + ":" + minute);
		diff = checkTime.getTime() - time.getTime();

		if (diff > -1) {
			return false;
		} else {
			return true;
		}
	}

	public String lateCheck(String inTime) throws ParseException{

		if (inTime == null || inTime.equals("null:null")) return defaultTime;

		Date checkTime = sdf.parse(inTime); 

		// 지각 체크
		if(standardInTime.getTime() < checkTime.getTime()) {
			diff = checkTime.getTime() - standardInTime.getTime();
		} else {			
			return defaultTime;
		}
		overHour = diff / (1000 * 60 * 60);
		overMinute = diff / (1000 * 60) - overHour * 60;
		result = overHour + ":" + overMinute;

		return sdf.format(sdf.parse(result));
	}

	public String workAndOverCheck(String checkIn, String checkOut, String type) throws ParseException{
		
		if (checkOut == null || checkOut.equals("")) return defaultTime;

		Date checkinTime = sdf.parse(checkIn).getTime() > standardInTime.getTime() ? sdf.parse(checkIn) : standardInTime;
		Date checkoutTime = sdf.parse(checkOut);

		diff = checkoutTime.getTime() - checkinTime.getTime();
		
		overHour = diff / (1000 * 60 * 60);
		overHour = (overHour < 0) ? 00 : overHour;	
		overMinute = diff / (1000 * 60) - (overHour * 60);
				
		switch (type) {
			case "근무시간":
				// 휴게시간 1시간 제외
				result = ((overHour >= 4) ? (overHour - 1) : overHour) + ":" + overMinute;		
				return sdf.format(sdf.parse(result));	
			case "야근":
				if ((overHour - 1) <= 7) return defaultTime;
				
				// 휴게 시간 + 근로시간 제외
				result = overHour - 9 + ":" + overMinute;		
				return sdf.format(sdf.parse(result));
		}
		return defaultTime;
	}
}
