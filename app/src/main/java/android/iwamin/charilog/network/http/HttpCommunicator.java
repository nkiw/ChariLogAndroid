package android.iwamin.charilog.network.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCommunicator {

	public HttpResponse doGet(URL url, String jsonBody) {
		HttpURLConnection con = null;
		HttpResponse response = null;

		try {
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
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
				response = new HttpResponse(con.getResponseCode(), con.getResponseMessage(), sb.toString());
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
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return response;
	}

	public HttpResponse doPost(URL url, String jsonBody) {
		HttpURLConnection con = null;
		HttpResponse response = null;

		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
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
			response = new HttpResponse(con.getResponseCode(), con.getResponseMessage(), "");
		} catch (IOException e) {
			Log.e("DO_POST2", e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return response;
	}
}
