package android.iwamin.charilog;

import android.iwamin.charilog.entity.CyclingRecord;
import android.iwamin.charilog.lib.CommonLib;
import android.iwamin.charilog.repository.RepositoryReader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// 走行記録一覧を表示する
		showCyclingRecordList();
	}

	private void showCyclingRecordList() {
		List<CyclingRecord> list = new RepositoryReader().getCyclingRecordList(this);

		TableLayout table = (TableLayout)findViewById(R.id.tbl_list_record);

		List<String> items = new ArrayList<>();
		items.add("ID");
//		items.add("Time(Raw)");
		items.add("日付");
		items.add("時刻");
//		items.add("End Time");
		items.add("走行時間");
		items.add("走行距離");
		items.add("平均速度");
		items.add("最高速度");

		TableRow head = new TableRow(this);
		for (String item : items) {
			TextView view = new TextView(this);
			view.setText(item + " ");
			view.setGravity(Gravity.CENTER_HORIZONTAL);
			head.addView(view);
		}
		table.addView(head);

		for (CyclingRecord record : list) {
//			Log.v("LIST", record.toString());

			TableRow row = new TableRow(this);

			List<String> columns = new ArrayList<>();
			columns.add(String.valueOf(record.getId()));
//			columns.add(String.valueOf(record.getDateRaw()));
			columns.add(record.getDate());
			columns.add(new SimpleDateFormat("HH:mm").format(record.getDateRaw()));
//			columns.add(record.getEndTime());
			int time[] = CommonLib.msecToHourMinSec(record.getTotalTime());
			columns.add(String.format("%02d:%02d:%02d", time[0], time[1], time[2]));
			columns.add(String.format("%.3f", (double)record.getDistance() / 1000) + "km");
			columns.add(String.format("%.1f", record.getAveSpeed()) + "km/h");
			columns.add(String.format("%.1f", record.getMaxSpeed()) + "km/h");

			for (String column : columns) {
				TextView view = new TextView(this);
				view.setText(column + " ");
				view.setGravity(Gravity.CENTER_HORIZONTAL);
				row.addView(view);
			}
			table.addView(row);
		}

//		// for debug.
//		long condition = list.get(list.size() - 1).getDateRaw();
//		List<GPSData> listGPSData = new RepositoryReader().getGPSDataList(this, condition);
//		for (GPSData data : listGPSData) {
//			Log.v("TEST", data.toString());
//		}
	}

}
