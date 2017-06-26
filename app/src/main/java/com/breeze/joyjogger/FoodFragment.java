package com.breeze.joyjogger;

import java.util.Locale;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

//http://60.251.146.77/external/joyjoger/tw/foodtype1.htm
//http://60.251.146.77/external/joyjoger/tw/help.htm
//http://health.nkfust.edu.tw/svelte/sveltep3.htm

public class FoodFragment extends Fragment {

    public FoodFragment() {
        // Required empty public constructor
    }

    WebView mWv;
    //String rootPath ="http://health.nkfust.edu.tw/svelte/";
    String rootPath ="file:///android_asset/";
    String mBase = "page1.htm";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_food, container, false);
        initUi(rootView);

        return rootView;
    }
    void initUi(View v){

        String local = Locale.getDefault().getCountry();
        local = local.toLowerCase();

        if (!local.equals("tw" )&& !local.equals("cn" ) &&
                !local.equals("jp" )&& !local.equals("en" ))
            local = "en";
        mWv = (WebView) v.findViewById(R.id.wv1);
        mWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

//        String url = rootPath+local+mBase;
        String url = rootPath+mBase;

        mWv.loadUrl(url);

        RootActivity.gAdClick ++;
        if(RootActivity.gAdClick % RootActivity.MAX_AD_CLICKS == 0)
            AdBuddiz.showAd(RootActivity.gInstance);

    }

}
