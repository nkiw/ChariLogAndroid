package android.iwamin.charilog.network.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CyclingRecordGetTask extends AsyncTask<URL, Void, String> {
	@Override
	protected String doInBackground(URL... urls) {
		URL serverUrl = urls[0];
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;

		try {
			con = (HttpURLConnection) serverUrl.openConnection();

			con.setRequestMethod("GET");
			con.connect();

			InputStream in = con.getInputStream();
			InputStreamReader inStreamReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inStreamReader);

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			reader.close();
			inStreamReader.close();
			in.close();
		} catch (IOException e) {
			Log.e("GET_RECORD", e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return sb.toString();
	}
}
