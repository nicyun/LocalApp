package com.microsoft.bing.client.localapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private ProgressDialog loadingDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_activity);
		
		String typeCode = (String) getIntent().getExtras().get("type");
		
		if (savedInstanceState == null) {
			listFragment = new ListFragment();
			mapFragment = new MapFragment();
			loadingDlg = new ProgressDialog(POIActivity.this);
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.container, mapFragment);
			ft.add(R.id.container, listFragment);
			ft.hide(mapFragment);
			ft.hide(listFragment);
			ft.commit();

			loadingDlg.setMessage("Loading...");
			loadingDlg.setCancelable(false);
			loadingDlg.show();
			
			startInitQuery(typeCode);
		}
	}
	
	private void startInitQuery(String typeCode) {
		Query query = new Query("", typeCode, "010");
		query.setPageSize(10);
		query.setPageNum(0);
		PoiSearch poiSearch = new PoiSearch(this, query);
		// TODO: get current location and configure range
		poiSearch.setBound(new SearchBound(new LatLonPoint(39.978787, 116.311102), 1000));
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
					listFragment.addPOIs(poiItems);
					// Add to map view.
					mapFragment.addPOIs(poiItems);
					// This will show list view as default.
					switchFragment();
					loadingDlg.dismiss();
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
			return true;
		}
		if (id == R.id.action_switchview) {
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

		private List<String> listItems = new ArrayList<String>();
		private List<PoiItem> poiItems;
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
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					PoiItem item = poiItems.get(position);
			    	Intent intent = new Intent(getActivity(), POIDetailActivity.class);
			    	intent.putExtra("PoiItem", item);
			    	startActivity(intent);
				}
			});
		}

		public void addPOIs(List<PoiItem> poiItems) {
			this.poiItems = poiItems;
			for (PoiItem item : poiItems) {
				listItems.add(item.getTitle());
			}
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
		
		public void addPOIs(List<PoiItem> poiItems) {
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
