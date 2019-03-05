package com.sys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sys.model.Grade;
import com.sys.util.Close;
import com.sys.util.Connector;

public class GradeManageImpl implements GradeManage, Close{
	
	private Connection conn      = null;	
	private PreparedStatement ps = null;
	private ResultSet rs         = null;
	
	private String insert     = "insert into grade(gradeName) values(?)";
	private String selectList = "select * from grade";	
	private String update     = "update grade set gradeName = ? where gradeIdx = ?";
	private String delete     = "delete from grade where gradeIdx = ?";
	
	@Override
	public void insert(Grade grade) throws SQLException {	
		conn = new Connector().getConnection();
		
		try {	
			ps = conn.prepareStatement(insert);
			ps.setString(1, grade.getGradeName());        // 직급
			ps.executeUpdate();
		} finally {
			closed();
		}
	}

	@Override
	public void update(Grade grade) throws SQLException  {
		conn = new Connector().getConnection();
		
		try {	
			ps = conn.prepareStatement(update);
			ps.setString(1, grade.getGradeName());        // 직급
			ps.setInt(2, grade.getGradeIdx());            // 직급 인덱스			
			ps.executeUpdate();
		} finally {
			closed();
		}
	}

	@Override
	public List<Grade> select() throws SQLException  {
		conn = new Connector().getConnection();

		List<Grade> list = new ArrayList<>();

		try {
			ps = conn.prepareStatement(selectList);
			rs = ps.executeQuery();
			while (rs.next()) {
				Grade grade 	 = new Grade();
				int idx          = rs.getInt("gradeIdx");
				String gradeName = rs.getString("gradeName");

				if (idx != 0) grade.setGradeIdx(idx);                     // 직급
				if (gradeName != null) grade.setGradeName(gradeName);     // 직급 인덱스

				list.add(grade);
			}
		} finally {
			closed();
		}
		return list;
	}

	@Override
	public void delete(int gradeIdx) throws SQLException {		
		conn = new Connector().getConnection();
		
		try {
			ps = conn.prepareStatement(delete);
			ps.setInt(1, gradeIdx); // 직급 인덱스
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
