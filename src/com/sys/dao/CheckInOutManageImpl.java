package com.sys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.sys.model.CheckInOut;
import com.sys.model.Grade;
import com.sys.model.UserInfo;
import com.sys.util.Close;
import com.sys.util.Connector;
import com.sys.util.TimeCheck;

public class CheckInOutManageImpl implements CheckInOutManage, Close{

	private Connection conn      = null;	
	private PreparedStatement ps = null;	
	private ResultSet rs         = null;
	private TimeCheck timeCheck  = new TimeCheck();

	private String insertCheckIn  = "insert into checkInOut(checkInYear,checkInMonth,checkInDay,checkInHour,checkInMinute,checkInIp,checkLateTime,userIdx) values(?,?,?,?,?,?,?,?)";
	private String insertCheckOut = "update checkInOut set checkOutYear = ?,checkOutMonth = ?, checkOutDay = ?,checkOutHour = ?,checkOutMinute = ?,checkOutIp = ?,checkOverTime = ?"
			+ " where userIdx = ? AND checkInYear = ? AND checkInMonth = ? AND checkInDay = ?";
	private String selectMulti    = "select * from (select  u.userIdx, u.userName, u.userId, u.userPw, g.gradeName from userInfo u,grade g where u.gradeIdx = g.gradeIdx) u,"
			+ " checkInOut c where u.userIdx = c.userIdx AND checkInYear = ? AND checkInMonth = ? AND checkInDay = ? order by checkInYear desc, "
			+ "checkInMonth desc, checkInDay desc, checkInHour asc, checkInMinute asc";
	private String selectSingle   = "select * from checkInOut where userIdx = ? AND checkInYear = ? AND checkInMonth = ? AND checkInDay = ?";

	@Override
	public void Insert(CheckInOut check) throws SQLException, ParseException {
		conn = new Connector().getConnection();

		String hour   = check.getCheckInHour();
		String minute = check.getCheckInMinute();

		try {
			rs = executeSelectSingle(check);
			if (!rs.next()) {
				ps = conn.prepareStatement(insertCheckIn);
				ps.setString(1, check.getCheckInYear());                             // 년
				ps.setString(2, check.getCheckInMonth());                            // 월
				ps.setString(3, check.getCheckInDay());                              // 일
				ps.setString(4, hour);                                               // 시
				ps.setString(5, minute);                                             // 분
				ps.setString(6, check.getCheckInIp());                               // 출근 IP
				ps.setBoolean(7, timeCheck.Check(hour, minute, "지각"));              // 지각 여부
				ps.setInt(8, check.getUser().getUserIdx());                          // 유저 인덱스				
			} else {
				if(rs.getString("checkOutHour") == null) {
					ps = conn.prepareStatement(insertCheckOut);				
					ps.setString(1, check.getCheckInYear());                             // 년
					ps.setString(2, check.getCheckInMonth());                            // 월
					ps.setString(3, check.getCheckInDay());                              // 일
					ps.setString(4, hour);                                               // 시
					ps.setString(5, minute);                                             // 분
					ps.setString(6, check.getCheckInIp());                               // 출근 IP				
					ps.setBoolean(7, timeCheck.Check(hour, minute, "야근"));              // 초과 근무 여부
					ps.setInt(8, check.getUser().getUserIdx());                          // 유저 인덱스
					ps.setString(9, check.getCheckInYear());                             // 조회할 년
					ps.setString(10, check.getCheckInMonth());                           // 조회할 월
					ps.setString(11, check.getCheckInDay());                             // 조회할 일				
				} else return;
			}
			ps.executeUpdate();			
		} finally {
			closed();
		}
	}

	@Override
	public String select(CheckInOut check) throws SQLException {				
		rs = executeSelectSingle(check);

		if (rs.next()) {
			if (rs.getString("checkOutHour") == null) {
				return "퇴근 체크";
			}
		}
		else {
			return "출근 체크";		
		}
		return "체크 완료";
	}

	@Override
	public List<CheckInOut> defaultSelectAll(String year, String month, String day) throws SQLException {		
		conn = new Connector().getConnection();

		List<CheckInOut> list = new ArrayList<>();
		ps = conn.prepareStatement(selectMulti);
		ps.setString(1, year);
		ps.setString(2, month);
		ps.setString(3, day);
		rs = ps.executeQuery();
		
		while (rs.next()) {			
			list.add(getCheckInOutInResultSet());
		}
		return list;
	}

	@Override
	public List<CheckInOut> ConditionSelectAll(String employee, String startDate, String endDate, String lastState, String overState) throws SQLException {
		conn = new Connector().getConnection();		
		List<CheckInOut> list = new ArrayList<>();
		Statement stmt = null;
		String sql = "select * from (select u.userIdx, u.userName, u.userId, u.userPw, u.gradeName, c.checkIdx, c.checkInYear, c.checkInMonth, c.checkInDay, c.checkInHour, c.checkInMinute, c.checkInIp, c.checkOutYear,c.checkOutMonth,c.checkOutDay,c.checkOutHour,c.checkOutMinute,c.checkMemo,c.checkOutIp,c.checkLateTime,c.checkOverTime,concat(c.checkInYear,c.checkInMonth,c.checkInDay) d from (select  u.userIdx, u.userName, u.userId, u.userPw, g.gradeName from userInfo u,grade g \r\n" + 
					 "where u.gradeIdx = g.gradeIdx) u, checkInOut c\r\n" + 
					 "where u.userIdx = c.userIdx \r\n" +
					 "order by checkInYear desc, checkInMonth desc, checkInDay desc, checkInHour asc, checkInMinute asc) search where 1=1 ";				

		if (!employee.equals("")) {
			sql += "AND userName ='" + employee + "'" + " ";
		}
		if (!startDate.equals("")) {			
			sql += "AND date('" + startDate + "') <= date(d)" + " ";
		}
		if (!endDate.equals("")) {
			sql += "AND date(d) <= date('" + endDate + "')" + " ";
		}
		if (!lastState.equals("")) {
			sql += "AND checkLateTime = " + (lastState.equals("true") ? true : false) + "" + " ";
		}
		if (!overState.equals("")) {
			sql += "AND checkOverTime = " + (overState.equals("true") ? true : false) + "" + " ";
		}
		stmt = conn.createStatement();
		rs   = stmt.executeQuery(sql);

		while (rs.next()) {			
			list.add(getCheckInOutInResultSet());						
		}				
		return list;
	}
	
	@Override
	public void update(CheckInOut check) throws SQLException, ParseException {		
		conn = new Connector().getConnection();		
		
		String inHour    = check.getCheckInHour();
		String inMinute  = check.getCheckInMinute();
		String outHour   = check.getCheckOutHour();
		String outMinute = check.getCheckOutMinute();
		String update    = "update checkInOut set checkInHour = ?, checkInMinute = ?, checkOutHour = ?, checkOutMinute = ?, checkMemo = ?, "
				+ "checkLateTime = ?, checkOverTime = ?, checkOutYear = checkInYear, checkOutMonth = checkInMonth, checkOutDay = checkInDay";
		
		if (check.getCheckOutIp() != null) {
			update += ", checkOutIp = ? where checkIdx = ?";
		} else {
			update += " where checkIdx = ?";
		}
		ps = conn.prepareStatement(update);
		ps.setString(1, check.getCheckInHour());                        // 출근 시
		ps.setString(2, check.getCheckInMinute());                      // 출근 분
		ps.setString(3, check.getCheckOutHour());                       // 퇴근 시
		ps.setString(4, check.getCheckOutMinute());                     // 퇴근 분
		ps.setString(5, check.getCheckMemo());                          // 메모
		ps.setBoolean(6, timeCheck.Check(inHour, inMinute, "지각"));     // 지각 체크
		ps.setBoolean(7, timeCheck.Check(outHour, outMinute, "야근"));   // 야근 체크
		
		if (check.getCheckOutIp() != null) {
			ps.setString(8, check.getCheckOutIp());
			ps.setInt(9, check.getCheckIdx());                     	    // 출퇴근 인덱스
		} else {
			ps.setInt(8, check.getCheckIdx());                     	    // 출퇴근 인덱스
		}		
		ps.executeUpdate();
	}

	@Override
	public void closed() throws SQLException {
		
		if (rs != null)
			rs.close();
		if (ps != null)
			ps.close();
		if (conn != null)
			conn.close();
	}

	@Override
	public CheckInOut SingleSelect(CheckInOut check) throws SQLException {
		CheckInOut checkInfo = check;
		UserInfo user = new UserInfo();
		rs = executeSelectSingle(checkInfo);

		if (rs.next()) setCheckInOut(checkInfo, user);

		return checkInfo;
	}

	// ResultSet에서 checkInOut 정보를 가져온다.
	private CheckInOut getCheckInOutInResultSet() throws SQLException  {
		CheckInOut check = new CheckInOut();
		UserInfo user    = new UserInfo();
		Grade grade      = new Grade();

		grade.setGradeName(rs.getString("gradeName"));

		user.setGrade(grade);
		user.setUserName(rs.getString("userName"));
		user.setUserId(rs.getString("userId"));
		user.setUserPw(rs.getString("userPw"));

		setCheckInOut(check, user);

		return check;		
	}

	// 전달받은 객체의 각 프로퍼티에 해당하는 DB값 등록 
	private void setCheckInOut(CheckInOut checkInfo, UserInfo user) throws SQLException {				
		user.setUserIdx(rs.getInt("userIdx"));
		checkInfo.setUser(user);

		checkInfo.setCheckIdx(rs.getInt("checkIdx"));
		checkInfo.setCheckInYear(rs.getString("checkInYear"));
		checkInfo.setCheckInMonth(rs.getString("checkInMonth"));
		checkInfo.setCheckInDay(rs.getString("checkInDay"));
		checkInfo.setCheckInHour(rs.getString("checkInHour"));
		checkInfo.setCheckInMinute(rs.getString("checkInMinute"));
		checkInfo.setCheckInIp(rs.getString("checkInIp"));

		checkInfo.setCheckOutYear(rs.getString("checkOutYear"));
		checkInfo.setCheckOutMonth(rs.getString("checkOutMonth"));
		checkInfo.setCheckOutDay(rs.getString("checkOutDay"));
		checkInfo.setCheckOutHour(rs.getString("checkOutHour"));
		checkInfo.setCheckOutMinute(rs.getString("checkOutMinute"));
		checkInfo.setCheckOutIp(rs.getString("checkOutIp"));
		checkInfo.setCheckMemo(rs.getString("checkMemo"));
		checkInfo.setCheckLateTime(rs.getBoolean("checkLateTime"));
		checkInfo.setCheckOverTime(rs.getBoolean("checkOverTime"));			
	}

	private ResultSet executeSelectSingle(CheckInOut check) throws SQLException {			
		conn = new Connector().getConnection();

		PreparedStatement ps = conn.prepareStatement(selectSingle);

		ps.setInt(1, check.getUser().getUserIdx());     // 유저 인덱스
		ps.setString(2, check.getCheckInYear());        // 년
		ps.setString(3, check.getCheckInMonth());       // 월
		ps.setString(4, check.getCheckInDay());         // 일
		return ps.executeQuery();				
	}
}