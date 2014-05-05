package com.qihoo.feiyang.contact;


import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.GlobalsUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ContactMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contactmain);
		
		TextView lastChangeTime=(TextView) findViewById(R.id.lastChangeTime);
		lastChangeTime.setText("上次更新时间：" + GlobalsUtil.lastChangeTime);
		
		TextView changeInfo=(TextView) findViewById(R.id.contactmain_changeinfo);
		String change="";
		if(GlobalsUtil.contactChangeInfo.length>0){
			change=change + GlobalsUtil.contactChangeInfo[0]+"更新了名片\n";
			if(GlobalsUtil.contactChangeInfo.length>1){
				change=change + GlobalsUtil.contactChangeInfo[1]+"更新了名片...";
			}
		}
		changeInfo.setText(change);
		
		
		
		
		
	}
	
	
	public void onClickOfcontactmainChangeCheck(View source){
		System.out.println("contact main change info check btn click");
		Intent intent=new Intent(ContactMainActivity.this,ContactChangeInfoCheckActivity.class);

		startActivity(intent);
	}
	
	
	
	
    public void onClickOfBackward(View source) {
		
		finish();
		
	}
	
	public void onClickOfContactBottomSetting(View source){
		System.out.println("contact main bottom setting btn click");
		
		switch (source.getId()) {
		case R.id.contact_dir:
			System.out.println("setting btn : contact main directory");
			break;
		case R.id.contact_quickshare:
			System.out.println("setting btn : contact main quick share");
			break;
		case R.id.contact_favourite:
			System.out.println("setting btn : contact main favourite");
			break;
		case R.id.contact_setting:
			System.out.println("setting btn : contact main setting");
			break;

		default:
			break;
		}
		
		Toast.makeText(this, "逗逼，没时间开发了", 50).show();
	}
	
	public void onClickOfcontactmain_CloudContact(View source){
		System.out.println("contact main cloud contact btn click");
		
		Intent intent=new Intent(ContactMainActivity.this,ContactActivity.class);

		startActivity(intent);
	}
	
	public void onClickOfcontactmain_MyCard(View source){
		System.out.println("contact main my card btn click");
		
		Intent intent=new Intent(ContactMainActivity.this,ContactMyCardActivity.class);

		startActivity(intent);
	}

}
