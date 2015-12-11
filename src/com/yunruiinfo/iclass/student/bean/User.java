package com.yunruiinfo.iclass.student.bean;

public class User extends Base {
	
	private String username;		//用户名
	private String realname;		//真实姓名
	private String email;			//Email
	private String phone;			//电话
	private String mobile;			//手机
	private String gender;			//性别：1-男 2-女 其他-未知
	private String numbStudent;	//学号
	private String depName;		//院系
	private String majorName;		//专业
	private String gradTitle;		//年级
	private String className;		//班级
	private String summary;		//个人简介
	private String password;		//密码
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNumbStudent() {
		return numbStudent;
	}
	public void setNumbStudent(String numbStudent) {
		this.numbStudent = numbStudent;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getMajorName() {
		return majorName;
	}
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	public String getGradTitle() {
		return gradTitle;
	}
	public void setGradTitle(String gradTitle) {
		this.gradTitle = gradTitle;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
