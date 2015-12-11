package com.yunruiinfo.iclass.student.bean;

import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;

/**
 * 通讯结果实体类
 * @author SYZ
 */
public class Result {
	
	public User user;											//用户
	public Course course;										//课程信息
	public List<Course> courses = Collections.emptyList();		//课程列表
	public List<Notice> notices = Collections.emptyList();		//通知公告
	public List<Resourse> resourses = Collections.emptyList();	//课程资料
	
	public Update version;										//升级信息
	public List<Advert> ads = Collections.emptyList();			//顶部轮播
	public List<QauNews> news_list = Collections.emptyList();	//顶部轮播

	private int status;
	private String message;
	
	public boolean OK() {
		return status == 1;
	}
	public String Message() {
		return message;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public String toString(){
		return String.format("RESULT: CODE:%d,MSG:%s", status, message);
	}

}
