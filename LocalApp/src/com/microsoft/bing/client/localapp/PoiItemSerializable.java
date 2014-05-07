package com.microsoft.bing.client.localapp;

import java.io.Serializable;

import android.os.Parcel;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

public class PoiItemSerializable extends PoiItem implements Serializable {

	public PoiItemSerializable(String arg0, LatLonPoint arg1, String arg2,
			String arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}
	
	protected PoiItemSerializable(Parcel arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -7228440993573121973L;
	
}
