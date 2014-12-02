package com.xcy.xpassword.web;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;





public class DownloaderManager {
	

	
	private long unitSize=1000*1024;
	
	private long offsize=0;
	
	
	private Context context;
	
	public DownloaderManager(Context context)
	{
		this.context=context;
		
		
	}
	public void doDownloader(String remoteUrl) throws Exception
	{
		String fileName=new URL(remoteUrl).getFile();
		
		fileName=fileName.substring(fileName.lastIndexOf("/")+1,fileName.length()).replace("%20", " ");
		
		long fileLength=getRemoteFileSize(remoteUrl);
		
		long count=fileLength/unitSize;

	   if(fileLength<=unitSize)
	   {
		   DownloadThread thread=new DownloadThread(remoteUrl,fileName,0, fileLength);
	   }else 
	   {
		   for (int i=1;i<=count;i++)
		   {
			   DownloadThread thread=new DownloadThread(remoteUrl,fileName,0, fileLength);
			   thread.start();
			   this.offsize=this.offsize+unitSize;
		   }
		   if(fileLength%unitSize!=0)
		   {
			   DownloadThread downloadThread = new DownloadThread(

                       remoteUrl, fileName,this.offsize , fileLength

                               - unitSize *count);

               downloadThread.start();
		   }
	   }
		
		
		
		
	}
	
	@SuppressWarnings("unused")
	private long getRemoteFileSize(String remoteFileUrl) throws Exception 
	{
		long fileSize=0;
		URL url= new URL(remoteFileUrl);
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(6*1000);
		
		conn.setRequestMethod("HEAD");
		
		if(conn.getResponseCode()>400)
		{
			throw new Exception("get head info failed !!!!!");
		}
		
		String sHeader ;
		for(int i=1;;i++)
		{
			sHeader=conn.getHeaderFieldKey(i);
			if(sHeader!=null&& sHeader.equals("Content-Length"))
			{
				fileSize=Long.parseLong(conn.getHeaderField(sHeader));
			     break;
			}
		}
		
		return fileSize;
	}
	
	private void createFile(String fileName,long fileSize) throws IOException
	{
		File file =new File(fileName);
		RandomAccessFile raf =new RandomAccessFile(file, "rw");
		raf.setLength(fileSize);
		raf.close();
		
	}
	

	 class DownloadThread extends Thread
	 {
		 private String Url;
		 
		 private long offsize=0;
		 
		 private long length=0;
		 
		 private String fileName;
		 
		 
		 public DownloadThread(String url,String name,long offsize, long length)
		 {
			 this.Url=url;
			 this.offsize=offsize;
			 this.length=length;
			 this.fileName=name;
		 }
		 @Override
		 public void run() 
		 {
			 		try {
						HttpURLConnection conn=(HttpURLConnection)new URL(this.Url).openConnection();
						conn.setRequestMethod("GET");
						conn.setRequestProperty("RANGE", "bytes="+this.offsize+"-"+(this.offsize+this.length -1));
						BufferedInputStream bis=new BufferedInputStream(conn.getInputStream());
						
						byte[] buff =new byte[1024];
						 int bytesRead;
						 
						 File newFile=new File(fileName);
						 
						 RandomAccessFile file =new RandomAccessFile(newFile, "rw");
						 while((bytesRead=bis.read(buff, 0, buff.length))!=-1)
						 {
							 file.seek(this.offsize);
							 file.write(buff,0,bytesRead);
							 this.offsize=this.offsize+bytesRead;
						 }
						 
						file.close();
						bis.close();
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
	
		 }
	
	
	 }
	
	

}
