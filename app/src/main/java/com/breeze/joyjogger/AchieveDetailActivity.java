package com.breeze.joyjogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.basic.gui.ThreeLinesView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AchieveDetailActivity extends Activity {
 	
	static long mStartTime = 0;
    static final boolean mAdMob = true; /* TRUE for release version*/
	public static void setDate(Calendar cal){
		mStartTime = cal.getTimeInMillis();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_achieve_detail);
		initUi();
	}
	void initUi(){
		Calendar day = Calendar.getInstance();
		day.setTimeInMillis(mStartTime);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");        
    	
    	Calendar due = Calendar.getInstance();
    	due.setTimeInMillis(mStartTime);
    	due.add(Calendar.DAY_OF_YEAR, 6);
       String text = getResources().getString(R.string.weekly_report) + " "+ 
       		sdf.format(day.getTime()) +" ~ "+ sdf.format(due.getTime());
       TextView tv = (TextView)findViewById(R.id.tv_date); 
       tv.setText(text); 
       
       String szTitle;
		 szTitle = getResources().getString(R.string.achieverate_goal1);			 
       
       TextView tv1 = (TextView)findViewById(R.id.pp_title1); 
       tv1.setText(String.format("%s %4.1f%%", szTitle, GoalScoreCalculator.low.average));
		
       szTitle = getResources().getString(R.string.achieverate_goal2);			 
       TextView tv2 = (TextView)findViewById(R.id.pp_title2); 
       tv2.setText(String.format("%s %4.1f%%", szTitle, GoalScoreCalculator.high.average));
		
       updateGoal(Configure.goals[0], GoalScoreCalculator.low,  R.id.item1a, R.id.item2a, R.id.item3a);
       updateGoal(Configure.goals[1], GoalScoreCalculator.high, R.id.item1b, R.id.item2b, R.id.item3b);

        RootActivity.gAdClick ++;
        if(RootActivity.gAdClick % RootActivity.MAX_AD_CLICKS == 0)
            AdBuddiz.showAd(RootActivity.gInstance);
        AdView mAdView = (AdView)  findViewById(R.id.adView2);

        if(mAdView != null) {

            AdRequest adRequest;
            //
            if (!mAdMob) {
                adRequest = new AdRequest.Builder().
                        addTestDevice("905709C5D9C17913FB3B6A40C19C0640")
                        .build();
            }
            else{
                adRequest = new AdRequest.Builder().
                        build();
            }

            mAdView.loadAd(adRequest);


        }
	}
	void updateGoal(Goal g, GoalScore s, int a, int b, int c){
 		 ThreeLinesView va = (ThreeLinesView)findViewById(a); 
 		 ThreeLinesView vb = (ThreeLinesView)findViewById(b); 
 		 ThreeLinesView vc = (ThreeLinesView)findViewById(c);
 		 String szUnit = getResources().getString(R.string.str_minute);

 		 //times per day
 		 va.title = getResources().getString(R.string.goal_minutes_per_day);
 		 va.value =  String.format("%4d%s", g.minutesPerDay, szUnit);
 		 va.value1 = String.format("%4.1f%s", GoalScoreCalculator.result.minutesPerDay, szUnit);
 		 va.value2 = String.format("%4.1f%%", s.minutesPerDay);
 		 //days per week
 		szUnit = getResources().getString(R.string.str_days);
 		 vb.title = getResources().getString(R.string.goal_days_per_week);
 		 vb.value =  String.format("%4d%s", g.daysPerWeek, szUnit);
 		 vb.value1 = String.format("%4.1f%s", GoalScoreCalculator.result.daysPerWeek, szUnit);
 		 vb.value2 = String.format("%4.1f%%", s.daysPerWeek);		 
 		 //fat per week
  		szUnit = getResources().getString(R.string.str_kcal); 		 
 		 vc.title = getResources().getString(R.string.goal_kcal_per_week);
 		 vc.value =  String.format("%4d%s", g.kCalPerWeek, szUnit);
 		 vc.value1 = String.format("%4.1f%s", GoalScoreCalculator.result.fat, szUnit);
 		 vc.value2 = String.format("%4.1f%%", s.fat); 		 
	} 
		 
}
