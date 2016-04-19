package android.charilog;

import android.charilog.lib.CommonLib;
import android.charilog.network.ConnectionInfo;
import android.charilog.network.CyclingRecordDownload;
import android.charilog.network.WebController;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.charilog.preference.PreferenceCconstants.PREFERENCE_FILE_NAME;
import static android.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_PASSWORD;
import static android.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_URL;
import static android.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_USER_ID;

public class DownloadRecordActivity extends AppCompatActivity {

	EditText editTextUrl;
	EditText editTextUserId;
	EditText editTextPassword;

	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_record);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// テキストボックスのオブジェクト取得
		editTextUrl = (EditText)findViewById(R.id.et_server_url);
		editTextUserId = (EditText)findViewById(R.id.et_user_id);
		editTextPassword = (EditText)findViewById(R.id.et_password);

		// プリファレンスからサーバーURL、ユーザーID、パスワードを読み出す
		preferences	= getSharedPreferences(PREFERENCE_FILE_NAME, MODE_PRIVATE);
		editTextUrl.setText(preferences.getString(PREFERENCE_KEY_URL, ""));
		editTextUserId.setText(preferences.getString(PREFERENCE_KEY_USER_ID, ""));
		editTextPassword.setText(preferences.getString(PREFERENCE_KEY_PASSWORD, ""));

		// 「走行記録をダウンロード」ボタンのクリックリスナー設定
		Button buttonDownload = (Button)findViewById(R.id.button_download);
		buttonDownload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<CyclingRecordDownload> list
						= new WebController(DownloadRecordActivity.this, DownloadRecordActivity.this)
							.downloadRecord(getConnectionInfo());
				updateList(list);
			}
		});
	}

	private ConnectionInfo getConnectionInfo() {
		ConnectionInfo info = null;

		String url = editTextUrl.getText().toString();
		String userId = editTextUserId.getText().toString();
		String password = editTextPassword.getText().toString();

		if (url.equals("")) {
			Toast.makeText(DownloadRecordActivity.this,
					"URLが入力されていません", Toast.LENGTH_SHORT).show();
		} else if (userId.equals("")) {
			Toast.makeText(DownloadRecordActivity.this,
					"ユーザーIDが入力されていません", Toast.LENGTH_SHORT).show();
		} else if (password.equals("")) {
			Toast.makeText(DownloadRecordActivity.this,
					"パスワードが入力されていません", Toast.LENGTH_SHORT).show();
		} else {
			// プリファレンスにサーバーURL、ユーザーID、パスワード、デバイスIDを記憶する
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(PREFERENCE_KEY_URL, url);
			editor.putString(PREFERENCE_KEY_USER_ID, userId);
			editor.putString(PREFERENCE_KEY_PASSWORD, password);
			editor.commit();

			info = new ConnectionInfo(url, userId, password, null);
			Log.v("CON_INFO", info.toString());
		}
		return info;
	}

	private void updateList(List<CyclingRecordDownload> list) {
		TableLayout table = (TableLayout)findViewById(R.id.tbl_list_record);
		table.removeAllViews();

		List<String> items = new ArrayList<>();
		items.add("ID");
		items.add("日付");
		items.add("時刻");
		items.add("走行時間");
		items.add("走行距離");
		items.add("平均速度");
		items.add("最高速度");

		TableRow head = new TableRow(this);
		for (String item : items) {
			TextView view = new TextView(this);
			view.setText(item + " ");
			view.setGravity(Gravity.CENTER_HORIZONTAL);
			head.addView(view);
		}
		table.addView(head);

		for (CyclingRecordDownload record : list) {
			TableRow row = new TableRow(this);

			List<String> columns = new ArrayList<>();
			columns.add(String.valueOf(record.getRecordId()));
			columns.add(record.getDate());
			columns.add(new SimpleDateFormat("HH:mm").format(record.getDateTime()));
			int time[] = CommonLib.msecToHourMinSec(record.getTotalTime());
			columns.add(String.format("%02d:%02d:%02d", time[0], time[1], time[2]));
			columns.add(String.format("%.3f", (double)record.getDistance() / 1000) + "km");
			columns.add(String.format("%.1f", record.getAveSpeed()) + "km/h");
			columns.add(String.format("%.1f", record.getMaxSpeed()) + "km/h");

			for (String column : columns) {
				TextView view = new TextView(this);
				view.setText(column + " ");
				view.setGravity(Gravity.CENTER_HORIZONTAL);
				row.addView(view);
			}
			table.addView(row);
		}
	}
}
