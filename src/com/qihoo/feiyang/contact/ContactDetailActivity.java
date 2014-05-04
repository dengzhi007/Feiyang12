package com.qihoo.feiyang.contact;

import java.util.ArrayList;

import com.qihoo.feiyang.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {
	
	private ArrayList<String> contact_detail=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactdetail);
		
		String contact_name=(String) getIntent().getExtras().get("contact_name");
		String phone_number=(String) getIntent().getExtras().get("phone_number");
		//ArrayList<String> contact_detail=(ArrayList<String>) getIntent().getExtras().get("contact_detail");
		
		contact_detail=new ArrayList<String>();
		contact_detail.add(phone_number);
		contact_detail.add("qihoo360");
		contact_detail.add("xxxxx@gmail.com");
		
		TextView contact=(TextView) findViewById(R.id.contactdetail_name);
		contact.setText(contact_name);
		
		ListView content=(ListView) findViewById(R.id.contactdetail);
		content.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contact_detail));
		
	}
	
	public void onClickOfBackward(View source) {
		
		finish();
		
	}
}
