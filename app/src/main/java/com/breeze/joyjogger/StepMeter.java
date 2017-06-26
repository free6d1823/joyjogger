package com.breeze.joyjogger;

 

import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
 
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class StepMeter extends Service implements SensorEventListener{
	private static final int BUFFER_MINUTES = 60; //keep 60 minutes data record
	private static float[] mAccFatBuffer = new float[BUFFER_MINUTES]; //accumulate fat per minute
	private static float[] mRpmBuffer = new float[BUFFER_MINUTES]; //steps per minutes
	private static float[] mFatBuffer = new float[BUFFER_MINUTES]; //cal per minutes
	private static int mNextMinute;
	private static int mCurrentMinute;	
	private Calendar mStartTime; 
	private final static String TAG = "StepMeter";
	public static StepMeter mInstance = null;
    private static int mIdleCountDown; //time o sleep in seconds
 
	public static boolean isWorking(){
		if(mInstance == null)
			return false;
		return mInstance.mWorking;
	}
	   /** Interface for callback invocation when WiFi P2P state is changed */
	public static String DATA_CHANGED = "StepMeter.DATA_CHANGED";
	public static String SPEED_CHANGED = "StepMeter.SPEED_CHANGED";	
 
	
 
    ////////////////////////////////////////
    public class StepData {
    	public int mTimeSeconds; //
    	public int mSteps; //steps count
    	public int mDistanceMeter; //total distance in meter
    	public float mStrength; //g value

    }
   
    public class InstanceSpeed {
    	private long mLastTime = 0; //mS
    	private int mLastDistance = 0; //meter
    	private double mLastSpeed = 0; //meter/sec
    	public InstanceSpeed(){
    		
    	}
    	public void reset(){
    		mLastTime = 0;
    		mLastDistance = 0;
    		mLastSpeed = 0;
    	}
    	public void setData(long time, int meters){
    		long dt = time - mLastTime;
    		if(dt<1000) //at least accumulate 1 sec
    			return;
    		if(mLastTime != 0) //first time, don't update speed
    			mLastSpeed = ((meters - mLastDistance)*1000/dt+mLastSpeed)/2;
    		mLastTime = time;
    		mLastDistance = meters;
    	}
    	public double getSpeed(){
    		return mLastSpeed*3.6; //m/s to km/hr
    	}
    }    

	private SensorManager mSm;

	public StepData m = new StepData(); //updated every second	
 
	public InstanceSpeed mSpeed = new InstanceSpeed(); //instance speed  
	private long mStartTimeMs;

	private int mLastSec;
	private int mSpeedTmp = 0;
	private boolean mWorking = false;
	public FatCalculator mFatCalculator;
	/* enable the srvice */
	public static void enable(Context context){
		Intent objIntent2 = new Intent(context, StepMeter.class);
		Log.d(TAG, "start");
		context.startService(objIntent2);			
	}
	/* disable the service */
	public static void disable(Context context){
		Intent objIntent2 = new Intent(context, StepMeter.class);
		context.stopService(objIntent2);		
	}
	public static void start(){
		if(mInstance != null) {
			mInstance.startCounter();

		}
	}
 
	public static void stop(){
		if(mInstance != null){ 
			mInstance.stopCounter();
			mInstance.saveRecord();
 	
		}
		
	}
	public static void saveStepCounter( ){
		if(mInstance != null){ 
			mInstance.saveRecord();
 	
		}
	}
	
	public static void resetCounter(){
		if(mInstance != null){
			mInstance.m.mTimeSeconds = 0;
			mInstance.m.mSteps = 0;
			
			mInstance.m.mDistanceMeter = 0;
			mInstance.mSpeed.reset();
			mInstance.m.mStrength = 0;
 
			//new design
			mNextMinute = 0;
			mCurrentMinute = 0;
			for(int i=0; i<mAccFatBuffer.length; i++ ){
				mAccFatBuffer[i] = 0;
				mRpmBuffer[i] = 0;
				mFatBuffer[i] = 0;
			}
			mIdleCountDown = Configure.getSleepAfterNoMotionSeconds();
		}
	}	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override 
	public int onStartCommand(Intent intent, int flags, int startId){
		if(mInstance != null) { //don't allow multiple instance
			Log.d(TAG, "StepMeter don't allow multiple instance");		
			return 1;
		}
		mInstance = this; 		
		Log.d(TAG, "StepMeter Service started!");		
		
		mFatCalculator = new FatCalculator();

		resetCounter();	
		
		return START_STICKY;
	}
	  
	@Override
	public void onDestroy(){
		mInstance =null;
 		
	}
	void onTimerSecond(){
		mSpeed.setData(SystemClock.elapsedRealtime(), m.mDistanceMeter);
		mIdleCountDown --;
		if(mIdleCountDown <0){
			Log.d(TAG, "Entering idle mode");
			Toast.makeText(mInstance, R.string.prompt_auto_stop, Toast.LENGTH_LONG).show();
			
			RootActivity.gInstance.startStepCounter(false);
            mIdleCountDown = 0x8ffffff; /* 2014/11/19 don't enter again */

			
		}
	}
	void onTimerMinuteInc(){
 
	    //accumulate fat
		float cal = mFatCalculator.getFatConsumed((float)mSpeedTmp, 60);
 
		mRpmBuffer[mNextMinute] = mSpeedTmp;
		mFatBuffer[mNextMinute] = cal;	    
	    mAccFatBuffer[mNextMinute] = mAccFatBuffer[mCurrentMinute] + cal;
        Log.i(TAG, "Rpm= "+ mSpeedTmp + " Fat= " + cal);
	    //	    
	    mCurrentMinute = mNextMinute;
	    mNextMinute ++;
	    if(mNextMinute == BUFFER_MINUTES){
	    	//save buffer
	    	saveRecord();
	    	mNextMinute = 0;	  
	    }
		mSpeedTmp = 0;	
		final Intent intent = new Intent(SPEED_CHANGED);
	    sendBroadcast(intent);		    
 	

	    DayRecord.checkStorageSpace();
	}
	public static float[] getLastHourRpm(){
		float[] se = new float[BUFFER_MINUTES];
		int j = mCurrentMinute;
 		for(int i=BUFFER_MINUTES -1 ;i>=0; i--){
 
			se[i] = (float) (mRpmBuffer[j]);
			j--;
			if(j <0 ) j = mRpmBuffer.length-1;
		}
		return se;
	}
	public static float[] getLastHourAccFat(){
		float[] se = new float[BUFFER_MINUTES];
		int j = mCurrentMinute;
 		for(int i=BUFFER_MINUTES-1 ;i>=0; i--){
 
			se[i] = mAccFatBuffer[j];
 			j--;
			if(j <0 ) j = BUFFER_MINUTES-1;
		}

		return se;		
	}
 
	Handler mHandlerTime = new Handler();
 	private void startCounter(){
        mFatCalculator.update(Configure.weight, Configure.stepSize);

        mSm = (SensorManager) getSystemService(SENSOR_SERVICE);
    	Sensor sn = mSm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// also: TYPE_ALL
		if (sn == null ){
			Log.d(TAG, "sensor error!");
		}
		else{
	
			mSm.registerListener(this, sn, SensorManager.SENSOR_DELAY_UI);
		}		
 		
 		mSpeedTmp = 0;
		mStartTimeMs = SystemClock.elapsedRealtime();
		mStartTime = Calendar.getInstance();
		resetCounter();
	
		mWorking = true;

		final Runnable timerRun = new Runnable(){
			
			public void run()
			{
				m.mTimeSeconds = (int) ((SystemClock.elapsedRealtime() -  mStartTimeMs )/1000);
				long dT = m.mTimeSeconds - mLastSec;
				if(dT > 0){
					mLastSec += dT; //second changed
					if(mLastSec%60 == 0)//minute
					{
						onTimerMinuteInc();
					
						
					}
					onTimerSecond();
					  //broadcast
			  		final Intent intent = new Intent(DATA_CHANGED);
			  	    sendBroadcast(intent);				


				}
				if(mWorking)
					mHandlerTime.postDelayed(this, 333);					
			}
		  };
 
		  mLastSec = 0;
		  mHandlerTime.postDelayed(timerRun, 1000);
 	}
    private void stopCounter(){
    	mWorking = false;    
    	if(mSm != null)
    		mSm.unregisterListener(this);
    }
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {	
	}
	private float weight(float a, float b)
	{
		return (float) (a*0.2 + b*0.8);
	}
	private float mx=0, my=0, mz=0, mv=0;

	private int mSlop = 0; 	

	@Override
	public void onSensorChanged(SensorEvent event) {

		if(mWorking){
 			float x = weight(event.values[0], mx);
			float y = weight(event.values[1], my);
			float z = weight(event.values[2], mz);
			float v = (float) Math.sqrt(x*x +y*y + z*z);
			boolean turn = false;
			if(mSlop > 0)//up
			{
				if(v < mv-Configure.sensitivity){//0.1 for walk, 2 for run
					mSlop = -1;
					turn = true;
					m.mSteps ++;	
					m.mDistanceMeter =   (int) (m.mSteps* Configure.stepSize/100);
					m.mStrength = mv;					
					mSpeedTmp ++; //acumulate 
					//moved, reset idle count down		
					mIdleCountDown = Configure.getSleepAfterNoMotionSeconds();


				}
			}
			else
			{
				if(v>mv+Configure.sensitivity){
					mSlop = 1;
					turn = true;
				}
			}
			if (turn){ 
			//Log.d(TAG, "a=  "+ x +","+y+","+z+" ="+v);
			}
			mx = x; my =y; mz=z; mv = v;


	 			
		}
   
	}
	
	private void saveRecord(){
	    Log.d(TAG, "saved file 1");
	 
	    DayRecord.pRPM = mRpmBuffer;
	    DayRecord.pFat = mFatBuffer;
	    DayRecord.saveDay(mStartTime, mCurrentMinute+1);	
	}
 

}
