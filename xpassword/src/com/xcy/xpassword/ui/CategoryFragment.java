package com.xcy.xpassword.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyEffect;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;
import com.xcy.xpassword.R;
import com.xcy.xpassword.adapter.CategoryAdapter;

import com.xcy.xpassword.app.BaseFragment;
import com.xcy.xpassword.app.OnSettingChangeListener;
import com.xcy.xpassword.app.XDebug;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.CategoryItem;
import com.xcy.xpassword.model.Password;
import com.xcy.xpassword.model.PasswordItem;
import com.xcy.xpassword.model.SettingKey;
import com.xcy.xpassword.service.Mainbinder;
import com.xcy.xpassword.service.OnGetAllPasswordCallback;
import com.xcy.xpassword.service.OnPasswordListener;

/**
 * 分类显示界面
 * 
 * @author xcy
 * 
 */
public class CategoryFragment extends BaseFragment implements
		OnGetAllPasswordCallback, OnPasswordListener, OnSettingChangeListener,
		android.view.View.OnClickListener {

	private CategoryAdapter categoryAdapter;

	private Mainbinder mainbinder;

	private JazzyListView listView;

	private View noDataView;

	private XDebug debug = new XDebug("CategoryFragment", true);

	private LinearLayout labLayout;

	private XPasswordActivity activity;

	private Dialog passwordDialog;

	private TextView countTextView;

	private int category_index;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			unregistOnPasswordListener();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mainbinder = (Mainbinder) service;
			mainbinder.getAllPassword(CategoryFragment.this);
			mainbinder.registOnPasswordListener(CategoryFragment.this);
		}
	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = (XPasswordActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity.CategoryIndex = -1;

		categoryAdapter = new CategoryAdapter(getActivity(), activity);

		getBaseActivity().getMyApplication().registOnSettingChangeListener(
				SettingKey.JAZZY_EFFECT, this);

		Intent intent = new Intent("com.xcy.xpassword");
		getActivity().bindService(intent, serviceConnection,
				Context.BIND_AUTO_CREATE);
	}

	private JazzyEffect getJazzyEffect() {
		String strKey = getBaseActivity().getSetting(SettingKey.JAZZY_EFFECT,
				JazzyHelper.TILT + "");
		JazzyEffect jazzyEffect = JazzyHelper.valueOf(Integer.valueOf(strKey));
		return jazzyEffect;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistOnPasswordListener();
		getActivity().unbindService(serviceConnection);
		getBaseActivity().getMyApplication().unregistOnSettingChangeListener(
				SettingKey.JAZZY_EFFECT, this);
	}

	private void unregistOnPasswordListener() {
		if (mainbinder != null) {
			mainbinder.unregistOnPasswordListener(this);
			mainbinder = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity.CategoryIndex = -1;
		View rootView = inflater.inflate(R.layout.fragment_xpassword,
				container, false);
		labLayout = (LinearLayout) rootView.findViewById(R.id.main_list_layout);
		countTextView = (TextView) rootView.findViewById(R.id.main_list_count);

		listView = (JazzyListView) rootView.findViewById(R.id.main_listview);

		listView.setAdapter(categoryAdapter);

		listView.setTransitionEffect(getJazzyEffect());

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				CategoryItem item = categoryAdapter.categorys.get(index);

				XBaseFragment passwordFragment = new XBaseFragment(item,
						"passwordFragment");

				activity.fragmentManager.beginTransaction()
						.replace(R.id.container, passwordFragment)
						.addToBackStack("category").commit();
			}
		});

		noDataView = rootView.findViewById(R.id.main_no_passsword);
		noDataView.setOnClickListener(this);

		if (categoryAdapter == null) {
			noDataView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			labLayout.setVisibility(View.VISIBLE);
		} else {
			initView();
		}

		return rootView;
	}

	private void initView() {
		int sum = 0;
		if (noDataView != null) {

			if (categoryAdapter.categorys.size() == 0) {
				noDataView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				labLayout.setVisibility(View.GONE);
			} else {
				noDataView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				labLayout.setVisibility(View.VISIBLE);

				for (int i = 0; i < categoryAdapter.categorys.size(); i++) {
					sum = sum
							+ categoryAdapter.categorys.get(i).passworditems
									.size();
				}

				countTextView.setText("共有" + String.valueOf(sum) + "项信息");
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		listView = null;
		noDataView = null;
	}

	@Override
	public void onGetAllPassword(List<Password> passwords) {

		categoryAdapter.setData(passwords, mainbinder);
		initView();
	}

	@Override
	public void onSettingChange(SettingKey key) {
		if (listView != null && key == SettingKey.JAZZY_EFFECT) {
			listView.setTransitionEffect(getJazzyEffect());
		}
	}

	@Override
	public void onNewPassword(Password password) {
	
		categoryAdapter.onNewPassword(password, password.getCategory());

		initView();
	}

	@Override
	public void onDeletePassword(int id, Category c) {


		categoryAdapter.onDeletePassword(id, c);
		initView();
	}

	@Override
	public void onUpdatePassword(Password newPassword) {
		debug.LogE("onUpdatePassword");
		// categoryAdapter.onNewPassword(newPassword,
		// newPassword.getCategory());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_no_passsword:

			activity.showDialogCategory();
			break;
		default:
			break;
		}
	}

}
