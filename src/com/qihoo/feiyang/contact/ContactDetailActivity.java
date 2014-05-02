package com.qihoo.feiyang.contact;

import java.util.ArrayList;

import com.qihoo.feiyang.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactdetail);
		
		String contact_name=(String) getIntent().getExtras().get("contact_name");
		ArrayList<String> contact_detail=(ArrayList<String>) getIntent().getExtras().get("contact_detail");
		
		TextView contact=(TextView) findViewById(R.id.contactdetail_name);
		contact.setText(contact_name);
		
		ListView content=(ListView) findViewById(R.id.contactdetail);
		content.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contact_detail));
		
	}
}
