package com.example.ok.task.Map;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ok.task.R;
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
    TextView Location;
    LatLng l1 = null, l = null;
    Button Way;
    Marker marker = null, PostionMark = null;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //cheack network connection
        Way = (Button) findViewById(R.id.inter);
        Location = (TextView) findViewById(R.id.Location);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            flag = true;
        } else {
            Toast.makeText(MainActivity.this, "من فضلك تحقق من الاتصال بالانترنت", Toast.LENGTH_LONG).show();
            finish();
        }
//////////////////////////////////////////////////////////////////////////////////

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//LocationManger
//cheack gps
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
///////////////////////////////////add map fragment to activity////////////////////////////////////////////////

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap = googleMap;

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//maptype
///to be in egypt only
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(30.045206, 31.236495))
                    .zoom(10)
                    .bearing(0)
                    .tilt(45)
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

//permision
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1000 * 60 * 1, this);//update ever 10meter
                l1 = new LatLng(location.getLatitude(), location.getLongitude());
                // System.out.println(l1+"tg;rlthrht");
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 11);
                    System.out.println(addresses + "jkgbjgjhg");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                PostionMark = mMap.addMarker(new MarkerOptions()
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
            } else {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);//if location return null
            }
            //add marker dynamic
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                public void onMapClick(LatLng point) {
                    l = new LatLng(point.latitude, point.longitude);
                    try {
                        addresses = geocoder.getFromLocation(point.latitude, point.latitude, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (marker != null) {
                        marker.remove(); //remove to add only one marker
                    }

                    marker = mMap.addMarker(new MarkerOptions()
                            .position(l)
                            .title(addresses.get(0).getAddressLine(2))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1))
                            .snippet(addresses.get(0).getCountryName())
                            .visible(true)
                    );
                    Location.setText("Lat:  " + point.latitude + "\n" + "Long: " + point.longitude);

                }
            });
            Way.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Route route = new Route();
                    if (l1 != null && l != null) {
                        route.drawRoute(mMap, MainActivity.this, l1, l, false, "en");
                    } else {

                    }
                }
            });

        }
    }


    @Override
    public void onLocationChanged(Location location) {//to get location on moving
        l1 = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (PostionMark != null) {
            PostionMark.remove();
        }
        PostionMark = mMap.addMarker(new MarkerOptions()
                .position(l1)
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
