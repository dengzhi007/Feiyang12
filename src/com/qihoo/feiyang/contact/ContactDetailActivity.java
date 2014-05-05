package com.qihoo.feiyang.contact;

import java.util.ArrayList;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.GlobalsUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {
	
	private Bitmap contact_avatar=null;
	private String contact_name=null;
	private ArrayList<String> contact_detail=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactdetail);
		
		int contact_select= (Integer) getIntent().getExtras().get("contact_select");

		contact_avatar=GlobalsUtil.contactAvatars.get(contact_select);
		contact_name=GlobalsUtil.contactNames.get(contact_select);
		contact_detail=(ArrayList<String>) getIntent().getExtras().get("contact_detail");
		
		ImageView avatar=(ImageView) findViewById(R.id.contactdetail_avatar);
		avatar.setImageBitmap(contact_avatar);
		
		TextView name=(TextView) findViewById(R.id.contactdetail_name);
		name.setText(contact_name);
		
		ListView detail=(ListView) findViewById(R.id.contactdetail);
		detail.setAdapter(new MyAdapter(this));
		
	}
	
	public void onClickOfBackward(View source) {
		
		finish();
		
	}
	
	private class MyAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		
		public MyAdapter(Context context){
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contact_detail.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if(convertView==null){
				convertView = inflater.inflate(R.layout.contactdetaillistitem, null);
			}


			TextView detail = (TextView) convertView.findViewById(R.id.contactdetaillistitem);
			
			detail.setText(contact_detail.get(position));


			return convertView;
		}
		
	}

}
