package com.qihoo.feiyang.contact;

import java.util.ArrayList;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.GlobalsUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
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
				//send sms
				/*
				SmsManager smsManager = SmsManager.getDefault();
				String smsContent="老实人的名片：\n手机：xxxxxxxxxxx\n电话：xxxxxxxx\n公司：xxxxxxxx\n地址：xxxxxxxxxx";
                if(phoneNum!=null) {
                    
                 smsManager.sendTextMessage(phoneNum, null, smsContent, null, null);
                }
                
                Toast.makeText(this, "发送成功", 50).show();
                
                
                */
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

			info.setText(GlobalsUtil.cardinfo[position]);
			phone.setText(GlobalsUtil.phoneinfo[position]);

			return convertView;
		}
		
	}
}
