package android.charilog.network.json;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class JsonDeleteRecord {
	private static final String JSON_NAME_USER_ID = "userId";
	private static final String JSON_NAME_PASSWORD = "password";
	private static final String JSON_NAME_RECORD_ID = "recordId";

	private String userId;
	private String password;
	private Integer recordId;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		try {
			json.put(JSON_NAME_USER_ID, userId);
			json.put(JSON_NAME_PASSWORD, password);
			json.put(JSON_NAME_RECORD_ID, recordId);
		} catch (JSONException je) {
			Log.e("TO_JSON", je.getMessage());
		}

		return json;
	}
}
