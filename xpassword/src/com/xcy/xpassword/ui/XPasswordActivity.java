package com.xcy.xpassword.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.qq.e.ads.InterstitialAd;
import com.qq.e.ads.InterstitialAdListener;
import com.qq.e.appwall.GdtAppwall;
import com.qq.e.gridappwall.GridAppWall;
import com.qq.e.gridappwall.GridAppWallListener;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyListView;
import com.xcy.xpassword.R;
import com.xcy.xpassword.R.drawable;
import com.xcy.xpassword.app.BaseActivity;
import com.xcy.xpassword.app.XDebug;
import com.xcy.xpassword.app.XQQAD;
import com.xcy.xpassword.database.PasswordDatabase;
import com.xcy.xpassword.dialog.ExportDialog;
import com.xcy.xpassword.dialog.ImportDialog;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.model.CategoryItem;
import com.xcy.xpassword.model.SettingKey;
import com.xcy.xpassword.service.Mainbinder;

/**
 * 主窗口界面
 * 
 * @author xcy
 * 
 */
public class XPasswordActivity extends BaseActivity
{
	
	private Mainbinder mainbinder;
	private long lastBackKeyTime;
    public  List<Integer>cateDrawables=new ArrayList<Integer>();
    public FragmentManager fragmentManager;
    public int CategoryIndex=-1;
    Dialog cateDialog=null;
    public XQQAD qqAD=new XQQAD(this);
    
    public static List<Category>gCategorys=new ArrayList<Category>();



  
	public  void InitCategoryDrawable()
	{
		cateDrawables.add(R.drawable.type_sns);
		cateDrawables.add(R.drawable.type_dianshang);
		cateDrawables.add(R.drawable.type_vedio);
		cateDrawables.add(R.drawable.type_email);
		cateDrawables.add(R.drawable.type_database);
		cateDrawables.add(R.drawable.type_game);
		cateDrawables.add(R.drawable.type_travel);

		cateDrawables.add(R.drawable.type_other);
	}

	private XDebug debug=new XDebug(XPasswordActivity.class.getSimpleName(), true);;
	
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
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xpassword);
		
		
		if(savedInstanceState==null)
		{
			fragmentManager=getFragmentManager();
			String value=getSetting(SettingKey.DSIPLAY_EFFECT, "all");
			if(value.equalsIgnoreCase("all"))
			{
				fragmentManager.beginTransaction()
				
				.add(R.id.container, new XBaseFragment("mainFragment"),"main")
				.commit();
			}else {
				fragmentManager.beginTransaction()
				
				.add(R.id.container, new CategoryFragment(),"category")
				.commit();
			}
			

		}

		InitCategoryDrawable();

		Intent intent = new Intent("com.xcy.xpassword");
		this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		
	

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(serviceConnection);
	}

	private boolean isExistSDCard()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id)
		{
			case R.id.action_add_password:
			
				if(this.CategoryIndex<0)
					showDialogCategory();
				else {
					Intent intent =new Intent(XPasswordActivity.this,EditPasswordActivity.class);
 					
 					intent.putExtra("category_index", this.CategoryIndex);
 					
 					startActivity(intent);
				}
				
				
				break;

			case R.id.action_import:
				debug.LogV("action_import:");
				if (mainbinder == null)
					break;
				ImportDialog importDialog = new ImportDialog(getActivity(), mainbinder);
				importDialog.show();
				break;

			case R.id.action_export:
				
				debug.LogV("action_export:");
				if (mainbinder == null)
					break;
				if (!isExistSDCard())
				{
					showToast(R.string.export_no_sdcard);
					break;
				}
				ExportDialog exportDialog = new ExportDialog(this, mainbinder);
				exportDialog.show();
				break;

			case R.id.action_set_lock_pattern:
				debug.LogV("action_set_lock_pattern");
				startActivity(new Intent(this, SetLockpatternActivity.class));
				break;
				
			case R.id.action_display:
				
				debug.LogV("aciton_display");
				
				break;
			case R.id.action_all_display:
				
				putSetting(SettingKey.DSIPLAY_EFFECT, "category");
				this.fragmentManager.beginTransaction()
				.replace(R.id.container, new CategoryFragment())
				.addToBackStack("category")
				.commit();
				break;
			case R.id.action_category_display:
				putSetting(SettingKey.DSIPLAY_EFFECT, "all");
				this.fragmentManager.beginTransaction()
				.replace(R.id.container, new XBaseFragment("mainFragment"))
				.addToBackStack("all")
				.commit();
				break;	
			
			case R.id.action_about:
				
				onAboutClick();
				break;
			case R.id.action_feedback:
			
				onFeedbackClick();
				break;
			case R.id.action_exit:
				
				qqAD.XAppwall();
				
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 反馈
	 */
	private void onFeedbackClick()
	{
		
	
		startActivity(new Intent(this, FeedbackActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.xpassword, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				long delay = Math.abs(System.currentTimeMillis() - lastBackKeyTime);
//				if (delay > 4000)
//				{
//					// ˫���˳�����
//					showToast(R.string.toast_key_back);
//					lastBackKeyTime = System.currentTimeMillis();
//					return true;
//				}
//				
				
				break;

			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	

	@Override
	protected void onResume() {
		
		PasswordDatabase db =new PasswordDatabase(this);
		String initCategory=this.getSetting(SettingKey.CATEGORY_LIST, null);
		if(initCategory==null)
		{

			for(int i=0;i<cateDrawables.size();i++)
			{
				Category category =new Category();
				category.index=i;
				category.name=getResources().getStringArray(R.array.password_category)[i];
				category.iconID=cateDrawables.get(i);
				db.insertCategory(category);
				gCategorys.add(category);
				this.putSetting(SettingKey.CATEGORY_LIST,"test");
			}
		}else
		{
			gCategorys.clear();
			gCategorys=db.getAllCategory();
			
		}
		
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 关于和说明
	 */
	private void onAboutClick()
	{
		Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.action_about_us);
		builder.setNeutralButton(R.string.common_sure, null);
		String message = getString(R.string.drawer_about_detail, getMyApplication().getVersionName());
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.LEFT);
		textView.setText(message);
		textView.setTextSize(16);
		builder.setView(textView);
		builder.show();
	}

	
	/**
	 * password category dialog
	 */
	public void showDialogCategory(){
		 
		 LayoutInflater inflater=getLayoutInflater();
	     View rootView =inflater.inflate(R.layout.dialog_content, (ViewGroup) findViewById(R.id.dialog));
         JazzyListView	listView = (JazzyListView) rootView.findViewById(R.id.dialog_listview);
	     listView.setAdapter(new ListItemAdapter());
	     listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int index,
						long arg3) {


                	Intent intent =new Intent(XPasswordActivity.this,EditPasswordActivity.class);
 					
 					intent.putExtra("category_index", index);
 					
 					startActivity(intent);
 					cateDialog.cancel();
				}
			});
	     
	     Builder builder = new AlertDialog.Builder(this);
     
          builder.setTitle(getResources().getString(R.string.dialog_tilte));
	      builder.setView(rootView);
          cateDialog=builder.create();
          cateDialog.show();
		
	}


	class ListItemAdapter extends BaseAdapter {

             @Override
             public int getCount() {
                     return gCategorys.size();
             }

             @Override
             public Object getItem(int position) {
                     return  gCategorys.get(position);
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
        	     
        	     title.setText(gCategorys.get(position).name);
        	     image.setImageResource(gCategorys.get(position).iconID);;
        	     return rootView;
             }

}
	
	
}
