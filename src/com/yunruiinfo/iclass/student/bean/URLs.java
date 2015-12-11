package com.yunruiinfo.iclass.student.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.yunruiinfo.iclass.student.util.CyptoUtils;


/**
 * 接口URL实体类
 * @author SYZ
 * @version 1.0
 * @created 2013
 */
public class URLs {
	public final static String UTF_8 = "UTF-8";
	
	//网络教学平台部分
	public static final String HOST = "http://210.44.48.33:8080/";//"http://ketang.yunruiinfo.com/";
	public static final String API_URL = HOST + "iclass/api";						//接口地址
	public static final String BASE_URL = "http://jiaoxue.qau.edu.cn/";			//网页基准路径
	public static final String TIMETABLE = HOST + "iclass/timetable/login";		//课程表
	public static final String ASK_TEACHER = HOST + "iclass/chat/login";			//问道老师
	//教学平台网页部分
	//登录跳转地址
	public static final String EOL_LOGIN = "http://jiaoxue.qau.edu.cn/eol/homepage/common/opencourse/login.jsp";
	//网上讨论区
	public static final String EOL_COURSE_DISCUSS = "http://jiaoxue.qau.edu.cn/eol/common/forum/forum/catalog.jsp";
	//教学材料 lid=课程ID
	public static final String EOL_COURSE_FILES = "http://jiaoxue.qau.edu.cn/eol/common/script/listview.jsp?folderid=0&lid=";
	//问道老师 lid=课程ID
	//public static final String EOL_ASK_TEACHER = "http://jiaoxue.qau.edu.cn/eol/lesson/exchange_mail_stud.jsp?lid=";
	
	//运营平台部分
	public static final String YR_HOST = "http://115.28.189.220/";
	public static final String YR_API = YR_HOST + "lcop/api";							//运营接口地址
	public static final String FEED_BACK = YR_HOST + "lcop/user/feedback";			//意见反馈
	public static final String WELCOME = YR_HOST + "lcop/advert/home";				//欢迎图片
	public static final String RECOMMEND = YR_HOST + "lcop/recommend/index.html";		//学习推荐
	public static final String TEST_ONLINE = YR_HOST + "laq/stu";						//在线测试
	
	/**
	 * URL加参数
	 * @param p_url
	 * @param params
	 * @return
	 */
	public static String makeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			try {
				// url.append(String.valueOf(params.get(name)));
				// URLEncoder处理
				url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return url.toString().replace("?&", "?");
	}
	
	/**
	 * 获取登录跳转链接
	 * @param url
	 * @return
	 */
	public static String getLoginUrl(String url, User user) {
		String pwd = CyptoUtils.decode("APPICLASS", user.getPassword());
		String loginUrl = URLs.EOL_LOGIN + "?IPT_LOGINUSERNAME=" + user.getUsername();
		loginUrl += "&IPT_LOGINPASSWORD=" + pwd;
		try {
			loginUrl += "&IPT_URL=" + URLEncoder.encode(url, Base.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return loginUrl;
	}
}
