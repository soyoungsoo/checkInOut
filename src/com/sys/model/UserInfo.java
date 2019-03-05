package com.sys.model;

/**
 * 사용자 정보를 담는 클래스
 * @author AIN
 */
public class UserInfo {

	private Integer userIdx;
	private String userName;
	private String userId;
	private String userPw;
	private boolean deleteState;
	private Grade grade;
	
	public UserInfo() {}

	public UserInfo(Integer userIdx, String userName, String userId, String userPw, boolean deleteState, Grade grade) {
		super();
		this.userIdx     = userIdx;
		this.userName    = userName;
		this.userId      = userId;
		this.userPw      = userPw;
		this.deleteState = deleteState;
		this.grade       = grade;
	}

	public Integer getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(Integer userIdx) {
		this.userIdx = userIdx;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public boolean isDeleteState() {
		return deleteState;
	}

	public void setDeleteState(boolean deleteState) {
		this.deleteState = deleteState;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserInfo [userIdx=");
		builder.append(userIdx);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userPw=");
		builder.append(userPw);
		builder.append(", deleteState=");
		builder.append(deleteState);
		builder.append(", grade=");
		builder.append(grade);
		builder.append("]");
		return builder.toString();
	}

	
}
