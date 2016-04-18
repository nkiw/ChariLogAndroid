package android.iwamin.charilog.network.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class HttpResponseContent {
	private int responseCode;
	private String responseMessage;
	private String body;
}
