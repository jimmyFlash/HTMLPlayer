package com.jimmy.htmlplayer.ui.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

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



    // the main list of html sections with all it's session
    private  List<List<HTMLObject>> htmlGroup;


    // value object for htmlGroup of workshop-day
    private HTMLObject htmlObj;

    private String TAG = ViewerActivity.class.getSimpleName();
    //private HashMap< Integer, HashMap<String, ArrayList<String> > > finalmap;
    private HashMap< Integer, HashMap<String, ArrayList<HTMLObject> > > finalmap;
    private TabLayout tabLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        continaerFrame = (FrameLayout) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        if(savedInstanceState == null){

            // load the json string and parse in AsyncTask
            new LoadJson(UIConstants.jsonFileName).execute();
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
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setInitialFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, PagerFragment.newInstance())
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



                        setInitialFragment();

                        bindWidgetsWithAnEvent();

                        // in a background thread copy pdf from assets folder to folder on users sd card
                        new CopyPDFDirectory(ViewerActivity.this, "pdf").execute();
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

        setInitialFragment();
    }
}
