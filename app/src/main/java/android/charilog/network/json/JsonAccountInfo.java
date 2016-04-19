package android.charilog.network.json;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class JsonAccountInfo {
	private static final String JSON_NAME_USER_ID = "userId";
	private static final String JSON_NAME_PASSWORD = "password";

	private String userId;
	private String password;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();

		try {
			json.put(JSON_NAME_USER_ID, userId);
			json.put(JSON_NAME_PASSWORD, password);
		} catch (JSONException je) {
			Log.e("TO_JSON", je.getMessage());
		}

		return json;
	}
}
