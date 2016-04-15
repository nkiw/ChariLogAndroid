package android.iwamin.charilog.network.task;

import android.iwamin.charilog.network.http.HttpCommunicator;
import android.iwamin.charilog.network.param.PostRequestUserCreate;
import android.os.AsyncTask;

import java.net.URL;

public class UserCreatePostTask extends AsyncTask<PostRequestUserCreate, Void, String> {

	@Override
	protected String doInBackground(PostRequestUserCreate... params) {
		URL serverUrl = params[0].getUrl();
		String jsonBody = params[0].toJson().toString();
//		Log.v("JSON", jsonBody);

		String response = new HttpCommunicator().doPost(serverUrl, jsonBody);

		return response;
	}
}
