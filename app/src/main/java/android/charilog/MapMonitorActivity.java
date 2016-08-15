package android.charilog;

import android.charilog.lib.CommonLib;
import android.charilog.monitor.CyclingMonitor;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PolylineOverlay;

public class MapMonitorActivity extends AppCompatActivity {

    private Timer timer = new Timer();
    private MapController mapController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = getLayoutInflater();
        RelativeLayout layout = (RelativeLayout)layoutInflater.inflate(R.layout.activity_map_monitor, null);
        MapView mapView = new MapView(this, getResources().getString(R.string.ymap_application_id));

        if (mapView != null) {
            MapController mapController = mapView.getMapController();
            mapController.setCenter(new GeoPoint(35665721, 139731006)); //初期表示の地図を指定
            mapController.setZoom(1);                          //初期表示の縮尺を指定

            GeoPoint tokyo = new GeoPoint(35681396, 139766049);
            GeoPoint mid = new GeoPoint(35665721, 139731006);
            PolylineOverlay polylineOverlay = new PolylineOverlay(new GeoPoint[]{tokyo, mid}){
                @Override
                protected boolean onTap(){
                    //ラインをタッチした際の処理
                    return true;
                }
            };
            mapView.getOverlays().add(polylineOverlay);

            PinOverlay pinOverlay = new PinOverlay(PinOverlay.PIN_VIOLET);
            mapView.getOverlays().add(pinOverlay);
            pinOverlay.addPoint(mid,null);

            layout.addView(mapView,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
        } else {
            Log.e("mMapView", "mMapView is null");
        }
        setContentView(layout);

        // 画面更新用の周期ハンドラ設定
        timer = new Timer();
        timer.schedule(timerTask, 500, 500);

    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendMessage(Message.obtain(handler, 0, null));
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CyclingMonitor.CyclingInfo info = CyclingMonitor.getInstance().getInfo();
            Location location = info.getLocation();

            if (location == null) {
                return;
            }

            int latitudeE6 = (int)(location.getLatitude() * 1000000);
            int longitudeE6 = (int)(location.getLongitude() * 100000);
            GeoPoint currentPoint = new GeoPoint(latitudeE6, longitudeE6);
            mapController.setCenter(currentPoint); //初期表示の地図を指定


            // 走行状況の表示
            int[] time = CommonLib.msecToHourMinSec(info.getTotalTime());
            String stringTime = "";
            if (time[0] > 0) {
                stringTime = time[0] + "時間" + time[1] + "分" + time[2] + "秒";
            } else if (time[1] > 0) {
                stringTime = time[1] + "分" + time[2] + "秒";
            } else {
                stringTime = time[2] + "秒";
            }

            int distance = info.getTotalDistance();
            String stringDistance;
            if (distance >= 1000) {
                stringDistance = ((double) distance / 1000) + " [km]";
            } else {
                stringDistance = distance + " [m]";
            }
        }
    };

}
