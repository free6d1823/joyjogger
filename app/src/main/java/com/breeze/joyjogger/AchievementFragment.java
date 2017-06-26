package com.breeze.joyjogger;


import android.os.Bundle;
import android.app.Fragment;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.CalendarView;
import android.widget.DatePicker;

import android.widget.TextView;

import com.basic.gui.HeaderView;
import com.basic.gui.ItemView;
import com.basic.simplot.BasicRadar;
import com.basic.simplot.WeeklyReport;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;


public class AchievementFragment extends Fragment implements OnClickListener{

    private Context mContext;
    private TextView mTv1;
    Calendar mCalWeekly = null;
    WeeklyReport mWeeklyReport;
    BasicRadar mRadar;
    static boolean mBusy = false;
    static final boolean mAdMob = true; /* TRUE to open AdMOb */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = RootActivity.gInstance;
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);
        initUi(view);
        return view;
    }
    GoalScore[] mScore = new GoalScore[2];

    void initUi(View v){

        mScore[0] = null;
        mScore[1] = null;

        mWeeklyReport = (WeeklyReport) v.findViewById(R.id.plot_weekly);
        mRadar = (BasicRadar)v.findViewById(R.id.plot_radar);
        mTv1 = (TextView) v.findViewById(R.id.pp_title);

        mTv1.setOnClickListener(this);
        myDialog = new ProgressDialog(mContext);
        myDialog.setTitle("Please wait" );
        myDialog.setCancelable(false);
        myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        updateGoalAchieve(v, 0);
        updateGoalAchieve(v, 1);

        View list = v.findViewById(R.id.list_item);
        list.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                showAchieveDetail();

            }

        });


        updateUi(v);
    }
    @Override
    public void onStart() {
        super.onStart();
    if( getView() != null)
        updateUi(getView());
        RootActivity.gAdClick ++;
        if(RootActivity.gAdClick % RootActivity.MAX_AD_CLICKS == 0)
            AdBuddiz.showAd(RootActivity.gInstance);
    }
    @Override
    public void onStop() {
        super.onStop();
        //if(!mBusy) //free memory when this is invisible
        //	GoalScoreCalculator.free();
    }
    Handler mHandler = new Handler( ){
        @Override
        public void handleMessage(Message msg){
            int p = msg.getData().getInt("setWeeklyReport");
            if(p==1)setWeeklyReport();
        }
    };
    void showAchieveDetail(){
        if(mCalWeekly == null)
            return;
        Intent i = new Intent(mContext, AchieveDetailActivity.class);
        AchieveDetailActivity.setDate(mCalWeekly);
        startActivity(i);
    }
    ProgressDialog myDialog = null;
    void setWeeklyReport(  ){
        //calculate data
        if(myDialog != null)
            myDialog.dismiss();

        mScore[0] = GoalScoreCalculator.low;
        mScore[1] = GoalScoreCalculator.high;

        mRadar.updateAxis(GoalScoreCalculator.result.daysPerWeek,
                GoalScoreCalculator.result.minutesPerDay, GoalScoreCalculator.result.fat);

        mWeeklyReport.setWeeklyData(mCalWeekly.getTimeInMillis() );
        mWeeklyReport.update();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Calendar due = Calendar.getInstance();
        due.setTime(mCalWeekly.getTime());
        due.add(Calendar.DAY_OF_YEAR, 6);
        String text = mContext.getResources().getString(R.string.title_achievement) + " "+
                sdf.format(mCalWeekly.getTime()) +" ~ "+ sdf.format(due.getTime());

        mTv1.setText(text);
        updateUi(getView());
        updateGoalAchieve(getView(),0);
        updateGoalAchieve(getView(),1);
    }
    DatePickerDialog dateDialog = null;
    void showWeeklyDialog(){
        if(mCalWeekly == null)
            mCalWeekly = Calendar.getInstance();
        dateDialog = new DatePickerDialog(mContext,
                new OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (view.isShown() && mBusy==false) {


                            mCalWeekly.set(Calendar.YEAR,year);
                            mCalWeekly.set(Calendar.MONTH,monthOfYear);
                            mCalWeekly.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                            mCalWeekly.add(Calendar.DAY_OF_YEAR, Calendar.SUNDAY - mCalWeekly.get(Calendar.DAY_OF_WEEK));
                            //mCalWeekly.set(Calendar.DAY_OF_WEEK,
                            //		mCalWeekly.getFirstDayOfWeek());

                            new Thread(){
                                public void run(){
                                    mBusy = true;
                                    GoalScoreCalculator.calculate(mCalWeekly.getTimeInMillis() );
                                    mBusy = false;
                                    Message m = new Message();
                                    Bundle b = m.getData();
                                    b.putInt("setWeeklyReport", 1);
                                    m.setData(b);
                                    mHandler.sendMessage(m);

                                }
                            }.start();
                            myDialog.show();
                        }
                    }},
                mCalWeekly.get(Calendar.YEAR),
                mCalWeekly.get(Calendar.MONTH),
                mCalWeekly.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = dateDialog.getDatePicker();

        CalendarView cv = dp.getCalendarView();

        cv.setShowWeekNumber(true);
        dateDialog.getDatePicker().setCalendarViewShown(true);
        dateDialog.getDatePicker().setSpinnersShown(false);

        dateDialog.show();


    }
    @Override
    public void onClick(View v) {
        RootActivity.gAdClick ++;
        if(RootActivity.gAdClick % RootActivity.MAX_AD_CLICKS == 0)
            AdBuddiz.showAd(RootActivity.gInstance);

        if(v.getId() == R.id.pp_title)
            showWeeklyDialog();

    }

    void setCheckMark(ItemView v, float score){
        if(score > 95)
            v.setIcon(R.drawable.goal_check_a);
        else if (score >80)
            v.setIcon(R.drawable.goal_check_b);
        else
            v.setIcon(R.drawable.goal_check_none);
    }
    /* pos =0 Low; =1 high */
    void updateGoalAchieve(View v, int pos){
        HeaderView hv;

        ItemView iv1;

        ItemView iv2;
        ItemView iv3;
        String szTitle;

        if(pos == 0)
        {
            hv = (HeaderView) v.findViewById(R.id.headera);
            iv1 = (ItemView)v.findViewById(R.id.item1a);
            iv2 = (ItemView)v.findViewById(R.id.item2a);
            iv3 = (ItemView)v.findViewById(R.id.item3a);
            szTitle = mContext.getResources().getString(R.string.achieverate_goal1);
        }
        else{
            hv = (HeaderView) v.findViewById(R.id.headerb );
            iv1 = (ItemView)v.findViewById(R.id.item1b);
            iv2 = (ItemView)v.findViewById(R.id.item2b);
            iv3 = (ItemView)v.findViewById(R.id.item3b);
            szTitle = mContext.getResources().getString(R.string.achieverate_goal2);
        }
        //
        if(mScore[pos] != null){
            if(pos == 0){
                if(mScore[pos].average > 90)
                    hv.setIcon( R.drawable.goal_medal2c);
                else if(mScore[pos].average > 80)
                    hv.setIcon( R.drawable.goal_medal2d);
            }else{
                if(mScore[pos].average > 99)
                    hv.setIcon( R.drawable.goal_medal2a);
                else if(mScore[pos].average > 90)
                    hv.setIcon( R.drawable.goal_medal2b);
                else if(mScore[pos].average > 80)
                    hv.setIcon( R.drawable.goal_medal2c);
                else if(mScore[pos].average > 70)
                    hv.setIcon( R.drawable.goal_medal2d);
                else if(mScore[pos].average > 60)
                    hv.setIcon( R.drawable.goal_medal2e);
            }

            hv.title = String.format("%s %4.1f%%", szTitle, mScore[pos].average);
            iv1.value = String.format("%4.1f%%", mScore[pos].minutesPerDay);
            setCheckMark(iv1, mScore[pos].minutesPerDay);
            iv2.value = String.format("%4.1f%%", mScore[pos].daysPerWeek);
            setCheckMark(iv2, mScore[pos].daysPerWeek);
            iv3.value = String.format("%4.1f%%", mScore[pos].fat);
            setCheckMark(iv3, mScore[pos].fat);

        } else
            hv.title =  szTitle;
        iv1.title =  mContext.getResources().getString(R.string.goal_minutes_per_day);
        iv2.title =  mContext.getResources().getString(R.string.goal_days_per_week);
        iv3.title =  mContext.getResources().getString(R.string.goal_kcal_per_week);
        hv.invalidate();
        iv1.invalidate();
        iv2.invalidate();
        iv3.invalidate();

    }
    private void updateUi(View v){
        String szTitle;
        int visible;
        View radar = v.findViewById(R.id.plot_radar);
        HeaderView hva = (HeaderView) v.findViewById(R.id.headera);
        View v1a = v.findViewById(R.id.item1a);
        View v2a = v.findViewById(R.id.item2a);
        View v3a = v.findViewById(R.id.item3a);
        HeaderView hvb = (HeaderView) v.findViewById(R.id.headerb );
        View v1b = v.findViewById(R.id.item1b);
        View v2b = v.findViewById(R.id.item2b);
        View v3b = v.findViewById(R.id.item3b);

        if(mCalWeekly == null){
            szTitle = mContext.getResources().getString(R.string.prompt_pickup_a_week);
            visible = View.INVISIBLE;
        }else
        {
            visible = View.VISIBLE;
            szTitle = mContext.getResources().getString(R.string.achieverate_goal1);
        }
        hva.title =  szTitle;
        radar.setVisibility(visible);
        v1a.setVisibility(visible);
        v2a.setVisibility(visible);
        v3a.setVisibility(visible);
        v1b.setVisibility(visible);
        v2b.setVisibility(visible);
        v3b.setVisibility(visible);
        hvb.setVisibility(visible);
        AdView mAdView = (AdView) v.findViewById(R.id.adView2);

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
}