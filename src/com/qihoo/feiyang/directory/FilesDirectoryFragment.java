package com.qihoo.feiyang.directory;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.qihoo.feiyang.R;
import com.qihoo.feiyang.util.StrongBoxAndFavoriteUtil;
import com.qihoo.yunpan.sdk.android.http.action.FileGetNodeList;
import com.qihoo.yunpan.sdk.android.http.action.LinkCreateFile;
import com.qihoo.yunpan.sdk.android.http.model.FileNodeList;
import com.qihoo.yunpan.sdk.android.http.model.LinkCreateFileData;
import com.qihoo.yunpan.sdk.android.http.model.YunFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class FilesDirectoryFragment extends Fragment implements OnClickListener {
	private ListView lv_files;
	private static List<FileDirectoryItem> fileList = new ArrayList<FileDirectoryItem>();
	private static ImageView btnFolderBackward = null;
	private static ImageView btnFavorite = null;
	private Context context;
	private static String currentFolderPath = "/";
	private static FileDirectoyItemAdapter adapter = null;
	private static int selectedCount = 0;
	private TextView textTitle = null;
	
	class favoriteItem{
		String path;
		String nid;
		String pid;
		
		public favoriteItem(String path, String nid, String pid){
			this.path = path;
			this.nid = nid;
			this.pid = pid;
		}

		@Override
		public boolean equals(Object o) {
			if (((favoriteItem)o).path.equals(this.path)
			    && ((favoriteItem)o).nid.equals(this.nid)
			    && ((favoriteItem)o).pid.equals(this.pid)){
				return true;
			}
			return false;
		}
	}
	
	static List<favoriteItem> shareFileList = new ArrayList<favoriteItem>();
	
	public Boolean isSetFavorite(List<favoriteItem> shareFileList){
		if (shareFileList.isEmpty()){
			return true;
		}
		for (int i = 0; i != shareFileList.size(); ++i){
			if (!StrongBoxAndFavoriteUtil.ifFileIsFavorite(shareFileList.get(i).nid)){
				return true;
			}
		}
		return false;
	}
	
	public FilesDirectoryFragment(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.directory_fragment_files, container, false);
		findView(view);
		btnFolderBackward = (ImageView)getActivity().findViewById(R.id.directory_backward);
		btnFavorite = (ImageView)getActivity().findViewById(R.id.directory_button_favorite);
		btnFolderBackward.setOnClickListener(new FolderBackwardListener());
		btnFavorite.setOnClickListener(new BtnFavoriteListener());
		btnFavorite.setVisibility(View.INVISIBLE);
		textTitle = (TextView)getActivity().findViewById(R.id.directory_title);
		
		adapter = FileDirectoyItemAdapter.instance(getActivity(), fileList, this);
		refreshFileList(currentFolderPath);
		lv_files.setOnItemClickListener(new FolderClickListener());
		lv_files.setAdapter(adapter);
		return view;
	}

	private void findView(View v) {
		lv_files = (ListView) v.findViewById(R.id.directory_files);
	}
	
	private String formatFileSize(long fileSizeByte){
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
	
	private String getFileName(String filePath, Boolean isFolder){
		if (isFolder){
			String s = filePath.substring(0, filePath.length() - 1);
			return s.substring(s.lastIndexOf("/") + 1);
		}
		else{
			return filePath.substring(filePath.lastIndexOf("/") + 1);
		}
	}
	
	private int getFileIcon(String fileName, Boolean isFolder){
		if (isFolder){
			return R.drawable.share_folder;
		}
		else{
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
	}
	
	private void refreshFileList(String rootNodePath) {
		fileList.clear();
		shareFileList.clear();
		selectedCount = 0;
		btnFavorite.setVisibility(View.INVISIBLE);
		//遍历文件夹
		FileNodeList fileNodeList = new FileGetNodeList().getNodeList(rootNodePath);
		List<YunFile> yunpanFileList = fileNodeList.data.node_list;
		for (int i = 0; i != yunpanFileList.size(); ++i){
			String filePath = yunpanFileList.get(i).name;
			Boolean isFolder = (yunpanFileList.get(i).type == 1);
			String fileName = getFileName(filePath, isFolder);
			String fileSize = formatFileSize(yunpanFileList.get(i).count_size);
			int fileIcon = getFileIcon(fileName, isFolder);
			String fileMTime = yunpanFileList.get(i).mtime;
			String nid = yunpanFileList.get(i).nid;
			String pid = yunpanFileList.get(i).pid;
			String fileInfo = "";
			if (!isFolder){
				fileInfo = fileSize + " 修改日期:" + fileMTime;
			}
			if (StrongBoxAndFavoriteUtil.ifFileIsFavorite(nid)){
				fileName += " [已收藏]";
			}
			fileList.add(new FileDirectoryItem(fileIcon, fileName, fileInfo, isFolder, filePath, nid, pid, false));
		}
		if (!currentFolderPath.equals("/")){
			String s = currentFolderPath.substring(0, currentFolderPath.length() - 1);
			String currentFolder = s.substring(s.lastIndexOf("/") + 1);
			textTitle.setText(currentFolder);
		}else{
			textTitle.setText("目录");
		}
		
		adapter.notifyDataSetInvalidated();
	}
	
	class FolderClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if (fileList.get(position).isFolder){
				currentFolderPath = fileList.get(position).filePath;
				refreshFileList(currentFolderPath);
			}
		}
	}
	
	class FolderBackwardListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if (currentFolderPath.equals("/")){
				((Activity)context).finish();
			}			
			String parentFolderPath = "/";
			if (!currentFolderPath.equals("/")){
				String s = currentFolderPath.substring(0, currentFolderPath.length() - 1);
				parentFolderPath = s.substring(0, s.lastIndexOf("/") + 1);
			}
			currentFolderPath = parentFolderPath;
			refreshFileList(currentFolderPath);
		}
	}
	
	class BtnFavoriteListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Boolean isSetF = isSetFavorite(shareFileList);
			for (int i = 0; i != shareFileList.size(); ++i){
				String path = shareFileList.get(i).path;
				String nid = shareFileList.get(i).nid;
				String pid = shareFileList.get(i).pid;
				if (isSetF){
					Boolean addfresult = StrongBoxAndFavoriteUtil.addFileIntoFavorite(path, nid, pid);
					Log.i("Share", "addF " + addfresult);
				}else{
					Boolean delfresult = StrongBoxAndFavoriteUtil.removeFileFromFavorite(nid);
					Log.i("Share", "delF " + delfresult);
				}
			}
			refreshFileList(currentFolderPath);
			btnFavorite.setBackgroundResource(R.drawable.directory_btn_favorite);
			if (isSetF){
				Toast.makeText(getActivity(), "添加收藏成功！", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getActivity(), "取消收藏成功！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onClick(View v) {//OnCheckboxClick
		fileList.get((Integer)v.getTag()).isChecked ^= true;
		CheckBox checkbox = (CheckBox)v.findViewById(R.id.directory_file_item_favorite);
		int index = (Integer)v.getTag();
		String path = fileList.get(index).filePath;
		String nid = fileList.get(index).nid;
		String pid = fileList.get(index).pid;
		
		if (fileList.get(index).isChecked){//add
			if (++selectedCount == 1){
				btnFavorite.setVisibility(View.VISIBLE);
			}
			shareFileList.add(new favoriteItem(path, nid, pid));
			checkbox.setBackgroundResource(R.drawable.share_file_item_selected);
		}else{//del
			if (--selectedCount == 0){
				btnFavorite.setVisibility(View.INVISIBLE);
			}
			shareFileList.remove(new favoriteItem(path, nid, pid));
			checkbox.setBackgroundResource(R.drawable.share_file_item_unselected);
		}
		
		if (isSetFavorite(shareFileList)){
			btnFavorite.setBackgroundResource(R.drawable.directory_btn_favorite);
		}
		else{
			btnFavorite.setBackgroundResource(R.drawable.directory_btn_notfavorite);
		}
	}

	
	
	private void showPopupWindows(View v) {
		// menu.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
}