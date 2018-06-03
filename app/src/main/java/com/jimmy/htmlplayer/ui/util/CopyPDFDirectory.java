package com.jimmy.htmlplayer.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

public class CopyPDFDirectory extends AsyncTask<Void, Void , Void >{
	
	
	private String exportDirectory;
	private Context actv_;
	private String foldNm_;
	
	public CopyPDFDirectory(Context cntxt, String folder ){
		foldNm_ = folder;
		actv_ = cntxt;
	}
	
	

	private void CopyAssets() { 
		AssetManager assetManager = actv_.getAssets(); 
		String[] files = {}; 
		try { 
			files = assetManager.list("pdf"); 
		} catch (IOException e) { 
			Log.e("tag", e.getMessage()); 
			
		} 
		
		for(String filename : files) { 
			
		    Log.i("File name", filename);
			
			InputStream in= null; 
			OutputStream out = null; 
			try { 
				in = assetManager.open("pdf/" + filename);
				
				/*
				 getExternalFilesDir()
					It returns the path to files folder inside Android/data/data/your_package/ on your SD card.
					It is used to store any required files for your app
					(e.g. images downloaded from web or cache files).
					Once the app is uninstalled, any data stored in this folder is gone too.

				  getExternalStorageDirectory()
					It returns the root path to your SD card (e.g mnt/sdcard/).
					If you save data on this path and uninstall the app, that data won't be lost.
				 */
				
				File outfoldr = new File(actv_.getExternalFilesDir(null), foldNm_);
				if (!outfoldr.exists()) {
					outfoldr.mkdir();
			    }

				File outFile = new File(outfoldr, filename);
				// if files resides inside the "Files" directory itself 
				out = new FileOutputStream(outFile); 
				copyFile(in, out); 
				in.close();
				in = null; 
				
				out.flush(); 
				out.close();
				out = null; 
				
			} catch(Exception e) {
				Log.e("file input stream error", e.getMessage());
			}
		}

	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		
		byte[] buffer = new byte[1024]; 
		int read; 
		while((read = in.read(buffer)) != -1){ 
			out.write(buffer, 0, read); 
		} 
	}

	@Override
	protected Void doInBackground(Void... params) {
		 CopyAssets();
		return null;
	}
	
	
	
	@Override
	protected void onPostExecute(Void result) {
		Log.d("copy async task", " completed ");
	
		
		super.onPostExecute(result);
	}
	
}