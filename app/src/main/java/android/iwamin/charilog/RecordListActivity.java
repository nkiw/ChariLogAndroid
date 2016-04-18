package android.iwamin.charilog;

import android.content.SharedPreferences;
import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.lib.CommonLib;
import android.iwamin.charilog.network.ConnectionInfo;
import android.iwamin.charilog.network.WebController;
import android.iwamin.charilog.repository.RepositoryReader;
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

import static android.iwamin.charilog.preference.PreferenceCconstants.PREFERENCE_FILE_NAME;
import static android.iwamin.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_DEVICE_ID;
import static android.iwamin.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_PASSWORD;
import static android.iwamin.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_URL;
import static android.iwamin.charilog.preference.PreferenceCconstants.PREFERENCE_KEY_USER_ID;

public class RecordListActivity extends AppCompatActivity {
	RepositoryReader repositoryReader;

	EditText editTextUrl;
	EditText editTextUserId;
	EditText editTextPassword;
	EditText editTextDeviceId;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// インテント
		final Bundle extras = getIntent().getExtras();
		repositoryReader = new RepositoryReader();

		editTextUrl = (EditText)findViewById(R.id.et_server_url);
		editTextUserId = (EditText)findViewById(R.id.et_user_id);
		editTextPassword = (EditText)findViewById(R.id.et_password);
		editTextDeviceId = (EditText)findViewById(R.id.et_device_id);

		// プリファレンスからサーバーURL、ユーザーID、パスワードを読み出す
		preferences	= getSharedPreferences(PREFERENCE_FILE_NAME, MODE_PRIVATE);
		editTextUrl.setText(preferences.getString(PREFERENCE_KEY_URL, ""));
		editTextUserId.setText(preferences.getString(PREFERENCE_KEY_USER_ID, ""));
		editTextPassword.setText(preferences.getString(PREFERENCE_KEY_PASSWORD, ""));
		String deviceId = preferences.getString(PREFERENCE_KEY_DEVICE_ID, "");
		if (deviceId.equals("")) {
			deviceId = android.os.Build.MODEL;	// デフォルト値として端末名を使用
		}
		editTextDeviceId.setText(deviceId);

		// 削除ボタンのクリックリスナー設定
		Button buttonDel = (Button)findViewById(R.id.button_del);
		buttonDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isExecute = (extras != null) ? extras.getBoolean("IS_EXE") : false;

				if (isExecute) {
					Toast.makeText(RecordListActivity.this, "実行中は削除できません", Toast.LENGTH_SHORT).show();
				} else {
					EditText editText = (EditText) findViewById(R.id.et_del_id);
					String idStr = editText.getText().toString();
					if (!idStr.equals("")) {
						int id = Integer.parseInt(editText.getText().toString());
						if (id > 0) {
							repositoryReader.deleteCyclingRecord(RecordListActivity.this, id);
						}
					}
					editText.setText("");
					showCyclingRecordList();
				}
			}
		});

		// ユーザー作成ボタンのクリックリスナー設定
		Button buttonUserCreate = (Button)findViewById(R.id.button_user_create);
		buttonUserCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ConnectionInfo info = getConnectionInfo();
				if (info != null) {
					new WebController(RecordListActivity.this, RecordListActivity.this)
							.createUser(info);
				}
			}
		});

		// 同期ボタンのクリックリスナー設定
		Button buttonSync = (Button)findViewById(R.id.button_sync);
		buttonSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ConnectionInfo info = getConnectionInfo();
				if (info != null) {
					new WebController(RecordListActivity.this, RecordListActivity.this)
							.synchronize(info);
				}
			}
		});

		// 「端末名を使用」ボタンのクリックリスナー設定
		Button buttonModelName = (Button)findViewById(R.id.button_model_name);
		buttonModelName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextDeviceId.setText(android.os.Build.MODEL);
			}
		});

		// 走行記録一覧を表示する
		showCyclingRecordList();
	}

	private void showCyclingRecordList() {
		List<CyclingRecord> list = repositoryReader.getCyclingRecordList(this);

		TableLayout table = (TableLayout)findViewById(R.id.tbl_list_record);
		table.removeAllViews();

		List<String> items = new ArrayList<>();
		items.add("ID");
//		items.add("Time(Raw)");
		items.add("日付");
		items.add("時刻");
//		items.add("End Time");
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

		for (CyclingRecord record : list) {
//			Log.v("LIST", record.toString());

			TableRow row = new TableRow(this);

			List<String> columns = new ArrayList<>();
			columns.add(String.valueOf(record.getId()));
//			columns.add(String.valueOf(record.getDateRaw()));
			columns.add(record.getDate());
			columns.add(new SimpleDateFormat("HH:mm").format(record.getDateRaw()));
//			columns.add(record.getEndTime());
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

//		// for debug.
//		long condition = list.get(list.size() - 1).getDateRaw();
//		List<GPSData> listGPSData = new RepositoryReader().getGPSDataList(this, condition);
//		for (GPSData data : listGPSData) {
//			Log.v("TEST", data.toString());
//		}
	}

	private ConnectionInfo getConnectionInfo() {
		ConnectionInfo info = null;

		String url = editTextUrl.getText().toString();
		String userId = editTextUserId.getText().toString();
		String password = editTextPassword.getText().toString();
		String deviceId = editTextDeviceId.getText().toString();

		if (url.equals("")) {
			Toast.makeText(RecordListActivity.this,
					"URLが入力されていません", Toast.LENGTH_SHORT).show();
		} else if (userId.equals("")) {
			Toast.makeText(RecordListActivity.this,
					"ユーザーIDが入力されていません", Toast.LENGTH_SHORT).show();
		} else if (password.equals("")) {
			Toast.makeText(RecordListActivity.this,
					"パスワードが入力されていません", Toast.LENGTH_SHORT).show();
		} else if (deviceId.equals("")) {
			Toast.makeText(RecordListActivity.this,
					"デバイスIDが入力されていません", Toast.LENGTH_SHORT).show();
		} else {
			// プリファレンスにサーバーURL、ユーザーID、パスワード、デバイスIDを記憶する
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(PREFERENCE_KEY_URL, url);
			editor.putString(PREFERENCE_KEY_USER_ID, userId);
			editor.putString(PREFERENCE_KEY_PASSWORD, password);
			editor.putString(PREFERENCE_KEY_DEVICE_ID, deviceId);
			editor.commit();

//			// MACアドレス取得 ※Android6.0からMACアドレスを取得できなくなった
//			WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//			String deviceId = wifiInfo.getMacAddress();

			info = new ConnectionInfo(url, userId, password, deviceId);
			Log.v("CON_INFO", info.toString());
		}
		return info;
	}

}
