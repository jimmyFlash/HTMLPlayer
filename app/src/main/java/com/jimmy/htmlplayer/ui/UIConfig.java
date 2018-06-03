package com.jimmy.htmlplayer.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.jimmy.htmlplayer.datahandlers.pojo.HTMLObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jamal on 9/30/2016.
 */

public class UIConfig {


     public static HashMap<Integer, HashMap<String, ArrayList<HTMLObject>>> setsList;
//    public static HashMap<Integer, HashMap<String, ArrayList<String>>> setsList;
    public static int selectedSet = 1;
    public static String[] chapTitlesArr;
    public static int currSlideOfSelectedSet = 0;

    public static boolean isFirstRun = true;
    public static boolean isLoadNewSet = true;
    //	public static boolean isStateChanged = false;
    public boolean isMenuOn = true;

    public static final String ASSETS_URI = "file:///android_asset/";


    /**
     * Convert dp to px unit
     * @param context
     * @param dpValue
     * @return
     */
    public static int dpTOpx(Context context, int dpValue) {
        Resources r = context.getResources();
        int val = 0;

        val = dpValue;

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                val, r.getDisplayMetrics());

        return px;
    }

}
