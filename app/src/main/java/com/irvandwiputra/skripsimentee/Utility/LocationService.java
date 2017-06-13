package com.irvandwiputra.skripsimentee.Utility;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by alvinoktavianus on 6/13/17.
 */

public class LocationService extends Service implements LocationListener {
    private final Context context;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    protected LocationManager locationManager;

    public LocationService(Context context) {
        this.context = context;
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!checkGPS && !checkNetwork) {
                Toast.makeText(context, "No service provider available", Toast.LENGTH_SHORT).show();
            } else {
                canGetLocation = true;
                if (checkNetwork) {
                    try {
                        //noinspection MissingPermission
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            //noinspection MissingPermission
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (checkGPS) {
                    try {
                        //noinspection MissingPermission
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            //noinspection MissingPermission
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude() {
        if (location != null) {
            return location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            return location.getLongitude();
        }
        return longitude;
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Not Enabled");
        alertDialog.setMessage("Do you wants to turn On GPS");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
