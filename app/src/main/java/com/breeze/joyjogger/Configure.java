package com.breeze.joyjogger;


import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Build;
 
import android.preference.PreferenceManager;

public class Configure {
	//PersonalProfile
	public static final int	FEMALE = 0;
	public static final int MALE = 1;
	public static String name; //name of owner
	public static int gender = -1;//0=femail, 1=mail
	public static float height = 1; //cm
	public static float weight = 1; //kg
	public static float waist = 1; //cm
	public static long  dateUpdate = 0;
	private static Context mContext;
	

	public Configure(Context context){
		mContext = context;
		goals = new Goal[2];
		goals[0] =new Goal(20,3,3); //low goal
		goals[1] =new Goal(60,7,20); //hight goal		

        /* if en, default system is english */
        String local = Locale.getDefault().getCountry();
        local = local.toLowerCase();

        if (!local.equals("tw" )&& !local.equals("cn" ) &&
                !local.equals("jp" ) )
            local = "en";
                system = SYSTEM_ENGLISH;
	}
	public static Configure init(Context context){
		return new Configure(context);
	}
	//app settings
	public static final int SYSTEM_METRIC = 0;//cm, m, kg, g, km, l, cc, 
	public static final int SYSTEM_ENGLISH = 1;//0.39inch,3.28ft, 2.2 lb, 0.04ounce, 0.62ml, 0.26gal, 0.03 oz
	public static int system = 0;

	public static float stepSize = 0; //in cm
	//sport_type; sensitivity
	public static final float WALK = 0.4f;
	public static final float JOG = 0.7f;
	public static final float RUN = 0.99f;
    public static final int TYPE_WALK = 0;
    public static final int TYPE_JOG = 1;
    public static final int TYPE_RUN = 2;

	public static int sportType = TYPE_WALK;
    public static float sensitivity;
	public static int getCurrentMotionType() {
        return sportType;
    }
    private static float getCurrentMotionSensitivity() {
        switch(sportType){
            case TYPE_JOG: return JOG;
            case TYPE_RUN: return RUN;
            default: return WALK;
        }
	}
	public static void setCurrentMotionType(int sel){
		if (sel> TYPE_RUN || sel < TYPE_WALK)
            sel = TYPE_WALK;
		Configure.sportType = sel;
        Configure.sensitivity = getCurrentMotionSensitivity();
	}
	//SLEEP
	public final static int[] sSleepTimeOptions = { 5, 10,15,20,30};  
	public final static int DEFAULT_SLEEP_TIME_OPTION = 0;	
	
	public static int sleepAfterNoMotionOption = DEFAULT_SLEEP_TIME_OPTION;
	public static int getSleepAfterNoMotionSeconds(){
		if(sleepAfterNoMotionOption>= sSleepTimeOptions.length)
			sleepAfterNoMotionOption = 0;
		return sSleepTimeOptions[sleepAfterNoMotionOption]*60;}
	//storage settings	
	public final static int[] sFreeSpaceOptions = { 50,100,500, 1000,2000,6000};  	
	public static int freeSpaceMB = 1000;
	public static int getFreeSpaceOption(){
		int i = sFreeSpaceOptions.length - 1;
		while(i>0){
			if(freeSpaceMB >= sFreeSpaceOptions[i])
				break;
			i--;
		}
		return i;
	}
	public static void setFreeSpaceOption(int i){
		freeSpaceMB = sFreeSpaceOptions[i];
	}
	/////////////////////////////////////////
 
	public final static int[] sKeepDataOptions = { 7,31,92,184,366, 30000}; 
	public final static String[] getKeepDataOptionsString(){
		return mContext.getResources().getStringArray(R.array.record_keep_days_options);  		
	}
	public static int keepDataDays = 366;
	public static int getKeepDataOption(){
		int i = sKeepDataOptions.length - 1;
		while(i>0){
			if(keepDataDays >= sKeepDataOptions[i])
				break;
			i--;
		}
		return i;
	}
	public static void setKeepDataOption(int i){
		keepDataDays = sKeepDataOptions[i];
	}	
	//goals 1,2 sGoalMinutesPerWeekOptions//////////////////////////////////////
	public static Goal[] goals = null;
	//goals 1,2 sGoalMinutesPerWeekOptions//////////////////////////////////////	
	public static int[][] sGoalMinutesPerDayOptions = {
		{15,30,45,60},{90,120,150,180}
	};
	public final static String[] getGoalMinutesPerDayOptionsString(int goal){
		return (goal == 0)?
				mContext.getResources().getStringArray(R.array.goal1_minutes_per_day_options):
					mContext.getResources().getStringArray(R.array.goal2_minutes_per_day_options);	
	}	
	public static int getGoalMinutesPerDayOption(int goal){
		int i = sGoalMinutesPerDayOptions[goal].length - 1;
		while(i>0){
			if(goals[goal].minutesPerDay >= sGoalMinutesPerDayOptions[goal][i])
				break;
			i--;
		}
		return i;
	}
	public static void setGoalMinutesPerDayOption(int goal, int i){
		goals[goal].minutesPerDay = sGoalMinutesPerDayOptions[goal][i];
	}	
	////////////////////////////////////////////////////////////////////
	//goals 1,2 sGoalDaysPerWeekOptions//////////////////////////////////////	
	public static int[][] sGoalDaysPerWeekOptions = {
		{2,3,4},{5,6,7}
	};
	public final static String[] getGoalDaysPerWeekOptionsString(int goal){
		String[] options = new String[sGoalDaysPerWeekOptions[goal].length];
		for(int i=0;i<sGoalDaysPerWeekOptions[goal].length;i++){
			options[i] = Integer.toString(sGoalDaysPerWeekOptions[goal][i])+ mContext.getResources().getString(R.string.str_days);
			
		}
		return options;	
	}	
	public static int getGoalDaysPerWeekOption(int goal){
		int i = sGoalDaysPerWeekOptions[goal].length - 1;
		while(i>0){
			if(goals[goal].daysPerWeek >= sGoalDaysPerWeekOptions[goal][i])
				break;
			i--;
		}
		return i;
	}
	public static void setGoalDaysPerWeekOption(int goal, int i){
		goals[goal].daysPerWeek = sGoalDaysPerWeekOptions[goal][i];
	}	
	////////////////////////////////////////////////////////////////////
 
	//goals 1,2 kCalPerWeek//////////////////////////////////////	
	public static int[][] sGoalKcalPerWeekOptions = {
		{2,4,5,10},{30,50,80,100}
	};
	public final static String[] getGoalKcalPerWeekOptionsString(int goal){
		String[] options = new String[sGoalKcalPerWeekOptions[goal].length];
		for(int i=0;i<sGoalKcalPerWeekOptions[goal].length;i++){
			options[i] = Float.toString(sGoalKcalPerWeekOptions[goal][i])+ mContext.getResources().getString(R.string.str_kcal);
			
		}
		return options;	
	}	
	public static int getGoalKcalPerWeekOption(int goal){
		int i = sGoalKcalPerWeekOptions[goal].length - 1;
		while(i>0){
			if(goals[goal].kCalPerWeek >= sGoalKcalPerWeekOptions[goal][i])
				break;
			i--;
		}
		return i;
	}
	public static void setGoalKcalPerWeekOption (int goal, int i){
		goals[goal].kCalPerWeek = sGoalKcalPerWeekOptions[goal][i];
	}	
	////////////////////////////////////////////////////////////////////	
	//load configure from file
	public static void loadProfile(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		name = settings.getString("name", Build.MODEL+Build.SERIAL); 
		gender = settings.getInt("gender", gender);
		height = settings.getFloat("height", height);
		weight = settings.getFloat("weight", weight);
		waist = settings.getFloat("waist", weight);
		dateUpdate = settings.getLong("DateUpdate", 0);
		if(dateUpdate == 0){
			Calendar c = Calendar.getInstance();			
			dateUpdate = c.getTimeInMillis();			
		}
 
	
	}
	public static void loadSettings(){
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		stepSize = settings.getFloat("stepSize", stepSize);
		sportType = settings.getInt("sporttype", sportType);
        sensitivity = getCurrentMotionSensitivity(); /* update sportType*/

		system = settings.getInt("system", system);
		
		sleepAfterNoMotionOption = settings.getInt("sleepAfterNoMotionOption", sleepAfterNoMotionOption);
		keepDataDays = settings.getInt("keepDataDays", keepDataDays);	
		freeSpaceMB = settings.getInt("freeSpaceMB", freeSpaceMB);			
		//goal 1
 
		goals[0].minutesPerDay = settings.getInt("goal1MinutesPerDay", goals[0].minutesPerDay);	
		goals[0].daysPerWeek = settings.getInt("goal1DaysPerWeek", goals[0].daysPerWeek);
		goals[0].kCalPerWeek =   settings.getInt("goal1KcalPerWeek", goals[0].kCalPerWeek);	
		goals[1].minutesPerDay = settings.getInt("goal2MinutesPerDay", goals[1].minutesPerDay);	
		goals[1].daysPerWeek = settings.getInt("goal2DaysPerWeek", goals[1].daysPerWeek);	
		goals[1].kCalPerWeek = settings.getInt("goal2KcalPerWeek", goals[1].kCalPerWeek);	
 	
	}		
 
	//save configure into /data/
	public static void saveProfile() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor ed = settings.edit();
		ed.putString("name", name);
		ed.putInt("gender", gender); 
		ed.putFloat("height", height);
		ed.putFloat("weight", weight);
		ed.putFloat("waist", waist);		
		Calendar c = Calendar.getInstance();
		
		dateUpdate = c.getTimeInMillis();
		ed.putLong("dateUpdate", dateUpdate);
		//
		ed.commit();
	}
	public static void saveSettings() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor ed = settings.edit();
		ed.putInt("system", system);
		ed.putFloat("stepSize", stepSize);
		ed.putInt("sporttype", sportType);
		//
		ed.putInt("sleepAfterNoMotionOption", sleepAfterNoMotionOption);
		ed.putInt("keepDataDays", keepDataDays);	
		ed.putInt("freeSpaceMB", freeSpaceMB);			
		

		//goal 1
		ed.putInt("goal1MinutesPerDay", goals[0].minutesPerDay);	
		ed.putInt("goal1DaysPerWeek", goals[0].daysPerWeek);	
		ed.putInt("goal1KcalPerWeek", goals[0].kCalPerWeek);	
		//goal2 
		ed.putInt("goal2MinutesPerDay", goals[1].minutesPerDay);	
		ed.putInt("goal2DaysPerWeek", goals[1].daysPerWeek);	
		ed.putInt("goal2KcalPerWeek", goals[1].kCalPerWeek);			
		
		ed.commit();		
	}	

}
