package com.example.dmk.appservice;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class MyService extends Service implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String ACTION_MYSERVICE = "com.example.dmk.appservice.RESPONSE";
    public final String LOG_TAG = "myLog";

    private Location mLastLocation;

    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 3000; // 10 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static final String CUR_LOC = "current_location";
    public static final String CUR_LONG = "current_longitude";
    public static final String CUR_LAT = "current_latitude";

    private DataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabaseHelper = new DataBaseHelper(this, "mydatabase.db", null, 1);

        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        Log.d(LOG_TAG, "MyService on create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(LOG_TAG, "MyService on start command");

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }



        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();



        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        Log.d(LOG_TAG, "MyService on destroy");
    }

    private void displayLocation() {
        Log.d(LOG_TAG, "displayLocation");
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_MYSERVICE);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intentUpdate.putExtra(CUR_LOC, latitude + ", " + longitude);
            intentUpdate.putExtra(CUR_LAT,latitude);
            intentUpdate.putExtra(CUR_LONG,longitude);
            sendBroadcast(intentUpdate);

            new MyAsyncTask().execute(Double.toString(latitude), Double.toString(longitude));
                    Log.d(LOG_TAG, latitude + ", " + longitude);

            Cursor cursor = mSqLiteDatabase.query(DataBaseHelper.DATABASE_TABLE, new String[] {DataBaseHelper.LAT,
                            DataBaseHelper.LONG, DataBaseHelper.TST},
                    null, null,
                    null, null, null) ;

            cursor.moveToLast();

            String _lat = cursor.getString(cursor.getColumnIndex(DataBaseHelper.LAT));
            String _long = cursor.getString(cursor.getColumnIndex(DataBaseHelper.LONG));
            String _tst = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TST));

            cursor.close();

            Log.d(LOG_TAG, _lat + ":" + _long + " " + _tst);

        } else {
            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_MYSERVICE);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intentUpdate.putExtra(CUR_LOC, "(Couldn't get the location. Make sure location is enabled on the device)");
            sendBroadcast(intentUpdate);
            Log.d(LOG_TAG, "(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        Log.d(LOG_TAG, "buildGoogleApiClient");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.d(LOG_TAG, "Error GooglePlayServicesUtil " + Integer.toString(resultCode));

            } else {
                Log.d(LOG_TAG, "checkPlayServices");
                onDestroy();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {

            Log.d(LOG_TAG, "startLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);


    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        Log.d(LOG_TAG,"onConnected");
        displayLocation();

        startLocationUpdates();


    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.d(LOG_TAG,"onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        Log.d(LOG_TAG,"onLocationChanged");

        // Displaying the new location on UI
        displayLocation();
    }





    class MyAsyncTask extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {

            ContentValues newValues = new ContentValues();

            newValues.put(DataBaseHelper.LAT, params[0]);
            newValues.put(DataBaseHelper.LONG, params[1]);
            newValues.put(DataBaseHelper.TST, new Date().toString());

            mSqLiteDatabase.insert(DataBaseHelper.DATABASE_TABLE, null, newValues);
            PolyService pService;
            Log.d(LOG_TAG, "start Retrofit");
            
            pService = new ApiFactory.getPolyService();
            Log.d(LOG_TAG, "call Data");
            Call<List<RestData>> call = pService.somedata("ololo");
            try {
                Log.d(LOG_TAG, "try Execute");

                List<RestData> data = call.execute().body();


                for (RestData d : data) {
                    Log.d(LOG_TAG, d.mKey + " - " + d.mValue);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



    }
}
