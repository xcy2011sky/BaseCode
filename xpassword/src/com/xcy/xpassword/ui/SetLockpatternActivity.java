package com.xcy.xpassword.ui;

import java.util.ArrayList;
import java.util.List;

import com.xcy.lib.annotation.FindViewById;
import com.xcy.xpassword.R;
import com.xcy.xpassword.app.BaseActivity;
import com.xcy.xpassword.view.LockPatternUtil;
import com.xcy.xpassword.view.LockPatternView;
import com.xcy.xpassword.view.LockPatternView.Cell;
import com.xcy.xpassword.view.LockPatternView.DisplayMode;
import com.xcy.xpassword.view.LockPatternView.OnPatternListener;

import android.app.ActionBar;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.TextView;


/**
 * ����ͼ���������
 * 
 * @author xcy
 * 
 */
public class SetLockpatternActivity extends BaseActivity implements OnPatternListener, Callback
{
	@FindViewById(R.id.set_lockpattern_view)
	private LockPatternView lockPatternView;

	@FindViewById(R.id.set_lockpattern_text)
	private TextView textView;

	/** ģʽ ��֤ */
	private static final int MODE_AUTH = 0;
	/** ģʽ ���ڵ�һ�� */
	private static final int MODE_FIRST_STEP = 1;
	/** ģʽ ���ڵڶ��� */
	private static final int MODE_SECOND_STEP = 2;

	private static final int MEG_AUTH_ERROR = 1;
	private static final int MEG_GOTO_SECOND_STEP = 2;
	private static final int MEG_SET_SUCCESS = 3;
	private static final int MEG_GOTO_FIRST_STEP = 4;

	/** ��ǰ���ڵ�ģʽ */
	private int mode = MODE_AUTH;
	private Handler handler = new Handler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_lockpattern);
		initActionBar();
		lockPatternView.setOnPatternListener(this);
		initMode();
	}

	/**
	 * ��ʼ��ģʽ������û�û���������룬���ڵ�һ�� {@link #MODE_FIRST_STEP}�������û���Ҫ��֤
	 * {@link #MODE_AUTH}
	 */
	private void initMode()
	{
		List<LockPatternView.Cell> list = LockPatternUtil.getLocalCell(this);
		if (list.size() != 0)
		{
			mode = MODE_AUTH;
			textView.setText(R.string.set_lock_pattern_auth);
		}
		else
		{
			mode = MODE_FIRST_STEP;
			textView.setText(R.string.set_lock_pattern_first_step);
			showFirstUserDialog();
		}
	}

	/** ��һ��ʹ�ã����ý���ͼ�� */
	private void showFirstUserDialog()
	{
		Builder builder = new Builder(this);
		builder.setMessage(R.string.set_lock_pattern_first_message);
		builder.setNeutralButton(R.string.set_lock_pattern_first_sure, null);
		builder.show();
	}

	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onPatternStart()
	{
		textView.setText(R.string.set_lock_pattern_step_tips);
	}

	@Override
	public void onPatternCleared()
	{

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern)
	{

	}

	private List<Cell> lastCells;

	@Override
	public void onPatternDetected(List<Cell> pattern)
	{
		switch (mode)
		{
			case MODE_AUTH:
				if (LockPatternUtil.authPatternCell(this, pattern))
				{
					// ��֤ͨ���һ��
					lockPatternView.setDisplayMode(DisplayMode.Correct);
					lockPatternView.setEnabled(false);
					textView.setText(R.string.set_lock_pattern_auth_ok);
					handler.sendEmptyMessageDelayed(4, 1000);
				}
				else
				{
					// ��֤��ͨ�������������
					lockPatternView.setEnabled(false);
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					textView.setText(R.string.set_lock_pattern_auth_error);
					handler.sendEmptyMessageDelayed(MEG_AUTH_ERROR, 1000);
				}
				break;
			case MODE_FIRST_STEP:
				// ��һ�����룬��¼
				lockPatternView.setEnabled(false);
				lastCells = new ArrayList<LockPatternView.Cell>(pattern);
				textView.setText(R.string.set_lock_pattern_first_step_tips);
				handler.sendEmptyMessageDelayed(MEG_GOTO_SECOND_STEP, 1000);
				break;
			case MODE_SECOND_STEP:
				if (LockPatternUtil.checkPatternCell(lastCells, pattern))
				{
					// ���óɹ�
					lockPatternView.setEnabled(false);
					lockPatternView.setDisplayMode(DisplayMode.Correct);
					textView.setText(R.string.set_lock_pattern_second_step_tips);
					handler.sendEmptyMessageDelayed(MEG_SET_SUCCESS, 2000);
					LockPatternUtil.savePatternCell(this, pattern);
				}
				else
				{
					// �����������벻һ�£�����һ����������
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					lockPatternView.setEnabled(false);
					textView.setText(R.string.set_lock_pattern_second_step_error);
					handler.sendEmptyMessageDelayed(MEG_GOTO_FIRST_STEP, 1000);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		lockPatternView.setEnabled(true);
		lockPatternView.clearPattern();
		switch (msg.what)
		{
			case MEG_AUTH_ERROR:
				textView.setText(R.string.set_lock_pattern_auth);
				break;

			case MEG_GOTO_SECOND_STEP:
				mode = MODE_SECOND_STEP;
				textView.setText(R.string.set_lock_pattern_second_step);
				break;
			case MEG_SET_SUCCESS:
				Intent intent = new Intent(this, XPasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;

			case MEG_GOTO_FIRST_STEP:
				mode = MODE_FIRST_STEP;
				textView.setText(R.string.set_lock_pattern_first_step);
				break;
		}
		return true;
	}
}
