package android.example.iwamin.gpssample;

import android.app.Service;
import android.content.Intent;
import android.example.iwamin.gpssample.repository.LocationRepository;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener {
	private static final int PERIOD_SAMPLING = 1000;		// サンプリング周期[msec]

	private LocationRepository locationRepository;
	private LocationManager locationManager;

	@Override
	public void onCreate() {
		if (locationRepository == null) {
			locationRepository = new LocationRepository();
		}

		// 記録開始の準備
		locationRepository.readyLogging(this);

		// GPSの位置取得処理開始
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
		locationRepository.stopLogging();
	}

	@Override
	public void onLocationChanged(Location location) {
		locationRepository.reportLocationChange(location);

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
