package com.yunruiinfo.iclass.student.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.bean.User;
import com.yunruiinfo.iclass.student.util.CyptoUtils;
import com.yunruiinfo.iclass.student.util.JsonUtils;
import com.yunruiinfo.iclass.student.util.StringUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class LoginActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.left_button) 	private ImageView	mBack;			
	@ViewInject(R.id.login)			private Button		mLogin;
    @ViewInject(R.id.message)		private TextView	mMessage;		
    @ViewInject(R.id.username)		private EditText	mUserName;		//用户名
    @ViewInject(R.id.password)		private EditText	mPassword;		//密码

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);	
		mBack.setImageResource(R.drawable.ic_header_back);
	}
	
	private void doLogin() {
		String username = mUserName.getText().toString().trim();
		final String password = mPassword.getText().toString().trim();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			UIHelper.ToastMessage(LoginActivity.this, "学号或密码不能为空！");
		} else {
			HttpUtils http = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("type", "login");
			params.addBodyParameter("username", username);
			params.addBodyParameter("password", password);
			http.send(HttpMethod.POST, URLs.API_URL, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException error, String msg) {
					mMessage.setText("登录失败...");
					UIHelper.ToastMessage(LoginActivity.this, msg);
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						mMessage.setText("登录失败...");
						UIHelper.ToastMessage(LoginActivity.this, result.Message());
						return;
					} else {
						mMessage.setText("登录成功...");
						
						User user = result.user;
						user.setPassword(CyptoUtils.encode("APPICLASS", password));
						appContext.saveUserInfo(result.user);
						
						UIHelper.showHome(LoginActivity.this);
						finish();
					}
				}
				
				@Override
				public void onStart() {
					mMessage.setText("正在登录...");
				}
			});
		}		
	}
	
	@OnClick({R.id.left_button, R.id.login})
	@Override
	public void onClick(View v){
		switch (v.getId()){
		case R.id.left_button:
			finish();
			break;
		case R.id.login:
			doLogin();					 
			break;
		}
	}
}