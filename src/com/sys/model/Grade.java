package com.sys.model;

/**
 * 직급을 담는 클래스
 * @author AIN
 */
public class Grade {

	private Integer gradeIdx;
	private String gradeName;
	
	public Grade() {}
	
	public Grade(Integer gradeIdx, String gradeName) {
		super();
		this.gradeIdx  = gradeIdx;
		this.gradeName = gradeName;
	}

	public Integer getGradeIdx() {
		return gradeIdx;
	}

	public void setGradeIdx(Integer gradeIdx) {
		this.gradeIdx = gradeIdx;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Grade [gradeIdx=");
		builder.append(gradeIdx);
		builder.append(", gradeName=");
		builder.append(gradeName);
		builder.append("]");
		return builder.toString();
	}

	
		
}
