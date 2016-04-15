package android.iwamin.charilog.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
public class ConnectionInfo {
	String url;
	String userId;
	String password;
	String deviceId;
}
