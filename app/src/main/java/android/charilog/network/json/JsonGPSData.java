package android.charilog.network.json;

import android.charilog.repository.entity.GPSData;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class JsonGPSData {
	private static final String JSON_NAME_DATE_TIME = "dateTime";
	private static final String JSON_NAME_LATITUDE = "latitude";
	private static final String JSON_NAME_LONGITUDE = "longitude";
	private static final String JSON_NAME_ALTITUDE = "altitude";

	private Long dateTime;
	private Double latitude;
	private Double longitude;
	private Double altitude;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		try {
			json.put(JSON_NAME_DATE_TIME, dateTime);
			json.put(JSON_NAME_LATITUDE, latitude);
			json.put(JSON_NAME_LONGITUDE, longitude);
			json.put(JSON_NAME_ALTITUDE, altitude);
		} catch (JSONException je) {
			Log.e("TO_JSON", je.getMessage());
		}

		return json;
	}

	public JsonGPSData(GPSData e) {
		this.dateTime = e.getTime();
		this.latitude = e.getLatitude();
		this.longitude = e.getLongitude();
		this.altitude = e.getAltitude();
	}
}
