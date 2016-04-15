package android.iwamin.charilog.network.param;

import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class GetRequestCyclingRecord {
	private URL url;
}
