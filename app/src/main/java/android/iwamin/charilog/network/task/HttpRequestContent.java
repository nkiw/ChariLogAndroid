package android.iwamin.charilog.network.task;

import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class HttpRequestContent {
	private URL url;
	private String body;
}
