package com.qihoo.yunpan.sdk.android;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.qihoo.yunpan.sdk.android.config.YunpanSDKConfig;
import com.qihoo.yunpan.sdk.android.config.YunpanSDKConstants;
import com.qihoo.yunpan.sdk.android.http.HttpShotConnector;
import com.qihoo.yunpan.sdk.android.http.ResponseContent;
import com.qihoo.yunpan.sdk.android.http.SdkBaseAction;
import com.qihoo.yunpan.sdk.android.http.model.YunFileNode;
import com.qihoo.yunpan.sdk.android.http.parse.ParseJsonUtil;
import com.qihoo.yunpan.sdk.android.task.TransferStatus;
import com.qihoo.yunpan.sdk.android.util.SDKLogUtil;

public class GetNodeByNidAction extends SdkBaseAction<YunFileNode> {

	private final String METHOD = "File.getNodeByNid";

	public GetNodeByNidAction() {
		super(YunFileNode.class);
	}

	private String _nid = "";

	public YunFileNode getFileNodeByNid(String nid) {
		YunFileNode node = new YunFileNode();
		try {
			if (nid == null || nid.equals("")) {
				// 输入参数有误
				node.errno = YunpanSDKConfig.ERROR_PARAM;
				node.errmsg = TransferStatus.getErrorContentMessage(node.errno);
				return node;
			}
			this._nid = nid;
			node = super.doAction();
		} catch (Throwable e) {
			SDKLogUtil.writeLog(e);
			e.printStackTrace();
			// 未知错误
			node.errno = YunpanSDKConfig.ERROR_OTHER_EXCEPTION;
			node.errmsg = TransferStatus.getErrorContentMessage(node.errno);
		}
		return node;
	}

	@Override
	protected URI getRequstUri() throws Throwable {
		return new HttpShotConnector().getRequestUri(YunpanSDKConstants.getConfig_Api(false), METHOD);
	}

	@Override
	protected Map<String, String> getRequestCookie() throws Throwable {
		Map<String, String> cookie = new HashMap<String, String>();
		cookie.put(YunpanSDKConfig.PARAM_TOKEN, YunpanSDKConstants.getParameter_Token());
		cookie.put("nid", _nid);
		cookie.put("is_fpath", "1");
		return cookie;
	}

	@Override
	protected YunFileNode parseContent(ResponseContent rc) throws Throwable {
		String responseStr = new String(rc.responseBytes, YunpanSDKConfig.ENCODING);
		return new ParseJsonUtil().parseYunFileNode(responseStr);
	}

}
