package android.iwamin.charilog.network;

import android.content.Context;
import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.repository.RepositoryReader;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.List;

public class ServerSyncronizer {
	private static ServerSyncronizer _instance;

	private ServerSyncronizer() {

	}

	public static ServerSyncronizer getInstance() {
		if (_instance == null) {
			_instance = new ServerSyncronizer();
		}
		return _instance;
	}

	public void synchronize(Context context, String serverUrl) {
		try {
			URL url = new URL("http://" + serverUrl + "/record");
//			URL testUrl = new URL("http://" + serverUrl + "/test");

			// POST
			List<CyclingRecord> list = new RepositoryReader().getCyclingRecordList(context);
			for (CyclingRecord record : list) {
				CyclingRecordPostTaskParam param = new CyclingRecordPostTaskParam();
				param.setUrl(url);
				param.setRecord(record);
				AsyncTask<CyclingRecordPostTaskParam, Void, String> postTask
						= new CyclingRecordPostTask().execute(param);
				String response = postTask.get();
			}

			// GET
			AsyncTask<URL, Void, String> getTask = new CyclingRecordGetTask().execute(url);
			String jsonBody = getTask.get().toString();
			Log.v("RES", jsonBody);

		} catch(Exception e) {
			Log.e("HTTP", e.getMessage());
		} finally {
		}
	}
}
