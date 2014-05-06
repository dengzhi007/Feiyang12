package com.qihoo.feiyang.contact;

import java.util.ArrayList;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.GlobalsUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ContactMyCardActivity extends Activity {
	
	
	
	ArrayList<String> contactDetail=null;
	
	String contactName=null;
	String phoneNum=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.contactmycard);
		
		ImageView avatar=(ImageView)findViewById(R.id.mycardavatar);
		avatar.setImageBitmap(GlobalsUtil.mainAvatar);
		
		ListView content=(ListView) findViewById(R.id.mycardinfolist);
		content.setAdapter(new MyAdapter(this));
		
	}
	
	public void onClickOfSendMyCard(View source){
		switch(source.getId()){
			case R.id.cancelmycard:
				System.out.println("cancel my card btn click");
				finish();
				break;
			case R.id.sendmycard:
				System.out.println("send my card btn click");
		
				System.out.println("contact main cloud contact btn click");
				
				Intent intent=new Intent(ContactMyCardActivity.this,ContactActivity.class);

				startActivity(intent);
				
				finish();
				
				break;

			default:
				break;
		}
	}
	
	
private class MyAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		
		public MyAdapter(Context context){
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return GlobalsUtil.cardinfo.length;
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

			if(convertView==null)
				convertView=inflater.inflate(R.layout.contactmycardlistitem, null);

			TextView info = (TextView) convertView.findViewById(R.id.contactname);
			EditText phone = (EditText) convertView.findViewById(R.id.contactdetail);

			info.setText(GlobalsUtil.cardinfo[position]+": ");
			phone.setText(GlobalsUtil.phoneinfo[position]);
			phone.setTag(position);
			/*
			phone.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Toast.makeText(ContactMyCardActivity.this, "逗逼，不要乱改，没时间开发了", 50).show();
					
					
				}
			});
		    */ 
			return convertView;
		}
		
	}
}
