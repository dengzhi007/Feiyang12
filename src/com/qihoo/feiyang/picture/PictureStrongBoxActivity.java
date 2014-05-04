package com.qihoo.feiyang.picture;

import java.util.ArrayList;
import java.util.List;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.StrongBoxFile;
import com.qihoo.feiyang.util.StrongBoxUtil;
import com.qihoo.yunpan.sdk.android.GetNodeByNidAction;
import com.qihoo.yunpan.sdk.android.http.model.YunFile;
import com.qihoo.yunpan.sdk.android.http.model.YunFileNode;
import com.stay.pull.lib.PullToRefreshGridView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PictureStrongBoxActivity extends Activity {
	private static final String TAG = "PictureStrongBoxActivity";
	private PullToRefreshGridView refreshGridView = null;
	private GridView gridView =null;
	
	private EditText passwd = null;
	private Button sure = null;
	private int MAX_TRY = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPasswordView();
	}
	
	private void initPasswordView() {
		setContentView(R.layout.photo_password);
		passwd = (EditText) findViewById(R.id.photo_password);
		sure = (Button) findViewById(R.id.password_sure);

		sure.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int count = 1;
				boolean isRight = false;
				for (count = 1; count<=MAX_TRY; count++) {
					String pwd = passwd.getText().toString();
					if (pwd.equals("123")) {
						isRight = true;
						break;
					} else {
						Toast.makeText(PictureStrongBoxActivity.this, 
								"你还剩下" + (MAX_TRY-count)+ "次机会", Toast.LENGTH_SHORT);
					}
				}
				
				if (isRight) {
					hideInputMethod();
					initStrongBoxView();
				} else {
					PictureStrongBoxActivity.this.finish();
				}
			}
		});
	}
	
	private void hideInputMethod() {
		InputMethodManager inputMethodManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken()
				, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private void initStrongBoxView() {
		setContentView(R.layout.photo_main);
		refreshGridView = (PullToRefreshGridView) findViewById(R.id.pict_gallery);
		gridView = refreshGridView.getRefreshableView();
		AddableAdapter adapter = new StrongBoxAdapter(this);
		ThumbHandler handler = new ThumbHandler(adapter);
		LoadFromDBThread thread = new LoadFromDBThread(handler);
		thread.start();
	}
}

class StrongBoxAdapter extends AddableAdapter {
	private Context context;
	private List<View> list = new ArrayList<View>();
	
	public StrongBoxAdapter(Context context) {
		this.context = context; 
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
		return list.get(position);
	}

	@Override
	public void addPicture(Bundle data) {
		String nid = data.getString("nid");
		String name = data.getString("name");
		
		Bitmap map = FileUtil.loadBitmapFromCache(FileUtil.getThumbPicName(nid));
		String inflater=Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.photo_main_list_item, null);
		ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pict_thumb);
		TextView textView = (TextView) linearLayout.findViewById(R.id.pict_dirname);
		imageView.setImageBitmap(map);
		textView.setText(name);
		list.add(linearLayout);
		
		this.notifyDataSetChanged();
	}
	
}

class LoadFromDBThread extends Thread {
	private static final String TAG = "LoadFromDBThread";
	private Handler handler = null;
	private GetNodeByNidAction nidAction = new GetNodeByNidAction();
	public LoadFromDBThread(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		List<StrongBoxFile> strongFiles = StrongBoxUtil.getAllStrongBoxPictures();
		for (StrongBoxFile sf : strongFiles){
			String nid = sf.getNid();
			String fullName = sf.getFullName();
			Bitmap map = FileUtil.loadBitmapFromCache(FileUtil.getThumbPicName(nid));
			if (map == null) {
				Log.e(TAG, nid + " " + fullName + " seems error");
				/*YunFileNode node = nidAction.getFileNodeByNid(nid);
				YunFile file = node.data;
				System.out.println("thumb   " + file.thumb);
				System.out.println("preview " + file.preview);*/
			} else {
				Message msg = handler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString("name", sf.getName());
				bundle.putString("fullname", sf.getFullName());
				bundle.putString("nid", sf.getNid());
				bundle.putString("pid", sf.getPid());
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
	}
}
