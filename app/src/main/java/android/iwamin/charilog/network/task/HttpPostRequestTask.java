package android.iwamin.charilog.network.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostRequestTask extends AsyncTask<HttpRequestContent, Void, HttpResponseContent> {
	private static final int TIME_OUT = 5000;	// タイムアウト時間[msec]

	@Override
	protected HttpResponseContent doInBackground(HttpRequestContent... params) {
		URL url = params[0].getUrl();
		String jsonBody = params[0].getBody();

		HttpURLConnection con = null;
		HttpResponseContent response = null;

		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setConnectTimeout(TIME_OUT);
			con.connect();

			OutputStream out = null;
			try {
				out = con.getOutputStream();
				DataOutputStream os = new DataOutputStream(out);
				os.write(jsonBody.getBytes("UTF-8"));
				os.flush();
				os.close();
			} catch (IOException e) {
				Log.e("DO_POST1", e.getMessage());
			} finally {
				if (out != null) {
					out.close();
				}
			}

			InputStream in = null;
			InputStreamReader inStreamReader = null;
			BufferedReader reader = null;
			StringBuilder sb = new StringBuilder();
			try {
				in = con.getInputStream();
				inStreamReader = new InputStreamReader(in);
				reader = new BufferedReader(inStreamReader);

				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
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
			response = new HttpResponseContent(con.getResponseCode(), con.getResponseMessage(), sb.toString());
		} catch (IOException e) {
			Log.e("DO_POST2", e.getMessage());
		} catch (Exception e) {
			Log.e("DO_POST3", e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return response;
	}
}
