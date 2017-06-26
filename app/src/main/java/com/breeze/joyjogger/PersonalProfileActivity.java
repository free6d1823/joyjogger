package com.breeze.joyjogger;

 
import java.text.SimpleDateFormat;
import java.util.Calendar;

 

import android.app.Activity;
import android.content.pm.ActivityInfo;
 
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
 
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
 
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
 
import android.widget.TextView;

public class PersonalProfileActivity extends Activity
		implements ProfileSettingsDialog.Listener,
		ProfileEditboxDialog.Listener,
		ProfileWheelDialog.Listener{
	public PersonalProfile mProfile = null; 
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_personal_profile);
		
		initUi();
	}
	public void changeGender(){
        ProfileSettingsDialog dlg = new ProfileSettingsDialog(this);
        dlg.mListener = this;
        dlg.mType = ProfileSettingsDialog.GENDER;
		dlg.show();
 
	}
	void changeName(){
        ProfileEditboxDialog dlg = new ProfileEditboxDialog(this);
        dlg.mListener = this;
        dlg.mType = ProfileEditboxDialog.NAME;

		dlg.show();
			
	}
	void changeHeight(){
        ProfileWheelDialog dlg = new ProfileWheelDialog(this);
        dlg.mListener = this;
        dlg.mType = ProfileWheelDialog.HEIGHT;
		dlg.show();

	}
	void changeWeight(){
		ProfileWheelDialog dlg = new ProfileWheelDialog(this);
        dlg.mListener = this;
        dlg.mType = ProfileWheelDialog.WEIGHT;

		dlg.show();		 

	}	
	void changeWaist(){
		ProfileWheelDialog dlg = new ProfileWheelDialog(this);
        dlg.mListener = this;
        dlg.mType = ProfileWheelDialog.WAIST;

		dlg.show();		
 

	}
	LayoutInflater mLi = null;

	public class ProfileItem extends BaseAdapter {
		int MAX_ITEM_COUNTS = 5;
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
            String temp;
     	    switch(position){
     	   case 0: //id
    	    	tv1.setText(R.string.str_personal_id);
    			tv3.setText(Configure.name);
    			itemView.setOnClickListener(new OnClickListener(){
    				@Override
    				public void onClick(View v) {
    					changeName();				
    				}
    			});
    			tv2.setText(" ");
    	    	break;
     	    case 1: //gender
     	    	tv1.setText(R.string.str_gender);

     			if(Configure.gender == Configure.MALE)
                    temp = getResources().getString(R.string.str_male);
     			else
                    temp = getResources().getString(R.string.str_female);
                tv2.setText(" ");
     			tv3.setText(temp);
     			itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					changeGender();
     				}
     			});
     	    	break;
     	    case 2: //height
     	    	tv1.setText(R.string.str_height);
     	    	tv2.setText(Float.toString(Configure.height));
                if(RootActivity.bEnglish) {
                    Float len = Configure.height/2.54f;
                    tv2.setText(String.format(" %1d %s", (int)(len/12), getResources().getText(R.string.str_feet)));
                    tv3.setText(String.format(" %.1f %s", (len - 12* (int)(len/12)), getResources().getText(R.string.str_inch )));
                }
                else {
                    tv2.setText(Float.toString(Configure.height));
                    tv3.setText(R.string.str_cm);
                }
                itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					changeHeight();				
     				}
     			});     			
     	    	break;
     	    case 3: //weight
     	    	tv1.setText(R.string.str_weight);
                if(RootActivity.bEnglish) {
                    tv2.setText(String.format(" %.1f ", Configure.weight/0.454f));
                    tv3.setText(R.string.str_pound);
                }
                else {
                    tv2.setText(Float.toString(Configure.weight));
                    tv3.setText(R.string.str_kg);
                }
     			itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					changeWeight();				
     				}
     			});     	
     	    	break;
     	    case 4: //waist
     	    	tv1.setText(R.string.str_waist);
                if(RootActivity.bEnglish) {
                    tv2.setText(String.format(" %.1f ", Configure.waist/2.54f ));
                    tv3.setText(R.string.str_inch);
                } else {
                    tv2.setText(Float.toString(Configure.waist));
                    tv3.setText(R.string.str_cm);
                }
                itemView.setOnClickListener(new OnClickListener(){
     				@Override
     				public void onClick(View v) {
     					changeWaist();				
     				}
     			});     	
     	    	break;
 	    	default:
 	    		return null;
     	    }
    		return itemView;
		}
		
	}
	private void updateUi(){
		mProfile = new PersonalProfile(
				Configure.gender,
				Configure.height,
				Configure.weight,
				Configure.waist);		
		TextView tv5 = (TextView) findViewById(R.id.tv_bmi);
		tv5.setTextColor(getResources().getColor(mProfile.getBmiServilityColorId()));
 
		
		String text;
        if(RootActivity.bEnglish)
        text = String.format("%.1f ", mProfile.getBmi());
        else
        text = String.format("%.1f", mProfile.getBmi())  + " Kg/m2 ";

		String desc = getResources().getString( mProfile.getBmiDescriptionId()  );
		tv5.setText(text+ desc);		
		
		TextView tv7 = (TextView) findViewById(R.id.tv_waistDesc);
		tv7.setTextColor(getResources().getColor(mProfile.getWaistServilityColorId()));
		tv7.setText(mProfile.getWaistDescId());
		
		TextView tv6 = (TextView) findViewById(R.id.tv_date);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Configure.dateUpdate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM/dd");
		tv6.setText(sdf.format(c.getTime()));

	}
	ProfileItem mPi = null;
	private void initUi(){
		mLi = (LayoutInflater)this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mPi = new ProfileItem();

		ListView lv = (ListView) findViewById(R.id.lvProfile);		   	 	
		lv.setAdapter(mPi );
		mPi.notifyDataSetChanged();        
//	
		updateUi();
	}//end initUi()
	@Override
	public void onProfileChanged() {
		mPi.notifyDataSetChanged();   
		updateUi();
	}
	@Override
	public void onSettingsChanged() {
		// no set in this activity		
	} 
	
	
}
