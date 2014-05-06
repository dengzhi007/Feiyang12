package com.qihoo.feiyang.contact;

import java.util.ArrayList;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.GlobalsUtil;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactChangeInfoCheckActivity extends Activity {
	
	private static final ContentProviderOperation ContentProviderOperation = null;


	MyAdapter listItemAdapter=null;
	
	
	ArrayList<Boolean> checklist=null;
	ListView contactchangeLV=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contactchangeinfocheck);

		checklist= new ArrayList<Boolean>();
		for(int i=0;i<GlobalsUtil.contactChangeInfo.size();i++){
			checklist.add(false);
		}
		
		contactchangeLV=(ListView) findViewById(R.id.contactchangeinfoLV);
		listItemAdapter=new MyAdapter(this);
		//SimpleAdapter listItemAdapter = new SimpleAdapter(this,listitem, R.layout.contactchangeinfolistitem, new String[] {"change_time","change_info","change_check"}, new int[] {R.id.contactchangetime,R.id.contactchangeinfo,R.id.contactchangecheck});
		contactchangeLV.setAdapter(listItemAdapter);
		
		
		
	}
	

	
	
	
	
    public void onClickOfBackward(View source) {
		
		finish();
		
	}

    public void onClickOfContactChangeBottomSetting(View source){
		System.out.println("contact change bottom setting btn click");
		
		switch (source.getId()) {
		case R.id.contactchange_selectall:
			System.out.println("setting btn : contact change select all");
			
			for(int i=0;i<checklist.size();i++){
				checklist.set(i, true);
			}
			
			listItemAdapter.notifyDataSetChanged();
			
			break;
		case R.id.contactchange_change:
			System.out.println("setting btn : contact change change");
			
			
			int changes=0;
			
			for(int i=0,j=0;i<checklist.size();i++,j++){
				
				if(checklist.get(i)){
					
					changes++;
					
					if(j<0){
                    	Toast.makeText(this, "j<0", 50).show();
                    	return;
                    }
					
					System.out.println(GlobalsUtil.contactChangeInfo.get(j)+ "changed");
					//change local contacts
					ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(); 
                    ContentProviderOperation operation = ContentProviderOperation.newInsert(Uri.parse("content://com.android.contacts/raw_contacts")).withValue("_id", null).build(); 
					operations.add(operation); 
                    operation = ContentProviderOperation.newInsert(Uri.parse("content://com.android.contacts/data")).withValueBackReference("raw_contact_id", 0).withValue("data2", GlobalsUtil.contactChangeInfo.get(j)).withValue("mimetype", "vnd.android.cursor.item/name").build(); 
					operations.add(operation); 
                    operation = ContentProviderOperation.newInsert(Uri.parse("content://com.android.contacts/data")).withValueBackReference("raw_contact_id", 0).withValue("data1", GlobalsUtil.contactChangePhone.get(j)).withValue("data2", "2").withValue("mimetype", "vnd.android.cursor.item/phone_v2").build(); 
                    operations.add(operation); 
                    //operation = ContentProviderOperation.newInsert(Uri.parse("content://com.android.contacts/data")).withValueBackReference("raw_contact_id", 0).withValue("data1", "zq@itcast.cn").withValue("data2", "2").withValue("mimetype", "vnd.android.cursor.item/email_v2").build(); 
                    //operations.add(operation); 
                    try {
						getContentResolver().applyBatch("com.android.contacts", operations);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OperationApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 

                    
                    GlobalsUtil.contactNames.add(0,GlobalsUtil.contactChangeInfo.get(j));
        		    GlobalsUtil.contactPhones.add(0,GlobalsUtil.contactChangePhone.get(j));
        		    GlobalsUtil.contactAvatars.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.contactlistitem_avatar));
        		    GlobalsUtil.contactIds.add(0,(long) 0);
        		    
                    GlobalsUtil.contactChangeInfo.remove(j);
                    GlobalsUtil.contactChangePhone.remove(j);
                    GlobalsUtil.contactChangeTime.remove(j);
                    
                    j--;

				}
				
			}
			if(changes>0){
				Toast.makeText(this, "更新了"+changes+"个名片", 50).show();
			}else{
				Toast.makeText(this, "未选择更新名片", 50).show();
			}
			
			checklist= new ArrayList<Boolean>();
			for(int i=0;i<GlobalsUtil.contactChangeInfo.size();i++){
				checklist.add(false);
			}
			
			listItemAdapter.notifyDataSetChanged();
			
			break;
		case R.id.contactchange_ignore:
			System.out.println("setting btn : contact change ignore");
			
			for(int i=0;i<checklist.size();i++){
				checklist.set(i, false);
			}
			
			listItemAdapter.notifyDataSetChanged();

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
			return GlobalsUtil.contactChangeInfo.size();
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
			View v = inflater.inflate(R.layout.contactchangeinfolistitem, null);
			
			TextView time = (TextView) v.findViewById(R.id.contactchangetime);
			TextView info = (TextView) v.findViewById(R.id.contactchangeinfo);
			CheckBox check = (CheckBox) v.findViewById(R.id.contactchangecheck);
			
			time.setText(GlobalsUtil.contactChangeTime.get(position));
			info.setText(GlobalsUtil.contactChangeInfo.get(position) + " 更新了名片    " + GlobalsUtil.contactChangePhone.get(position));
			check.setChecked(checklist.get(position));
			
			
			check.setTag(position);
			check.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox cb= (CheckBox)v;
					int row=(Integer) v.getTag();
			        checklist.set(row, cb.isChecked());
					System.out.println("contact change check list :"+row+" changed" );
					
				}
				
			});

			return v;
		}
		
	}
	

}
