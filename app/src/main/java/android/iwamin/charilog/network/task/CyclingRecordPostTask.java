package android.iwamin.charilog.network.task;

import android.iwamin.charilog.network.http.HttpCommunicator;
import android.iwamin.charilog.network.http.HttpResponse;
import android.iwamin.charilog.network.param.PostRequestCyclingRecord;
import android.os.AsyncTask;

import java.net.URL;

public class CyclingRecordPostTask extends AsyncTask<PostRequestCyclingRecord, Void, HttpResponse> {

	@Override
	protected HttpResponse doInBackground(PostRequestCyclingRecord... params) {
		URL serverUrl = params[0].getUrl();
		String jsonBody = params[0].toJson().toString();
//		Log.v("JSON", jsonBody);

		HttpResponse response = new HttpCommunicator().doPost(serverUrl, jsonBody);

		return response;
	}
}
