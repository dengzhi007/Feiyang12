package com.qihoo.feiyang.picture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.StrongBoxFile;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
import com.qihoo.yunpan.sdk.android.GetNodeByNidAction;
import com.stay.pull.lib.PullToRefreshGridView;

public class PictureStrongBoxActivity extends Activity {
	private static final String TAG = "PictureStrongBoxActivity";
	private PullToRefreshGridView refreshGridView = null;
	private GridView gridView =null;
	
	private EditText passwd = null;
	private Button sure = null;
	private int MAX_TRY = 3;
	private int try_times = 0;
	private List<StrongBoxFile> fileLists = new ArrayList<StrongBoxFile>();
	
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
				try_times++;
				String pwd = passwd.getText().toString();
				if (pwd.equals("123")) {
					hideInputMethod();
					initStrongBoxView();
					return;
				} else {
					if (try_times < MAX_TRY) {
						passwd.setText("");
						Toast.makeText(PictureStrongBoxActivity.this, 
								"你还剩下" + (MAX_TRY-try_times)+ "次机会", Toast.LENGTH_SHORT).show();
					} else {
						PictureStrongBoxActivity.this.finish();
					}
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
		AddableAdapter adapter = new StrongBoxAdapter(this, fileLists);
		gridView.setAdapter(adapter);
		ThumbHandler handler = new ThumbHandler(adapter);
		LoadFromDBThread thread = new LoadFromDBThread(handler);
		thread.start();
	}
}

class StrongBoxAdapter extends AddableAdapter {
	private Context context;
	private List<StrongBoxFile> fileLists = null;
	
	public StrongBoxAdapter(Context context, List<StrongBoxFile> fileLists) {
		this.context = context; 
		this.fileLists = fileLists;
	}
	
	public int getCount() {
		return fileLists.size();
	}

	public Object getItem(int position) {
		return fileLists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View currentView = createNewView(fileLists.get(position));
		currentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FullScreenStrongBoxActivity.boxFiles = fileLists;
				Intent intent = new Intent(context, FullScreenStrongBoxActivity.class);
				intent.putExtra("index", position);
				System.out.println("create " + position);
				context.startActivity(intent);
			}
		});
		return currentView;
	}
	
	private void addStrongFile(Bundle data) {
		String name = data.getString("name");
		String fullName = data.getString("fullname");
		String nid = data.getString("nid");
		String pid = data.getString("pid");
		StrongBoxFile file = new StrongBoxFile(name, fullName, nid, pid);
		fileLists.add(file);
	}

	@Override
	public void addPicture(Bundle data) {
		addStrongFile(data);
		this.notifyDataSetChanged();
	}
	
	private View createNewView(StrongBoxFile file) {
		String nid = file.getNid();
		String name = file.getName();
		Bitmap map = FileUtil.loadBitmapFromCache(FileUtil.getThumbPicName(nid));
		String inflater=Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.photo_main_list_item, null);
		ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pict_thumb);
		TextView textView = (TextView) linearLayout.findViewById(R.id.pict_dirname);
		imageView.setImageBitmap(map);
		textView.setText(name);
		return linearLayout;
	}
}

class LoadFromDBThread extends Thread {
	private static final String TAG = "LoadFromDBThread";
	private Handler handler = null;
	public LoadFromDBThread(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		List<StrongBoxFile> strongFiles = StrongBoxAndFavoriteUtil.getAllStrongBoxPictures();
		for (StrongBoxFile sf : strongFiles){
			String nid = sf.getNid();
			String fullName = sf.getFullName();
			//Bitmap map = FileUtil.loadBitmapFromCache(FileUtil.getThumbPicName(nid));
			Bitmap map = FileUtil.getLocalPictureFromStrongBox(fullName);
			if (map == null) {
				Log.e(TAG, nid + " " + fullName + " seems error");
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
