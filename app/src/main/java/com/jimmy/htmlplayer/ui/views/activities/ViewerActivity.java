package com.jimmy.htmlplayer.ui.views.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jimmy.htmlplayer.PermissionManager;
import com.jimmy.htmlplayer.R;
import com.jimmy.htmlplayer.businesslogic.json.JsonValidator;
import com.jimmy.htmlplayer.businesslogic.json.LoadLocalJSON;
import com.jimmy.htmlplayer.datahandlers.pojo.HTMLObject;
import com.jimmy.htmlplayer.ui.UIConstants;
import com.jimmy.htmlplayer.ui.util.CopyPDFDirectory;
import com.jimmy.htmlplayer.ui.views.fragments.PagerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.jimmy.htmlplayer.ui.UIConfig.ASSETS_URI;
import static com.jimmy.htmlplayer.ui.UIConfig.chapTitlesArr;
import static com.jimmy.htmlplayer.ui.UIConfig.selectedSet;
import static com.jimmy.htmlplayer.ui.UIConfig.setsList;

/**
 * Created by jamal.safwat on 9/19/2016.
 */




public class ViewerActivity extends AppCompatActivity {


    private FrameLayout continaerFrame;

    private JSONObject rawJson;// json object created from loaded json stirng


    private BroadcastReceiver mMessageReceiver;

    // the main list of html sections with all it's session
    private  List<List<HTMLObject>> htmlGroup;


    // value object for htmlGroup of workshop-day
    private HTMLObject htmlObj;

    private String TAG = ViewerActivity.class.getSimpleName();
    //private HashMap< Integer, HashMap<String, ArrayList<String> > > finalmap;
    private HashMap< Integer, HashMap<String, ArrayList<HTMLObject> > > finalmap;
    private TabLayout tabLayout;

    private static final int  REQUEST_MULTI_PERMISSION = 10;
    private PermissionManager pm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        continaerFrame = (FrameLayout) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        if(savedInstanceState == null){

            // load the json string and parse in AsyncTask
            new LoadJson(UIConstants.jsonFileName).execute();
            // Our handler for received Intents. This will be called whenever an Intent
            // with an action named "custom-event-name" is broadcasted.
            mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().equals(UIConstants.KEY_MESSAGE_FILTER_INTENT)){
                        // Get extra data included in the Intent
                        int cat = intent.getIntExtra(UIConstants.KEY_MESSAGE_CATEGORY_EXTRA, 0);
                        int selectdItm = intent.getIntExtra(UIConstants.KEY_MESSAGE_SELECTED_EXTRA, 0);
                        Log.e("receiver", "Got message: " + cat + "," + selectdItm);


                        setCurrentTabFragment(cat, selectdItm);

                    }

                }
            };
        }else{
            for (int i = 0 ; i < chapTitlesArr.length ; i++){
                tabLayout.addTab(tabLayout.newTab().setText(chapTitlesArr[i]), i == (selectedSet -1 ));
            }
            bindWidgetsWithAnEvent();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);

        super.onPause();
    }


    @Override
    protected void onResume() {

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(UIConstants.KEY_MESSAGE_FILTER_INTENT));

        super.onResume();
    }


    private void setInitialFragment(int slideNo) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, PagerFragment.newInstance(slideNo))
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    class LoadJson extends AsyncTask <String, String, JSONObject> {

        private final String fileJsonNm;
        private JSONObject theJson;

        public LoadJson(String fileNm) {

            fileJsonNm = fileNm;
        }

        @Override
        protected JSONObject doInBackground(String... params) {



            try {
                theJson = LoadLocalJSON.loadJSONFromAsset(ViewerActivity.this, fileJsonNm);
            }catch  (Exception e) {
                theJson = null;
            }

            return theJson;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if(jsonObject != null){
                rawJson = jsonObject;

                // check the loaded json has key with matching name "Content"
                boolean filesArr = JsonValidator.contains(rawJson,UIConstants.KEY_CONTENT);

                // check the loaded json has key with matching name "Sections"
                boolean basicSectionArr = JsonValidator.contains(rawJson,UIConstants.KEY_SECTIONS);

                // if both keys are found
                if(filesArr && basicSectionArr){

                    // final map of chapters
                     finalmap  = new HashMap<>();

                    try {

                       // HashMap<String, ArrayList<String>>  hMap = new HashMap<>();
                        HashMap<String, ArrayList<HTMLObject>>  hMap = new HashMap<>();

                        // extract the array of section headlines
                        JSONArray totalarr = rawJson.getJSONArray(UIConstants.KEY_SECTIONS);

                        chapTitlesArr = new String[totalarr.length()];

                        // extract the array of content htm pages
                        JSONArray slidesArr = rawJson.getJSONArray(UIConstants.KEY_CONTENT);

                        // loop for the total number of sections
                            for ( int i = 0 ; i < totalarr.length(); i++){

                                // per section extracts the json object in the totalarr array
                                JSONObject jsO =  totalarr.getJSONObject(i);

                                // get each section unique id
                                String sectionId = jsO.getString(UIConstants.KEY_SECTION_ID);
                                String sectionTitle = jsO.getString(UIConstants.KEY_SECTION_NAME);
                                String sectionThumb = jsO.getString(UIConstants.KEY_SECTION_THUMB);

                                tabLayout.addTab(tabLayout.newTab().setText(sectionTitle), i == (selectedSet -1 ));


                                chapTitlesArr[i] = sectionTitle;
                                // array list to store html pages per section
                                //List<String> htmlUrl = new ArrayList<>();
                                List<HTMLObject> htmlUrl = new ArrayList<>();

//                                Log.d(TAG, " the id of the section is   " + sectionId  + ", of " + totalarr.length() + " sections");

                                if(!hMap.containsKey(sectionTitle)){
                                    // populate the hash map of section content with section if and array of html files associated with it
                                    //hMap.put(sectionTitle, (ArrayList<String>) htmlUrl);
                                    hMap.put(sectionTitle, (ArrayList<HTMLObject>) htmlUrl);
                                }else{
                                    htmlUrl = hMap.get(sectionTitle);
                                }


                                // loop on the slides array
                                for (int j = 0 ; j < slidesArr.length(); j++) {

                                    // gte each json object referencing a slide
                                    JSONObject jsO2 =  slidesArr.getJSONObject(j);

                                    // populate the proxy / POJO object with slide data
                                    HTMLObject htmlObj = new HTMLObject();
                                    htmlObj.setHtml(ASSETS_URI + jsO2.getString(UIConstants.KEY_FILE_NAME));
                                    htmlObj.setId(jsO2.getString(UIConstants.KEY_SECTION));
                                    htmlObj.setThumb(jsO2.getString(UIConstants.KEY_THUMB));

                                    Log.d(TAG, " the html pojo created is of properties    " + htmlObj.getHtml()
                                            + "," + htmlObj.getId() + "," + htmlObj.getThumb());

                                    // if the current slide object id === that of the base section id
                                    if (htmlObj.getId().equalsIgnoreCase(sectionId)) {
                                        // add that slide to the slides array for the current section map
                                        //htmlUrl.add(htmlObj.getHtml());
                                        htmlUrl.add(htmlObj);
                                    }
                                }

                                // add that chapter to chapters main map
                                finalmap.put(Integer.valueOf(sectionId), hMap);

                            }

                        setsList = finalmap;

                        int size = finalmap.size();
                        Log.d(TAG, " the total number of chapters: " + size);
                        Log.d(TAG, "  chapter title: " +  chapTitlesArr[0] );
                        Log.d(TAG, " the slides in chapter: " + ((HTMLObject) ((ArrayList) ( (HashMap ) finalmap.get(selectedSet)).get( chapTitlesArr[0])).get(0)).getHtml());

                        setInitialFragment(0);

                        bindWidgetsWithAnEvent();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isAllPermissionsGranted()) {

                             pm = new PermissionManager.PermissionBuilder(ViewerActivity.this, REQUEST_MULTI_PERMISSION)
                                        .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .build();

                        }else{
                            // in a background thread copy pdf from assets folder to folder on users sd card
                            new CopyPDFDirectory(ViewerActivity.this, "pdf").execute();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(ViewerActivity.this, "Failed to parse local json file", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void bindWidgetsWithAnEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        selectedSet = tabPosition + 1;
        setInitialFragment(0);
    }


    private void setCurrentTabFragment(int tabPosition, int selectedSlide) {
        tabLayout.getTabAt(tabPosition).select();
        selectedSet = tabPosition + 1;
        setInitialFragment(selectedSlide);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_MULTI_PERMISSION:
                if(pm != null){
                    boolean allSet = pm.multiplePermissionProcessor(permissions, grantResults);
                    if(allSet){
                        new CopyPDFDirectory(ViewerActivity.this, "pdf").execute();
                        Log.e("PERMISSIOOOON", "ALL GRANTED");
                    }else{
                        ArrayList<String> deniedPerm = pm.getDeniedPermissionsList();
                        Log.e("PERMISSIOOOON", "DENIED " + deniedPerm.size() + ", " + deniedPerm.toString());
                        // can check if a certain permission you're instrested in exits is denied, if not you proceed or halt
                    }
                }
                 break;

            // permission requested from a fragment inside this activity( fragments onRequestPermissionsResult
            // is overridden by the hosting activity
            // hence we forward back the call to the fragments onRequestPermissionsResult handler
            case PermissionManager.FRAGMENT_DELEGATE_PERMISSIONS:
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                      // check if your fragment is instance of certain class or implements certain interface and do callbacks
                        boolean allSet = pm.multiplePermissionProcessor(permissions, grantResults);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean isAllPermissionsGranted() {
        boolean writeToExternalStore;

        writeToExternalStore = ContextCompat.checkSelfPermission(ViewerActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        // add more permissions if required and use logic operators in return

        return  writeToExternalStore;
    }

}
