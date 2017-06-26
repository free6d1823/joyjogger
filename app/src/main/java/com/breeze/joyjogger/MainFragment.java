package com.breeze.joyjogger;

import com.basic.gui.SpeedometerView;
import com.basic.simplot.AccumulatePlot;
import com.basic.simplot.YPlot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cj on 2014/11/9.
 */
public class MainFragment extends Fragment {
    private View mRootView = null;
    private AccumulatePlot mAcFat;
    private TextView mTvTime;
    private TextView mTbSteps;

    private SpeedometerView mSpeedometer;
    private YPlot mStepPerM; //step counts per minute
    private BroadcastReceiver mReceiver = null;

    private Context mContext;
    public static MainFragment gInstance = null;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main, container, false);
        mRootView = rootView;
        mContext = RootActivity.gInstance;

        initUi(rootView);
        initCallbacks();
        gInstance = this;
        return rootView;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        gInstance = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(StepMeter.DATA_CHANGED);
        filter.addAction(StepMeter.SPEED_CHANGED);
        mContext.registerReceiver(mReceiver, filter);

        if(StepMeter.isWorking()){
            AnimationDrawable animationDrawable2 = (AnimationDrawable)mIvStep.getDrawable();
            animationDrawable2.start();
            onMinuteChanged();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(mReceiver);

    }
    void initCallbacks(){
        mReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(StepMeter.DATA_CHANGED.equals(action)){
                    StepMeter.StepData info = StepMeter.mInstance.m;
                    onDataChanged(info);
                }
                else if(StepMeter.SPEED_CHANGED.equals(action)){

                    onMinuteChanged();
                }

            }

        };
    }
    void updateButtonsState(View view){
        Button bt1 = (Button) view.findViewById(R.id.btnStart);
        //bt1.setEnabled(StepMeter.isWorking() == true);
        bt1.setText(StepMeter.isWorking()? R.string.stop: R.string.start);
        Button bt3 = (Button) view.findViewById(R.id.btnReset);
        if(StepMeter.isWorking() == true){
            //bt3.setEnabled(true);
            bt3.setVisibility(View.VISIBLE);
        }
        else{
            bt3.setVisibility(View.INVISIBLE);
        }
    }
    public void startStepMeter(){
        StepMeter.start();
        //mIvStep.setBackground(getResources().getDrawable(R.drawable.stepcounts));
        mIvStep.setImageResource( R.drawable.stepcounts) ;

        AnimationDrawable animationDrawable2 = (AnimationDrawable)mIvStep.getDrawable();
        animationDrawable2.start();


        updateButtonsState(mRootView);
    }
    public void stopStepMeter(){

        StepMeter.stop();



        AnimationDrawable animationDrawable2 = (AnimationDrawable)mIvStep.getDrawable();
        animationDrawable2.stop();
        mIvStep.setImageResource( R.drawable.step_left) ;

        updateButtonsState(mRootView);
    }
    ImageView mIvStep;
    void initUi(View view){
    	/*

    	final Button bt2 = (Button) view.findViewById(R.id.btnStop);*/
        final Button bt1 = (Button) view.findViewById(R.id.btnStart);
        final Button bt3 = (Button) view.findViewById(R.id.btnReset);

        mIvStep = (ImageView) view.findViewById(R.id.imageLight);

        mIvStep.setImageResource (R.drawable.stepcounts);

        bt1.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                //always called from RootActivity
                RootActivity.gInstance.startStepCounter( !StepMeter.isWorking());

            }

        });
        bt3.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StepMeter.resetCounter();
                updateButtonsState(mRootView);
            }

        });

        mTvTime = (TextView)view.findViewById(R.id.strTime);
        mTvTime.setText("00:00:00");
        mTbSteps = (TextView)view.findViewById(R.id.tvSteps);


        mSpeedometer = (SpeedometerView)view.findViewById(R.id.strSpeed);
        // Add label converter
        mSpeedometer.setLabelConverter(new SpeedometerView.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        updateSpeedMeterGui();
        mStepPerM = (YPlot)view.findViewById(R.id.strBurnrate);

        mAcFat = (AccumulatePlot)view.findViewById(R.id.strFat);
        mAcFat.clear();

        updateButtonsState(view);
    }

    public void updateSpeedMeterGui(){

        // configure value range and ticks
        mSpeedometer.setMajorTickStep(5);
        mSpeedometer.setMinorTicks(1);
        if(RootActivity.bEnglish) {
            mSpeedometer.setMaxSpeed(25);        // Configure value range colors
            mSpeedometer.addColoredRange(1, 15, Color.GREEN);
            mSpeedometer.addColoredRange(15, 20, Color.YELLOW);
            mSpeedometer.addColoredRange(20, 25, Color.RED);
        }
        else {
            mSpeedometer.setMaxSpeed(40);
            // Configure value range colors
            mSpeedometer.addColoredRange(1, 20, Color.GREEN);
            mSpeedometer.addColoredRange(20, 30, Color.YELLOW);
            mSpeedometer.addColoredRange(30, 40, Color.RED);
        }


    }

    public void onDataChanged(StepMeter.StepData m) {
        if(m != null){
            int s = m.mTimeSeconds%60;
            int mi = m.mTimeSeconds/60;
            int h = mi /60;
            mi = mi%60;
            String szTm = String.format("%02d:%02d:%02d", h, mi, s);
            mTvTime.setText(szTm);
            szTm = String.format("%8d", m.mSteps);

            mTbSteps.setText(szTm);
            if(RootActivity.bEnglish) {
                mSpeedometer.setDistance((int)(m.mDistanceMeter* 3.28084f));
                mSpeedometer.setSpeed(StepMeter.mInstance.mSpeed.getSpeed()* 0.62137f);
            }
            else {
                mSpeedometer.setDistance(m.mDistanceMeter);

                mSpeedometer.setSpeed(StepMeter.mInstance.mSpeed.getSpeed());
            }
        }
    }

    public void onMinuteChanged() {
        float[] line = StepMeter.getLastHourRpm();
        float[] line2 = StepMeter.getLastHourAccFat();

        if( line != null ){

            mStepPerM.setSeries(line);
            mAcFat.setSeries(line2);

        }
    }

}