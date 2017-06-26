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
 

public class ProfileEditboxDialog extends Dialog implements
 	View.OnClickListener {
	public Listener mListener = null;
	public static final int NAME = 0;
	EditText mInput = null;

	public int	mType;
	public Context mContext;
	public Dialog d;
	public Button yes;
	
	public interface Listener {
		public void onProfileChanged(); //0~4
		public void onSettingsChanged();//5~10
	}

    public ProfileEditboxDialog(Context c, int a ) {
        super(c,a );

    }
    public ProfileEditboxDialog(Context c ) {
        super(c);
        mContext = c;

    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.dialog_profile_editbox);

	    yes = (Button) findViewById(R.id.btn_yes);
	    yes.setOnClickListener(this);
		mInput = (EditText)findViewById(R.id.et_value);
	    switch(mType){
	    case NAME:
    		setName();
    		break;
	    }

	}

	void setName(){

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                80);
        //mInput.setLayoutParams(lp);
        mInput.setText(Configure.name); 	
        mInput.setInputType(InputType.TYPE_CLASS_TEXT);
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
		if (v.getId() == R.id.btn_yes) {
			if (mType == NAME) {
				Configure.name = mInput.getText().toString();
				notifyProfileChanged();
			}
		}
		dismiss();
	}
 
}
