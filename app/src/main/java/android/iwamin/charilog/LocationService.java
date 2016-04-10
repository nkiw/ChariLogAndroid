package android.iwamin.charilog;

import android.app.Service;
import android.content.Intent;
import android.iwamin.charilog.repository.RepositoryWriter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener {
	private static final int PERIOD_SAMPLING = 1000;		// サンプリング周期[msec]
	private static final int GPS_IGNORE_COUNT = 3;			// GPS取得値無視区間[count]

	private RepositoryWriter repositoryWriter;
	private LocationManager locationManager;
	private long gpsCount;

	@Override
	public void onCreate() {
		if (repositoryWriter == null) {
			repositoryWriter = new RepositoryWriter();
		}

		// 記録開始の準備
		repositoryWriter.readyLogging(this);

		// GPSの位置取得処理開始
		gpsCount = 0;
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

		if (locationManager != null) {
			try {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, PERIOD_SAMPLING, 0, this);
			} catch (SecurityException e) {
				Log.e("startLocation", e.getMessage());
			}
		} else {
			Log.e("startLocation", "LocationManager is null");
		}
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
		gpsCount++;
		if (gpsCount < GPS_IGNORE_COUNT) {
			// GPS起動直後はばらつきが大きいため無視する
			return;
		}

		repositoryWriter.reportLocationChange(location);

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
