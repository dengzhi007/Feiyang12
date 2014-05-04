package com.qihoo.feiyang.share;

public class FileItem {
	public int fileIcon;
	public String fileName;
	public String fileInfo;
	public Boolean isFolder;
	public String filePath;
	public Boolean isShare;

	public FileItem(int fileIcon, String fileName, String fileInfo, Boolean isFoolder, String filePath, Boolean isShare) {
		this.fileIcon = fileIcon;
		this.fileName = fileName;
		this.fileInfo = fileInfo;
		this.isFolder = isFoolder;
		this.filePath = filePath;
		this.isShare = isShare;
		
	}
}