package android.iwamin.charilog.network.task;

import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.network.param.CyclingRecordPostTaskParam;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CyclingRecordPostTask extends AsyncTask<CyclingRecordPostTaskParam, Void, String> {

	@Override
	protected String doInBackground(CyclingRecordPostTaskParam... params) {
		URL serverUrl = params[0].getUrl();
		CyclingRecord record = params[0].getRecord();
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;
		Integer responseCode = null;
		String responseMessage = null;

		try {
			StringBuilder body = new StringBuilder();
			body.append("{\n");
			body.append(String.format("\"userId\" : \"%s\",\n", "nobi"));
			body.append(String.format("\"deviceId\" : \"%s\",\n", "dev_hoge"));
			body.append(String.format("\"dateTime\" : %d,\n", record.getDateRaw()));
			body.append(String.format("\"date\" : \"%s\",\n", record.getDate()));
			body.append((String.format("\"startTime\" : \"%s\",\n", record.getStartTime())));
			body.append((String.format("\"endTime\" : \"%s\",\n", record.getEndTime())));
			body.append((String.format("\"totalTime\" : %d,\n", record.getTotalTime())));
			body.append((String.format("\"distance\" : %d,\n", record.getDistance())));
			body.append((String.format("\"aveSpeed\" : %.1f,\n", record.getAveSpeed())));
			body.append((String.format("\"maxSpeed\" : %.1f\n", record.getMaxSpeed())));
			body.append("}");
			Log.v("JSON", body.toString());

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

			} finally {
				if (out != null) {
					out.close();
				}
			}

			responseCode = con.getResponseCode();
			responseMessage = con.getResponseMessage();
		} catch (IOException e) {
			Log.e("POST_RECORD", e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return responseCode.toString() + responseMessage;
	}
}
