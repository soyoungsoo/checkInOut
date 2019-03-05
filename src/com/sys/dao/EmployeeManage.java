package com.sys.dao;

import java.sql.SQLException;
import java.util.List;

import com.sys.model.UserInfo;

public interface EmployeeManage {

	public void insert(UserInfo user) throws SQLException;
	
	public void update(UserInfo user) throws SQLException;
	
	public UserInfo select(UserInfo user) throws SQLException;
	
	public List<UserInfo> selectList() throws SQLException;
	
	public void delete(int userIdx) throws SQLException;
}
