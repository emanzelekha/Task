package com.example.ok.task;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    Boolean flag = false;
    LocationManager lm;
    Geocoder geocoder;
    List<Address> addresses;
    int gps = 0;
    Marker marker;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            flag = true;
        } else {
            Toast.makeText(MainActivity.this, "من فضلك تحقق من الاتصال بالانترنت", Toast.LENGTH_LONG).show();
            finish();
        }


        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            gps_enabled = true;
        }


        if (!gps_enabled) {

            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS Not enabled");
            dialog.setPositiveButton("Open GPS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });


            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        } else {
            gps = 1;
            geocoder = new Geocoder(this, Locale.getDefault());
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  mMap.setMyLocationEnabled(true);

            // Show rationale and request permission.

            mMap = googleMap;

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
///to be in egypt only
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(30.045206, 31.236495))
                    .zoom(10)
                    .bearing(0)
                    .tilt(45)
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (gps == 1) {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1000 * 60 * 1, this);//update ever 10meter
                LatLng l1 = new LatLng(location.getLatitude(), location.getLongitude());
                // System.out.println(l1+"tg;rlthrht");
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 11);
                    System.out.println(addresses + "jkgbjgjhg");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                mMap.addMarker(new MarkerOptions()
                        .position(l1)

                        .title(addresses.get(0).getAddressLine(2))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4))
                        .snippet(addresses.get(0).getCountryName())
                        .visible(true)
                );


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(l1)
                        .zoom(8)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    public void onMapClick(LatLng point) {
                        LatLng l = new LatLng(point.latitude, point.longitude);
                        try {
                            addresses = geocoder.getFromLocation(point.latitude, point.latitude, 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(l)
                                .title(addresses.get(0).getAddressLine(2))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4))
                                .snippet(addresses.get(0).getCountryName())
                                .visible(true)
                        );

                    }
                });
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
        System.out.println(l + "tg;rlthrht");
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.clear();//to clear last postion
        mMap.addMarker(new MarkerOptions()
                .position(l)
                .title(addresses.get(0).getAddressLine(2))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4))
                .snippet(addresses.get(0).getCountryName())
                .visible(true)
        );
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
