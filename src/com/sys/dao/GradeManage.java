package com.sys.dao;

import java.sql.SQLException;
import java.util.List;

import com.sys.model.Grade;

public interface GradeManage {

	public void insert(Grade grade) throws SQLException;
	
	public void update(Grade grade) throws SQLException;
	
	public List<Grade> select() throws SQLException;
	
	public void delete(int gradeIdx) throws SQLException;
}
