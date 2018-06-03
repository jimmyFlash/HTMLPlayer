package com.jimmy.htmlplayer.ui.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.jimmy.htmlplayer.ui.UIConstants;

import java.io.File;
import java.util.List;

public class PDFOpen {
	
	private Activity  actv_;
	

	private String foldNm_;
	
	public PDFOpen(Activity actv , String filnm){
		
		actv_ = actv;
		//foldNm_ = filnm.substring(0, filnm.lastIndexOf("/"));
		
		File outfoldr = new File(actv.getExternalFilesDir(null), UIConstants.copyFoldNm);
		if (outfoldr.exists()) {
			
			File pdf  = new File(actv.getExternalFilesDir(null), UIConstants.copyFoldNm + File.separator + filnm);
			
			PackageManager packageManager = actv.getPackageManager();

			Intent testIntent = new Intent(Intent.ACTION_VIEW);
			testIntent.setType("application/pdf");
			
			
			List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
			

			if (list.size() > 0 && pdf.isFile()) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				Uri uri = Uri.fromFile(pdf);
				
				intent.setDataAndType(uri, "application/pdf"); 
				actv.startActivity(intent);
				
			}else {
				
				Log.i(" opening pdf " , "something wrong");
				
				Toast.makeText(actv, "No pdf viewer application found on system or pdf not found " , Toast.LENGTH_LONG).show();
			}
			
	    }else{
	    	Log.d(" folder doesn't exit  " , "containing folder doesn't exist");
	    }
		
		
	}
	
}
