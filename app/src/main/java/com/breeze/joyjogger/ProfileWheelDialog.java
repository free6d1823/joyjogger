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

import com.breeze.wheel.ArrayWheelAdapter;
import com.breeze.wheel.NumericWheelAdapter;
import com.breeze.wheel.WheelView;


public class ProfileWheelDialog extends Dialog implements
 	View.OnClickListener {
	public Listener mListener = null;
	public static final int HEIGHT = 200;
	public static final int WEIGHT = 300;
	public static final int WAIST = 400;
	public static final int MEASURE = 500; //measure system
	public static final int STEP_SIZE = 600;
	public int	mType;
	public Context mContext;
	public Dialog d;
	public Button yes, no;
	
	public interface Listener {
		public void onProfileChanged(); //0~4
		public void onSettingsChanged();//5~10
	}

    public ProfileWheelDialog(Context c, int a ) {
        super(c,a );
    }
    public ProfileWheelDialog(Context c ) {
        super(c);
        mContext = c;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.dialog_profile_wheel);

	    yes = (Button) findViewById(R.id.btn_yes);
	    yes.setOnClickListener(this);
	    TextView tvTitle = (TextView)findViewById(R.id.tv_title);
	    TextView tvMessage = (TextView)findViewById(R.id.tv_message);
	    switch(mType){

	    case HEIGHT:
	    	tvTitle.setText(R.string.str_input_height);
	    	tvMessage.setText(R.string.str_input_height_desc);
	    	setHeight();
	    	break;
	    case WEIGHT:
	    	tvTitle.setText(R.string.str_input_weight);
	    	tvMessage.setText(R.string.str_input_weight_desc);
	    	setWeight();
	    	break;
	    case WAIST:
	    	tvTitle.setText(R.string.str_input_waist);
	    	tvMessage.setText(R.string.str_input_waist_desc);
	    	setWaist();
	    	break;
		case MEASURE:
			tvTitle.setText(R.string.settings_measure_system);
			tvMessage.setText(R.string.settings_measure_system_desc);
			setMeasure();
			break;
		case STEP_SIZE:
			tvTitle.setText(R.string.settings_step_distance);
			tvMessage.setText(R.string.str_step_distance_desc);
			setStepSize();
			break;
	    }

	}
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Initializes wheel
	 *
	 * @param id
	 *            the wheel widget Id
	 */
	private void initWheel(int id, int min, int max, String format, int value) {

		NumericWheelAdapter adapter = new NumericWheelAdapter(this.getContext(),
				min,max, format);
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(value - min); //item id
		wheel.setVisibleItems(2);//display area

		//wheel.addChangingListener(changedListener);
		//wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(true);
	}
	void setMeasure(){
		WheelView wheel1 = getWheel(R.id.w1);
		wheel1.setVisibility(View.INVISIBLE);
		TextView tvUnit1 = (TextView)findViewById(R.id.tv_unit1);
		tvUnit1.setVisibility(View.INVISIBLE);
		WheelView wheel2 = getWheel(R.id.w2);
		wheel2.setVisibility(View.VISIBLE);
		TextView tvUnit2 = (TextView)findViewById(R.id.tv_unit2);
		tvUnit2.setVisibility(View.INVISIBLE);

		String items[] = new String[] {
				mContext.getString(R.string.system_metric),
				mContext.getString(R.string.system_english) };

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext,
				items);

		wheel2.setViewAdapter(adapter);

		wheel2.setVisibleItems(2);//display area

		//wheel.addChangingListener(changedListener);
		//wheel.addScrollingListener(scrolledListener);
		wheel2.setCyclic(true);
		wheel2.setEnabled(true);
		if(Configure.system == 0)
			wheel2.setCurrentItem(0);
		else
			wheel2.setCurrentItem(1);
	}
	void setStepSize(){
		WheelView wheel1 = getWheel(R.id.w1);
		wheel1.setVisibility(View.INVISIBLE);
		TextView tvUnit1 = (TextView)findViewById(R.id.tv_unit1);
		tvUnit1.setVisibility(View.INVISIBLE);
		WheelView wheel2 = getWheel(R.id.w2);
		wheel2.setVisibility(View.VISIBLE);
		TextView tvUnit2 = (TextView)findViewById(R.id.tv_unit2);
		tvUnit2.setVisibility(View.VISIBLE);
		if(RootActivity.bEnglish){
			initWheel(R.id.w2, 12, 48, "%02d", (int) (Configure.stepSize / 2.54f));//0~12"
			tvUnit2.setText(R.string.str_inch);
		}else {
			initWheel(R.id.w2, 30, 120, "%03d", (int) (Configure.stepSize));//30~120 cm"
			tvUnit2.setText(R.string.str_cm);
		}
	}

	void setHeight(){
        TextView tvUnit1 = (TextView)findViewById(R.id.tv_unit1);
        tvUnit1.setVisibility(View.VISIBLE);
		TextView tvUnit2 = (TextView)findViewById(R.id.tv_unit2);
		tvUnit2.setVisibility(View.VISIBLE);

        if(RootActivity.bEnglish){
            float len = Configure.height/2.54f;
			initWheel(R.id.w1, 3, 7, "%-2d", (int)(len / 12));//3'~7'
			tvUnit1.setText(R.string.str_feet);
			initWheel(R.id.w2, 0,11, "%02d", (int)(len%12));//0~12"
            tvUnit2.setText(R.string.str_inch);

        }else {
			initWheel(R.id.w1, 1, 2, "%-2d", (int)(Configure.height / 100));//m
			tvUnit1.setText("");
			initWheel(R.id.w2, 0, 99, "%02d",(int)(Configure.height % 100));//cm
			tvUnit2.setText(R.string.str_cm);
        }
	}
	void setWeight(){
		WheelView wheel1 = getWheel(R.id.w1);
		wheel1.setVisibility(View.INVISIBLE);
        TextView tvUnit1 = (TextView)findViewById(R.id.tv_unit1);
        tvUnit1.setVisibility(View.INVISIBLE);
		WheelView wheel2 = getWheel(R.id.w2);
		wheel2.setVisibility(View.VISIBLE);
		TextView tvUnit2 = (TextView)findViewById(R.id.tv_unit2);
		tvUnit2.setVisibility(View.VISIBLE);

		if(RootActivity.bEnglish) {
			initWheel(R.id.w2, 30, 300, "%-3d", (int) (Configure.weight / 0.454f));//30~300pt.
			tvUnit2.setText(R.string.str_pound);
        }else {
			initWheel(R.id.w2, 15, 150, "%-3d", (int) (Configure.weight));//15~150kg.
            tvUnit2.setText(R.string.str_kg);
        }
	}
	void setWaist(){
		WheelView wheel1 = getWheel(R.id.w1);
		wheel1.setVisibility(View.INVISIBLE);
		TextView tvUnit1 = (TextView)findViewById(R.id.tv_unit1);
		tvUnit1.setVisibility(View.INVISIBLE);
		WheelView wheel2 = getWheel(R.id.w2);
		wheel2.setVisibility(View.VISIBLE);
		TextView tvUnit2 = (TextView)findViewById(R.id.tv_unit2);
		tvUnit2.setVisibility(View.VISIBLE);
        if(RootActivity.bEnglish) {
			initWheel(R.id.w2, 20, 120, "%-3d", (int) (Configure.waist / 2.54f));//20~120inch
            tvUnit2.setText(R.string.str_inch);
        }else {
			initWheel(R.id.w2, 50, 500, "%-3d", (int) (Configure.waist));//50~200cm
            tvUnit2.setText(R.string.str_cm);
        }
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
		float value;
		if (R.id.btn_yes == v.getId()) {
			switch (mType) {
			case HEIGHT:
				if(RootActivity.bEnglish) {
					value = ( getWheel(R.id.w1).getCurrentItemValue()*12 +
							  getWheel(R.id.w2).getCurrentItemValue())*2.54f;
				}else {
					value = getWheel(R.id.w1).getCurrentItemValue()*100 +
							getWheel(R.id.w2).getCurrentItemValue();
				}
				Configure.height = value;
				notifyProfileChanged();
				break;
			case WEIGHT:
				if(RootActivity.bEnglish) {
					value = getWheel(R.id.w2).getCurrentItemValue()*0.454f;
				}else {
					value = getWheel(R.id.w2).getCurrentItemValue();
				}
				Configure.weight = value;
				notifyProfileChanged();
				break;
			case WAIST:
				if(RootActivity.bEnglish) {
					value = getWheel(R.id.w2).getCurrentItemValue()*2.54f;
				}else {
					value = getWheel(R.id.w2).getCurrentItemValue();
				}
				Configure.waist = value;
				notifyProfileChanged();
				break;
			case MEASURE:
				if( getWheel(R.id.w2).getCurrentItem() == 0)
					Configure.system = 0;
				else
					Configure.system = 1;

				notifySettingsChanged();
					/* change setting*/
				RootActivity.bEnglish = (Configure.system == 1);
				if(MainFragment.gInstance != null)
					MainFragment.gInstance.updateSpeedMeterGui();
				break;
			case STEP_SIZE:
				if(RootActivity.bEnglish)
					Configure.stepSize = 2.54f*
							getWheel(R.id.w2).getCurrentItemValue();
				else
					Configure.stepSize = getWheel(R.id.w2).getCurrentItemValue();

				notifySettingsChanged();
				break;
			}

		}//end of btn_yes
	    dismiss();
	}
		
 
}
