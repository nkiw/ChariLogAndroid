package android.charilog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class GPSData {
	private long dateRaw;
	private long time;
	private double latitude;
	private double longitude;
	private double altitude;
}
