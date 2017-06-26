package com.breeze.joyjogger;


import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
 
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class SettingsActivity extends Activity 
				implements ProfileSettingsDialog.Listener,
		ProfileWheelDialog.Listener{
 
	LayoutInflater mLi;
	ProfileItem mPi = null;
	StorageItem mSi = null;	
 
	public static SettingsActivity mInstance = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_settings);
		mInstance = this;
		initUi();
	}	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mInstance = null;
	} 
	private void initUi(){
		mLi = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mPi = new ProfileItem();
		mSi = new StorageItem();
 
		ListView lv = (ListView) findViewById(R.id.lvProfile);		   	 	
		lv.setAdapter(mPi );
		mPi.notifyDataSetChanged();        
		ListView lv2 = (ListView) findViewById(R.id.lvProfile2);		   	 	
		lv2.setAdapter(mSi );
      
 
		
		updateUi();
	}//end initUi()    
	void updateUi(){
		
	}

	//sensitivity
    void change0(){
		ProfileSettingsDialog dlg = new ProfileSettingsDialog(this);
        dlg.mType = ProfileSettingsDialog.SPORT_TYPE;
        dlg.mListener = this;
		dlg.show();
		  	
    }
    void change1(){
		ProfileWheelDialog dlg = new ProfileWheelDialog(this);
        dlg.mType = ProfileWheelDialog.MEASURE;
        dlg.mListener = this;
		dlg.show();	  	
    }    
    //stepsize
    void change2(){
		ProfileWheelDialog dlg = new ProfileWheelDialog(this);
        dlg.mType = ProfileWheelDialog.STEP_SIZE;
        dlg.mListener = this;
        dlg.show();
    }

    //sleep mode
    void change3(){
         ProfileSettingsDialog dlg = new ProfileSettingsDialog(this);
        dlg.mType = ProfileSettingsDialog.NO_MOTION;
        dlg.mListener = this;
        dlg.show();
			
    }    
    public class ProfileItem extends BaseAdapter {
		int MAX_ITEM_COUNTS = 4;
		@Override
		public int getCount() {
			return  MAX_ITEM_COUNTS;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			if(position >=  MAX_ITEM_COUNTS)
				return -1;
			else return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout itemView; //xml is LinearLayout not relativeLayout
     		
     	   if (convertView == null) {
     	        itemView = (LinearLayout) mLi.inflate(
     	        		R.layout.profile_item, parent, false);
     	    } else {
     	    	itemView = (LinearLayout) convertView;
     	    }		
  
     		if(position >= MAX_ITEM_COUNTS)
     			return null;
     		
     	    TextView tv1 = (TextView) itemView.findViewById(R.id.tv_title);
     	    TextView tv2 = (TextView) itemView.findViewById(R.id.tv_value);
     	    TextView tv3 = (TextView) itemView.findViewById(R.id.tv_unit); 
 	 	    
     	    switch(position){
     	    case 0: //sensitivity
     	    	int type = Configure.getCurrentMotionType();
     	    	tv1.setText(R.string.settings_motion_type);
     			if(type == 0){
     				tv2.setText(R.string.settings_type_walk);
     			}
     			else if (type == 1){
     				tv2.setText(R.string.settings_type_jog);
     			}
     			else{
     				tv2.setText(R.string.settings_type_run);   				
     			}
    
     			tv3.setText(""); 
     			itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
      					change0();				
     				}
     			});
     	    	break;
 
     	    case 1: //unit
     	    	tv1.setText(R.string.settings_measure_system);
     	    	if(Configure.system == 0)
     	    		tv2.setText(R.string.system_metric);
     	    	else
     	    		tv2.setText(R.string.system_english);
     			tv3.setText("");  
     			itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					change1();				
     				}
     			});     			
     	    	break;     	      
     	    case 2: //step size
     	    	tv1.setText(R.string.settings_step_distance);
                if(RootActivity.bEnglish) {
                    tv2.setText(String.format("%.1f", Configure.stepSize / 2.54f));
                    tv3.setText(R.string.str_inch);
                }
                else {
                    tv2.setText(Float.toString(Configure.stepSize));
                    tv3.setText(R.string.str_cm);
                }
     			itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					change2();				
     				}
     			});     			
     	    	break;
     	    case 3: //sleep mode
     	    	tv1.setText(R.string.settings_minutes_to_sleep);
     	    	int nSleep = Configure.sleepAfterNoMotionOption;
     	    	if(nSleep>= Configure.sSleepTimeOptions.length)
     	    		nSleep = Configure.DEFAULT_SLEEP_TIME_OPTION;
     	    	String szUnit =  getResources().getString(R.string.str_minute);
     	    	
     	    	tv2.setText(Integer.toString(Configure.sSleepTimeOptions[nSleep]));
     			tv3.setText(szUnit);  
     			itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					change3();				
     				}
     			});     			
     	    	break;     	    	
 	    	default:
 	    		return null;
     	    }
    		return itemView;
		}
		
	}
    ///////////////////////////////////////////////////////
    //change days keep
    void change20(){
        ProfileSettingsDialog dlg = new ProfileSettingsDialog(this);
        dlg.mType = ProfileSettingsDialog.KEEP_RECORD;
        dlg.mListener = this;

		dlg.show();	
		
  	    	
    }       
    void change21(){
        ProfileSettingsDialog dlg = new ProfileSettingsDialog(this);
        dlg.mType = ProfileSettingsDialog.FREE_SPACE;
        dlg.mListener = this;


		dlg.show();	      
    }
    public class StorageItem extends BaseAdapter {

 		int MAX_ITEM_COUNTS = 3;
 		@Override
 		public int getCount() {
 			return  MAX_ITEM_COUNTS;
 		}

 		@Override
 		public Object getItem(int position) {
 			return null;
 		}

 		@Override
 		public long getItemId(int position) {
 			if(position >=  MAX_ITEM_COUNTS)
 				return -1;
 			else return position;
 		}
 		
 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			LinearLayout itemView; //xml is LinearLayout not relativeLayout
       		
      	   if (convertView == null) {
      	        itemView = (LinearLayout) mLi.inflate(
      	        		R.layout.profile_item, parent, false);
      	    } else {
      	    	itemView = (LinearLayout) convertView;
      	    }		
   
      		if(position >= MAX_ITEM_COUNTS)
      			return null;
      		
      	    TextView tv1 = (TextView) itemView.findViewById(R.id.tv_title);
      	    TextView tv2 = (TextView) itemView.findViewById(R.id.tv_value);
      	    TextView tv3 = (TextView) itemView.findViewById(R.id.tv_unit); 
  	 	    
      	    switch(position){
      	    case 0: //keepDataDays
     	
      	    	tv1.setText(R.string.settings_record_keep_days);
      	    	String[] opts = Configure.getKeepDataOptionsString();
      	    	tv2.setText(opts[Configure.getKeepDataOption()]);     			   
      			tv3.setText(""); 
      			itemView.setOnClickListener(new OnClickListener(){
      				@Override
      				public void onClick(View v) {
       					change20();				
      				}
      			});
      	    	break;
  
      	    case 1: //freespace
      	    	tv1.setText(R.string.settings_mini_free_space);
      	    	String text = Integer.toString(Configure.freeSpaceMB) + getResources().getString(R.string.str_mb);
      	    	tv2.setText(text);

      			itemView.setOnClickListener(new OnClickListener(){
      				@Override
      				public void onClick(View v) {
      					change21();
      				}
      			});     			
      	    	break;
                case 2: //freespace
                    tv1.setText(R.string.str_current_free_space);
                    text= String.format("  %d/%d MB", DayRecord.getFreeSapceMb(),
                            DayRecord.getTotalSapceMb() );

                    tv2.setText(text);

                    break;
                default:
  	    		return null;
      	    }
     		return itemView;
 		}
 		
 	}
	@Override
	public void onProfileChanged() {
		// not from this activity
		
	}
	@Override
	public void onSettingsChanged() {
		mSi.notifyDataSetChanged(); 
		mPi.notifyDataSetChanged(); 
		
	}
  
}