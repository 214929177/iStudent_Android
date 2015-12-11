package com.yunruiinfo.iclass.student.bean;

import java.io.Serializable;

import android.app.Activity;
import android.support.v4.app.Fragment;

@SuppressWarnings("serial")
public class Icon implements Serializable {
	public int icon;
	public String name;
	public Class<? extends Activity> activity;
	public Class<? extends Fragment> fragment;
	public Serializable object;
	public Icon(String name, int icon, Class<Activity> activity) {
		this.icon = icon;
		this.name = name;
		this.activity = activity;
	}
	public Icon(String name, int icon, Class<? extends Fragment> fragment, Serializable object) {
		this.icon = icon;
		this.name = name;
		this.fragment = fragment;
		this.object = object;
	}
}
