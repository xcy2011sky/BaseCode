package com.xcy.xpassword.ui;

import java.util.List;

import com.qq.e.splash.SplashAd;
import com.qq.e.splash.SplashAdListener;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.xcy.lib.annotation.FindViewById;
import com.xcy.xpassword.R;
import com.xcy.xpassword.app.BaseActivity;
import com.xcy.xpassword.view.LockPatternUtil;
import com.xcy.xpassword.view.LockPatternView;
import com.xcy.xpassword.view.LockPatternView.Cell;
import com.xcy.xpassword.view.LockPatternView.DisplayMode;
import com.xcy.xpassword.view.LockPatternView.OnPatternListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * ��ڣ���ӭҳ
 * 
 * @author zengdexing
 * 
 */
public class EntryActivity extends BaseActivity implements Callback, OnPatternListener
{
	@FindViewById(R.id.entry_activity_iconview)
	private View iconView;
	private Handler handler;

	private final int MESSAGE_START_MAIN = 1;
	private final int MESSAGE_CLEAR_LOCKPATTERNVIEW = 3;
	private final int MESSAGE_START_SETLOCKPATTERN = 4;

	@FindViewById(R.id.entry_activity_bg)
	private View backgroundView;
	
	@FindViewById(R.id.entry_activity_lockPatternView)
	private LockPatternView lockPatternView;
	
	@FindViewById(R.id.entry_activity_tips)
	private TextView tipsView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		
		// qq 实时分析log
		StatConfig.setDebugEnable(true);
		//qq 实时分析服务
		StatService.trackCustomEvent(this, "onCreate", "");
		
		

		handler = new Handler(this);
		lockPatternView.setOnPatternListener(this);

		List<Cell> cells = LockPatternUtil.getLocalCell(this);
		if (cells.size() == 0)
		{
			
			lockPatternView.setEnabled(false);
			handler.sendEmptyMessageDelayed(MESSAGE_START_SETLOCKPATTERN, 2000);
		}

		tipsView.setText("");
		initAnimation();
	}

	private void XKaiPing(){
//		//准备展示开屏广告的容器
//		FrameLayout container = (FrameLayout) this
//		.findViewById(R.id.splashcontainer);
//		//创建开屏广告,广告拉取成功后会自动展示在container中。Container会首先被清空
//		new SplashAd(this, container, Constants.APPId, Constants.SplashPosId,
//		new SplashAdListener() {
//		//广告拉取成功开始展示时调用
//		public void onAdPresent() {
//		Log.i("test", "present");
//		Toast.makeText(FullscreenActivity.this,
//		"SplashPresent", Toast.LENGTH_LONG).show();
//		}
//		//广告拉取超时(3s)或者没有广告时调用,errCode参见SplashAd类的常量声明
//		public void onAdFailed(int errCode) {
//		Toast.makeText(FullscreenActivity.this,
//		"SplashFail"+errCode, Toast.LENGTH_LONG).show();
//		FullscreenActivity.this.finish();
//		}
//		//广告展示时间结束(5s)或者用户点击关闭时调用。
//		public void onAdDismissed() {
//		Toast.makeText(FullscreenActivity.this,
//		"SplashDismissed", Toast.LENGTH_LONG).show();
//		FullscreenActivity.this.finish();
//		}
//		});
		
	
	}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	//阻止用户在展示过程中点击手机返回键,推荐开发者使用
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	return true;
	}
	return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case MESSAGE_START_SETLOCKPATTERN:
				startActivity(new Intent(this, SetLockpatternActivity.class));
				finish();
				break;

			case MESSAGE_START_MAIN:
				Intent intent = new Intent(this, XPasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;

			case MESSAGE_CLEAR_LOCKPATTERNVIEW:
				lockPatternView.clearPattern();
				tipsView.setText("");
				break;

			default:
				break;
		}
		return true;
	}

	
	private void initAnimation()
	{
		Animation iconAnimation = AnimationUtils.loadAnimation(this, R.anim.entry_animation_icon);
		iconView.startAnimation(iconAnimation);

		backgroundView.startAnimation(getAlpAnimation());
		lockPatternView.startAnimation(getAlpAnimation());
		tipsView.startAnimation(getAlpAnimation());
	}

	private Animation getAlpAnimation()
	{
		return AnimationUtils.loadAnimation(this, R.anim.entry_animation_alpha_from_0_to_1);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onPatternStart()
	{
		handler.removeMessages(MESSAGE_CLEAR_LOCKPATTERNVIEW);
		tipsView.setText("");
	}

	@Override
	public void onPatternCleared()
	{
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern)
	{
	}

	@Override
	public void onPatternDetected(List<Cell> pattern)
	{
		if (LockPatternUtil.checkPatternCell(LockPatternUtil.getLocalCell(this), pattern))
		{
			// ��֤ͨ��
			lockPatternView.setDisplayMode(DisplayMode.Correct);
			handler.sendEmptyMessage(MESSAGE_START_MAIN);
		}
		else
		{
			// ��֤ʧ��
			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			tipsView.setText(R.string.lock_pattern_error);
			handler.sendEmptyMessageDelayed(MESSAGE_CLEAR_LOCKPATTERNVIEW, 1000);
		}

	}


}
