package com.jimmy.htmlplayer.ui.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jimmy.htmlplayer.R;
import com.jimmy.htmlplayer.businesslogic.services.MessageService;
import com.jimmy.htmlplayer.datahandlers.pojo.HTMLObject;
import com.jimmy.htmlplayer.ui.UIConstants;
import com.jimmy.htmlplayer.ui.util.PDFOpen;
import com.jimmy.htmlplayer.ui.views.activities.ViewerActivity;
import com.jimmy.htmlplayer.ui.views.adapters.MyPageChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.jimmy.htmlplayer.ui.UIConfig.chapTitlesArr;
import static com.jimmy.htmlplayer.ui.UIConfig.isFirstRun;
import static com.jimmy.htmlplayer.ui.UIConfig.selectedSet;
import static com.jimmy.htmlplayer.ui.UIConfig.setsList;


public class ScreenSlidePageFragment extends Fragment implements OnTouchListener {

	public WebView webView;
	public ImageView imageView;
	private int currentPos = 0;
	public String urlLoadedOnTouch;
	private RelativeLayout rlayout;
	private ViewGroup container_;
	private HashMap selectedSetMap;
	private String selectedHTML;
	private ArrayList mapTitlesArray;
    private Intent genricIntent;

    public static ScreenSlidePageFragment newInstance(int position) {

		ScreenSlidePageFragment webFrag = new ScreenSlidePageFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt(UIConstants.KEY_CURRENT_FRAGMENT_POSITION, position);
		webFrag.setArguments(args);
		return webFrag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//if(savedInstanceState == null){
		    currentPos = getArguments().getInt(UIConstants.KEY_CURRENT_FRAGMENT_POSITION);

        // }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


		//Log.e("current pos", currentPos + "< is it from savedIns " + savedInstanceState);

		container_ = container;

		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.fragment_screen_slide_page, container, false);

		rlayout = (RelativeLayout) rootView.findViewById(R.id.rlout);
		webView = (WebView) rootView.findViewById(R.id.webView1);

		rlayout.setVisibility(View.VISIBLE);
		webView.setVisibility(View.GONE);
		setWebViewSettings(webView);

		imageView = (ImageView) rootView.findViewById(R.id.imageView1);
		imageView.setBackgroundResource(R.drawable.loadinganime);
		AnimationDrawable gifAnimation = (AnimationDrawable) imageView.getBackground();
		gifAnimation.start();

//

		 selectedSetMap =  ( (HashMap ) setsList.get(selectedSet));

		 mapTitlesArray = (ArrayList ) selectedSetMap.get( chapTitlesArr[selectedSet - 1]);
		Log.e("current pos", currentPos + "< is it from savedIns " + savedInstanceState + "," + selectedSet + ","+
				mapTitlesArray.size());
		 if(currentPos >= mapTitlesArray.size()){
			 currentPos = 0;
		 }
		selectedHTML =  ((HTMLObject)((ArrayList ) selectedSetMap.get( chapTitlesArr[selectedSet - 1] ))
				.get(currentPos)).getHtml();

		Log.e("what to load","set: " + selectedSet + "/ pos:" + currentPos + "/" +selectedHTML +
				" vs " + ((HTMLObject)((ArrayList ) selectedSetMap.get( chapTitlesArr[selectedSet - 1] )).get(0)).getHtml()
				+ " size-all: " + selectedSetMap.size());

		webView.loadUrl(selectedHTML);

		return rootView;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewSettings(WebView webView) {

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
		// webView.getSettings().setDefaultFontSize(24);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
//		webView.getSettings().setBuiltInZoomControls(true);
//		webView.getSettings().setSupportZoom(true);
		webView.setBackgroundColor(0xcccccc);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webView.setTag(currentPos);
		webView.setInitialScale(0);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// Log.d("ScreenSlidePageFragment - shouldOverrideUrlLoading ",
				// "web view page loaded url:" + url);

				int extPntInd = url.lastIndexOf(".");
				int assetPathInd = ("file:///android_asset/").lastIndexOf("/");

				String extension = url.substring(extPntInd);

				String path;
				String folder;
				String filNm;

				// //Log.d("the path: ", path+ "---- the extension: " + ext +
				// " -------------- the folder: " + folder +
				// "------ the file name and ext: " + filNm);
				if (extension.equals(".pdf")) {

					path = url.substring(assetPathInd + 1);
					folder = path.substring(0, path.lastIndexOf("/"));
					filNm = path.substring(path.lastIndexOf("/") + 1);

					PDFOpen pdfins = new PDFOpen(getActivity(), filNm);

					return true;
				} else if (extension.equals(".mp4")) {

					assetPathInd = ("file:///res/raw/").lastIndexOf("/");
					// file:///res/raw/
					path = url.substring(assetPathInd + 1);
					folder = (path.contains("/")) ? path.substring(0,
							path.lastIndexOf("/")) : "";
					filNm = path.substring(path.lastIndexOf("/") + 1);
					String filnnNoExt = filNm.substring(0, filNm.indexOf("."));
					// Log.d("the path: ", path+ "---- the extension: " + ext +
					// " -------------- the folder: " + folder +
					// "------ the file name and ext: " + filNm +
					// " the fiel name only without extension : " + filnnNoExt);

					VideoPlayerFragment cngDi = VideoPlayerFragment
							.newInstance(filnnNoExt);
					cngDi.show(getActivity().getFragmentManager(),
							"videoplayerfragment");

				} else if (extension.equals(".html") || extension.equals(".htm")) {

					boolean foundURL = false;

					// quick search in the range of the current selected set
					if (!TextUtils.isEmpty(url) && selectedSetMap.size() > 1) {
						if (!url.equals(selectedHTML)) {// prevent reloading of the current html
							 // search in the current set category
							for (int i = 0; i < selectedSetMap.size(); i++) {
								String htmlSlid = ((HTMLObject) ((ArrayList) selectedSetMap.get(chapTitlesArr[selectedSet - 1])).get(i)).getHtml();
								if (url.equals(htmlSlid)) {

									// Log.d("UPDATE THE POSTION OF THE VIEW PAGER FORCEBLY",
									// "the matching position to update to is: "
									// + i +"------------------------");

									foundURL = true;
									MyPageChangeListener.updateView(i);
									return true;
								}
							}
						}
					}
					if(!foundURL){// not found in current set do in depth search on all stored htmls

						catScan : for (int i = 1; i <= setsList.size(); i++) {

							if(selectedSet != i ){// skip current set since quick scan didn't find matches

								HashMap<String, ArrayList<HTMLObject>> currCatHashMapSet = setsList.get(i);

								for (ArrayList<HTMLObject> value : currCatHashMapSet.values()) {
									for (int j = 0; j < value.size(); j++) {

										if(url.equals( (value.get(j)).getHtml())){
											foundURL = true;

											try {
											    Log.e("SSSSSSSSSSS", "START SERVICE");
												 genricIntent = new Intent(getActivity(), MessageService.class);
												genricIntent.putExtra(UIConstants.KEY_MESSAGE_CATEGORY_EXTRA, i);
												genricIntent.putExtra(UIConstants.KEY_MESSAGE_SELECTED_EXTRA, j);
												getActivity().startService(genricIntent);

                                                return true;
											} catch (NullPointerException e) {
												e.printStackTrace();
											}
										}

									}
								}
							}
						}
						// it's a generic page just load it
						webView.loadUrl(url);
					}
				}

				return true;
			}
		});

	}

    @Override
    public void onPause() {
	    getActivity().stopService(new Intent(getActivity(), MessageService.class));
        super.onPause();
    }

    @Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (isFirstRun) {
			isFirstRun = false;

			String initialHtml =  ((HTMLObject)((ArrayList) selectedSetMap.get(chapTitlesArr[selectedSet - 1])).get(0)).getHtml();
			rlayout.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
//			webView.loadUrl(initialHtml);
//			urlLoadedOnTouch = initialHtml;
		} else {

//			Log.e("nextPos", currentPos + "");
//			Log.e("PagerFragment.pagerPos", PagerFragment.mViewPager.getCurrentItem() + "");
			
			if (currentPos == PagerFragment.mViewPager.getCurrentItem()) {
				rlayout.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
			}
		}
	}

/*	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(UIConstants.KEY_CURRENT_FRAGMENT_POSITION, currentPos);
	}*/

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		urlLoadedOnTouch = webView.getUrl();

		/*
		 * if(urlLoadedOnToch != null && MainActivity.GalLinks.length > 1){
		 * if(!urlLoadedOnToch.equals(MainActivity.GalLinks[pos])){
		 * //Log.d("NONE MATCHING URLS FOUND", urlLoadedOnToch
		 * +"------------------------" + MainActivity.GalLinks[pos]);
		 * 
		 * for ( int i = 0 ; i < MainActivity.GalLinks.length; i++){
		 * if(urlLoadedOnToch.equals(MainActivity.GalLinks[i])){
		 * 
		 * //Log.d("UPDATE THE POSTION OF THE VIEW PAGER FORCEBLY",
		 * "the matching position to update to is: " + i
		 * +"------------------------");
		 * 
		 * MyPageChangeListener.updateViee(i); break;
		 * 
		 * } }
		 * 
		 * 
		 * } }
		 */

		return false;
	}

	public ImageView getImgView() {
		return imageView;
	}

	public WebView getwebView() {
		return webView;
	}

	public RelativeLayout getDecView() {
		return rlayout;
	}

}
