package android.iwamin.charilog.entity;

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

	public CyclingRecord(int id, long dateRaw, String date, String startTime, String endTime, long totalTime, int distance, double aveSpeed, double maxSpeed) {
		this.id = id;
		this.dateRaw = dateRaw;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalTime = totalTime;
		this.distance = distance;
		this.aveSpeed = aveSpeed;
		this.maxSpeed = maxSpeed;
	}

	public double getAveSpeed() {
		return aveSpeed;
	}

	public long getDateRaw() {
		return dateRaw;
	}

	public String getDate() {
		return date;
	}

	public int getDistance() {
		return distance;
	}

	public String getEndTime() {
		return endTime;
	}

	public int getId() {
		return id;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public String getStartTime() {
		return startTime;
	}

	public long getTotalTime() {
		return totalTime;
	}

	@Override
	public String toString() {
		return "CyclingRecord{" +
				"id=" + id +
				", dateRaw=" + dateRaw +
				", date='" + date + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", totalTime=" + totalTime +
				", distance=" + distance +
				", aveSpeed=" + aveSpeed +
				", maxSpeed=" + maxSpeed +
				'}';
	}
}
