package android.iwamin.charilog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CyclingRecord {

	private int id;
	private long dateRaw;
	private String date;
	private String startTime;
	private String endTime;
	private long totalTime;
	private int distance;
	private double aveSpeed;
	private double maxSpeed;
}
