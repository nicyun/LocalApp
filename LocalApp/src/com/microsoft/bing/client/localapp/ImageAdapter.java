package com.microsoft.bing.client.localapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }
    
    public String getCode(int position) {
    	return mCodes[position];
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View item = convertView;
    	if (item == null) {
    		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
    		item = inflater.inflate(R.layout.grid_item, parent, false);
    		
    		TextView textView = (TextView)item.findViewById(R.id.grid_item_text);
    		ImageView imageView = (ImageView) item.findViewById(R.id.grid_item_image);

            imageView.setImageResource(mThumbIds[position]);
            textView.setText(mTexts[position]);
    	}
    	
        return item;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map, 
            R.drawable.ic_action_map
    };
    
    // corresponding code.
	// Detailed category please check: http://code.autonavi.com/Public/down/AMap_Api_Table.zip
	// 050000 餐厅
	// 060000 购物
	// 070000 生活
	// 080000 体育
	// 090000 医疗
	// 100000 住宿
	// 110000 风景
	// 150000 交通
	// 170000 公司
    private String[] mCodes = {
    		"050000",
    		"060000",
    		"070000",
    		"080000",
    		"090000",
    		"100000",
    		"110000",
    		"150000",
    		"170000"
    };
    
    private String[] mTexts = {
    		"餐厅",
    		"购物",
    		"生活",
    		"体育",
    		"医疗",
    		"住宿",
    		"风景",
    		"交通",
    		"公司"
    };
}