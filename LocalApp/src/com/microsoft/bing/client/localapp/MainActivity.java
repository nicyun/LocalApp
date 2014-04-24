package com.microsoft.bing.client.localapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
    
    public void onResterantButtonClick(View view) {
    	Intent intent = new Intent(this, POIActivity.class);
    	startActivity(intent);
    }
}
