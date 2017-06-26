package com.breeze.joyjogger;

import java.util.Locale;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class HelpActivity extends Activity {
	WebView mWv;
	String rootPath = "http://60.251.146.77/external/joyjoger/";
	String mBase = "/help.htm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.fragment_food);
		initUi();
	}
    void initUi(){
    	
    	String local = Locale.getDefault().getCountry();
    	local = local.toLowerCase();

    	if (!local.equals("tw" )&& !local.equals("cn" ) &&
    			!local.equals("jp" )&& !local.equals("en" ))
    		local = "en"; 
    	mWv = (WebView) findViewById(R.id.wv1);        
        mWv.setWebViewClient(new WebViewClient() {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return false;
	        }
	    });

		String url = rootPath+local+mBase;
 
		mWv.loadUrl(url);      



    }
}
