package com.breeze.joyjogger;
import java.lang.Runnable;


import android.content.Context;
import android.os.Handler;
import android.util.Log;


public class AutoCheckInput implements Runnable,
		ProfileSettingsDialog.Listener,
		ProfileEditboxDialog.Listener,
		ProfileWheelDialog.Listener{
	static Handler mHandler=null;
	static AutoCheckInput mInstance = null;
 	boolean mHasSetSystem = false;
	Context	mContext = null;
	public AutoCheckInput(Context c){
		mContext = c;
	}
	@Override
	public void run() {
		Log.d("ZZZZZ", "run auto check...");
		if(Configure.name.isEmpty())
			showDialog(ProfileEditboxDialog.NAME);
		else if(Configure.gender < 0)
			showDialog(ProfileSettingsDialog.GENDER);
		else if(Configure.height < 100)
			showWheelDialog(ProfileWheelDialog.HEIGHT);
		else if(Configure.weight < 10)
			showWheelDialog(ProfileWheelDialog.WEIGHT);
		else if(Configure.waist < 10)
			showWheelDialog(ProfileWheelDialog.WAIST);
		else if(Configure.stepSize < 10)
			showWheelDialog(ProfileWheelDialog.STEP_SIZE);
	}
	static void start(Context c){
		if(mHandler != null)
			return;
		mInstance = new AutoCheckInput(c);
		
		mHandler = new Handler();
		mHandler.postDelayed(mInstance, 0);

	}
	void showDialog(int type){
		if(type == ProfileEditboxDialog.NAME){
			ProfileEditboxDialog dlg = new ProfileEditboxDialog(mContext);
			dlg.mType = type;
			dlg.mListener = this;
			dlg.show();
		}else {
			ProfileSettingsDialog dlg = new ProfileSettingsDialog(mContext);
			dlg.mType = type;
			dlg.mListener = this;
			dlg.show();
		}
	}
	void showWheelDialog(int type){

		if(mHasSetSystem == false){
			mHasSetSystem = true;
			Log.d("XXXX", "Now shows Dialog MEASURE");
			ProfileWheelDialog dlg = new ProfileWheelDialog(mContext);
			dlg.mType = ProfileWheelDialog.MEASURE;
			dlg.mListener = this;
			dlg.show();
			return;
		}
		Log.d("XXXX", "Now shows Dialog "+ type);
		ProfileWheelDialog dlg = new ProfileWheelDialog(mContext);
		dlg.mType = type;
		dlg.mListener = this;
		dlg.show();
	}
	@Override
	public void onProfileChanged() {
		Log.d("XXXX", "onProfileChanged ");
		mHandler.postDelayed(mInstance, 100);
	}
	@Override
	public void onSettingsChanged() {
		Log.d("XXXX", "onSettingsChanged ");
		mHandler.postDelayed(mInstance, 100);
	}
}
