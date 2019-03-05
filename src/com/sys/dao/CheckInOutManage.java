package com.sys.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.sys.model.CheckInOut;


/**
 * 출퇴근 관리 인터페이스
 * @author AIN
 */
public interface CheckInOutManage {

	public void Insert(CheckInOut check) throws SQLException, ParseException;
	
	public String select(CheckInOut check) throws SQLException;
	
	public CheckInOut SingleSelect(CheckInOut check) throws SQLException;
	
	public List<CheckInOut> defaultSelectAll(String year, String month, String day) throws SQLException;
	
	public List<CheckInOut> ConditionSelectAll(String employee, String startDate, String endDate, String lastState, String lateState) throws SQLException;
	
	public void update(CheckInOut check) throws SQLException, ParseException;
}