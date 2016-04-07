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
	private static final int PERIOD_SAMPLING = 500;		// サンプリング周期[msec]

	private LocationManager mLocationManager;

	public LocationService() {
	}

	@Override
	public void onCreate() {
		mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

		if (mLocationManager != null) {
			try {
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, PERIOD_SAMPLING, 0, this);
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
		if (mLocationManager != null) {
			try {
				mLocationManager.removeUpdates(this);
			} catch (SecurityException e) {
				Log.e("OnPause", e.getMessage());
			}
		} else {
			Log.e("startLocation", "LocationManager is null");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		LocationRepository.getInstance().setCurrentLocation(location);

//		TextView tvTime = (TextView)findViewById(R.id.tv_time_val);
//		TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude_val);
//		TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude_val);
//
//		tvTime.setText(String.valueOf(location.getTime()));
//		tvLatitude.setText(String.valueOf(location.getLatitude()));
//		tvLongitude.setText(String.valueOf(location.getLongitude()));

		Log.v("----------", "onLocationChanged");
		Log.v("Latitude", String.valueOf(location.getLatitude()));
		Log.v("Longitude", String.valueOf(location.getLongitude()));
		Log.v("Accuracy", String.valueOf(location.getAccuracy()));
		Log.v("Altitude", String.valueOf(location.getAltitude()));
		Log.v("Time", String.valueOf(location.getTime()));
		Log.v("Speed", String.valueOf(location.getSpeed()));
		Log.v("Bearing", String.valueOf(location.getBearing()));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
