package com.breeze.joyjogger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.purplebrain.adbuddiz.sdk.AdBuddizLogLevel;

import java.util.Locale;

public class RootActivity extends Activity {
    private static int NUM_PAGES = 4;
    private final int DEFAULT_FRAGMENT = 0;
    public final static int MAX_AD_CLICKS = 25;
    public static int gAdClick = 0;
    public static boolean bEnglish = true; /* True if english system unit */
    SectionsPagerAdapter mPagerAdapter;

    ViewPager mPager;
    public static SensorManager mSm = null;
    public static RootActivity gInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_root);
        AdBuddiz.setPublisherKey("9f0ace13-d099-40c3-800c-17efda4a020b");
        //AdBuddiz.setTestModeActive(); //Remove it before publishing to Google Play.
        AdBuddiz.setLogLevel(AdBuddizLogLevel.Info);    // or Error, Silent

        AdBuddiz.cacheAds(this); // this = current Activity
        //disable page 4 if not enabled
        String local = Locale.getDefault().getCountry();
        local = local.toLowerCase();

        if (local.equals("tw" ))
            NUM_PAGES = 4;
        else
            NUM_PAGES = 3;

        if (NUM_PAGES == 3){
            ImageView iv4 = (ImageView) findViewById(R.id.imageView4);
            iv4.setVisibility(View.INVISIBLE);
        }
        gInstance = this;
        Configure.init(this);
        Configure.loadProfile();
        Configure.loadSettings();

        if (Configure.system == 0)
            bEnglish = false;
        else
            bEnglish = true;

        DayRecord.init(this);
        mSm = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(DEFAULT_FRAGMENT);
        mPager.setOnPageChangeListener(new OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int arg0) {
                updateIndicator();

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int arg0) {

            }

        });
        StepMeter.enable(this);
        updateIndicator();
    }
    //called by StepMeter, keep screen on while walking
    public void startStepCounter(boolean on){
        if(on == true){
            if( MainFragment.gInstance !=null)
                MainFragment.gInstance.startStepMeter();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else{
            if( MainFragment.gInstance !=null)
                MainFragment.gInstance.stopStepMeter();
            getWindow().setFlags(
                    0,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }
    public void saveStepCounter( ){
        StepMeter.saveStepCounter();
    }
    @Override
    public void onResume(){
        super.onResume();
     }
    @Override
    public void onStart(){
        super.onStart();
        AutoCheckInput.start(this);
        gAdClick ++;
        if(gAdClick % MAX_AD_CLICKS == 0)
            AdBuddiz.showAd(this);
    }
    @Override
    public void onPause(){
        if( StepMeter.isWorking())
            saveStepCounter();
        super.onPause();

    }
    @Override
    public void onDestroy(){
         super.onDestroy();
        StepMeter.disable(this);
    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == DEFAULT_FRAGMENT) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_root, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_profile:
                startPersonalProfileActivity();
                return true;

            case R.id.action_settings:
                startSettingsActivity();
                return true;
            case R.id.action_goal_settings:
                startGoalSettingsActivity();
                return true;
            case R.id.action_help:
                startHelpActivity();
                return true;
            case R.id.action_about:
                startAboutActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return new MainFragment();
            if(position == 1)
                return new HistoryFragment();
            if(position == 2)
                return new AchievementFragment();
            else
                return new FoodFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }
    public void updateIndicator() {

        int[] ids={R.id.imageView1, R.id.imageView2,R.id.imageView3, R.id.imageView4};
        int[] idImageU={R.drawable.page1_u, R.drawable.page2_u,R.drawable.page3_u,R.drawable.page4_u};//,R.drawable.page4_u,R.drawable.page5_u};
        int[] idImageD={R.drawable.page1_d, R.drawable.page2_d,R.drawable.page3_d,R.drawable.page4_d};
        for(int i=0;i<mPagerAdapter.getCount();i++)
        {
            ImageView dot = (ImageView)findViewById(ids[i]);

            if(i==mPager.getCurrentItem())
            {
                dot.setImageResource(idImageU[i]);

            }else
            {
                dot.setImageResource(idImageD[i]);
            }

        }

    }
    private void startPersonalProfileActivity(){
        Intent i = new Intent(this, PersonalProfileActivity.class);
        startActivity(i);
    }
    private void startSettingsActivity(){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    private void startGoalSettingsActivity(){
        Intent i = new Intent(this, GoalSettings.class);
        startActivity(i);
    }
    private void startHelpActivity(){
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);

    }
    private void startAboutActivity(){
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);

    }
    ///////////////////////////////
    /*
    public static class AdFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }
        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
            AdView mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("905709C5D9C17913FB3B6A40C19C0640")
                    .build();

            mAdView.loadAd(adRequest);
        }

    }
*/
}
