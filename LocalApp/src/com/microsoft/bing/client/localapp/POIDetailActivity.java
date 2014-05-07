package com.microsoft.bing.client.localapp;

import com.amap.api.services.core.PoiItem;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class POIDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poidetail_activity);

		if (savedInstanceState == null) {
		}
		
		// initialize
		PoiItem item = (PoiItem) getIntent().getExtras().getParcelable("PoiItem");
		initFields(item);
	}
	
	private void initFields(PoiItem item) {
		((TextView) findViewById(R.id.poidetail_name)).setText(item.getTitle());
		((TextView) findViewById(R.id.poidetail_address)).setText(item.getSnippet());
		((TextView) findViewById(R.id.poidetail_type)).setText(item.getTypeDes());
		((TextView) findViewById(R.id.poidetail_area)).setText(item.getAdName());
		((TextView) findViewById(R.id.poidetail_phone)).setText(item.getTel());
		((TextView) findViewById(R.id.poidetail_city)).setText(item.getCityName());
		((TextView) findViewById(R.id.poidetail_province)).setText(item.getProvinceName());
		((TextView) findViewById(R.id.poidetail_distance)).setText(item.getDistance() + "");
		((TextView) findViewById(R.id.poidetail_website)).setText(item.getWebsite());
		((TextView) findViewById(R.id.poidetail_zip)).setText(item.getPostcode());
		((TextView) findViewById(R.id.poidetail_email)).setText(item.getEmail());
		((TextView) findViewById(R.id.poidetail_direction)).setText(item.getDirection());
		((TextView) findViewById(R.id.poidetail_tuan)).setText(item.isGroupbuyInfo() + "");
		((TextView) findViewById(R.id.poidetail_discount)).setText(item.isDiscountInfo() + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poidetail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
