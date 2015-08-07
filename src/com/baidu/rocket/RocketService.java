package com.baidu.rocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RocketService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//创建服务
	@Override
	public void onCreate() {
		super.onCreate();
		
		RocketToast mRtoast = new RocketToast(this);
		mRtoast.showRocket();
	}
	
	
	//销毁服务
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
