package android.iwamin.charilog.entity;

public class GPSData {
	private long dateRaw;
	private long time;
	private double latitude;
	private double longitude;
	private double altitude;

	public double getAltitude() {
		return altitude;
	}

	public long getDateRaw() {
		return dateRaw;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getTime() {
		return time;
	}

	public GPSData(long dateRaw, long time, double latitude, double longitude, double altitude) {
		this.dateRaw = dateRaw;
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	@Override
	public String toString() {
		return "GPSData{" +
				"dateRaw=" + dateRaw +
				", time=" + time +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", altitude=" + altitude +
				'}';
	}
}
