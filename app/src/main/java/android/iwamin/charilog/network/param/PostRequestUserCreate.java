package android.iwamin.charilog.network.param;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class PostRequestUserCreate {
	private static final String JSON_NAME_USER_ID = "userId";
	private static final String JSON_NAME_PASSWORD = "password";

	private URL url;
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
