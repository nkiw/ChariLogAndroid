package android.iwamin.charilog;

import android.app.Service;
import android.content.Intent;
import android.iwamin.charilog.monitor.CyclingMonitor;
import android.iwamin.charilog.repository.RepositoryWriter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener {
	private static final int PERIOD_SAMPLING = 1000;		// サンプリング周期[msec]
	private static final int ACCURACY_DEFAULT = 20;			// 許容精度のデフォルト値[m]

	private RepositoryWriter repositoryWriter;
	private LocationManager locationManager;
	private int accuracy;

	@Override
	public void onCreate() {
		if (repositoryWriter == null) {
			repositoryWriter = new RepositoryWriter();
		}

		// 走行モニターを初期化
		CyclingMonitor.getInstance().reset();

		// 記録開始の準備
		repositoryWriter.readyLogging(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// GPSの位置取得処理開始
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

		// 許容精度を設定
		final Bundle extras = intent.getExtras();
		accuracy = (extras != null) ? extras.getInt("ACCURACY") : ACCURACY_DEFAULT;

		if (locationManager != null) {
			try {
				Criteria criteria = new Criteria();
				criteria.setSpeedRequired(false);
				locationManager.requestLocationUpdates(
						locationManager.getBestProvider(criteria, true),
						PERIOD_SAMPLING,
						0,
						this);
			} catch (SecurityException e) {
				Log.e("startLocation", e.getMessage());
			}
		} else {
			Log.e("startLocation", "LocationManager is null");
		}

		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onDestroy() {
		// GPS取得処理を停止する
		if (locationManager != null) {
			try {
				locationManager.removeUpdates(this);
			} catch (SecurityException e) {
				Log.e("OnPause", e.getMessage());
			}
		} else {
			Log.e("onDestroy", "LocationManager is null");
		}

		// 記録処理を停止する
		repositoryWriter.stopLogging();
	}

	@Override
	public void onLocationChanged(Location location) {
		CyclingMonitor monitor = CyclingMonitor.getInstance();

		if (location.getAccuracy() <= accuracy) {	// 位置精度が有効範囲内か
			// 走行状況に有効な位置情報を通知する
			monitor.reportLocationChange(location);
			// 位置情報を記録する
			repositoryWriter.reportLocationChange(location);
		}

		// 走行状況に現在の位置精度を設定
		monitor.setAccuracy(location.getAccuracy());

//		Log.v("onLocationChanged", location.toString());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.v("onStatusChanged", provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v("onProviderEnabled", provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.e("onProviderDisabled", provider);
	}
}
