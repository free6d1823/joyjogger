package com.breeze.joyjogger;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
 
import android.widget.TextView;


public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_about);
		
 	  
		TextView iv = (TextView)findViewById(R.id.tvEmail);
		iv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent mailer = new Intent(Intent.ACTION_SEND);
				mailer.setType("text/plain");
				mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{"cjc500@sparqnet.net"});
				mailer.putExtra(Intent.EXTRA_SUBJECT, "Feedback to Joy Jogger");
				String smail = getResources().getString(R.string.send_mail);
				startActivity(Intent.createChooser(mailer, smail));			       
			}
			
		});
        TextView tvShare = (TextView) findViewById(R.id.tvSharing);
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String szText;
                szText = getResources().getText(R.string.str_sharing_title).toString();
                szText += "\n";
                szText += getResources().getText(R.string.str_slogon).toString();
                szText += "\n";
                szText += "https://play.google.com/store/apps/details?id=com.breeze.joyjogger";
                Intent mailer = new Intent(Intent.ACTION_SEND);
                mailer.setType("text/plain");
                mailer.putExtra(Intent.EXTRA_SUBJECT, "I'd like to share this application with you");
                mailer.putExtra(Intent.EXTRA_TEXT, szText);
                String smail = getResources().getString(R.string.str_sharing);
                startActivity(Intent.createChooser(mailer, smail));

            }
        });
	}
}
