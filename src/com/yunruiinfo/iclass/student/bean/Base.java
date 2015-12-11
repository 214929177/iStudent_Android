package com.yunruiinfo.iclass.student.bean;

import com.lidroid.xutils.db.annotation.NoAutoIncrement;

public class Base {
	public final static String UTF_8 = "UTF-8";
	@NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
	private int id;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
