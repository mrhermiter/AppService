package com.example.dmk.appservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

    Button buttonStart;
    Button buttonStop;
    TextView textView;
    private MyBroadcastReceiver myBroadcastReceiver;
    Marker curMarker;
    GoogleMap googleMap;

    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.d("mapApp", exception.toString());
        }
    }

    private void addMarker(){

        /** Make sure that the map has been initialised **/
        if(null != googleMap){



            curMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(0, 0))
                            .title("Marker")
                            .draggable(true)
            );

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        textView = (TextView) findViewById(R.id.textView1);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });

        myBroadcastReceiver = new MyBroadcastReceiver();
        myBroadcastReceiver.link(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter(MyService.ACTION_MYSERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        createMapView();
        addMarker();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    static class MyBroadcastReceiver extends BroadcastReceiver{

        MainActivity thisActivity;

        void link(MainActivity act){
            thisActivity = act;
        }

        void unlink(){
            thisActivity = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String curLocation = intent.getStringExtra(MyService.CUR_LOC);
            double curLong = intent.getDoubleExtra(MyService.CUR_LONG,34);
            double curLat = intent.getDoubleExtra(MyService.CUR_LAT,34);
            thisActivity.textView.setText(curLocation);

            thisActivity.curMarker.setPosition(new LatLng(curLat, curLong));
            /* = new MarkerOptions()
                    .position(new LatLng(curLat, curLong))
                    .title("Marker")
                    .draggable(true);*/

        }
    }

}
