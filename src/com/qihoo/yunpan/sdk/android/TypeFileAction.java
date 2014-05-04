package com.qihoo.yunpan.sdk.android;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.qihoo.yunpan.sdk.android.config.YunpanSDKConfig;
import com.qihoo.yunpan.sdk.android.config.YunpanSDKConstants;
import com.qihoo.yunpan.sdk.android.http.HttpShotConnector;
import com.qihoo.yunpan.sdk.android.http.ResponseContent;
import com.qihoo.yunpan.sdk.android.http.SdkBaseAction;
import com.qihoo.yunpan.sdk.android.http.action.FileGetNodeList.OrderType;
import com.qihoo.yunpan.sdk.android.http.model.FileNodeList;
import com.qihoo.yunpan.sdk.android.http.parse.ParseJsonUtil;
import com.qihoo.yunpan.sdk.android.task.TransferStatus;
import com.qihoo.yunpan.sdk.android.util.SDKLogUtil;

public class TypeFileAction extends SdkBaseAction<FileNodeList> {

	private final String CATEGORY_FILE_METHOD = "File.getNodeListByCategory";
	private final String ALBUM_METHOD = "File.getDirListIncludePic";

	private int start = -1, count = -1;
	private OrderType orderParam = OrderType.mtime;
	private boolean isdesc = true;
	private boolean thumb = false;
	private boolean preview = false;

	public enum CategoryType {
		OTHER(0), IMAGE(1), DOCUMENT(2), MUSIC(3), VIDEO(4), ALBUM(-1);
		// 成员变量
		private int _value = -1;

		public int get_value() {
			return _value;
		}

		// 构造方法
		private CategoryType(int value) {
			this._value = value;
		}
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public OrderType getOrderParam() {
		return orderParam;
	}

	public void setOrderParam(OrderType orderParam) {
		this.orderParam = orderParam;
	}

	public boolean isIsdesc() {
		return isdesc;
	}

	public void setIsdesc(boolean isdesc) {
		this.isdesc = isdesc;
	}

	public boolean isThumb() {
		return thumb;
	}

	public void setThumb(boolean thumb) {
		this.thumb = thumb;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public TypeFileAction() {
		super(FileNodeList.class);
	}

	private CategoryType _type = CategoryType.ALBUM;

	public FileNodeList getFileTypeList(CategoryType type) {
		FileNodeList list = new FileNodeList();
		try {
			if (type == null) {
				// 输入参数有误
				list.errno = YunpanSDKConfig.ERROR_PARAM;
				list.errmsg = TransferStatus.getErrorContentMessage(list.errno);
				return list;
			}
			this._type = type;
			list = super.doAction();
		} catch (Throwable e) {
			SDKLogUtil.writeLog(e);
			e.printStackTrace();
			// 未知错误
			list.errno = YunpanSDKConfig.ERROR_OTHER_EXCEPTION;
			list.errmsg = TransferStatus.getErrorContentMessage(list.errno);
		}
		return list;
	}

	@Override
	protected URI getRequstUri() throws Throwable {
		String method = "";
		if (_type == CategoryType.ALBUM)
			method = ALBUM_METHOD;
		else
			method = CATEGORY_FILE_METHOD;
		return new HttpShotConnector().getRequestUri(YunpanSDKConstants.getConfig_Api(false), method);
	}

	@Override
	protected Map<String, String> getRequestCookie() throws Throwable {
		Map<String, String> cookie = new HashMap<String, String>();
		cookie.put(YunpanSDKConfig.PARAM_TOKEN, YunpanSDKConstants.getParameter_Token());
		cookie.put("file_category", String.valueOf(_type.get_value()));
		if (isThumb()) {
			cookie.put("thumb", "1");
			cookie.put("size", "205_205");
		}
		if (isPreview()) {
			cookie.put("preview", "1");
		}
		cookie.put("skey", orderParam.toString());
		if (isdesc) {
			cookie.put("isdesc", "1");
		}
		if (start >= 0)
			cookie.put("start", String.valueOf(start));
		if (count >= 0)
			cookie.put("count", String.valueOf(count));
		return cookie;
	}

	@Override
	protected FileNodeList parseContent(ResponseContent rc) throws Throwable {
		String responseStr = new String(rc.responseBytes, YunpanSDKConfig.ENCODING);
		FileNodeList list = new ParseJsonUtil().parseFileNodeList(responseStr);
		return list;
	}

}
