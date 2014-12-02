package com.xcy.xpassword.database;

import java.util.ArrayList;
import java.util.List;

import com.xcy.xpassword.model.Password;
import com.xcy.xpassword.model.Category;
import com.xcy.xpassword.ui.XPasswordActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class PasswordDatabase extends SQLiteOpenHelper
{
	private static final int version = 1;
    private Context context;
    DESCrypt des=null;
	public PasswordDatabase(Context context)
	{

	
	
		
		super(context, "password", null, version);
		des=new DESCrypt();
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

	
		String sql = "create table password(" + "id integer primary key autoincrement, " + "create_date integer, "
				+ "title text, " + "iconid integer, "+"category integer," +"url text,"+ "account text, " + "password text, " + "is_top integer default 0, " + "note text)";
		db.execSQL(sql);
	
		String sql2= "create table category(" + "id integer primary key autoincrement, " + "gindex  integer, "+ "name text, " + "iconid integer)";
		db.execSQL(sql2);

		
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion < 1)
		{
			String sql = "alter table password add is_top integer default 0";
			db.execSQL(sql);
		}
	}

	public long insertPassword(Password password)
	{
		long id = -1;
		try
		{
			SQLiteDatabase sqLiteDatabase = getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("create_date", password.getCreateDate());
			contentValues.put("title", password.getTitle());
			
			contentValues.put("iconid", password.getIconID());
			
			contentValues.put("category", password.getCategory().getIndex());
			contentValues.put("url", password.getUrl());
			
			contentValues.put("account", password.getAccount());
			
			//Log.v("xcy","before  encrypt ="+password.getPassword());
			
			String cipher=des.encrypt(password.getPassword());
			//Log.v("xcy","after encrypt ="+ ciper);
			
			//Log.v("xcy","after decrypt ="+ des.decrypt(ciper));
			
			
			contentValues.put("password", cipher);
			contentValues.put("note", password.getNote());
			contentValues.put("is_top", password.isTop() ? 1 : 0);
			id = sqLiteDatabase.insert("password", null, contentValues);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}

	public long insertCategory(Category category)
	{
		long id = -1;
		try
		{
			SQLiteDatabase sqLiteDatabase = getWritableDatabase();
			ContentValues values =new ContentValues();
			values.put("gindex", category.index);
			values.put("name",category.name);
			values.put("iconid", category.iconID);
			
			id=sqLiteDatabase.insert("category", null, values);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * update password
	 * 
	 * Password password = new Password();
	 * password.setId(123);
	 * password.setPassword(&quot;�µ�����&quot;);
	 * passwordDatabase.updatePassword(password);
	 * </code>
	 * @param password
	 *        
	 * @return the number of rows affected
	 */
	public int updatePassword(Password password)
	{
		int result = 0;
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		try
		{
			ContentValues contentValues = new ContentValues();
			if (password.getCreateDate() != 0)
				contentValues.put("create_date", password.getCreateDate());
			if (password.getTitle() != null)
				contentValues.put("title", password.getTitle());
			
			if (password.getIconID() != 0)
				contentValues.put("iconid", password.getIconID());
			
			if (password.getCategory() != null)
				contentValues.put("category", password.getCategory().getIndex());
			
			if (password.getUrl() != null)
				contentValues.put("url", password.getUrl());
			
			if (password.getAccount() != null)
				contentValues.put("account", password.getAccount());
			if (password.getPassword() != null)
			{
				String cipher=des.encrypt(password.getPassword());
							
				contentValues.put("password", cipher);
			}
			//	contentValues.put("password", password.getPassword());
			if (password.getNote() != null)
				contentValues.put("note", password.getNote());
			contentValues.put("is_top", password.isTop() ? 1 : 0);
			result = sqLiteDatabase.update("password", contentValues, "id = ?",
					new String[] { String.valueOf(password.getId()) });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}
	public int updateCategory(Category	category)
	{
		int result = 0;
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		try
		{
			ContentValues contentValues = new ContentValues();
			if (category.index != -1)
				contentValues.put("gindex", category.index);
			if (category.name != null)
				contentValues.put("name", category.name);
			
			if (category.getIconID() != 0)
				contentValues.put("iconid", category.getIconID());
			
			
			result = sqLiteDatabase.update("category", contentValues, "id = ?",
					new String[] { String.valueOf(category.id )});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}
	public Password getPassword(int id)
	{
		Password password = null;

		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		Cursor cursor = null;
		try
		{
			cursor = sqLiteDatabase.query("password", null, "id = ?", new String[] { String.valueOf(id) }, null, null,
					null);

			if (cursor.moveToNext())
			{
				password = new Password();
				password.setId(cursor.getInt(cursor.getColumnIndex("id")));
				password.setIconID(cursor.getInt(cursor.getColumnIndex("iconid")));
				password.setCreateDate(cursor.getLong(cursor.getColumnIndex("create_date")));
				password.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				password.setCategory(XPasswordActivity.gCategorys.get(cursor.getInt(cursor.getColumnIndex("category"))));
				password.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				
				password.setAccount(cursor.getString(cursor.getColumnIndex("account")));
				
				String plain=des.decrypt(cursor.getString(cursor.getColumnIndex("password")));
				password.setPassword(plain);
				
				password.setNote(cursor.getString(cursor.getColumnIndex("note")));
				password.setTop(cursor.getInt(cursor.getColumnIndex("is_top")) == 1 ? true : false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}

		return password;
	}
	public Category getCategory(int id)
	{
		Category category = null;

		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		Cursor cursor = null;
		try
		{
			cursor = sqLiteDatabase.query("category", null, "id = ?", new String[] { String.valueOf(id) }, null, null,
					null);

			if (cursor.moveToNext())
			{
				category = new Category();
				category.id=(cursor.getInt(cursor.getColumnIndex("id")));
				 category.setIconID(cursor.getInt(cursor.getColumnIndex("iconid")));
			
				
				 category.setIndex(cursor.getInt(cursor.getColumnIndex("gindex")));
				 category.setName(cursor.getString(cursor.getColumnIndex("name")));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}

		return category;
	}

	
	public List<Password> getAllPassword()
	{
		List<Password> passwords = new ArrayList<Password>();
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();

		Cursor cursor = null;

		try
		{
			cursor = sqLiteDatabase.query("password", null, null, null, null, null, null);

			while (cursor.moveToNext())
			{
				Password password = new Password();
				password.setId(cursor.getInt(cursor.getColumnIndex("id")));
				password.setIconID(cursor.getInt(cursor.getColumnIndex("iconid")));
			
				
				password.setCreateDate(cursor.getLong(cursor.getColumnIndex("create_date")));
				password.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		
				
				password.setCategory(XPasswordActivity.gCategorys.get(cursor.getInt(cursor.getColumnIndex("category"))));
				
				password.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				
				password.setAccount(cursor.getString(cursor.getColumnIndex("account")));
				
				String plain=des.decrypt(cursor.getString(cursor.getColumnIndex("password")));
				password.setPassword(plain);
				
				//password.setPassword(cursor.getString(cursor.getColumnIndex("password")));
				password.setNote(cursor.getString(cursor.getColumnIndex("note")));
				password.setTop(cursor.getInt(cursor.getColumnIndex("is_top")) == 1 ? true : false);

				passwords.add(password);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}

		return passwords;

	}

	public List<Category> getAllCategory()
	{
		List<Category> categorys = new ArrayList<Category>();
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();

		Cursor cursor = null;

		try
		{
			cursor = sqLiteDatabase.query("category", null, null, null, null, null, null);

			while (cursor.moveToNext())
			{
				Category category = new Category();
				 category.id=(cursor.getInt(cursor.getColumnIndex("id")));
				 category.setIconID(cursor.getInt(cursor.getColumnIndex("iconid")));
			
				
				 category.setIndex(cursor.getInt(cursor.getColumnIndex("gindex")));
				 category.setName(cursor.getString(cursor.getColumnIndex("name")));
		
				 categorys.add(category);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}

		return categorys;

	}

	

	/**
	 * delete password
	 * 
	 * @param id
	 *            
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int deletePasssword(int id)
	{
		int result = -1;
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		result = sqLiteDatabase.delete("password", "id = ?", new String[] { String.valueOf(id) });
		return result;
	}
	public int deleteCategory(int id)
	{
		int result = -1;
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		result = sqLiteDatabase.delete("category", "id = ?", new String[] { String.valueOf(id) });
		return result;
	}
}
