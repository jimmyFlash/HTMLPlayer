package com.jimmy.htmlplayer.ui.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jimmy.htmlplayer.R;


public class VideoPlayerFragment extends DialogFragment implements OnCompletionListener,OnPreparedListener,OnTouchListener{
	
	
	public static void main (String [] s){
		
	}

	private String vid;
	private MediaController media_Controller;
	private DisplayMetrics dm;
	private Dialog dialog;
	
	private static  VideoPlayerFragment fragment;
	
	
	private int totalDuration;
	private SeekBar sk;
	private Handler hndl;
	private Task bgRun;
	private ImageButton plybk;
	
	private Boolean stoped = false;
	private Boolean completed = false;
	private Boolean muted = false;
	
	VideoView vido;
	TextView disp;
	private ImageButton stop;
	private LinearLayout contol;
	private ImageButton mute;
	
	private MediaPlayer mediaP;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		vid = (getArguments() != null) ? getArguments().getString("video_str") : "";
		
		// setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
	}
	
	 // Constructor
    public static VideoPlayerFragment newInstance(String  string_arg) {
    	
    	//if(fragment == null){

            fragment = new VideoPlayerFragment();

            Bundle arguments = new Bundle();
            arguments.putString("video_str", string_arg);
            fragment.setArguments(arguments);
            
    	//}    	
        

        return fragment;
    }

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		/*WindowManager.LayoutParams a = dialog.getWindow().getAttributes();
		a.dimAmount = 0;
		dialog.getWindow().setAttributes(a);*/
		
		
	}
	
	
	public void clsVid(View view){
		dialog.dismiss();
	}
	
	
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {
/*		  dialog = new Dialog(getActivity());
		  
          dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
          dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
          dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
          dialog.setCancelable(true);
          dialog.setCanceledOnTouchOutside(false);
          
          View parent= getActivity().getLayoutInflater().inflate(R.layout.video_player, null);
		  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		  dialog.setContentView(parent, layoutParams);
		  
		  vido = (VideoView) parent.findViewById(R.id.video_view);
		  Button btn = (Button) parent.findViewById(R.id.cloBtn);
		  Log.d(" the videos id to play", vid + "");
		  
		  int res = getResources().getIdentifier(vid, "raw", getActivity().getPackageName());
			
			// Set URL
	        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + res);
	        vido.setVideoURI(video);
	        
	        
//media_Controller = new MediaController(getActivity()); 
	        dm = new DisplayMetrics(); 
	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); 
	        int height = dm.heightPixels; 
	        int width = dm.widthPixels; 
	        
	        
	        Log.i("dimensions " , height + "," + width);
	        

// vido.setMediaController(media_Controller);


	        vido.setOnCompletionListener( this);
	        vido.setOnPreparedListener(this);
	        vido.setOnTouchListener( this);
			
	        vido.setZOrderOnTop(true);

	        // Start video
	        vido.start();
	        
	         return dialog;*/
	        
		
		    hndl = new Handler();
			
		    bgRun =  new Task();

	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent);
	        

	   
	        LayoutInflater inflater = getActivity().getLayoutInflater();

	        final View inflatView  = inflater.inflate(R.layout.video_player, null);
	       
	        
	          vido = (VideoView) inflatView.findViewById(R.id.video_view);
	          ImageButton btn = (ImageButton) inflatView.findViewById(R.id.cloBtn);
			  plybk = (ImageButton) inflatView.findViewById(R.id.posply);
			  disp = (TextView) inflatView.findViewById(R.id.display);
			  stop = (ImageButton) inflatView.findViewById(R.id.stpply);
			  mute = (ImageButton) inflatView.findViewById(R.id.mute);
		
			  contol = (LinearLayout) inflatView.findViewById(R.id.plyBar);
			  
			  contol.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			  
			  sk = (SeekBar) inflatView.findViewById(R.id.seekBar1);
			  sk.setProgress(0);
			  

			  sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				  int progress = 0;
				   
				  @Override
				  public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				      progress = progresValue;
				      //Toast.makeText(getActivity(), "Changing seekbar's progress: " + progress, Toast.LENGTH_SHORT).show();
				  }
				  
				 
				  @Override
				  public void onStartTrackingTouch(SeekBar seekBar) {
					  vido.pause();
					  stopRepeatingTask();
				   
				  }
				 
				  @Override
				  public void onStopTrackingTouch(SeekBar seekBar) {
					 int seekPos = seekBar.getProgress();
					 
					 
					 if(completed == true || stoped == true){
						 completed = false ;
						 stoped = false;
						 
					 }
					  
//int  videoPosition = vido.getCurrentPosition() + seekPos;
					 int  videoPosition =  seekPos;
					 vido.seekTo(videoPosition);
					 vido.start();
				
					 bgRun.run();
//				     Log.d("Covered: " , progress + "/" + seekBar.getProgress());
				    
				  }
				});

			  btn.setOnClickListener(new OnClickListener() {
				
					@Override
					public void onClick(View v) {
						stopRepeatingTask();
						dismiss();
					}
				});
			  
			  
			  mute.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						muted = !muted;
						
						if(muted == true){
							mediaP.setVolume(0.0f, 0.0f);
							mute.setImageResource(R.drawable.sndoff);
						}else{
							mediaP.setVolume(1.0f, 1.0f);
							mute.setImageResource(R.drawable.sndon);
						}
						
						
					}
				});
			  
			  
			  plybk.setOnClickListener(new OnClickListener() {
				
					@Override
					public void onClick(View v) {
						
						if(vido.isPlaying()){
							
							vido.pause();
							plybk.setImageResource(R.drawable.ply);
						} else {
							
							if(stoped == true){
								stoped = false;
								bgRun.run();
							}
							
							if(muted == true){
								muted = false;
								mediaP.setVolume(1.0f, 1.0f);
								
							}
							
							if(completed == true){
								completed = false;
								vido.seekTo(0);
								sk.setProgress(0);
								bgRun.run();
							}
							
							vido.start();
							plybk.setImageResource(R.drawable.pus);
						}
						
					}
				});
			  
			  
			  stop.setOnClickListener(new OnClickListener() {
				
					@Override
					public void onClick(View v) {
						stopRepeatingTask();
						vido.pause();
						vido.seekTo(0);
						plybk.setImageResource(R.drawable.ply);
						sk.setProgress(0);
						stoped = true;
					}
				});
				  
			    int res = getResources().getIdentifier(vid, "raw", getActivity().getPackageName());
				
				// Set URL
		        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + res);
		        vido.setVideoURI(video);

		        dm = new DisplayMetrics(); 
		        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); 
		        int height = dm.heightPixels; 
		        int width = dm.widthPixels; 
		        
		        //Log.i("dimensions " , height + "," + width);

		        vido.setOnCompletionListener( this);
		        vido.setOnPreparedListener(this);
		        vido.setOnTouchListener(this);

		        // Start video
		        vido.start();
		        bgRun.run();
		   	 	plybk.setImageResource(R.drawable.pus);
		        builder.setView(inflatView);
		        return builder.create();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		
		
		if(contol.getVisibility() == View.VISIBLE){
			contol.setVisibility(View.GONE);
		}else{
			contol.setVisibility(View.VISIBLE);
		}
	
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer vid) {
		
		mediaP = vid;
		
		 totalDuration = vid.getDuration();
		 
		 int duration = (int) (totalDuration/1000);
         int hours = duration / 3600; 
         int minutes = (duration / 60) - (hours * 60);
         int seconds = duration - (hours * 3600) - (minutes * 60) ;
         String formatted = String.format("%d:%02d:%2d", hours, minutes, seconds);
     
         
//         Log.d(" payback time", formatted);
         disp.setText(formatted);
         

         sk.setMax( totalDuration);
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		stopRepeatingTask();
		Toast.makeText(getActivity(), "Video playback complete", Toast.LENGTH_LONG).show();
		completed = true;
		
		plybk.setImageResource(R.drawable.ply);
		
	}
	
	class Task implements Runnable{
		@Override
		public void run(){
			
			hndl.post(new Runnable(){
				@Override
				public void run(){
					
					sk.setProgress(vido.getCurrentPosition());
					
					 int duration = (int) (vido.getCurrentPosition()/1000);
			         int hours = duration / 3600; 
			         int minutes = (duration / 60) - (hours * 60);
			         int seconds = duration - (hours * 3600) - (minutes * 60) ;
			         String formatted = String.format("%d:%02d:%2d", hours, minutes, seconds);

					 int duration_ = (int) (totalDuration/1000);
			         int hours_ = duration_ / 3600; 
			         int minutes_ = (duration_ / 60) - (hours_ * 60);
			         int seconds_ = duration_ - (hours_ * 3600) - (minutes_ * 60) ;
			         String formatted_all = String.format("%d:%02d:%2d", hours_, minutes_, seconds_);
			         
					 disp.setText(formatted + " / " + formatted_all);
					 hndl.postDelayed(bgRun, 100);
				}
			});
		}

	}
	
	void stopRepeatingTask() {
	  hndl.removeCallbacks(bgRun);
    }
	
	
	
}
