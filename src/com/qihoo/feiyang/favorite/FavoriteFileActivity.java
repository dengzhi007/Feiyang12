package com.qihoo.feiyang.favorite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.picture.FullScreenPictureActivity;
import com.qihoo.feiyang.util.FavoriteFile;
import com.qihoo.feiyang.util.FileUtil;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
import com.qihoo.yunpan.sdk.android.config.YunpanSDKConfig;
import com.qihoo.yunpan.sdk.android.http.action.FileGetNodeByName;
import com.qihoo.yunpan.sdk.android.http.model.YunFile;
import com.qihoo.yunpan.sdk.android.http.model.YunFileNode;

public class FavoriteFileActivity extends Activity {
	private List<FavoriteFile> favo_files = null;
	private List<Boolean> checked = null;
	private ListView favorite_list = null;
	private BaseAdapter adapter = null;
	private MyFavoriteHandler handler;
	private ExecutorService exec = Executors.newFixedThreadPool(5);
	
	private View backward =null;
	private View favorite_cancel = null;
	private View favorite_layout_cancel = null;
	private View favorite_layout_navigate = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_main);

		favorite_list = (ListView) findViewById(R.id.favorite_list);
		favorite_cancel = findViewById(R.id.favorite_cancel);
		backward = findViewById(R.id.favorite_return);
		favorite_layout_cancel = findViewById(R.id.favorite_layout_cancel);
		favorite_layout_navigate = findViewById(R.id.favorite_layout_navigate);

		backward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FavoriteFileActivity.this.finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checked = new ArrayList<Boolean>();
		favo_files = StrongBoxAndFavoriteUtil.getAllFavoriteFiles();
		for (int i=0; i<favo_files.size(); i++) {
			checked.add(false);
		}
		handler = new MyFavoriteHandler(this);
		adapter = new MyListAdapter(this, favo_files, checked, handler, exec);
		favorite_list.setAdapter(adapter);
		favorite_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Iterator<Boolean> bit = checked.iterator();
				Iterator<FavoriteFile> fit = favo_files.iterator();
				while (bit.hasNext() && fit.hasNext()) {
					boolean ch = bit.next();
					FavoriteFile file = fit.next();
					if (ch) {
						bit.remove();
						fit.remove();
						// TODO delete from databases
						String nid = file.getNid();
						//StrongBoxAndFavoriteUtil.removeFileFromFavorite(nid);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		favorite_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				FavoriteFile file = favo_files.get(index);
				if (FileUtil.isPicture(file.getName())) {
					Intent intent = new Intent(FavoriteFileActivity.this, FullScreenPictureActivity.class);
					Bundle data = new Bundle();
					data.putInt("index", 0);
					Bundle pictures[] = new Bundle[1];
					pictures[0] = new Bundle();
					pictures[0].putString("nid", file.getNid());
					pictures[0].putString("name", file.getFullName());
					pictures[0].putString("pid", file.getPid());
					data.putParcelableArray("pictures", pictures);
					intent.putExtra("data", data);
					FavoriteFileActivity.this.startActivity(intent);
				}
			}
		});
	}
	
	public void setBottomVibleAction() {
		for (Boolean b : checked) {
			if (b) {
				favorite_layout_cancel.setVisibility(View.VISIBLE);
				favorite_layout_navigate.setVisibility(View.INVISIBLE);
				return;
			}
		}
		favorite_layout_cancel.setVisibility(View.INVISIBLE);
		favorite_layout_navigate.setVisibility(View.VISIBLE);
		return;
	}
}



class MyFavoriteHandler extends Handler {
	private Map<String, TextView> loadingViews = new HashMap<String, TextView>();
	private FavoriteFileActivity context;
	
	public MyFavoriteHandler(FavoriteFileActivity context) {
		this.context = context;
	}
	
	public void addLoadingView(String nid, TextView view) {
		loadingViews.put(nid, view);
	}
	
	@Override
	public void handleMessage(Message msg) {
		Bundle data = msg.getData();
		String nid = data.getString("nid");
		if (data.getBoolean("exist")) {
			String info = data.getString("info");
			TextView textView = loadingViews.get(nid);
			textView.setText(info);
			loadingViews.remove(nid);
		} else {
			loadingViews.remove(nid);
			context.setBottomVibleAction();
		}
	}
}

class LoadFileInfoThread implements Runnable {
	private String nid;
	private String fullName;
	private Handler handler;
	
	public LoadFileInfoThread(String nid, String fullName, Handler handler) {
		super();
		this.nid = nid;
		this.fullName = fullName;
		this.handler = handler;
	}

	@Override
	public void run() {
		FileGetNodeByName fileGetNodeByName = new FileGetNodeByName();
		YunFileNode node = fileGetNodeByName.getFileNodeByName(fullName);
		Message msg = handler.obtainMessage();
		Bundle information = new Bundle();
		information.putString("nid", nid);
		if (node.data != null) {
			String info = FileTypeUtil.formatFileInfo(node.data);
			information.putString("info", info);
			information.putBoolean("exist", true);
		} else if (node.errno.equals("3008")) {
			System.out.println("file not exists");
			// TODO 删除db条目,同事更新list
			information.putBoolean("exist", false);
		}
		msg.setData(information);
		handler.sendMessage(msg);
	}
}

class MyListAdapter extends BaseAdapter {
	private List<FavoriteFile> favo_files = null;
	private List<Boolean> checked = null;
	private FavoriteFileActivity context;
	private MyFavoriteHandler handler;
	private ExecutorService exec;
	
	public MyListAdapter(FavoriteFileActivity context, List<FavoriteFile> favo_files, List<Boolean> checked, 
			MyFavoriteHandler handler, ExecutorService exec) {
		this.context = context;
		this.favo_files = favo_files;
		this.checked = checked;
		this.handler = handler;
		this.exec = exec;
	}
	
	@Override
	public int getCount() {
		return favo_files.size();
	}

	@Override
	public Object getItem(int postion) {
		return favo_files.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		return postion;
	}

	@Override
	public View getView(final int postion, View v, ViewGroup viewGroup) {
		String inflater=Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
		View layoutView = layoutInflater.inflate(R.layout.favorite_list_item, null);
		final FavoriteFile file = favo_files.get(postion);
		TextView share_file_item_name = (TextView) layoutView.findViewById(R.id.favorite_file_item_name);
		String name = file.getName();
		share_file_item_name.setText(name);
		CheckBox favorite_file_item_share = (CheckBox) layoutView.findViewById(R.id.favorite_file_item_share);
		if (checked.get(postion)) {
			favorite_file_item_share.setBackgroundResource(R.drawable.share_file_item_selected);
		} else {
			favorite_file_item_share.setBackgroundResource(R.drawable.share_file_item_unselected);
		}
		favorite_file_item_share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					buttonView.setBackgroundResource(R.drawable.share_file_item_selected);
				} else {
					buttonView.setBackgroundResource(R.drawable.share_file_item_unselected);
				}
				checked.set(postion, isChecked);
				context.setBottomVibleAction();
			}
		});
		
		ImageView favorite_file_item_icon = (ImageView) layoutView.findViewById(R.id.favorite_file_item_icon);
		int resId = FileTypeUtil.getFileIcon(name);
		favorite_file_item_icon.setImageResource(resId);
		TextView favorite_file_item_info = (TextView) layoutView.findViewById(R.id.favorite_file_item_info);
		handler.addLoadingView(file.getNid(),favorite_file_item_info);
		exec.execute(new LoadFileInfoThread(file.getNid(), file.getFullName(), handler));
		return layoutView;
	}
	
	public void removeFavoriteItem(String nid) {
		
	}
	
}


final class FileTypeUtil {
	public static int getFileIcon(String fileName){
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (extension.equals("apk")){
			return R.drawable.share_apk;
		}
		else if (extension.equals("avi")){
			return R.drawable.share_avi;
		}
		else if (extension.equals("epub")){
			return R.drawable.share_epub;
		}
		else if (extension.equals("mkv")){
			return R.drawable.share_mkv;
		}
		else if (extension.equals("pdf")){
			return R.drawable.share_pdf;
		}
		else if (extension.equals("rar")){
			return R.drawable.share_rar;
		}
		else if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")){
			return R.drawable.share_image;
		}
		else if (extension.equals("txt") || extension.equals("doc") || extension.equals("docx") || extension.equals("ini")){
			return R.drawable.share_doc;
		}
		else if (extension.equals("zip")){
			return R.drawable.share_zip;
		}
		else{
			return R.drawable.share_file;
		}
	}
	
	private static String formatFileSize(long fileSizeByte){
		double fileSizeByteDouble = (double)fileSizeByte;
		if (fileSizeByteDouble < (double)(1024 * 1024)){
			return String.format("%.1f", fileSizeByteDouble / (double)1024) + "KB";
		}
		else if (fileSizeByteDouble < (double)(1024 * 1024 * 1024)){
			return String.format("%.1f", fileSizeByteDouble / (double)(1024 * 1024)) + "MB";
		}
		else{
			return String.format("%.1f", fileSizeByteDouble / (double)(1024 * 1024 * 1024)) + "GB";
		}
	}
	
	public static String formatFileInfo(YunFile file) {
		String fileSize = formatFileSize(file.count_size);
		String fileMTime = file.mtime;
		return fileSize + " 修改日期:" + fileMTime;
	}
}

