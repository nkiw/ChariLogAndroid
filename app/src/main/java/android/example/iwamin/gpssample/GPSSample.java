package android.example.iwamin.gpssample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GPSSample extends AppCompatActivity implements LocationListener {
	// Git test.
	private LocationManager mLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpssample);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
		}

		mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
	}

	@Override
	protected void onResume() {
		if (mLocationManager != null) {
			try {
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
			} catch (SecurityException e) {
				Log.e("OnResume", e.getMessage());
			}
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mLocationManager != null) {
			try {
				mLocationManager.removeUpdates(this);
			} catch (SecurityException e) {
				Log.e("OnPause", e.getMessage());
			}
		}
		super.onPause();
	}

	@Override
	public void onLocationChanged(Location location) {
		TextView tvTime = (TextView)findViewById(R.id.tv_time_val);
		TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude_val);
		TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude_val);

		tvTime.setText(String.valueOf(location.getTime()));
		tvLatitude.setText(String.valueOf(location.getLatitude()));
		tvLongitude.setText(String.valueOf(location.getLongitude()));

//		Log.v("----------", "----------");
//		Log.v("Latitude", String.valueOf(location.getLatitude()));
//		Log.v("Longitude", String.valueOf(location.getLongitude()));
//		Log.v("Accuracy", String.valueOf(location.getAccuracy()));
//		Log.v("Altitude", String.valueOf(location.getAltitude()));
//		Log.v("Time", String.valueOf(location.getTime()));
//		Log.v("Speed", String.valueOf(location.getSpeed()));
//		Log.v("Bearing", String.valueOf(location.getBearing()));
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
}
