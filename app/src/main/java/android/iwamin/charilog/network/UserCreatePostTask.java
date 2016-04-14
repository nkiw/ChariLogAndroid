package android.iwamin.charilog.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserCreatePostTask extends AsyncTask<UserCreatePostTaskParam, Void, String> {
	@Override
	protected String doInBackground(UserCreatePostTaskParam... params) {
		URL serverUrl = params[0].getUrl();

		HttpURLConnection con = null;
		Integer responseCode = null;
		String responseMessage = null;

		try {
			StringBuilder body = new StringBuilder();
			body.append("{\n");
			body.append(String.format("\"userId\" : \"%s\",\n", params[0].getUserId()));
			body.append(String.format("\"password\" : \"%s\"\n", params[0].getPassword()));
			body.append("}");
//			Log.v("JSON", body.toString());

			con = (HttpURLConnection) serverUrl.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.connect();

			OutputStream out = null;
			try {
				out = con.getOutputStream();
				DataOutputStream os = new DataOutputStream(out);
				os.write(body.toString().getBytes("UTF-8"));
				os.flush();
				os.close();
			} catch (IOException e) {
				Log.e("POST_USER", e.getMessage());
			} finally {
				if (out != null) {
					out.close();
				}
			}
			responseCode = con.getResponseCode();
			responseMessage = con.getResponseMessage();
		} catch (IOException e) {
			Log.e("POST_USER", e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return responseCode.toString() + responseMessage;
	}
}
