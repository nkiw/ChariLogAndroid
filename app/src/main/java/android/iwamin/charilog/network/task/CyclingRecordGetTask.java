package android.iwamin.charilog.network.task;

import android.iwamin.charilog.network.http.HttpCommunicator;
import android.iwamin.charilog.network.http.HttpResponse;
import android.iwamin.charilog.network.param.GetRequestCyclingRecord;
import android.os.AsyncTask;

import java.net.URL;

public class CyclingRecordGetTask extends AsyncTask<GetRequestCyclingRecord, Void, HttpResponse> {
	@Override
	protected HttpResponse doInBackground(GetRequestCyclingRecord... params) {
		URL serverUrl = params[0].getUrl();

		return new HttpCommunicator().doGet(serverUrl, "");
	}
}
