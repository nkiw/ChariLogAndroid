package android.charilog.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class CyclingRecordDownload {
	private static final String JSON_NAME_RECORD_ID = "recordId";
	private static final String JSON_NAME_DATE_TIME = "dateTime";
	private static final String JSON_NAME_DATE = "date";
	private static final String JSON_NAME_START_TIME = "startTime";
	private static final String JSON_NAME_END_TIME = "endTime";
	private static final String JSON_NAME_TOTAL_TIME = "totalTime";
	private static final String JSON_NAME_DISTANCE = "distance";
	private static final String JSON_NAME_AVE_SPEED = "aveSpeed";
	private static final String JSON_NAME_MAX_SPEED = "maxSpeed";

	private Integer recordId;
	private Long dateTime;
	private String date;
	private String startTime;
	private String endTime;
	private Long totalTime;
	private Integer distance;
	private Double aveSpeed;
	private Double maxSpeed;

	public CyclingRecordDownload(JSONObject jo) {
		try {
			this.recordId = jo.getInt(JSON_NAME_RECORD_ID);
			this.dateTime = jo.getLong(JSON_NAME_DATE_TIME);
			this.date = jo.getString(JSON_NAME_DATE);
			this.startTime = jo.getString(JSON_NAME_START_TIME);
			this.endTime = jo.getString(JSON_NAME_END_TIME);
			this.totalTime = jo.getLong(JSON_NAME_TOTAL_TIME);
			this.distance = jo.getInt(JSON_NAME_DISTANCE);
			this.aveSpeed = jo.getDouble(JSON_NAME_AVE_SPEED);
			this.maxSpeed = jo.getDouble(JSON_NAME_MAX_SPEED);
		} catch (JSONException je) {
			Log.e("CYCRECDOWN", je.getMessage());
		}
	}
}
