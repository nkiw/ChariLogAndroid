package android.iwamin.charilog.network;

import android.content.Context;
import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.lib.CommonLib;
import android.iwamin.charilog.network.http.HttpResponse;
import android.iwamin.charilog.network.param.GetRequestCyclingRecord;
import android.iwamin.charilog.network.param.PostRequestCyclingRecord;
import android.iwamin.charilog.network.param.PostRequestUserCreate;
import android.iwamin.charilog.network.task.CyclingRecordGetTask;
import android.iwamin.charilog.network.task.CyclingRecordPostTask;
import android.iwamin.charilog.network.task.UserCreatePostTask;
import android.iwamin.charilog.repository.RepositoryReader;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.List;

public class WebController {
	private static WebController _instance;

	private WebController() {

	}

	public static WebController getInstance() {
		if (_instance == null) {
			_instance = new WebController();
		}
		return _instance;
	}

	public void synchronize(Context context, ConnectionInfo connectionInfo) {
		try {
			// URLの設定
			URL url = new URL("http://" + connectionInfo.getUrl() + "/record");
			URL testUrl = new URL("http://" + connectionInfo.getUrl() + "/test");

			// ログイン情報の設定
			String userId = connectionInfo.getUserId();
			String password = CommonLib.encryptSHA256(connectionInfo.getPassword());
			String deviceId = CommonLib.encryptSHA256(connectionInfo.getDeviceId());

			// POST
			List<CyclingRecord> list = new RepositoryReader().getCyclingRecordList(context);
			for (CyclingRecord record : list) {
				PostRequestCyclingRecord param = new PostRequestCyclingRecord();
				param.setUrl(url);
				param.setUserId(userId);
				param.setPassword(password);
				param.setDeviceId(deviceId);
				param.setRecord(record);
				AsyncTask<PostRequestCyclingRecord, Void, HttpResponse> postTask
						= new CyclingRecordPostTask().execute(param);
				String response = postTask.get().toString();
			}

			// GET
			GetRequestCyclingRecord param = new GetRequestCyclingRecord(url);
			AsyncTask<GetRequestCyclingRecord, Void, HttpResponse> getTask
					= new CyclingRecordGetTask().execute(param);
			String jsonBody = getTask.get().toString();
			System.out.println(jsonBody);
		} catch(Exception e) {
			Log.e("HTTP", e.getMessage());
		} finally {
		}
	}

	public HttpResponse createUser(ConnectionInfo connectionInfo) {
		HttpResponse response = null;
		try {
			URL url = new URL("http://" + connectionInfo.getUrl() + "/account");

			String encryptedPassword = CommonLib.encryptSHA256(connectionInfo.getPassword());
//			Log.v("CIPHER", encryptedPassword);

			// サーバーに新規ユーザー登録要求を送信
			PostRequestUserCreate param = new PostRequestUserCreate(
					url,
					connectionInfo.getUserId(),
					encryptedPassword
			);
			AsyncTask<PostRequestUserCreate, Void, HttpResponse> postTask
					= new UserCreatePostTask().execute(param);

			response = postTask.get();
			if (response != null) {
				// 受信データ確認
				Log.v("CRE_USER", response.toString());
			}
		} catch (Exception e) {
			Log.e("CRE_USER", e.getMessage());
		}
		return response;
	}
}
