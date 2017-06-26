package com.breeze.joyjogger;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GoalSettings extends Activity {
	public final static String SET_GOAL = "set_goal";

	LayoutInflater mLi;	
	GoalItem mGi1;
	GoalItem mGi2;	
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_goal_settings);
		initUi();
	}
	private void initUi(){
		setTitle(R.string.title_activity_goal_settings);
		
		TextView tv = (TextView)findViewById(R.id.pp_title);
 		tv.setText(R.string.str_goal1);
		TextView tv2 = (TextView)findViewById(R.id.pp_title2);
 		tv2.setText(R.string.str_goal2);
 
		mLi = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mGi1 = new GoalItem(GoalItem.GOAL1);
		ListView lv = (ListView) findViewById(R.id.lvProfile);		   	 	
		lv.setAdapter(mGi1 );
		//mGi1.notifyDataSetChanged();  
		mGi2 = new GoalItem(GoalItem.GOAL2);
		ListView lv2 = (ListView) findViewById(R.id.lvProfile2);		   	 	
		lv2.setAdapter(mGi2 );
		//mGi2.notifyDataSetChanged();   
	}//end initUi()   	
   public class GoalItem extends BaseAdapter {
		public final static int GOAL1 = 0;	
		public final static int GOAL2 = 1;
		public int mGoal;
 		int MAX_ITEM_COUNTS = 3;
 		
 		public GoalItem(int goal){
		 
 			mGoal = goal;
 		}
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
      	    case 0: //goal_minutes_per_day
     	
      	    	tv1.setText(R.string.goal_minutes_per_day);    	    	
      	    	tv2.setText(Integer.toString(Configure.goals[mGoal].minutesPerDay));     			          
      	    	tv3.setText(R.string.str_minute);
       			itemView.setOnClickListener(new OnClickListener(){
      				@Override
      				public void onClick(View v) {
      					change0();
      				}
      			});
      	    	break;
  
      	    case 1: //goal_days_per_week
      	    	tv1.setText(R.string.goal_days_per_week);    	    	
      	    	tv2.setText(Integer.toString(Configure.goals[mGoal].daysPerWeek));     			          
      	    	tv3.setText(R.string.str_days);

      			itemView.setOnClickListener(new OnClickListener(){
      				@Override
      				public void onClick(View v) {
      					change1();
      				}
      			});     			
      	    	break;     	      
      	    case 2: //goal_days_per_week
      	    	tv1.setText(R.string.goal_kcal_per_week);    	    	
      	    	tv2.setText(Float.toString(Configure.goals[mGoal].kCalPerWeek));     			          
      	    	tv3.setText(R.string.str_kcal);

      			itemView.setOnClickListener(new OnClickListener(){
      				@Override
      				public void onClick(View v) {
      					change2();
      				}
      			});     			
      	    	break;      	    	
  	    	default:
  	    		return null;
      	    }
     		return itemView;
 		}
   void change0(){
  		final CharSequence[] items = Configure.getGoalMinutesPerDayOptionsString(mGoal);
		AlertDialog.Builder adb = new AlertDialog.Builder(GoalSettings.this);
		adb.setTitle( R.string.goal_minutes_per_day );
		adb.setSingleChoiceItems(items,Configure.getGoalMinutesPerDayOption(mGoal), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Configure.setGoalMinutesPerDayOption(mGoal, which); 				
				notifyDataSetChanged(); 
				Configure.saveSettings();
			}
			
		});
		adb.show();        
   } 		
   
   void change1( ){
		final CharSequence[] items = Configure.getGoalDaysPerWeekOptionsString(mGoal);
		AlertDialog.Builder adb = new AlertDialog.Builder(GoalSettings.this);
		adb.setTitle( R.string.goal_days_per_week );
		adb.setSingleChoiceItems(items,Configure.getGoalDaysPerWeekOption(mGoal), 
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Configure.setGoalDaysPerWeekOption(mGoal, which); 				
				notifyDataSetChanged(); 
 				Configure.saveSettings();
			}
			
		});
		adb.show();   	   
  }   
   

   void change2( ){
		final CharSequence[] items = Configure.getGoalKcalPerWeekOptionsString(mGoal);
		AlertDialog.Builder adb = new AlertDialog.Builder(GoalSettings.this);
		adb.setTitle( R.string.goal_kcal_per_week );
		adb.setSingleChoiceItems(items,Configure.getGoalKcalPerWeekOption(mGoal), 
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Configure.setGoalKcalPerWeekOption(mGoal, which); 				
				notifyDataSetChanged(); 
				Configure.saveSettings();
			}
			
		});
		adb.show();  	   
   }   
 	}	

 
}
