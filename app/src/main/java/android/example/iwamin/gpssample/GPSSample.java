package android.example.iwamin.gpssample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.iwamin.gpssample.monitor.CyclingMonitor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GPSSample extends AppCompatActivity {
	private final int REQUEST_PERMISSION = 10;

	private Timer timer = new Timer();
	private boolean isExecute = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpssample);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Startボタンのクリックリスナー設定
		Button buttonStart = (Button)findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isExecute == false) {
					isExecute = true;
					readyLocationService();        // Locationサービス開始
				}
			}
		});

		// Stopボタンのクリックリスナー設定
		Button buttonStop = (Button)findViewById(R.id.buttonStop);
		buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isExecute == true) {
					stopLocationService();        // Locationサービス停止
					isExecute = false;
				}
			}
		});

		timer = new Timer();
		timer.schedule(timerTask, 500, 500);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		stopLocationService();
		timer.cancel();
		timer.purge();
		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// 許可された
				startLocationService();
			} else {
				// 許可されなかった
				Toast.makeText(this, "GPSの実行許可が必要です", Toast.LENGTH_SHORT).show();
				isExecute = false;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_gpssample, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void readyLocationService() {
		final int SDK_VERSION_ANDROID6 = 23;

		if (Build.VERSION.SDK_INT >= SDK_VERSION_ANDROID6) {
			// Android6.0以上は許可設定が必要
			int permission = ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACCESS_FINE_LOCATION);

			if (permission == PackageManager.PERMISSION_GRANTED) {
				// 許可されていた場合
				startLocationService();
			} else {
				// 許可されていない場合
				ActivityCompat.requestPermissions(
						this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_PERMISSION);
			}
		} else {
			startLocationService();
		}
	}

	private void startLocationService() {
		Intent intent = new Intent(this, LocationService.class);
		startService(intent);
	}

	private void stopLocationService() {
		Intent intent = new Intent(this, LocationService.class);
		stopService(intent);
	}

	private TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			handler.sendMessage(Message.obtain(handler, 0, null));
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			CyclingMonitor.CyclingInfo info = CyclingMonitor.getInstance().getCyclingInfo();
			Location location = info.getLocation();

			if (location != null) {
				// 位置情報の表示
				TextView tvTime = (TextView)findViewById(R.id.tv_time_val);
				TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude_val);
				TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude_val);
				TextView tvAltitude = (TextView)findViewById(R.id.tv_altitude_val);

				tvTime.setText(String.valueOf(location.getTime()));
				tvLatitude.setText(String.valueOf(location.getLatitude()));
				tvLongitude.setText(String.valueOf(location.getLongitude()));
				tvAltitude.setText(String.valueOf(location.getAltitude()));

				// 走行状況の表示
				TextView tvTotalTime = (TextView)findViewById(R.id.tv_total_time_val);
				TextView tvTotalDistance = (TextView)findViewById(R.id.tv_total_distance_val);
				TextView tvCurrentSpeed = (TextView)findViewById(R.id.tv_current_speed_val);
				TextView tvMaximumSpeed = (TextView)findViewById(R.id.tv_max_speed_val);
				TextView tvAverageSpeed = (TextView)findViewById(R.id.tv_ave_speed_val);

				tvTotalTime.setText(String.valueOf(info.getTotalTime() / 1000) + "[sec]");
				tvTotalDistance.setText(String.valueOf(info.getTotalDistance() + "[m]"));
				tvCurrentSpeed.setText(String.format("%.2f", info.getCurrentSpeed()) + "[km/h]");
				tvMaximumSpeed.setText(String.format("%.2f", info.getMaximumSpeed()) + "[km/h]");
				tvAverageSpeed.setText(String.format("%.2f", info.getAverageSpeed()) + "[km/h]");
			}
		}
	};
}
