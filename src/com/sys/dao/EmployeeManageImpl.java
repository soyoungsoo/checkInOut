package com.sys.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sys.model.Grade;
import com.sys.model.UserInfo;
import com.sys.util.Close;
import com.sys.util.Connector;

public class EmployeeManageImpl implements EmployeeManage, Close {

	private Connection conn      = null;
	private PreparedStatement ps = null;
	private ResultSet rs         = null;
	
	private String insert     = "insert into userInfo(userName, userId, UserPw, deleteState, gradeIdx) values(?,?,?,?,?)";
	private String selectList = "select userIdx, userName, userId, userPw, u.gradeIdx, gradeName from userInfo u,grade g "
			+ "where u.gradeIdx = g.gradeIdx AND deleteState = false order by u.gradeIdx asc";
	private String select     = "select * from userInfo where userName = ? AND userId = ? AND userPw = ? AND gradeIdx = ?";
	private String update     = "update userInfo set userName = ?,userId = ?, userPw = ?, gradeIdx = ? where userIdx = ?";
	private String delete     = "update userInfo set deleteState = true where userIdx = ?";

	@Override
	public void insert(UserInfo user) throws SQLException {
		conn = new Connector().getConnection();

		try {				
			ps = conn.prepareStatement(insert);
			ps.setString(1, user.getUserName());                   // 이름
			ps.setString(2, user.getUserId());                     // 아이디
			ps.setString(3, user.getUserPw());                     // 비밀번호
			ps.setBoolean(4, false);                               // 삭제 여부
			ps.setInt(5, user.getGrade().getGradeIdx());           // 직급 인덱스
			ps.executeUpdate();
		} finally {
			closed();
		}
	}

	@Override
	public void update(UserInfo user) throws SQLException {
		conn = new Connector().getConnection();
		
		try {
			ps = conn.prepareStatement(update);
			ps.setString(1, user.getUserName());                   // 이름
			ps.setString(2, user.getUserId());                     // 아이디
			ps.setString(3, user.getUserPw());                     // 비밀번호
			ps.setInt(4, user.getGrade().getGradeIdx());           // 직급 인덱스
			ps.setInt(5, user.getUserIdx());                       // 유저 인덱스
			ps.executeUpdate();
		} finally {
			closed();
		}
	}
	
	@Override
	public UserInfo select(UserInfo user) throws SQLException {
		conn = new Connector().getConnection();
		UserInfo userInfo = new UserInfo();
		Grade grade       = new Grade();
		try {
			ps = conn.prepareStatement(select);
			ps.setString(1, user.getUserName());                   // 이름
			ps.setString(2, user.getUserId());                     // 아이디
			ps.setString(3, user.getUserPw());                     // 비밀번호
			ps.setInt(4, user.getGrade().getGradeIdx());           // 직급 인덱스			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				userInfo.setUserIdx(rs.getInt("userIdx"));
				userInfo.setUserId(rs.getString("userId"));
				userInfo.setUserPw(rs.getString("userPw"));
				userInfo.setUserName(rs.getString("userName"));
				grade.setGradeIdx(rs.getInt("gradeIdx"));				
				userInfo.setGrade(grade);
			}
		} finally {
			closed();
		}
		return userInfo;
	}
	
	@Override
	public List<UserInfo> selectList() throws SQLException {
		conn = new Connector().getConnection();
		
		List<UserInfo> list = new ArrayList<>();
		
		try {
			ps = conn.prepareStatement(selectList);
			rs = ps.executeQuery();
			while (rs.next()) {
				UserInfo user = new UserInfo();
				Grade grade   = new Grade();

				grade.setGradeIdx(rs.getInt("gradeIdx"));
				grade.setGradeName(rs.getString("gradeName"));

				user.setGrade(grade);
				user.setUserIdx(rs.getInt("userIdx"));
				user.setUserName(rs.getString("userName")); 
				user.setUserId(rs.getString("userId")); 
				user.setUserPw(rs.getString("userPw"));

				list.add(user);
			}
		} finally {
			closed();
		}
		return list;
	}

	@Override
	public void delete(int userIdx) throws SQLException {
		conn = new Connector().getConnection();
		
		try {
			ps = conn.prepareStatement(delete);
			ps.setInt(1, userIdx); // 유저 인덱스
			ps.executeUpdate();			
		} finally {
			closed();
		}
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
}