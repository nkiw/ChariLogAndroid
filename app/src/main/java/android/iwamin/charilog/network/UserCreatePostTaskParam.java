package android.iwamin.charilog.network;

import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class UserCreatePostTaskParam {
	private URL url;
	private String userId;
	private String password;
}
