package com.jimmy.htmlplayer.businesslogic.json;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadLocalJSON {


	
	public static JSONObject loadJSONFromAsset(Context cntxt, String file) {
	    String json = null;
		JSONObject jsoonOb = null;
	    try {
	        InputStream is = cntxt.getAssets().open(file + ".json");
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        json = new String(buffer, "UTF-8");
			jsoonOb = new JSONObject(json);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } catch (JSONException e) {
			e.printStackTrace();
		}
		return jsoonOb;
	}

}
