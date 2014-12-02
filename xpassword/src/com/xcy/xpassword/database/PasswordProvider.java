package com.xcy.xpassword.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class PasswordProvider extends ContentProvider{

	public static final String DATABASE_NAME="password";
	public static final String TABLE_NAME="password";
	public static final int DATABASE_VERSION=1 ;

	public static final String authority="com.xcy.xpassword";
	public static final Uri CONTENT_URI=Uri.parse("content://"+authority+"/"+TABLE_NAME);
	
	
	private static final int CONTENT_TYPE_PASSWORD=0;
	private static final  int CONTENT_TYPE_PASSWORD_ITEM=1;
	
	SQLiteDatabase db =null;
	SQLiteOpenHelper  dbHelper=null;
	

	
	private static final  UriMatcher mUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
				mUriMatcher.addURI(authority, "password", CONTENT_TYPE_PASSWORD);
				mUriMatcher.addURI(authority, "password/#", CONTENT_TYPE_PASSWORD_ITEM);
	}
	
	@Override
	public boolean onCreate() {
		dbHelper=new DatabaseHelper(getContext());
		db=dbHelper.getWritableDatabase();
		
		
		return  db==null ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		     db=dbHelper.getReadableDatabase();
		 	Cursor cursor=null;
			switch(mUriMatcher.match(uri)){
				case CONTENT_TYPE_PASSWORD:
					cursor=db.query(TABLE_NAME, projection, selection, selectionArgs, null, null,null);
					break;
				case CONTENT_TYPE_PASSWORD_ITEM:
					String id=uri.getPathSegments().get(1);
					cursor=db.query(TABLE_NAME, projection	,"id="+id+(!TextUtils.isEmpty(selection) ? " AND (" +selection + ")"  : ""), selectionArgs, null, null, null);
					 break;
				 default :
					 throw new IllegalArgumentException("Unknown URI" + uri);
			}
			  cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(mUriMatcher.match(uri))
		{
		
	       case CONTENT_TYPE_PASSWORD :
               return "vnd.android.cursor.dir/password";
           case CONTENT_TYPE_PASSWORD_ITEM :
               return "vnd.android.cursor.item/password";
		
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
    
		Uri nUri=null;
		db=dbHelper.getWritableDatabase();
	    if(mUriMatcher.match(uri)!= CONTENT_TYPE_PASSWORD)
	    {
	    	throw new IllegalArgumentException("Unavaliable URI  "+ uri);
	    }
		long id=db.insert(TABLE_NAME, null, values);
		if(id>0)
		{
			nUri=ContentUris.withAppendedId(CONTENT_URI, id);
		}
		
		return nUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	
		db=dbHelper.getWritableDatabase();
		int result=-1;
		switch(mUriMatcher.match(uri))
		{
		case CONTENT_TYPE_PASSWORD:
			result=db.delete(TABLE_NAME, selection, selectionArgs);
			break;
		case CONTENT_TYPE_PASSWORD_ITEM:
			 String id =uri.getPathSegments().get(1);
			 result=db.delete(TABLE_NAME, "id="+id+(!TextUtils.isEmpty(selection) ? " AND ("+selection+")" : ""), selectionArgs);
			 break;
				
		}
		
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
				db=dbHelper.getWritableDatabase();
				db.update(TABLE_NAME, values, selection, selectionArgs);
		
		return 0;
	}

	static  class   DatabaseHelper extends SQLiteOpenHelper
	{
	
		  public DatabaseHelper(Context context)
		  {
			  super(context, DATABASE_NAME, null, DATABASE_VERSION);;
		  }
		  
			public DatabaseHelper(Context context, String name, CursorFactory factory,
					int version) {
				super(context, name, factory, version);

			}
	
			@Override
			public void onCreate(SQLiteDatabase db) {
//				String sql = "create table password(" + "id integer primary key autoincrement, " + "create_date integer, "
//						+ "title text, " + "iconid integer, "+"category integer," +"url text,"+ "account text, " + "password text, " + "is_top integer default 0, " + "note text)";
//				db.execSQL(sql);
			}
	
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

				if (oldVersion < 1)
				{
					String sql = "alter table password add is_top integer default 0";
					db.execSQL(sql);
				}
			}
			
		}
}
