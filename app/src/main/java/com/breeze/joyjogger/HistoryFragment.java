package com.breeze.joyjogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.basic.simplot.DailyReport;
import com.basic.simplot.MonthlyReport;
import com.basic.simplot.WeeklyReport;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.app.DatePickerDialog;


import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.app.Fragment;

 import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;


    public class HistoryFragment extends Fragment implements OnClickListener {

        TextView mTv1;
        TextView mTv2;
        TextView mTv3;
        DailyReport mDailyReport;
        Calendar mCalDaily;
        WeeklyReport mWeeklyReport;
        Calendar mCalWeekly;
        MonthlyReport mMonthlyReport;
        Calendar mCalMonthly = null;

        private Context mContext;

        public HistoryFragment() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_history, container, false);
            mContext = RootActivity.gInstance;
            initUi(rootView);
            return rootView;
        }
        void initUi(View v){
            mDailyReport = (DailyReport) v.findViewById(R.id.plot_daily);
            mWeeklyReport = (WeeklyReport) v.findViewById(R.id.plot_weekly);
            mMonthlyReport = (MonthlyReport) v.findViewById(R.id.plot_monthly);
            mTv1 = (TextView)v.findViewById(R.id.pp_daily);
            mTv1.setOnClickListener(this);
            mTv2 = (TextView)v.findViewById(R.id.pp_weekly);
            mTv2.setOnClickListener(this);
            mTv3 = (TextView)v.findViewById(R.id.pp_monthly);
            mTv3.setOnClickListener(this);

            mCalDaily = Calendar.getInstance();
            mCalDaily.set(Calendar.HOUR_OF_DAY, 0);
            mCalDaily.set(Calendar.MINUTE, 0);

            mCalWeekly = Calendar.getInstance();
            mCalWeekly.set(Calendar.DAY_OF_WEEK,
                    mCalWeekly.getFirstDayOfWeek());

            setDailyReport( );
        }
        final int DO_DAILY_REPORT = 1;
        final int DO_WEEKLY_REPORT = 2;
        final int DO_MONTHLY_REPORT = 3;
        void setDailyReport(  ){
            new Thread(){
                public void run(){

                    float[] rpm = new float[60*24];
                    float[] fat = new float[60*24];
                    DayRecord.pRPM = rpm;
                    DayRecord.pFat = fat;

                    DayRecord.loadDay(mCalDaily);
                    mDailyReport.setSeries(rpm, fat);

                    rpm = null;
                    fat = null; //DailyReport will duplicat buffer, free our buffers

                    Message m = new Message();
                    Bundle b = m.getData();
                    b.putInt("JobDone", DO_DAILY_REPORT);
                    m.setData(b);
                    mHandler.sendMessage(m);

                }
            }.start();


        }
        void showDailyDialog(){

            DatePickerDialog tempDialog = new DatePickerDialog(mContext,
                    new OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if (view.isShown()) {
                                mCalDaily.set(Calendar.YEAR,year);
                                mCalDaily.set(Calendar.MONTH,monthOfYear);
                                mCalDaily.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                setDailyReport();
                            }
                        }},
                    mCalDaily.get(Calendar.YEAR),
                    mCalDaily.get(Calendar.MONTH),
                    mCalDaily.get(Calendar.DAY_OF_MONTH));
            //tempDialog.getDatePicker().setMinDate(mCalDaily.getTimeInMillis());
            DatePicker dp = tempDialog.getDatePicker();
            CalendarView cv = dp.getCalendarView();
            cv.setShowWeekNumber(false);
            tempDialog.getDatePicker().setCalendarViewShown(true);
            tempDialog.getDatePicker().setSpinnersShown(false);
            tempDialog.show();
        }
        void setWeeklyReport(  ){

            new Thread(){
                public void run(){


                    mWeeklyReport.setWeeklyData(mCalWeekly.getTimeInMillis() );


                    Message m = new Message();
                    Bundle b = m.getData();
                    b.putInt("JobDone", DO_WEEKLY_REPORT);
                    m.setData(b);
                    mHandler.sendMessage(m);

                }
            }.start();


        }
        void showWeeklyDialog(){

            DatePickerDialog tempDialog = new DatePickerDialog(mContext,
                    new OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if (view.isShown()) {
                                mCalWeekly.set(Calendar.YEAR,year);
                                mCalWeekly.set(Calendar.MONTH,monthOfYear);
                                mCalWeekly.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                mCalWeekly.add(Calendar.DAY_OF_YEAR, Calendar.SUNDAY - mCalWeekly.get(Calendar.DAY_OF_WEEK));
                                setWeeklyReport();
                            }
                        }},
                    mCalWeekly.get(Calendar.YEAR),
                    mCalWeekly.get(Calendar.MONTH),
                    mCalWeekly.get(Calendar.DAY_OF_MONTH));
            //tempDialog.getDatePicker().setMinDate(mCalDaily.getTimeInMillis());
            DatePicker dp = tempDialog.getDatePicker();
            CalendarView cv = dp.getCalendarView();
            cv.setShowWeekNumber(true);
            tempDialog.getDatePicker().setCalendarViewShown(true);
            tempDialog.getDatePicker().setSpinnersShown(false);
            tempDialog.show();
        }
        void setMonthlyReport(  ){
            new Thread(){
                public void run(){
                    mMonthlyReport.setStartMonth(mCalMonthly.get(Calendar.YEAR),
                            mCalMonthly.get(Calendar.MONTH));

                    Message m = new Message();
                    Bundle b = m.getData();
                    b.putInt("JobDone", DO_MONTHLY_REPORT);
                    m.setData(b);
                    mHandler.sendMessage(m);

                }
            }.start();

        }
        void showMonthlyDialog(){
            if(mCalMonthly == null)
                mCalMonthly = Calendar.getInstance();
            DatePickerDialog tempDialog = new DatePickerDialog(mContext,
                    new OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if (view.isShown()) {
                                mCalMonthly = new GregorianCalendar(year, monthOfYear, 1);
                                setMonthlyReport();
                            }
                        }},
                    mCalMonthly.get(Calendar.YEAR),
                    mCalMonthly.get(Calendar.MONTH),
                    1);
            tempDialog.getDatePicker().setCalendarViewShown(false);
            tempDialog.getDatePicker().setSpinnersShown(true);
            tempDialog.show();
        }
        @Override
        public void onClick(View v) {
            RootActivity.gAdClick ++;
            if(RootActivity.gAdClick % RootActivity.MAX_AD_CLICKS == 0)
                AdBuddiz.showAd(RootActivity.gInstance);
            switch(v.getId()){

                case R.id.pp_daily:
                    showDailyDialog();
                    break;
                case R.id.pp_weekly:
                    showWeeklyDialog();
                    break;
                case R.id.pp_monthly:
                    showMonthlyDialog();
                    break;
            }
        }
        void onDailyReport(  ){
            mDailyReport.update();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM/dd");
            String text = mContext.getResources().getString(R.string.daily_report) +
                    sdf.format(mCalDaily.getTime());

            mTv1.setText(text);
        }
        void onWeeklyReport(  ){
            //mWaitingDialog.dismiss();

            mWeeklyReport.update();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

            Calendar due = Calendar.getInstance();
            due.setTime(mCalWeekly.getTime());
            due.add(Calendar.DAY_OF_YEAR, 6);
            String text = mContext.getResources().getString(R.string.weekly_report) + " "+
                    sdf.format(mCalWeekly.getTime()) +" ~ "+ sdf.format(due.getTime());

            mTv2.setText(text);

        }
        void onMonthlyReport(  ){

            if(mCalMonthly == null)
                return;
            mMonthlyReport.update();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM");
            String text = mContext.getResources().getString(R.string.monthly_report) + " "+
                    sdf.format(mCalMonthly.getTime());

            mTv3.setText(text);

        }
        Handler mHandler = new Handler( ){
            @Override
            public void handleMessage(Message msg){
                int p = msg.getData().getInt("JobDone");

                switch(p){
                    case DO_DAILY_REPORT: //set daily
                        onDailyReport(  );
                        break;
                    case DO_WEEKLY_REPORT: //seet weekly
                        onWeeklyReport(  );
                        break;
                    case DO_MONTHLY_REPORT: //seet monthly
                        onMonthlyReport(  );
                        break;

                }
            }
        };

    }