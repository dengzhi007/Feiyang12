package com.qihoo.feiyang.favorite;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.FavoriteFile;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;

public class FavoriteFileActivity extends Activity {
	private List<FavoriteFile> favo_files = null;
	private List<Boolean> checked = new ArrayList<Boolean>();
	private View favorite_cancel = null;
	private ListView favorite_list = null;
	private BaseAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_main);
		favo_files = StrongBoxAndFavoriteUtil.getAllFavoriteFiles();
		favorite_list = (ListView) findViewById(R.id.favorite_list);
		favorite_cancel = findViewById(R.id.favorite_cancel);
		
		for (int i=0; i<favo_files.size(); i++) {
			checked.add(false);
		}
		adapter = new MyListAdapter(this, favo_files, checked);
		favorite_list.setAdapter(adapter);
		favorite_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Iterator<Boolean> bit = checked.iterator();
				Iterator<FavoriteFile> fit = favo_files.iterator();
				while (bit.hasNext() && fit.hasNext()) {
					boolean ch = bit.next();
					fit.next();
					if (ch) {
						bit.remove();
						fit.remove();
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
}

class MyListAdapter extends BaseAdapter {
	private List<FavoriteFile> favo_files = null;
	private List<Boolean> checked = null;
	private Context context;
	
	public MyListAdapter(Context context, List<FavoriteFile> favo_files, List<Boolean> checked) {
		this.context = context;
		this.favo_files = favo_files;
		this.checked = checked;
	}
	
	@Override
	public int getCount() {
		return favo_files.size();
	}

	@Override
	public Object getItem(int postion) {
		return favo_files.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(final int postion, View v, ViewGroup viewGroup) {
		String inflater=Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
		View layoutView = layoutInflater.inflate(R.layout.favorite_list_item, null);
		final FavoriteFile file = favo_files.get(postion);
		TextView share_file_item_name = (TextView) layoutView.findViewById(R.id.favorite_file_item_name);
		String name = file.getName();
		share_file_item_name.setText(name);
		CheckBox favorite_file_item_share = (CheckBox) layoutView.findViewById(R.id.favorite_file_item_share);
		if (checked.get(postion)) {
			favorite_file_item_share.setBackgroundResource(R.drawable.share_file_item_selected);
		} else {
			favorite_file_item_share.setBackgroundResource(R.drawable.share_file_item_unselected);
		}
		favorite_file_item_share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					buttonView.setBackgroundResource(R.drawable.share_file_item_selected);
				} else {
					buttonView.setBackgroundResource(R.drawable.share_file_item_unselected);
				}
				checked.set(postion, isChecked);
			}
		});
		
		return layoutView;
	}
	
}

