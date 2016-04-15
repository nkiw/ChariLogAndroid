package android.iwamin.charilog.network.param;

import android.iwamin.charilog.entity.CyclingRecord;

import java.net.URL;

import lombok.Data;

@Data
public class CyclingRecordPostTaskParam {
	private URL url;
	private CyclingRecord record;
}
