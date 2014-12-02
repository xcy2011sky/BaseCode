package com.xcy.xpassword.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.twotoasters.jazzylistview.JazzyEffect;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;
import com.xcy.xpassword.R;
import com.xcy.xpassword.adapter.CategoryAdapter;
import com.xcy.xpassword.adapter.XBaseAdapter;
import com.xcy.xpassword.app.BaseFragment;
import com.xcy.xpassword.app.OnSettingChangeListener;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.CategoryItem;
import com.xcy.xpassword.model.Password;
import com.xcy.xpassword.model.PasswordItem;
import com.xcy.xpassword.model.SettingKey;
import com.xcy.xpassword.service.Mainbinder;
import com.xcy.xpassword.service.OnGetAllPasswordCallback;
import com.xcy.xpassword.service.OnPasswordListener;

/**
 * all password list
 * 
 * @author xcy
 * 
 */
@SuppressLint("ValidFragment")
public class XBaseFragment extends BaseFragment implements OnGetAllPasswordCallback, OnPasswordListener,
		OnSettingChangeListener, android.view.View.OnClickListener
{
	
	private XBaseAdapter  baseAdapter;
	private List<Password>passwords=new ArrayList<Password>();
	

	
	private Mainbinder mainbinder;

	private JazzyListView listView;

	private View noDataView;
	
	
	private LinearLayout labLayout;
	
	private XPasswordActivity activity;
	
	private  Dialog passwordDialog;
	
	
	//private TextView nameViewLabel;
	private TextView countTextView;

	@SuppressLint("ValidFragment")
	private int category_index;
	private CategoryItem categoryItem;
	private String name=null;
	
	public XBaseFragment(String mName)
	{
		name=mName;
	}
	public XBaseFragment(CategoryItem item,String mName)
	{
		name=mName;
		this.categoryItem=item;
	}
	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			unregistOnPasswordListener();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mainbinder = (Mainbinder) service;
			mainbinder.getAllPassword(XBaseFragment.this);
			mainbinder.registOnPasswordListener(XBaseFragment.this);
		}
	};

	@Override
	public void onAttach(Activity activity) {
		
		this.activity=(XPasswordActivity)activity;
		
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		baseAdapter = new XBaseAdapter(getActivity(),name,this.activity);
		
		if("passwordFragment".equals(name)) 	activity.CategoryIndex=this.categoryItem.category.getIndex();
		
		
		getBaseActivity().getMyApplication().registOnSettingChangeListener(SettingKey.JAZZY_EFFECT, this);

		Intent intent = new Intent("com.xcy.xpassword");
		getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	
	private JazzyEffect getJazzyEffect()
	{
		String strKey = getBaseActivity().getSetting(SettingKey.JAZZY_EFFECT, JazzyHelper.TILT + "");
		JazzyEffect jazzyEffect = JazzyHelper.valueOf(Integer.valueOf(strKey));
		return jazzyEffect;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregistOnPasswordListener();
		getActivity().unbindService(serviceConnection);
		getBaseActivity().getMyApplication().unregistOnSettingChangeListener(SettingKey.JAZZY_EFFECT, this);
	}

	private void unregistOnPasswordListener()
	{
		if (mainbinder != null)
		{
			mainbinder.unregistOnPasswordListener(this);
			mainbinder = null;
		}
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_xpassword, container, false);
		labLayout=(LinearLayout)rootView.findViewById(R.id.main_list_layout);
		countTextView=(TextView)rootView.findViewById(R.id.main_list_count);
		
		listView = (JazzyListView) rootView.findViewById(R.id.main_listview);
		
		listView.setAdapter(baseAdapter);

		
		listView.setTransitionEffect(getJazzyEffect());

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				showPasswordDialog(baseAdapter.passwords.get(index));
			}
		});
		


		noDataView = rootView.findViewById(R.id.main_no_passsword);
		noDataView.setOnClickListener(this);
		if (mainbinder == null)
		{
			noDataView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			labLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			initView();
		}

		
		activity.qqAD.XBanner(rootView);
		return rootView;
	}

	private void initView()
	{
		if (noDataView != null)
		{
			if (baseAdapter.getCount() == 0)
		
			{
				noDataView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				labLayout.setVisibility(View.GONE);
			}
			else
			{
				noDataView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				labLayout.setVisibility(View.VISIBLE);
 
				
				countTextView.setText("共有"+String.valueOf(baseAdapter.getCount())+"项信息项");
			}
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		listView = null;
		noDataView = null;
	}

	@Override
	public void onSettingChange(SettingKey key)
	{
		if (listView != null && key == SettingKey.JAZZY_EFFECT)
		{
			listView.setTransitionEffect(getJazzyEffect());
		}
	}

	@Override
	public void onNewPassword(Password password)
	{
		baseAdapter.onNewPassword(password);
	
		initView();
	}

	@Override
	public void onDeletePassword(int id,Category c)
	{
		baseAdapter.onDeletePassword(id,c);
	
		initView();
	}

	@Override
	public void onUpdatePassword(Password newPassword)
	{
		baseAdapter.onUpdatePassword(newPassword);
		
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.main_no_passsword:
				
			activity.showDialogCategory();

			if("passwordFragment".equals(name))
			{
				Intent intent = new Intent(activity, EditPasswordActivity.class);

				intent.putExtra("category_index", category_index);

				startActivity(intent);
			}
			

				
				break;
			default:
				break;
		}
	}

	public void showPasswordDialog( final PasswordItem passwordItem){  
	    final Context context = activity;  
	 
	    passwordDialog=new Dialog(context);
	    passwordDialog.setTitle("详细信息");
	    
		ImageView iconView=null;
		TextView titleView;
	    TextView categoryView;
		TextView dateView;
		TextView nameView;
        TextView passwordView;
		TextView urlView;
		TextView noteView;
		View noteConainer;
		View topIconView;
		View copyView;
		View deleteView;
		View editView;
		
	    LayoutInflater layoutInflater = activity.getLayoutInflater();  
	  
	    final View rootView = layoutInflater.inflate(R.layout.main_password_detail,null);  
	      
	    iconView=(ImageView)rootView.findViewById(R.id.main_icon);
	    
	    iconView.setImageResource(passwordItem.password.getIconID());
	    
	    
	    titleView=(TextView)rootView.findViewById(R.id.main_title);
	    titleView.setText(passwordItem.password.getTitle());
	    
	    categoryView=(TextView)rootView.findViewById(R.id.main_category);
	    category_index=passwordItem.password.getCategory().getIndex();
	    
	    categoryView.setText(activity.getResources().getStringArray(R.array.password_category)[category_index]);
	   
	    
	    dateView=(TextView)rootView.findViewById(R.id.main_date);
	    dateView.setText(passwordItem.dataString);
	    
	    
	    nameView=(TextView)rootView.findViewById(R.id.main_name);
	    nameView.setText(passwordItem.password.getAccount());
	    
	    passwordView=(TextView)rootView.findViewById(R.id.main_password);
	    passwordView.setText(passwordItem.password.getPassword());
	    
	    urlView=(TextView)rootView.findViewById(R.id.main_url);
	    urlView.setText(passwordItem.password.getUrl());
	    urlView.setClickable(true);
		urlView.setFocusable(true);
		urlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		
		noteView=(TextView)rootView.findViewById(R.id.main_note);
	    
	    noteConainer=(View)rootView.findViewById(R.id.main_note_container);
	    
	    topIconView=(View)rootView.findViewById(R.id.main_top);
		String note = passwordItem.password.getNote();
		if (TextUtils.isEmpty(note))
		{
			noteConainer.setVisibility(View.GONE);
		}
		else
		{
			noteConainer.setVisibility(View.VISIBLE);
			noteView.setText(note);
		}

		if (passwordItem.password.isTop())
		{
			topIconView.setVisibility(View.VISIBLE);
			dateView.setTextColor(context.getResources().getColor(R.color.title_color));
		}
		else
		{
			topIconView.setVisibility(View.GONE);
			dateView.setTextColor(context.getResources().getColor(R.color.text_color));
		}

		
		
	    copyView=(View)rootView.findViewById(R.id.main_copy);
	    copyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onCopyClick(passwordItem);
				passwordDialog.dismiss();
			}
		});
	    
	    deleteView=(View)rootView.findViewById(R.id.main_delete);
	    deleteView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 onDeleteClick(passwordItem);
				 passwordDialog.dismiss();
			}
		});
	    
	    editView=(View)rootView.findViewById(R.id.main_edit);
	    editView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onEditClick(passwordItem);
			   passwordDialog.dismiss();
				
			}
		});
	    
	    passwordDialog.setContentView(rootView);
	    passwordDialog.show();
	} 
	
	private void onCopyClick(final PasswordItem passwordItem)
	{
		Builder builder = new Builder(activity);

		String[] item = new String[] { activity.getResources().getString(R.string.copy_name),
				activity.getResources().getString(R.string.copy_password) };

		builder.setItems(item, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case 0:
						
						ClipboardManager cmbName = (ClipboardManager) activity
								.getSystemService(Context.CLIPBOARD_SERVICE);
						ClipData clipDataName = ClipData.newPlainText(null, passwordItem.password.getAccount());
						cmbName.setPrimaryClip(clipDataName);
						Toast.makeText(activity, R.string.copy_name_toast, Toast.LENGTH_SHORT).show();
						break;
					case 1:
						
						ClipboardManager cmbPassword = (ClipboardManager)activity
								.getSystemService(Context.CLIPBOARD_SERVICE);
						ClipData clipData = ClipData.newPlainText(null, passwordItem.password.getPassword());
						cmbPassword.setPrimaryClip(clipData);
						Toast.makeText(activity, R.string.copy_password_toast, Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			}
		});
		builder.show();
		
		
	}

	private void onEditClick(PasswordItem passwordItem)
	{
		Intent intent = new Intent(activity, EditPasswordActivity.class);
		intent.putExtra(EditPasswordActivity.ID, passwordItem.password.getId());
		
		intent.putExtra("category_index", passwordItem.password.getCategory().getIndex());
		intent.putExtra("iconid", passwordItem.password.getIconID());
		activity.startActivity(intent);

		
	}

	private void onDeleteClick(final PasswordItem passwordItem)
	{
		Builder builder = new Builder(activity);
		builder.setMessage(R.string.alert_delete_message);
		builder.setTitle(passwordItem.password.getTitle());
		builder.setNeutralButton(R.string.yes, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mainbinder.deletePassword(passwordItem.password.getId(),passwordItem.password.getCategory());
			}
		});
		builder.setNegativeButton(R.string.no, null);
		builder.show();
	}

	@Override
	public void onGetAllPassword(List<Password> oldpasswords) {
		// TODO Auto-generated method stub

		if("passwordFragment".equals(name))
		{
			for(PasswordItem item:categoryItem.passworditems)
			{
				passwords.add(item.password);
			}
			baseAdapter.setData(passwords, mainbinder);
		}else{
			baseAdapter.setData(oldpasswords, mainbinder);
		}
	
		

		initView();
	
	}

	
}
