package com.xcy.xpassword.ui;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.twotoasters.jazzylistview.JazzyListView;
import com.xcy.lib.annotation.FindViewById;
import com.xcy.xpassword.R;
import com.xcy.xpassword.app.BaseActivity;
import com.xcy.xpassword.app.BaseFragment;
import com.xcy.xpassword.app.XApplication;
import com.xcy.xpassword.app.XDebug;
import com.xcy.xpassword.model.CategoryItem;
import com.xcy.xpassword.model.DomCategoryItemParser;
import com.xcy.xpassword.model.Password;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.service.Mainbinder;
import com.xcy.xpassword.service.OnGetAllPasswordCallback;
import com.xcy.xpassword.service.OnGetPasswordCallback;
import com.xcy.xpassword.ui.XPasswordActivity.ListItemAdapter;

import android.R.integer;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 密码编辑
 * 
 * @author xcy
 * 
 */
public class EditPasswordActivity extends BaseActivity implements OnGetPasswordCallback, OnGetAllPasswordCallback
{
	/** 数据库ID */
	public static final String ID = "password_id";
	/**添加模式 */
	private static final int MODE_ADD = 0;
	/** 更新模式 */
	private static final int MODE_MODIFY = 1;

	/** 当前模式 */
	private int MODE = MODE_ADD;
	/** */
	private int id;
	
	private int iconid;
	
	private int category_index;

	
	private Mainbinder mainbinder;

	@FindViewById(R.id.editview_title)
	private EditText titleView;

	private Button titleSpinner;
	
	@FindViewById(R.id.editview_icon)
	private ImageView iconView;

	@FindViewById(R.id.editview_category)
	private TextView categoryView;
	
	@FindViewById(R.id.editview_url)
	private EditText urlView;
	
	@FindViewById(R.id.editview_name_lab)
	private TextView nameLabView;
	
	@FindViewById(R.id.editview_name)
	private AutoCompleteTextView nameView;

	@FindViewById(R.id.editview_password)
	private AutoCompleteTextView passwordView;

	@FindViewById(R.id.editview_note)
	private EditText noteView;

	@FindViewById(R.id.is_top)
	private CheckBox isTopView;
	
	private List<CategoryItem>categoryItems=new ArrayList<CategoryItem>();
	
	private List<Password>passwords=new ArrayList<Password>();
	DomCategoryItemParser parser=null;
	Dialog pwdDialog=null;
	
	XDebug debug=new XDebug("EditPasswordActivity", true);
	 
	
		private int snsIcon[]={
				R.drawable.sub_sns_qq,
				R.drawable.sub_sns_sina,
				R.drawable.sub_sns_wechat,
				R.drawable.sub_sns_zhenai,
				R.drawable.sub_sns_renren,
		};
		private int ebIcon[]={
				R.drawable.sub_eb_taobao,
				R.drawable.sub_eb_jd,
				R.drawable.sub_eb_yhd,
				R.drawable.sub_eb_58,
				R.drawable.sub_eb_amazon,
				R.drawable.sub_eb_suning,
		};
		private int videoIcon[]={
			R.drawable.sub_video_youku,
			R.drawable.sub_video_aiqiyi,
			R.drawable.sub_video_qqvideo,
			R.drawable.sub_video_pps,
			R.drawable.sub_video_pptv,
			R.drawable.sub_video_souhu,
			R.drawable.sub_video_letv,
			
		};
		private int studyIcon[]={
			R.drawable.sub_study_csdn,
			R.drawable.sub_mail_wangyi,
			R.drawable.sub_mail_sina,
			R.drawable.sub_mail_google,
			R.drawable.sub_mail_qqmail,
			R.drawable.sub_tool_baidu,
			
			};
		private int toolsIcon[]={
				R.drawable.sub_database_baiduyun,
				R.drawable.sub_database_jinshan,
				R.drawable.sub_tools_365,
				R.drawable.sub_database_360,
				R.drawable.sub_database_leshiyun,
				R.drawable.sub_tool_youdao,
	
				};
		private int gameIcon[]={
				R.drawable.sub_live_51job,
				R.drawable.sub_live_liepin,
				R.drawable.type_game,
				R.drawable.type_game,
				R.drawable.sub_live_moji,
				};
		private int travelIcon[]={
				R.drawable.sub_travel_qunaer,
				R.drawable.sub_travel_xiecheng,
				R.drawable.sub_travel_12306,
				R.drawable.sub_trave_baidumap,
				R.drawable.sub_travel_amap,	
				R.drawable.sub_travel_lygl,	
		};
	
		private int otherIcon[]={
				R.drawable.type_other,
				R.drawable.type_other,
				R.drawable.type_other,
				R.drawable.type_other,
				R.drawable.type_other,	
		};
   
		public int[] creatIcon(int i)
		{
			int [] value={};
			switch(i)
			{
			case 0:
				return snsIcon;
			case 1:
				return ebIcon;
			case 2:
				return videoIcon;
			case 3:
				return studyIcon;
			case 4:
				return toolsIcon;
			case 5:
				return gameIcon;
			case 6:
				return travelIcon;
			
			case 7:
				return otherIcon;
				
				
			}
			
			return value;
		}
	    public int[] imgIds = {
	    		R.drawable.type_sns, 
	            R.drawable.type_dianshang, 
	            R.drawable.type_vedio,
	            R.drawable.type_email,
	            R.drawable.type_database,
	            R.drawable.type_game,
	            R.drawable.type_travel,
	       
	            R.drawable.type_other,
	            };

	 
	 
	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mainbinder = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mainbinder = (Mainbinder) service;
			if (MODE == MODE_MODIFY)
			{
				mainbinder.getPassword(id, EditPasswordActivity.this);
			}
		
			mainbinder.getAllPassword(EditPasswordActivity.this);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_password);
		titleSpinner=(Button)findViewById(R.id.editview_title_spinner);
		
		id = getIntent().getIntExtra(ID, -1);
		category_index=getIntent().getIntExtra("category_index", 0);
		
		iconid=getIntent().getIntExtra("iconid", 0);
		
		debug.LogE("id="+id+"category_index= "+ category_index +"iconid="+iconid);

		iconid=iconid>0?iconid:imgIds[category_index];
		
		iconView.setImageResource(iconid);
	
		categoryView.setText(getResources().getStringArray(R.array.password_category)[category_index]);
	   
		try {
			InputStream is=getAssets().open("category.xml");
			parser=new DomCategoryItemParser(this);
			categoryItems=parser.parse(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(categoryItems.size()>0)
		{
			for(int i=0;i<categoryItems.get(category_index).passworditems.size();i++)
			{
				Password password=categoryItems.get(category_index).passworditems.get(i).password;
				
				passwords.add(password);
			}
		}
		

	    titleSpinner.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pwdDialog=onCreateDialog();
				pwdDialog.show();
			}
		});
		
		
		if (id == -1)
		{
			MODE = MODE_ADD;
		}
		else
		{
			MODE = MODE_MODIFY;
		}

		initActionBar();

		Intent intent = new Intent("com.xcy.xpassword");
		this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(serviceConnection);
	}

	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (MODE == MODE_ADD)
		{
			actionBar.setTitle(R.string.title_activity_add_password);
		}
		else
		{
			actionBar.setTitle(R.string.title_activity_modify_password);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.edit_password, menu);
		if (MODE == MODE_ADD)
		{
			menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_ok);
		}
		else
		{
			menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_save);
			menu.findItem(R.id.action_delete).setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_save)
		{
			if (mainbinder != null)
			{
				onSaveBtnClick();
			}
			return true;
		}
		else if (id == R.id.action_delete)
		{
			if (mainbinder != null)
			{
				deletePassword();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void deletePassword()
	{
		Builder builder = new Builder(this);
		builder.setMessage(R.string.alert_delete_message);
		builder.setNeutralButton(R.string.yes, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mainbinder.deletePassword(id,XPasswordActivity.gCategorys.get(category_index));
				finish();
			}
		});
		builder.setNegativeButton(R.string.no, null);
		builder.show();
	}

	private void onSaveBtnClick()
	{
		if (titleView.getText().toString().trim().equals(""))
		{
			Toast.makeText(this, R.string.add_password_save_no_data, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Password password = new Password();
			password.setTitle(titleView.getText().toString().trim());
			password.setCategory(XPasswordActivity.gCategorys.get(category_index));
			
			password.setAccount(nameView.getText().toString().trim());
			password.setPassword(passwordView.getText().toString().trim());
			
			password.setUrl(urlView.getText().toString().trim());
			
			password.setIconID(iconid);
			
			
			
			password.setNote(noteView.getText().toString().trim());
			
			password.setTop(isTopView.isChecked());
			if (MODE == MODE_ADD)
			{
				password.setCreateDate(System.currentTimeMillis());
				mainbinder.insertPassword(password);
			}
			else
			{
				password.setId(id);
				mainbinder.updatePassword(password);
			}
			Intent intent=new Intent();
			intent.setClass(this, XPasswordActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onGetPassword(Password password)
	{
		if (password == null)
		{
			Toast.makeText(this, R.string.toast_password_has_deleted, Toast.LENGTH_SHORT).show();
			finish();
		}

		titleView.setText(password.getTitle());
		iconView.setImageResource(password.getIconID());
		categoryView.setText(getResources().getStringArray(R.array.password_category)[password.getCategory().getIndex()]);
		
	
		
		nameView.setText(password.getAccount());
		passwordView.setText(password.getPassword());
		urlView.setText(password.getUrl());
		noteView.setText(password.getNote());
		isTopView.setChecked(password.isTop());
		titleView.setSelection(titleView.getText().length());
	}

	@Override
	public void onGetAllPassword(List<Password> passwords)
	{
	
		Set<String> arrays = new HashSet<String>();
		for (int i = 0; i < passwords.size(); i++)
		{
			Password password = passwords.get(i);
			arrays.add(password.getAccount());
			arrays.add(password.getPassword());
		}

		int id = R.layout.simple_dropdown_item;
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, id, new ArrayList<String>(arrays));
		nameView.setAdapter(arrayAdapter);
		passwordView.setAdapter(arrayAdapter);
	}

	
	class ListItemAdapter extends BaseAdapter {

	   
	
	
		@Override
        public int getCount() {
			
                return passwords.size();
        }

        @Override
        public Object getItem(int position) {
                return null;
        }

        @Override
        public long getItemId(int position) {
                return 0;
        }

        @Override
        public View getView(int position, 
                        View contentView, ViewGroup parent) {
       	 LayoutInflater inflater=getLayoutInflater();
	     View rootView =inflater.inflate(R.layout.dialog_content_item, null);
	     ImageView image=(ImageView)rootView.findViewById(R.id.dialog_item_icon);
	     TextView title=(TextView)rootView.findViewById(R.id.dialog_item_title);
	     
	     title.setText(passwords.get(position).getTitle());
	     image.setImageDrawable(getResources().getDrawable(creatIcon(category_index)[position]));
	     return rootView;

        }

}
	protected Dialog onCreateDialog() {
        Dialog dialog = null;
      
        LayoutInflater inflater=getLayoutInflater();
	     View rootView =inflater.inflate(R.layout.dialog_content, (ViewGroup) findViewById(R.id.dialog));
        JazzyListView	listView = (JazzyListView) rootView.findViewById(R.id.dialog_listview);
	     listView.setAdapter(new ListItemAdapter());
	     listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int index,
						long arg3) {

					 Password password=passwords.get(index);
			           titleView.setText(password.getTitle());
			           urlView.setText(password.getUrl());
			           noteView.setText(password.getNote());
			           iconView.setImageResource(creatIcon(category_index)[index]);
			           iconid=creatIcon(category_index)[index];
					pwdDialog.cancel();
				}
			});
	     
	     Builder builder = new AlertDialog.Builder(this);
      
         builder.setTitle(getResources().getString(R.string.dialog_tilte));
	     builder.setView(rootView);
        dialog = builder.create();

        return dialog;
}

}
