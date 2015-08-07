package com.baidu.rocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	//点击按钮开启服务
	public void startService(View view) {
		
		startService(new Intent(this, RocketService.class));
	}
	 
	//点击按钮关闭服务
	public void stopService(View view) {
		
		stopService(new Intent(this, RocketService.class));
		
	}
}
