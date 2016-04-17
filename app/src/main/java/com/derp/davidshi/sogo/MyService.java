package com.derp.davidshi.sogo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;

import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service {
    private static final String TAG = "GPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 30000;
    private static final float LOCATION_DISTANCE = 10f;
    private String userID;

    private ArrayList<String> possibleMatches = new ArrayList<String>();

    private class LocationListener implements android.location.LocationListener {
        Location loc;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            loc = new Location(provider);
        }

        private GeoFire geoFire;
        private Firebase ref;

        private double locLat1;
        private double locLat2;
        private double locLong1;
        private double locLong2;

        public void onLocationChanged(Location loc) {
            try {
                geoFire = new GeoFire(new Firebase("https://incandescent-fire-7723.firebaseIO.com/"));

                locLat1 = loc.getLatitude();
                locLong1 = loc.getLongitude();

                geoFire.setLocation(userID, new GeoLocation(loc.getLatitude(), loc.getLongitude()));

                ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com");

                Log.e("GPS", "onLocation Started!");

                Random rand = new Random();
                ref.child("Changethis").setValue(rand.nextDouble());

                rand.nextDouble();

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int i = 1;
                        for(DataSnapshot childSnapShot: snapshot.child("userids").getChildren()) {
                            String KEY = (String) childSnapShot.getValue();
                            if(i == snapshot.getChildrenCount()) {
                                break;
                            }
                            i++;

                            // Making sure the user doesn't get matched with themselves
                            if(userID != KEY) {
                                geoFire.getLocation(KEY, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(String key, GeoLocation location) {
                                        if (location != null) {
                                            locLat2 = location.latitude;
                                            locLong2 = location.longitude;
                                        } else {
                                            System.out.println(String.format("There is no location for key %s in GeoFire", key));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        System.err.println("There was an error getting the GeoFire location: " + firebaseError);
                                    }
                                });
                            }

                            double distance = mapDistance(locLat1, locLong1, locLat2, locLong2);

                            if (distance > 0) {
                                possibleMatches.add(KEY);
                                sendNotif("Nearby people found!", "Click here to learn more about their goals.");
                            }
                        }

                        SharedPreferences settings;
                        SharedPreferences.Editor editor;
                        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                        editor = settings.edit();

                        if (possibleMatches.size() > 0) {
                            editor.putString("Match", possibleMatches.get(0));
                            editor.commit();
                        } else {
                            editor.putString("Match", "3ad39c85-7b72-4e14-9a9b-5f9fbeb2d0c1");
                        }
                    }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });

                } catch (Exception e) {
                    Log.i("exception: ", e.toString());
                }
            }


        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();

        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        userID = settings.getString("UserID", null);

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void sendNotif(String title, String body) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(android.R.drawable.ic_dialog_alert).setContentTitle(title).setContentText(body);

        Intent resultIntent = new Intent(this, Matches.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Matches.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private double mapDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}