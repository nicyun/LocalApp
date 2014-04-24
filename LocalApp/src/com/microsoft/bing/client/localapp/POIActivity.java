package com.microsoft.bing.client.localapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;

public class POIActivity extends Activity implements OnPoiSearchListener {
	private ListFragment listFragment;
	private MapFragment mapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_activity);

		if (savedInstanceState == null) {
			listFragment = new ListFragment();
			mapFragment = new MapFragment();

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.container, mapFragment);
			ft.add(R.id.container, listFragment);
			ft.hide(mapFragment);
			ft.commit();
		}
		
		startInitQuery();
	}
	
	private void startInitQuery() {
		// Detailed category please check: http://code.autonavi.com/Public/down/AMap_Api_Table.zip
		Query query = new Query("", "050000", "010");
		query.setPageSize(10);
		query.setPageNum(0);
		PoiSearch poiSearch = new PoiSearch(this, query);
		// TODO: get current location and configure range
		poiSearch.setBound(new SearchBound(new LatLonPoint(39.908127, 116.375257), 1000));
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}
	

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// Pass for now.
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				PoiResult poiResult = result;
				int resultPages = poiResult.getPageCount();
				List<PoiItem> poiItems = poiResult.getPois();

				if (poiItems != null && poiItems.size() > 0) {
					// Add to list view.
					for (PoiItem item : poiItems) {
						listFragment.addItem(item.getTitle());
					}
					
					// Add to map view.
					mapFragment.addPOI(poiItems);
				} else {
					// No result
				}
			} else {
				// No result
			}
		} else {
			// Network error
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			// TODO: add a custom button in action bar for this, stop using the setting...
			switchFragment();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void switchFragment() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (listFragment.isVisible()) {
			ft.show(mapFragment);
			ft.hide(listFragment);
		} else {
			ft.show(listFragment);
			ft.hide(mapFragment);
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	// List fragment.
	public static class ListFragment extends Fragment {

		private ArrayList<String> listItems = new ArrayList<String>();
		private ArrayAdapter<String> adapter;
		
		public ListFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.poi_list_fragment,
					container, false);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
			ListView listView = (ListView) getView().findViewById(R.id.poilistview);
			listView.setAdapter(adapter);
		}
		
		public void addItem(String item) {
			listItems.add(item);
			adapter.notifyDataSetChanged();
		}
	}

	// Map fragment.
	public static class MapFragment extends Fragment {
		private MapView mapView;
		private AMap aMap;

		public MapFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.poi_map_fragment,
					container, false);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

	        mapView = (MapView) getView().findViewById(R.id.map);
	        mapView.onCreate(savedInstanceState);
	        init();
		}
		
		private void init() {
	    	if (aMap == null) {
	    		aMap = mapView.getMap();
	    	}
	    }
		
		public void addPOI(List<PoiItem> poiItems) {
			aMap.clear();
			PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
			poiOverlay.removeFromMap();
			poiOverlay.addToMap();
			poiOverlay.zoomToSpan();
		}
	    
	    @Override
		public void onResume() {
	    	super.onResume();
	    	mapView.onResume();
	    }
	    
	    @Override
		public void onPause() {
	    	super.onPause();
	    	mapView.onPause();
	    }
	    
	    @Override
		public void onSaveInstanceState(Bundle outState) {  
	        super.onSaveInstanceState(outState);  
	        mapView.onSaveInstanceState(outState);  
	    }  

	    @Override
		public void onDestroy() {  
	        super.onDestroy();  
	        mapView.onDestroy();  
	    }  
	}
}