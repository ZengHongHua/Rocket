package com.baidu.rocket;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class RocketToast implements OnTouchListener {

	private Context mContext;
	private WindowManager mWm;
	private WindowManager.LayoutParams mRocketParams;
	private WindowManager.LayoutParams mTipParams;

	private ImageView bottomImage;
	private ImageView mTv;
	private float startX;
	private float startY;

	private boolean isReady = false;

	// 构造方法
	public RocketToast(Context context) {
		this.mContext = context;

		// 窗体管理者
		mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		// 火箭的参数
		mRocketParams = new WindowManager.LayoutParams();
		mRocketParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mRocketParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mRocketParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mRocketParams.format = PixelFormat.TRANSLUCENT;
		mRocketParams.gravity = Gravity.LEFT | Gravity.TOP;
		// mRocketParams.windowAnimations =
		// com.android.internal.R.style.Animation_Toast;
		mRocketParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		mRocketParams.setTitle("Toast");

		// 发射图片的参数
		mTipParams = new WindowManager.LayoutParams();
		mTipParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mTipParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mTipParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mTipParams.format = PixelFormat.TRANSLUCENT;
		mTipParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		// mTipParams.windowAnimations =
		// com.android.internal.R.style.Animation_Toast;
		mTipParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		mTipParams.setTitle("Toast");
	}

	/**
	 * 显示出小火箭
	 */
	public void showRocket() {
		hideRocket();
		mTv = new ImageView(mContext);

		// 火箭按下时候的操作事件
		mTv.setOnTouchListener(this);

		// 设置图片
		AnimationDrawable rocket = new AnimationDrawable();
		rocket.addFrame(
				mContext.getResources().getDrawable(
						R.drawable.desktop_rocket_launch_1), 100);
		rocket.addFrame(
				mContext.getResources().getDrawable(
						R.drawable.desktop_rocket_launch_2), 100);
		rocket.setOneShot(false);

		mTv.setImageDrawable(rocket);
		rocket.start();

		mWm.addView(mTv, mRocketParams);
	}

	/**
	 * 显示发射区域的图片
	 */
	private void showTip() {
		hideTip();
		bottomImage = new ImageView(mContext);
		AnimationDrawable shoot = new AnimationDrawable();
		shoot.addFrame(
				mContext.getResources().getDrawable(
						R.drawable.desktop_bg_tips_1), 200);
		shoot.addFrame(
				mContext.getResources().getDrawable(
						R.drawable.desktop_bg_tips_2), 200);
		shoot.setOneShot(false);

		bottomImage.setImageDrawable(shoot);
		shoot.start();

		mWm.addView(bottomImage, mTipParams);

	}

	// 隐藏小火箭
	private void hideRocket() {
		if (bottomImage != null) {
			if (bottomImage.getParent() != null) {
				mWm.removeView(bottomImage);
			}
			bottomImage = null;
		}
	}

	// 隐藏发射区域
	private void hideTip() {
		if (bottomImage != null) {
			if (bottomImage.getParent() != null) {
				mWm.removeView(bottomImage);
			}
			bottomImage = null;
		}
	}

	// 移动的时时火箭
	private void offsetRocket(float diffX, float diffY) {

		mRocketParams.x += diffX;
		mRocketParams.y += diffY;

		// 更新UI
		mWm.updateViewLayout(mTv, mRocketParams);
	}

	private void setTipState(boolean isReady) {

		if (isReady) {
			bottomImage.setImageResource(R.drawable.desktop_bg_tips_3);
		} else {
			AnimationDrawable bg = new AnimationDrawable();
			bg.addFrame(
					mContext.getResources().getDrawable(
							R.drawable.desktop_bg_tips_1), 50);
			bg.addFrame(
					mContext.getResources().getDrawable(
							R.drawable.desktop_bg_tips_2), 50);
			bg.setOneShot(false);
			bottomImage.setImageDrawable(bg);

			bg.start();
		}
	}

	// 是否准备就绪
	private boolean checkReady() {
		// 1. 获得火箭在屏幕中的坐标
		int[] rocketLocation = new int[2];
		mTv.getLocationOnScreen(rocketLocation);
		// Log.d(TAG, "火箭的x:" + rocketLocation[0]);
		// Log.d(TAG, "火箭的y:" + rocketLocation[1]);

		// 2. 获得提示框在屏幕中的坐标
		int[] tipLocation = new int[2];
		bottomImage.getLocationOnScreen(tipLocation);
		// Log.d(TAG, "提示的x:" + tipLocation[0]);
		// Log.d(TAG, "提示的y:" + tipLocation[1]);

		// 3. 计算火箭是否有一半进入提示框
		int rocketWidth = mTv.getWidth();
		int rocketHeight = mTv.getHeight();

		// 火箭的高度的一半 + 火箭y > 提示的y === y方向进入了
		boolean isY = rocketHeight / 2f + rocketLocation[1] > tipLocation[1];

		// 火箭的宽度的一半 + 火箭x > 提示的x ====x方向的左边进入
		boolean isLeftX = rocketWidth / 2f + rocketLocation[0] > tipLocation[0];

		// 火箭的宽度的一半 + 火箭x < 提示的x+ 提示宽度
		boolean isRightX = (rocketWidth / 2f + rocketLocation[0]) < (tipLocation[0] + bottomImage
				.getWidth());

		if (isY && isLeftX && isRightX) {
			return true;
		}

		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 按下显示发射区域图片
			showTip();

			startX = event.getRawX();
			startY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 移动的时候

			float newX = event.getRawX();
			float newY = event.getRawY();

			float diffX = newX - startX;
			float diffY = newY - startY;

			if (checkReady()) {
				setTipState(true);
				isReady = true;
			} else {
				setTipState(false);
				isReady = false;
			}

			offsetRocket(diffX, diffY);
			startX = newX;
			startY = newY;
			break;
		case MotionEvent.ACTION_UP:
			// 松开的时候 发射出去

			if (isReady) {
				fly();
			}

			hideTip();
			break;

		default:
			break;
		}

		return true;
	}

	private void fly() {

		// 1.计算火箭应该发射的位置
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;

		int rocketWidth = mTv.getWidth();
		int rocketHeight = mTv.getHeight();

		float x = screenWidth / 2f - rocketWidth / 2f;
		float y = screenHeight - rocketHeight;
		mRocketParams.x = (int) (x + 0.5f);
		mRocketParams.y = (int) (y + 0.5f);
		mWm.updateViewLayout(mTv, mRocketParams);

		// 属性动画
		// mRocketParams.y 400 ----> 0(400 399 398 .....0)

		ValueAnimator animator = ValueAnimator.ofInt(mRocketParams.y, 0);
		animator.setDuration(600);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int value = (Integer) animation.getAnimatedValue();
				mRocketParams.y = value;
				mWm.updateViewLayout(mTv, mRocketParams);
			}
		});
		animator.start();

		Intent intent = new Intent(mContext, SmokeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

}
