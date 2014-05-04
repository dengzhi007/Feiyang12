package com.qihoo.feiyang.share;

import java.util.List;

import com.qihoo.feiyang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FileItemAdapter extends BaseAdapter {

	private List<FileItem> files;
	private LayoutInflater inflater;
	private OnClickListener listener;
	private static FileItemAdapter adapter;
	private FileItemAdapter(Context context, List<FileItem> files, OnClickListener listener) {
		inflater = LayoutInflater.from(context);
		this.files = files;
		this.listener = listener;
	}
	public static FileItemAdapter instance(Context context, List<FileItem> files, OnClickListener listener){
		if(adapter == null){
			adapter = new FileItemAdapter(context, files, listener);
		}
		return adapter;
	}
	
	
	
	public void setFiles(List<FileItem> files){
		this.files = files;
	}
	
	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public Object getItem(int position) {
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = null;
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.share_file_item, parent, false);
			holder = new ViewHolder();
			holder.viewFileIcon = (ImageView) convertView.findViewById(R.id.share_file_item_icon);
			holder.viewFileName = (TextView) convertView.findViewById(R.id.share_file_item_name);
			holder.viewFileInfo = (TextView) convertView.findViewById(R.id.share_file_item_info);
			holder.viewFileShare = (CheckBox) convertView.findViewById(R.id.share_file_item_share);
			convertView.setTag(holder);
		}
		if (files.get(position).isShare){
			holder.viewFileShare.setBackgroundResource(R.drawable.share_file_item_selected);
		}
		
		FileItem item = files.get(position);
		holder.viewFileIcon.setImageResource(item.fileIcon);

		holder.viewFileName.setText(item.fileName);
		if (item.fileInfo.length() != 0) {
			holder.viewFileInfo.setVisibility(View.VISIBLE);
			holder.viewFileInfo.setText(item.fileInfo);
		} else {
			holder.viewFileInfo.setVisibility(View.GONE);
		}
		holder.viewFileShare.setTag(position);
		holder.viewFileShare.setOnClickListener(listener);
		return convertView;
	}

	private class ViewHolder {
		ImageView viewFileIcon;
		TextView viewFileName;
		TextView viewFileInfo;
		CheckBox viewFileShare;
	}
	
	class fileShareClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}
}