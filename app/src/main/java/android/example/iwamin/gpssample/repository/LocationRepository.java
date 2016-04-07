package android.example.iwamin.gpssample.repository;

import android.location.Location;

public class LocationRepository {
	private static LocationRepository _instance;

	private Location currentLocation;

	private LocationRepository() {
	}

	public static LocationRepository getInstance() {
		if (_instance == null) {
			_instance = new LocationRepository();
		}
		return _instance;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location location) {
		this.currentLocation = location;
	}
}
