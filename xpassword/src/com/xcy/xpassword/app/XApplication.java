package com.xcy.xpassword.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xcy.xpassword.model.SettingKey;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.os.Looper;
import android.widget.Toast;

public class XApplication extends Application  implements OnSharedPreferenceChangeListener{
	
	
	private SharedPreferences sharedPreferences;
	private Map<SettingKey, List<OnSettingChangeListener>> onSettingChangeListenerMap = new HashMap<>();
    
    private XDebug debug=new XDebug("xapplication", true);

	
	private NetstateReceiver connectReceiver=new NetstateReceiver();
	
	public static boolean NetState=false;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		loadSettings();
		
		IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		  registerReceiver(connectReceiver, intentFilter);
	}

	
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		
		if(connectReceiver!=null)
			unregisterReceiver(connectReceiver);
		super.onTerminate();
	}



	private void loadSettings()
	{
		sharedPreferences = getSharedPreferences("settings", Context.MODE_MULTI_PROCESS);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	/**
	 * 
	 * 
	 * @param key
	 *           
	 * @param defValue
	 *           �ֵ
	 * @return
	 */
	public String getString(SettingKey key, String defValue)
	{
		return sharedPreferences.getString(key.name(), defValue);
	}

	/**
	 * �������ã����ø÷���������
	 * {@link OnSettingChangeListener#onSettingChange(SettingKey)}�ص���
	 * 
	 * @param key
	 *            ���ñ���key
	 * @param value
	 *            ��Ҫ�����ֵ
	 */
	public void putString(SettingKey key, String value)
	{
		sharedPreferences.edit().putString(key.name(), value).commit();
	}

	/**
	 * ע�����øı���������÷������������߳��е��ã��Ҳ���ʱ�������
	 * {@link #unregistOnSettingChangeListener(SettingKey, OnSettingChangeListener)}
	 * 
	 * @param key
	 *            ��Ҫ�����������
	 * @param onSettingChangeListener
	 *            ����仯�Ļص�
	 */
	public void registOnSettingChangeListener(SettingKey key, OnSettingChangeListener onSettingChangeListener)
	{
		checkUIThread();

		List<OnSettingChangeListener> onSettingChangeListeners;
		if (onSettingChangeListenerMap.containsKey(key))
		{
			onSettingChangeListeners = onSettingChangeListenerMap.get(key);
		}
		else
		{
			onSettingChangeListeners = new ArrayList<OnSettingChangeListener>();
			onSettingChangeListenerMap.put(key, onSettingChangeListeners);
		}
		onSettingChangeListeners.add(onSettingChangeListener);
	}

	/**
	 * ע�����ñ仯����÷�����
	 * {@link #registOnSettingChangeListener(SettingKey, OnSettingChangeListener)}
	 * ����ʹ��
	 * 
	 * @param key
	 *            ��Ҫע�������ѡ��
	 * @param onSettingChangeListener
	 *            
	 */
	public void unregistOnSettingChangeListener(SettingKey key, OnSettingChangeListener onSettingChangeListener)
	{
		checkUIThread();
		if (onSettingChangeListenerMap.containsKey(key))
		{
			List<OnSettingChangeListener> onSettingChangeListeners = onSettingChangeListenerMap.get(key);
			onSettingChangeListeners.remove(onSettingChangeListener);
			if (onSettingChangeListeners.size() == 0)
			{
				onSettingChangeListenerMap.remove(key);
			}
		}
	}

	
	public String getVersionName()
	{
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo;
		String version = "";
		try
		{
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * ersion
	 */
	public int getVersionCode()
	{
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo;
		int versionCode = 0;
		try
		{
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packInfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	private void checkUIThread()
	{
		if (!isRunOnUIThread())
			throw new RuntimeException("要运行在主线程里");
	}

	/**
	 * 判断是是UI线程
	 * 
	 * @return
	 */
	private boolean isRunOnUIThread()
	{
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		SettingKey settingKey = SettingKey.valueOf(SettingKey.class, key);
		List<OnSettingChangeListener> onSettingChangeListeners = onSettingChangeListenerMap.get(settingKey);
		if (onSettingChangeListeners != null)
		{
			for (OnSettingChangeListener onSettingChangeListener : onSettingChangeListeners)
			{
				onSettingChangeListener.onSettingChange(settingKey);
			}
		}
	}


class NetstateReceiver extends BroadcastReceiver {  
	private boolean flag=false;

    @Override  
    public void onReceive(Context context, Intent intent) {  
        ConnectivityManager manager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo gprs = manager  
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
        NetworkInfo wifi = manager  
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
       if(gprs!=null)
       {
    	   flag=gprs.isConnected();
       }
        
       if (!wifi.isConnected()&& !flag) {  
            // network closed 
    	   
        	NetState=false;
        	Toast.makeText(getApplicationContext(), "网络已断开，请链接网络", 1000).show();
        } else {  
            // network opend  
        	NetState=true;
        	Toast.makeText(getApplicationContext(), "网络已恢复", 1000).show();
        	
         }  
    }



}  
 

}
