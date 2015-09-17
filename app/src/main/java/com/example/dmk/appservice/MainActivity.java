package com.example.dmk.appservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends Activity {

    public static final String AUTH_PREFERENCES = "asettings";
    public static final String AUTH_LOGIN_KEY = "loginC";
    public static final String AUTH_PASS_KEY = "passC";
    public static final String AUTH_TOKEN_KEY = "tokenC";
    private SharedPreferences spAuth;

    private Button buttonStart;
    private Button buttonStop;
    private TextView textView;
    private MyBroadcastReceiver myBroadcastReceiver;
    private Marker curMarker;
    private GoogleMap googleMap;

    /*private void createMapView(){

        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();


                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.d("mapApp", exception.toString());
        }
    }*/

   /* private void addMarker(){


        if(null != googleMap){



            curMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(0, 0))
                            .title("Marker")
                            .draggable(true)
            );

        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBroadcastReceiver = new MyBroadcastReceiver();
        myBroadcastReceiver.link(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter(MyService.ACTION_MYSERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        if(!isAuth()){
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivityForResult(intent, 0);
        }


       /* buttonStart = (Button) findViewById(R.id.buttonStart);
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



        createMapView();
        addMarker();*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                startService(new Intent(MainActivity.this, MyService.class).putExtra(AUTH_LOGIN_KEY, data.getStringExtra(AuthActivity.CLOGIN)).putExtra(AUTH_PASS_KEY, data.getStringExtra(AuthActivity.CPASS)));
            }
        }
    }

    public boolean isAuth(){
        spAuth = getSharedPreferences(AUTH_PREFERENCES, MODE_PRIVATE);
        if(spAuth.contains(AUTH_TOKEN_KEY))
            return true;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            if(intent.hasExtra(MyService.CUR_LOC)) {
                String curLocation = intent.getStringExtra(MyService.CUR_LOC);
                double curLong = intent.getDoubleExtra(MyService.CUR_LONG, 34);
                double curLat = intent.getDoubleExtra(MyService.CUR_LAT, 34);
                thisActivity.textView.setText(curLocation);

                thisActivity.curMarker.setPosition(new LatLng(curLat, curLong));
            }

            if(intent.hasExtra(MyService.ERR_GET_TOKEN)){
                Toast t =  Toast.makeText(thisActivity.getApplicationContext(),
                        intent.getStringExtra(MyService.ERR_GET_TOKEN),
                        Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }

        }
    }

}
