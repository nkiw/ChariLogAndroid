package android.charilog.network;

import android.app.Activity;
import android.charilog.lib.CommonLib;
import android.charilog.network.json.JsonAccountInfo;
import android.charilog.network.json.JsonCyclingRecord;
import android.charilog.network.json.JsonDeleteRecord;
import android.charilog.network.json.JsonGPSData;
import android.charilog.network.task.HttpPostRequestTask;
import android.charilog.network.task.HttpRequestContent;
import android.charilog.network.task.HttpResponseContent;
import android.charilog.repository.RepositoryReader;
import android.charilog.repository.entity.CyclingRecord;
import android.charilog.repository.entity.GPSData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebController {
	private Context context;
	private Activity activity;

	public WebController(Activity activity, Context context) {
		this.context = context;
		this.activity = activity;
	}

	public void synchronize(ConnectionInfo connectionInfo) {
		boolean isSuccess = false;
		int count = 0;

		try {
			// URLの設定
			URL urlRecord = new URL("http://" + connectionInfo.getUrl() + "/record/upload");
			URL urlGPS = new URL("http://" + connectionInfo.getUrl() + "/gps/upload");
			URL urlKey = new URL("http://" + connectionInfo.getUrl() + "/gps/invalidate-key");
//			URL testUrl = new URL("http://" + connectionInfo.getUrl() + "/test");

			// ログイン情報の設定
			String userId = connectionInfo.getUserId();
			String password = CommonLib.encryptSHA256(connectionInfo.getPassword());
			String deviceId = CommonLib.encryptSHA256(connectionInfo.getDeviceId());

			// ローカルに保存されている未送信の走行記録を取得する
			RepositoryReader repositoryReader = new RepositoryReader();
			List<CyclingRecord> list = repositoryReader.getNotUploadedRecord(context);

			// 走行記録リストをサーバーに送信する
			for (CyclingRecord record : list) {
				// 走行記録のJSONを生成する
				JsonCyclingRecord param = new JsonCyclingRecord(
						userId, password, deviceId, record);
				// 走行記録をサーバーに送信する
				HttpRequestContent content = new HttpRequestContent(urlRecord, param.toJson().toString());
				AsyncTask<HttpRequestContent, Void, HttpResponseContent> postTask
						= new HttpPostRequestTask().execute(content);
				// レスポンスを取得する
				HttpResponseContent response = postTask.get();
//				System.out.println(response.toString());

				// アップロードに失敗した場合、処理を終了する
				if (response.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
					break;
				}

				// レスポンスからGPSデータ登録用keyを取得する
				String key = new JSONObject(response.getBody()).getString("key");
//				Log.v("KEY", key);

				// このレコードに対応するGPSデータを取得する
				List<GPSData> gpsList = repositoryReader.getGPSDataList(context, record.getDateRaw());
//				System.out.println("GPS_CNT: " + gpsList.size());

				// GPSデータ送信用のJSONオブジェクトを作成する
				JSONArray jsonArray = new JSONArray();
				for (GPSData gps : gpsList) {
					jsonArray.put(new JsonGPSData(gps).toJson());
				}
				JSONObject gpsBody = new JSONObject();
				gpsBody.put("key", key);
				gpsBody.put("data", jsonArray);
//				System.out.println("GPS_JSON: " + gpsBody.toString());
				// GPSデータを送信する
				HttpRequestContent gpsContent = new HttpRequestContent(urlGPS, gpsBody.toString());
				AsyncTask<HttpRequestContent, Void, HttpResponseContent> gpsPostTask
						= new HttpPostRequestTask().execute(gpsContent);
				// レスポンスを取得する
				HttpResponseContent gpsResponse = gpsPostTask.get();
//				System.out.println(gpsResponse.toString());

				// アップロードに失敗した場合、処理を終了する
				if (gpsResponse.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
					break;
				}

				// GPSデータ登録用キーの無効化を要求する
				JSONObject keyBody = new JSONObject();
				keyBody.put("key", key);
				HttpRequestContent keyContent = new HttpRequestContent(urlKey, keyBody.toString());
				AsyncTask<HttpRequestContent, Void, HttpResponseContent> keyPostTask
						= new HttpPostRequestTask().execute(keyContent);
				HttpResponseContent keyResponse = keyPostTask.get();
//				System.out.println(keyResponse.toString());

				// このレコードのアップロード済みをセットする
//				repositoryReader.setUploaded(context, record.getId());
				count++;
			}
			isSuccess = true;
		} catch(Exception e) {
			Log.e("HTTP", e.getMessage());
		} finally {
			AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
			if (isSuccess) {
				dialog.setTitle("成功");
				dialog.setMessage("新たに" + count + "件登録しました。");
			} else {
				dialog.setTitle("エラー");
				dialog.setMessage("途中でエラーが発生しました(" + count + "件登録済み)。");
			}
			dialog.show();
		}
	}

	public void createUser(ConnectionInfo connectionInfo) {
		HttpResponseContent response = null;
		try {
			URL url = new URL("http://" + connectionInfo.getUrl() + "/account");

			// パスワードを暗号化する
			String encryptedPassword = CommonLib.encryptSHA256(connectionInfo.getPassword());
			Log.v("CIPHER", encryptedPassword);

			// ユーザー情報のJSONを生成し、HTTPリクエストを作成する
			JsonAccountInfo request = new JsonAccountInfo(
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

		// 結果をダイアログで表示する
		if (response != null) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
			switch (response.getResponseCode()) {
				case HttpURLConnection.HTTP_CREATED:
					dialog.setTitle("成功");
					dialog.setMessage("ID:" + connectionInfo.getUserId() + "を作成しました。");
					break;
				case HttpURLConnection.HTTP_CONFLICT:
					dialog.setTitle("失敗");
					dialog.setMessage("ID:" + connectionInfo.getUserId() + "はすでに使用されています。");
					break;
				default:
					dialog.setTitle("エラー");
					dialog.setMessage("エラーが発生しました。");
					break;
			}
			dialog.show();
		}
	}

	public List<CyclingRecordDownload> downloadRecord(ConnectionInfo connectionInfo) {
		List<CyclingRecordDownload> list = new ArrayList<>();
		HttpResponseContent response = null;
		try {
			URL url = new URL("http://" + connectionInfo.getUrl() + "/record/download");

			// パスワードを暗号化する
			String encryptedPassword = CommonLib.encryptSHA256(connectionInfo.getPassword());

			// ユーザー情報のJSONを生成し、HTTPリクエストを作成する
			JsonAccountInfo request = new JsonAccountInfo(
					connectionInfo.getUserId(), encryptedPassword);
			HttpRequestContent content = new HttpRequestContent(url, request.toJson().toString());

			// HTTP POSTを実行する
			AsyncTask<HttpRequestContent, Void, HttpResponseContent> postTask
					= new HttpPostRequestTask().execute(content);

			// レスポンスを取得する
			response = postTask.get();

			// JSON解析
			JSONArray jsonArray = new JSONArray(response.getBody());
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(new CyclingRecordDownload(jsonArray.getJSONObject(i)));
			}
		} catch (Exception e) {
			Log.e("DOWNREC", e.getMessage());
		}

		// 結果をダイアログで表示する
		if (response != null) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
			switch (response.getResponseCode()) {
				case HttpURLConnection.HTTP_OK:
					// Nothing
					break;
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					dialog.setTitle("ユーザー認証失敗");
					dialog.setMessage("ユーザーID、又は、パスワードが間違っています。");
					dialog.show();
					break;
				default:
					dialog.setTitle("エラー");
					dialog.setMessage("エラーが発生しました。");
					dialog.show();
					break;
			}
		}

		return list;
	}

	public void deleteRecord(ConnectionInfo connectionInfo, Integer recordId) {
		HttpResponseContent response = null;
		try {
			URL url = new URL("http://" + connectionInfo.getUrl() + "/record/delete");

			// パスワードを暗号化する
			String encryptedPassword = CommonLib.encryptSHA256(connectionInfo.getPassword());
			Log.v("CIPHER", encryptedPassword);

			// ユーザー情報のJSONを生成し、HTTPリクエストを作成する
			JsonDeleteRecord request = new JsonDeleteRecord(
					connectionInfo.getUserId(), encryptedPassword, recordId);
			HttpRequestContent content = new HttpRequestContent(url, request.toJson().toString());

			// HTTP POSTを実行する
			AsyncTask<HttpRequestContent, Void, HttpResponseContent> postTask
					= new HttpPostRequestTask().execute(content);

			// レスポンスを取得する
			response = postTask.get();
			if (response != null) {
				Log.v("DEL_RECORD", response.toString());
			}
		} catch (Exception e) {
			Log.e("DEL_RECORD", e.getMessage());
		}

		// 結果をダイアログで表示する
		if (response != null) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
			switch (response.getResponseCode()) {
				case HttpURLConnection.HTTP_NO_CONTENT:
					dialog.setTitle("成功");
					dialog.setMessage("レコードID:" + recordId + "を削除しました。");
					break;
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					dialog.setTitle("ユーザー認証失敗");
					dialog.setMessage("ユーザーID、又は、パスワードが間違っています。");
					break;
				case HttpURLConnection.HTTP_FORBIDDEN:
					dialog.setTitle("不正なレコードID");
					dialog.setMessage("指定されたIDのレコードは削除できません。");
					break;
				default:
					dialog.setTitle("エラー");
					dialog.setMessage("エラーが発生しました。");
					break;
			}
			dialog.show();
		}
	}
}
