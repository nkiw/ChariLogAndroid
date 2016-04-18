package android.iwamin.charilog.network.json;

import android.iwamin.charilog.entity.CyclingRecord;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class JsonCyclingRecord {
	private static final String JSON_NAME_USER_ID = "userId";
	private static final String JSON_NAME_PASSWORD = "password";
	private static final String JSON_NAME_DEVICE_ID = "deviceId";
	private static final String JSON_NAME_DATE_TIME = "dateTime";
	private static final String JSON_NAME_DATE = "date";
	private static final String JSON_NAME_START_TIME = "startTime";
	private static final String JSON_NAME_END_TIME = "endTime";
	private static final String JSON_NAME_TOTAL_TIME = "totalTime";
	private static final String JSON_NAME_DISTANCE = "distance";
	private static final String JSON_NAME_AVE_SPEED = "aveSpeed";
	private static final String JSON_NAME_MAX_SPEED = "maxSpeed";

	private String userId;
	private String password;
	private String deviceId;
	private CyclingRecord record;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		try {
			json.put(JSON_NAME_USER_ID, userId);
			json.put(JSON_NAME_PASSWORD, password);
			json.put(JSON_NAME_DEVICE_ID, deviceId);
			json.put(JSON_NAME_DATE_TIME, record.getDateRaw());
			json.put(JSON_NAME_DATE, record.getDate());
			json.put(JSON_NAME_START_TIME, record.getStartTime());
			json.put(JSON_NAME_END_TIME, record.getEndTime());
			json.put(JSON_NAME_TOTAL_TIME, record.getTotalTime());
			json.put(JSON_NAME_DISTANCE, record.getDistance());
			json.put(JSON_NAME_AVE_SPEED, record.getAveSpeed());
			json.put(JSON_NAME_MAX_SPEED, record.getMaxSpeed());
		} catch (JSONException je) {
			Log.e("TO_JSON", je.getMessage());
		}

		return json;
	}

}
