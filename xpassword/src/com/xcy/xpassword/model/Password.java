package com.xcy.xpassword.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.xcy.xpassword.ui.XPasswordActivity;

import android.R.integer;
import android.R.string;
import android.graphics.drawable.Drawable;

public class Password implements Serializable {

	/**
	 * password item 
	 */
	private static final long serialVersionUID = -961233794781935060L;
	
	/** id for database*/
	private int id;
	
	/** date for password */
	private long createDate;
	

	private Category category;
	

	private String title;
	

	private int iconID;
	
	public int getIconID() {
		return iconID;
	}

	public void setIconID(int iconID) {
		this.iconID = iconID;
	}


	private String url;
	

	private String account;
	
	
	private String password;
	

	private String note;

	private boolean isTop = false;
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static Password createFormJson(String json) throws JSONException
	{
		Password password = new Password();
		JSONObject jsonObject = new JSONObject(json);
		password.setId(jsonObject.optInt("id", 0));
		password.setIconID(jsonObject.optInt("iconid", 0));
		password.setCreateDate(jsonObject.getLong("createDate"));
		password.setTitle(jsonObject.getString("title"));
		password.setUrl(jsonObject.getString("url"));
		
		password.setCategory(XPasswordActivity.gCategorys.get(jsonObject.getInt("category")));
		
		password.setAccount(jsonObject.getString("account"));
		password.setPassword(jsonObject.getString("password"));
		password.setNote(jsonObject.getString("note"));
		password.setTop(jsonObject.optBoolean("isTop", false));
		return password;
	}

	public String toJSON()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("id", id);
			jsonObject.put("iconid", iconID);
			
			jsonObject.put("createDate", createDate);
			jsonObject.put("title", title);
			jsonObject.put("category", category.getIndex());
			jsonObject.put("url", url);
			jsonObject.put("account", account);
			jsonObject.put("password", password);
			jsonObject.put("note", note);
			jsonObject.put("isTop", isTop);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public boolean isTop()
	{
		return isTop;
	}

	public void setTop(boolean isTop)
	{
		this.isTop = isTop;
	}

	@Override
	public String toString()
	{
		return "Password [id=" + id + ", iconid=" + iconID + ",reateDate=" + createDate + ", title=" + title + ", url=" + url +",category="+category.getIndex() + ", userName=" + account
				+ ", password=" + password + ", note=" + note + ", isTop=" + isTop + "]";
	}
}
