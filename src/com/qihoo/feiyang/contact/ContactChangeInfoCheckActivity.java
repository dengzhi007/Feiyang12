package com.qihoo.feiyang.contact;

import java.util.ArrayList;
import java.util.HashMap;

import com.qihoo.feiyang.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContactChangeInfoCheckActivity extends Activity {
	
	ArrayList<HashMap<String, Object>> listitem=null;
	ArrayList<Boolean> checklist=null;
	ListView contactchangeLV=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contactchangeinfocheck);
		
		contactchangeLV=(ListView) findViewById(R.id.contactchangeinfoLV);
		
		getListitem();
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listitem, R.layout.contactchangeinfolistitem, new String[] {"change_time","change_info","change_check"}, new int[] {R.id.contactchangetime,R.id.contactchangeinfo,R.id.contactchangecheck});
		
		contactchangeLV.setAdapter(listItemAdapter);
		
		
		
	}
	
	private void getListitem(){
		
		listitem=new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		String changeTime="2014.4.30";
		String changeInfo="老实人    更新了名片    13858011543";
		Boolean changeCheck=false;
		map.put("change_time", changeTime);
		map.put("change_info", changeInfo);
		map.put("change_check", changeCheck);
		
		listitem.add(map);
		
		map = new HashMap<String, Object>();
		changeTime="2014.5.2";
		changeInfo="honestman    更新了名片   18611747011";
		changeCheck=false;
		map.put("change_time", changeTime);
		map.put("change_info", changeInfo);
		map.put("change_check", changeCheck);
		
		listitem.add(map);
		
	}
	
	
	public void onClickOfcontactchangeCheckbox(View source){
		
		System.out.println("contact change checkbox btn click");
		
		
	}
	
    public void onClickOfBackward(View source) {
		
		finish();
		
	}
	public void onClickOfContactChangeBottomSetting(View source){
		System.out.println("contact change bottom setting btn click");
		
		switch (source.getId()) {
		case R.id.contactchange_selectall:
			System.out.println("setting btn : contact change select all");
			
			for (HashMap<String, Object> map : listitem) {
				map.put("change_check", true);
			}
			SimpleAdapter listItemAdapter = new SimpleAdapter(this,listitem, R.layout.contactchangeinfolistitem, new String[] {"change_time","change_info","change_check"}, new int[] {R.id.contactchangetime,R.id.contactchangeinfo,R.id.contactchangecheck});
			
			contactchangeLV.setAdapter(listItemAdapter);
			
			break;
		case R.id.contactchange_change:
			System.out.println("setting btn : contact change change");
			
			checklist=new ArrayList<Boolean>();
			
			for(int i=0;i<listitem.size();i++){
				CheckBox cb= (CheckBox) contactchangeLV.getChildAt(i).findViewById(R.id.contactchangecheck);
				if(cb.isChecked()){
					checklist.add(true);
				}
				else{
					checklist.add(false);
				}
				System.out.println("check list : " + cb.isChecked());
			}
			
			break;
		case R.id.contactchange_ignore:
			System.out.println("setting btn : contact change ignore");
			break;
		

		default:
			break;
		}
	}
}
