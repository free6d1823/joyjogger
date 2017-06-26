package com.breeze.joyjogger;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
 
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
 

public class ProfileSettingsDialog extends Dialog implements
 	View.OnClickListener {
	public Listener mListener = null;
    public static final int BASIC_ID = 2100;
	public static final int GENDER = 100;
	public static final int SPORT_TYPE = 700;
	public static final int NO_MOTION = 800;
	public static final int KEEP_RECORD = 900;
	public static final int FREE_SPACE = 1000;
	RadioGroup mSelection = null;
 
	public int	mType;
	public Context mContext;
	public Dialog d;
	public Button yes;
	
	public interface Listener {
		public void onProfileChanged(); //0~4
		public void onSettingsChanged();//5~10
	}

    public ProfileSettingsDialog(Context c, int a ) {
        super(c,a );

    }
    public ProfileSettingsDialog(Context c ) {
        super(c);
        mContext = c;

    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.dialog_profile_settings);

	    yes = (Button) findViewById(R.id.btn_yes);
	    yes.setOnClickListener(this);
	    TextView tvTitle = (TextView)findViewById(R.id.tv_title);
	    TextView tvMessage = (TextView)findViewById(R.id.tv_message);
		mSelection= (RadioGroup)findViewById(R.id.rg_selection);
	    switch(mType){
	    case GENDER:
	    	tvTitle.setText(R.string.str_select_gender);
	    	tvMessage.setText(R.string.str_select_gender_desc);
	    	setGender();
	    	break;
    	case SPORT_TYPE:
	    	tvTitle.setText(R.string.settings_motion_type);
	    	tvMessage.setVisibility(View.INVISIBLE);
	    	setSportType();
	    	break;

    	case NO_MOTION:// 8;
	    	tvTitle.setText(R.string.settings_minutes_to_sleep);
	    	tvMessage.setText(R.string.settings_minutes_to_sleep_desc);  
	    	setNoMotion();
	    	break;	   
    	case KEEP_RECORD:// 9;
	    	tvTitle.setText(R.string.settings_record_keep_days);
	    	tvMessage.setText(R.string.settings_record_keep_days_desc);  
	    	setKeepRecord();
	    	break;	   	 
    	case FREE_SPACE:// 10
	    	tvTitle.setText(R.string.settings_mini_free_space);
	    	tvMessage.setText(R.string.settings_mini_free_space_desc);  
	    	setFreeSpace();
	    	break;	
 
	    
	    }

	}
	void setNoMotion(){
     	String szUnit =  mContext.getResources().getString(R.string.str_minute);
		final CharSequence[] items = new CharSequence[Configure.sSleepTimeOptions.length];
		for(int i=0; i< Configure.sSleepTimeOptions.length;i++){
			items[i] = String.format("%d %s", Configure.sSleepTimeOptions[i], szUnit);
		}
 		mSelection.setVisibility(View.VISIBLE);
		
		for(int i=0;i<items.length;i++){
			RadioButton  tv0 = new RadioButton(mContext);
			tv0.setText(items[i]);
			tv0.setId(i+BASIC_ID);
	 		mSelection.addView(tv0, i, new 
					ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			
		}
  						
		mSelection.check(Configure.sleepAfterNoMotionOption+BASIC_ID);
		 	    		
	}
	void setKeepRecord(){
	    	final CharSequence[] items = Configure.getKeepDataOptionsString();

	 		mSelection.setVisibility(View.VISIBLE);
			
			for(int i=0;i<items.length;i++){
				RadioButton  tv0 = new RadioButton(mContext);
				tv0.setText(items[i]);
				tv0.setId(i+BASIC_ID);
		 		mSelection.addView(tv0, i, new 
						ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				
			}
	  						
			mSelection.check(Configure.getKeepDataOption()+BASIC_ID);
			
 		
	}	
	void setFreeSpace(){
		String szUnit = mContext.getResources().getString(R.string.str_mb);
		mSelection.setVisibility(View.VISIBLE);

		for(int i=0;i<Configure.sFreeSpaceOptions.length;i++){
			String name = Integer.toString(Configure.sFreeSpaceOptions[i])+ szUnit;
			RadioButton  tv0 = new RadioButton(mContext);
			tv0.setText(name);
			tv0.setId(i+BASIC_ID);
			mSelection.addView(tv0, i, new
					ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		}

		mSelection.check(Configure.getFreeSpaceOption()+BASIC_ID);
	 
	}

	void setSportType(){
		RadioButton  tv0 = new RadioButton(mContext);
		tv0.setText(R.string.settings_type_walk);
		tv0.setId(BASIC_ID +  0);
		RadioButton  tv1 = new RadioButton(mContext);
		tv1.setText(R.string.settings_type_jog);
		tv1.setId(BASIC_ID + 1);
		RadioButton tv2 = new RadioButton(mContext);
		tv2.setText(R.string.settings_type_run);	
		tv2.setId(BASIC_ID + 2);
  		mSelection.setVisibility(View.VISIBLE);
		mSelection.addView(tv0, 0, new 
				ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
  		
		mSelection.addView(tv1, 1, new 
				ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mSelection.addView(tv2, 2, new 
				ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		mSelection.check(Configure.getCurrentMotionType()+BASIC_ID);
		

	}

	void setGender(){
		String male = mContext.getResources().getString(R.string.str_male);
		String female = mContext.getResources().getString(R.string.str_female);
		RadioButton  tv1 = new RadioButton(mContext);
		tv1.setText(female);
		tv1.setId(BASIC_ID + Configure.FEMALE);
		RadioButton tv2 = new RadioButton(mContext);
		tv2.setText(male);	
		tv2.setId(BASIC_ID + Configure.MALE);
 
	 	mSelection.setVisibility(View.VISIBLE);
		mSelection.addView(tv1, 0, new 
				ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mSelection.addView(tv2, 1, new 
				ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				
		mSelection.check( (Configure.gender == Configure.FEMALE)?
				Configure.FEMALE+BASIC_ID:Configure.MALE+BASIC_ID);
 
 	        
  
	}

	//////////////
	void notifyProfileChanged(){
		Configure.saveProfile();
		mListener.onProfileChanged( );		
	}
	void notifySettingsChanged(){
		Configure.saveSettings();

		mListener.onSettingsChanged( );		
	}	 
	@Override
	public void onClick(View v) {
		int sel;
    try {
        switch (v.getId()) {
            case R.id.btn_yes:
                switch (mType) {
                    case GENDER:
                        sel = mSelection.getCheckedRadioButtonId();
                        if (sel == -1)
                            return;
                        Configure.gender = sel - BASIC_ID;
                        notifyProfileChanged();
                        break;
                    case SPORT_TYPE:
                        sel = mSelection.getCheckedRadioButtonId();

                        if (sel == -1)
                            return;
                        Configure.setCurrentMotionType(sel - BASIC_ID);

                        notifySettingsChanged();
                        break;
                    case NO_MOTION:
                        sel = mSelection.getCheckedRadioButtonId();
                        if (sel == -1)
                            return;
                        Configure.sleepAfterNoMotionOption = sel - BASIC_ID;
                        notifySettingsChanged();
                        break;
                    case KEEP_RECORD:
                        sel = mSelection.getCheckedRadioButtonId();
                        if (sel == -1)
                            return;
                        Configure.setKeepDataOption(sel - BASIC_ID);
                        notifySettingsChanged();
                        break;
                    case FREE_SPACE:
                        sel = mSelection.getCheckedRadioButtonId();
                        if (sel < 0)
                            return;
                        Configure.setFreeSpaceOption(sel - BASIC_ID);
                        notifySettingsChanged();
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
    }catch(NumberFormatException e){
        Log.d("ProfileSettings", e.toString());
    }


	    dismiss();
	}
		
 
}
