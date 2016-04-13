package android.iwamin.charilog.network;

import android.iwamin.charilog.entity.CyclingRecord;

import java.net.URL;

public class CyclingRecordPostTaskParam {
	private URL url;
	private CyclingRecord record;

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public CyclingRecord getRecord() {
		return record;
	}

	public void setRecord(CyclingRecord record) {
		this.record = record;
	}
}
