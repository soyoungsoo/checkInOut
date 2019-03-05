package com.sys.model;

/**
 * 출퇴근 기록을 담는 클래스
 * @author AIN
 */
public class CheckInOut {
	
	private Integer checkIdx;
	private String checkInYear;
	private String checkInMonth;
	private String checkInDay;
	private String checkInHour;
	private String checkInMinute;
	private String checkInIp;
	private String checkOutYear;
	private String checkOutMonth;
	private String checkOutDay;
	private String checkOutHour;
	private String checkOutMinute;
	private String checkMemo;
	private Boolean checkLateTime;
	private Boolean checkOverTime;
	private String checkOutIp;	
	private UserInfo user;	
	
	public CheckInOut() {}

	public CheckInOut(Integer checkIdx, String checkInYear, String checkInMonth, String checkInDay, String checkInHour,
			String checkInMinute, String checkInIp, String checkOutYear, String checkOutMonth, String checkOutDay,
			String checkOutHour, String checkOutMinute, String checkMemo, Boolean checkLateTime, Boolean checkOverTime,
			String checkOutIp, UserInfo user) {
		super();
		this.checkIdx       = checkIdx;
		this.checkInYear    = checkInYear;
		this.checkInMonth   = checkInMonth;
		this.checkInDay     = checkInDay;
		this.checkInHour    = checkInHour;
		this.checkInMinute  = checkInMinute;
		this.checkInIp      = checkInIp;
		this.checkOutYear   = checkOutYear;
		this.checkOutMonth  = checkOutMonth;
		this.checkOutDay    = checkOutDay;
		this.checkOutHour   = checkOutHour;
		this.checkOutMinute = checkOutMinute;
		this.checkMemo      = checkMemo;
		this.checkLateTime  = checkLateTime;
		this.checkOverTime  = checkOverTime;
		this.checkOutIp     = checkOutIp;
		this.user           = user;
	}

	public Integer getCheckIdx() {
		return checkIdx;
	}

	public void setCheckIdx(Integer checkIdx) {
		this.checkIdx = checkIdx;
	}

	public String getCheckInYear() {
		return checkInYear;
	}

	public void setCheckInYear(String checkInYear) {
		this.checkInYear = checkInYear;
	}

	public String getCheckInMonth() {
		return checkInMonth;
	}

	public void setCheckInMonth(String checkInMonth) {
		this.checkInMonth = checkInMonth;
	}

	public String getCheckInDay() {
		return checkInDay;
	}

	public void setCheckInDay(String checkInDay) {
		this.checkInDay = checkInDay;
	}

	public String getCheckInHour() {
		return checkInHour;
	}

	public void setCheckInHour(String checkInHour) {
		this.checkInHour = checkInHour;
	}

	public String getCheckInMinute() {
		return checkInMinute;
	}

	public void setCheckInMinute(String checkInMinute) {
		this.checkInMinute = checkInMinute;
	}

	public String getCheckInIp() {
		return checkInIp;
	}

	public void setCheckInIp(String checkInIp) {
		this.checkInIp = checkInIp;
	}

	public String getCheckOutYear() {
		return checkOutYear;
	}

	public void setCheckOutYear(String checkOutYear) {
		this.checkOutYear = checkOutYear;
	}

	public String getCheckOutMonth() {
		return checkOutMonth;
	}

	public void setCheckOutMonth(String checkOutMonth) {
		this.checkOutMonth = checkOutMonth;
	}

	public String getCheckOutDay() {
		return checkOutDay;
	}

	public void setCheckOutDay(String checkOutDay) {
		this.checkOutDay = checkOutDay;
	}

	public String getCheckOutHour() {
		return checkOutHour;
	}

	public void setCheckOutHour(String checkOutHour) {
		this.checkOutHour = checkOutHour;
	}

	public String getCheckOutMinute() {
		return checkOutMinute;
	}

	public void setCheckOutMinute(String checkOutMinute) {
		this.checkOutMinute = checkOutMinute;
	}

	public String getCheckMemo() {
		return checkMemo;
	}

	public void setCheckMemo(String checkMemo) {
		this.checkMemo = checkMemo;
	}

	public Boolean getCheckLateTime() {
		return checkLateTime;
	}

	public void setCheckLateTime(Boolean checkLateTime) {
		this.checkLateTime = checkLateTime;
	}

	public Boolean getCheckOverTime() {
		return checkOverTime;
	}

	public void setCheckOverTime(Boolean checkOverTime) {
		this.checkOverTime = checkOverTime;
	}

	public String getCheckOutIp() {
		return checkOutIp;
	}

	public void setCheckOutIp(String checkOutIp) {
		this.checkOutIp = checkOutIp;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CheckInOut [checkIdx=");
		builder.append(checkIdx);
		builder.append(", checkInYear=");
		builder.append(checkInYear);
		builder.append(", checkInMonth=");
		builder.append(checkInMonth);
		builder.append(", checkInDay=");
		builder.append(checkInDay);
		builder.append(", checkInHour=");
		builder.append(checkInHour);
		builder.append(", checkInMinute=");
		builder.append(checkInMinute);
		builder.append(", checkInIp=");
		builder.append(checkInIp);
		builder.append(", checkOutYear=");
		builder.append(checkOutYear);
		builder.append(", checkOutMonth=");
		builder.append(checkOutMonth);
		builder.append(", checkOutDay=");
		builder.append(checkOutDay);
		builder.append(", checkOutHour=");
		builder.append(checkOutHour);
		builder.append(", checkOutMinute=");
		builder.append(checkOutMinute);
		builder.append(", checkMemo=");
		builder.append(checkMemo);
		builder.append(", checkLateTime=");
		builder.append(checkLateTime);
		builder.append(", checkOverTime=");
		builder.append(checkOverTime);
		builder.append(", checkOutIp=");
		builder.append(checkOutIp);
		builder.append(", user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}
}