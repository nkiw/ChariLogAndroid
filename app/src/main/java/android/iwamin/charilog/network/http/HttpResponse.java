package android.iwamin.charilog.network.http;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class HttpResponse {
	private int responseCode;
	private String responseMessage;
	private String body;
}
