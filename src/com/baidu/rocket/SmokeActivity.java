package com.baidu.rocket;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SmokeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smoke);
		
		View root = findViewById(R.id.root);

		ObjectAnimator alpha1 = ObjectAnimator.ofFloat(root, "alpha", 0, 1);
		alpha1.setDuration(300);
		ObjectAnimator alpha2 = ObjectAnimator.ofFloat(root, "alpha", 1, 0);
		alpha2.setDuration(300);

		AnimatorSet set = new AnimatorSet();
		set.playSequentially(alpha1, alpha2);

		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				finish();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});

		set.start();
	}
}
