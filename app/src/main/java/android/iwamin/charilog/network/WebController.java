package android.iwamin.charilog.network;

import android.content.Context;
import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.lib.CommonLib;
import android.iwamin.charilog.network.json.JsonCyclingRecord;
import android.iwamin.charilog.network.json.JsonUserCreate;
import android.iwamin.charilog.network.task.HttpGetRequestTask;
import android.iwamin.charilog.network.task.HttpPostRequestTask;
import android.iwamin.charilog.network.task.HttpRequestContent;
import android.iwamin.charilog.network.task.HttpResponseContent;
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

			// ローカルに保存されている走行記録を取得する
			List<CyclingRecord> list = new RepositoryReader().getCyclingRecordList(context);

			// 走行記録リストをサーバーに送信する
			for (CyclingRecord record : list) {
				// 走行記録のJSONを生成し、HTTPリクエストを作成する
				JsonCyclingRecord param = new JsonCyclingRecord(
						userId, password, deviceId, record);
				HttpRequestContent content = new HttpRequestContent(url, param.toJson().toString());
				// HTTP POSTを実行する
				AsyncTask<HttpRequestContent, Void, HttpResponseContent> postTask
						= new HttpPostRequestTask().execute(content);
				// レスポンスを取得する
				HttpResponseContent response = postTask.get();
			}

			// サーバーに保存されている走行記録を全件取得(※デバッグ用)
			HttpRequestContent content = new HttpRequestContent(url, "");
			AsyncTask<HttpRequestContent, Void, HttpResponseContent> getTask
					= new HttpGetRequestTask().execute(content);
			HttpResponseContent response = getTask.get();
			System.out.println(response.toString());
		} catch(Exception e) {
			Log.e("HTTP", e.getMessage());
		} finally {
		}
	}

	public HttpResponseContent createUser(ConnectionInfo connectionInfo) {
		HttpResponseContent response = null;
		try {
			URL url = new URL("http://" + connectionInfo.getUrl() + "/account");

			// パスワードを暗号化する
			String encryptedPassword = CommonLib.encryptSHA256(connectionInfo.getPassword());
//			Log.v("CIPHER", encryptedPassword);

			// ユーザー情報のJSONを生成し、HTTPリクエストを作成する
			JsonUserCreate request = new JsonUserCreate(
					connectionInfo.getUserId(), encryptedPassword);
			HttpRequestContent content = new HttpRequestContent(url, request.toJson().toString());

			// HTTP POSTを実行する
			AsyncTask<HttpRequestContent, Void, HttpResponseContent> postTask
					= new HttpPostRequestTask().execute(content);

			// レスポンスを取得する
			response = postTask.get();
			if (response != null) {
				Log.v("CRE_USER", response.toString());
			}
		} catch (Exception e) {
			Log.e("CRE_USER", e.getMessage());
		}
		return response;
	}
}
