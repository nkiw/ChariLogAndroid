package android.charilog.network.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequestTask extends AsyncTask<HttpRequestContent, Void, HttpResponseContent> {
	private static final int TIME_OUT = 5000;	// タイムアウト時間[msec]

	@Override
	protected HttpResponseContent doInBackground(HttpRequestContent... params) {
		URL url = params[0].getUrl();

		HttpURLConnection con = null;
		HttpResponseContent response = null;

		try {
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(TIME_OUT);
			con.connect();

			InputStream in = null;
			InputStreamReader inStreamReader = null;
			BufferedReader reader = null;
			try {
				in = con.getInputStream();
				inStreamReader = new InputStreamReader(in);
				reader = new BufferedReader(inStreamReader);

				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				response = new HttpResponseContent(con.getResponseCode(), con.getResponseMessage(), sb.toString());
			} catch (IOException e) {
				Log.e("DO_GET1", e.getMessage());
			} finally {
				if (reader != null) {
					reader.close();
				}
				if (inStreamReader != null) {
					inStreamReader.close();
				}
				if (in != null) {
					in.close();
				}
			}
		} catch (IOException e) {
			Log.e("DO_GET2", e.getMessage());
		} catch (Exception e) {
			Log.e("DO_GET3", e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return response;
	}
}
