package com.qihoo.feiyang.picture;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.FileUtil;

public class ThumbPictureAdapter extends AddableAdapter {
	private Context context;
	private List<Bundle> list = new ArrayList<Bundle>();
	private IViewAddAndEventSet viewAdd = null;
	
	public ThumbPictureAdapter(Context context, IViewAddAndEventSet viewAdd) {
		this.context = context;
		this.viewAdd = viewAdd;
	}
	
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return viewAdd.addViewAndAddEvenet(context, position, list);
	}
	
	public void addPicture(Bundle data) {
		list.add(data);
		this.notifyDataSetChanged();
	}
}
