package com.xcy.lib.annotation;


import android.app.Activity;
import android.view.View;

/**
 * 
 * @author xcy
 * view Finder
 */
public abstract class ViewFinder {
	
	public abstract View FindViewById(int id);
	
	public static ViewFinder create(Activity activity){
		return new ActivityViewFinder(activity);
	}
	
	public static ViewFinder create(View view)
	{
		return new ViewViewFinder(view);
	}
	
	public static class ActivityViewFinder extends ViewFinder{

		private Activity mActivity;
		public ActivityViewFinder(Activity activity){
			super();
			this.mActivity=activity;
		}
		@Override
		public View FindViewById(int id) {
			// TODO Auto-generated method stub
			return this.mActivity.findViewById(id);
		}
		
	}

	public static class ViewViewFinder extends ViewFinder{

		private View mView;
		public ViewViewFinder(View view){
			super();
			this.mView=view;
		}
		@Override
		public View FindViewById(int id) {
			// TODO Auto-generated method stub
			return this.mView.findViewById(id);
		}
		
	}
}
