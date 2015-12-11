package com.yunruiinfo.iclass.student.bean;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "course")
public class Resourse extends Base {
	private String url;
	private String name;
	private String type;
	private long size;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
