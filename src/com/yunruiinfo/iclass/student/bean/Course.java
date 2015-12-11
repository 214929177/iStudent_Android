package com.yunruiinfo.iclass.student.bean;

import java.util.List;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "course")
public class Course extends Base {
	private String name;
	private String lessoncat;
	private String tutorname;
	private String intro;
	private String outline;
	private String calendar;
	
	private List<Teacher> teachers;
	private List<Notice> notices;
	
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getOutline() {
		return outline;
	}
	public void setOutline(String outline) {
		this.outline = outline;
	}
	public String getCalendar() {
		return calendar;
	}
	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}
	public List<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLessoncat() {
		return lessoncat;
	}
	public void setLessoncat(String lessoncat) {
		this.lessoncat = lessoncat;
	}
	public String getTutorname() {
		return tutorname;
	}
	public void setTutorname(String tutorname) {
		this.tutorname = tutorname;
	}
	public List<Notice> getNotices() {
		return notices;
	}
	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}
	
	
}
