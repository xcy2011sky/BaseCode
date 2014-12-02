package com.xcy.xpassword.app;


import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.qq.e.ads.InterstitialAd;
import com.qq.e.ads.InterstitialAdListener;
import com.qq.e.appwall.GdtAppwall;
import com.qq.e.gridappwall.GridAppWall;
import com.qq.e.gridappwall.GridAppWallListener;
import com.xcy.xpassword.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class XQQAD {

	public final String  appid="1103399760";
	public final String  IntersADid="6000509024547891";
	public final String  AppwallADid="1030308034646779";
	public final String  GridAppwallADid="7020002034947990";
	public final String  BaanerADid="2060702024348870"; 
	
	
	private Context mContext;
	
    private XDebug debug=new XDebug("XQQAD", true);
	
	
	
	public XQQAD(Context context) {
		this.mContext=context;
		
	}
	
	
	
	/** QQ 广告 插屏广告*/
	
	public void XInterAD()
	{
		// 创建插屏广告
		// appId : 在 http://e.qq.com/dev/ 能看到的app唯一字符串
		// posId : 在 http://e.qq.com/dev/ 生成的数字串,并非 appid 或者 appkey
		final InterstitialAd iad = new InterstitialAd((Activity) mContext, this.appid,IntersADid);
		iad.setAdListener(new InterstitialAdListener() {
		@Override
				public void onFail() {
				debug.LogV("xcy_gg：： chaping: onFal");
				//广告出错时的回调
				}
				@Override
				public void onBack() {
				//广告关闭时的回调
				}
				@Override
				public void onAdReceive() {
				//广告数据收到时的回调。在收到广告后可以调用 InterstitialAd.show 方法展示插屏
					debug.LogV("xcy_gg：： chaping: onFal");
					iad.show();
				}
				@Override
				public void onClicked() {
					// TODO Auto-generated method stub
					debug.LogV("xcy_gg：： chaping: onFal");
				}
				@Override
				public void onExposure() {
					// TODO Auto-generated method stub
					debug.LogV("xcy_gg：： chaping: onFal");
				}
				});
		//请求插屏广告,每次重新请求都可以调用此方法。
		iad.loadAd();
		/*
		* 展示插屏广告
		* 仅在adreceive事件发生后调用才有效。
		* IntersititialAd.show 方法会开启一个透明的activity
		*如广告情景不合适,也可考虑InterstitialAd.showAsPopupWindow
		*配套的关闭方法为closePopupWindow
		* 优先建议调用show
		*/
		
		
	}
	
	/** QQ 广告 应用墙广告*/
	public void XAppwall()
	{
		// 创建应用墙广告
		// appId : 在 http://e.qq.com/dev/ 能看到的app唯一字符串
		// posId : 在 http://e.qq.com/dev/ 生成的数字串,并非 appid 或者 appkey
		// testad 如果设置为true,则进入测试广告模式。该广告模式下不扣费。
		// 建议在调式时设置为true,发布前设置为false。
		final GdtAppwall appwall = new GdtAppwall(mContext,this.appid,this.AppwallADid, false);
		
		//调用展示应用墙的接口,请选择合适的场景调用此接口
		appwall.doShowAppWall();
	}
	
	 /**创建橱窗广告 */
	public void XGridAppWall()
	{
		// 创建橱窗广告
		//"appid" 在
		//"posid" 在http://e.qq.com/dev/
		//http://e.qq.com/dev/		能看到的app唯一字符串		生成的数字串,
		final GridAppWall g = new GridAppWall(this.appid,this.GridAppwallADid, (Activity) mContext, new GridAppWallListener() {
			public void onAdPresent() {
				//橱窗广告开始展示回调
				debug.LogV("xcy_gg：： Xchuchuang: onAdPresent");
				}
	
			@Override
			public void onAdDismissed() {
				// TODO Auto-generated method stub
				//橱窗广告关闭回调
				debug.LogV("xcy_gg：： Xchuchuang: onAdDismissed");
			}
	
			@Override
			public void onAdFailed(int arg0) {
				// TODO Auto-generated method stub
				//广告展示失败回调
				debug.LogV("xcy_gg：： Xchuchuang: onAdFailed");
			}
			
		//g.showRelativeTo(v);
		//g.showRelativeTo(300,400);
		});
		g.showRelativeTo(300, 400);
	}
	
	/**创建banner广告 */
	public void XBanner(View rootView )
	{
		RelativeLayout l = (RelativeLayout)rootView.findViewById(R.id.adcontent);
		// 创建Banner广告AdView对象
		// appId : 在 http://e.qq.com/dev/ 能看到的app唯一字符串
		// posId : 在 http://e.qq.com/dev/ 生成的数字串,并非 appid 或者 appkey
		AdView adv = new AdView((Activity) mContext, AdSize.BANNER, this.appid,this.BaanerADid);
		l.addView(adv);
		// 广告请求数据,可以设置广告轮播时间,默认为30s
		AdRequest adr = new AdRequest();
		//设置广告刷新时间,为30~120之间的数字,单位为s,0标识不自动刷新
		adr.setRefresh(30);
		//在banner广告上展示关闭按钮
		adr. setShowCloseBtn(true);
		//设置空广告和首次收到广告数据回调
		//调用fetchAd方法后会发起广告请求 */
		adv.setAdListener(new AdListener() {
		//加载广告失败时的回调
		public void onNoAd() {
		}
		//加载广告成功时的回调
		public void onAdReceiv() {
		}
		//Banner关闭时的回调,仅在展示Banner关闭按钮时有效
		public void onBannerClosed() {
		}
		//Banner广告曝光时的回调
		public void onAdExposure() {
		}
		@Override
		public void onAdClicked() {
			// TODO Auto-generated method stub
			
		}
		});
		/* 发起广告请求,收到广告数据后会展示数据 */
		adv.fetchAd(adr);
	}
}
