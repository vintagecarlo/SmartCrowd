package com.smartcrowd.patch.smart_crowd;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by user on 25/02/2017.
 */

public class BetaMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    GPSTracker gps;
    UiSettings mapSettings;
    String lat,longs;
    double latitudes,longitudes,latt,lng;
    String strAddress = "";
    String strt,brngy,cty;
    String brngycty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gps = new GPSTracker(this);
        if(gps.canGetLocation()){

            latitudes = gps.getLatitude();
            longitudes = gps.getLongitude();
            lat = new Double(latitudes).toString();
            longs = new Double(longitudes).toString();

           // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitudes + "\nLong: " + longitudes, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        Geocoder gc = new Geocoder(this);
        if(gc.isPresent()){
            try {
                List<Address> list = gc.getFromLocationName(CardAdapter.temp,1);
                try {
                    Address address = list.get(0);
                    // Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
                    latt = address.getLatitude();
                    lng = address.getLongitude();
                     String comb = Double.toString(latt)+","+Double.toString(lng);
                      Toast.makeText(this, comb, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if((latt == 0.0)||(lng == 0.0)){
            String[] loc = CardAdapter.temp.split(",");
            strt = loc[0];
            brngy = loc[1];
            cty = loc[2];
            brngycty = brngy+","+cty;
            if(gc.isPresent()){
                try {
                    List<Address> list = gc.getFromLocationName(brngycty,1);
                    try {
                        Address address = list.get(0);
                      //  Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
                        latt = address.getLatitude();
                        lng = address.getLongitude();
                        //String comb = Double.toString(latt)+","+Double.toString(lng);
                        // Toast.makeText(this, comb, Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LatLng sydney = new LatLng(latt,lng);
            //String comb = Double.toString(latt)+","+Double.toString(lng);
            //Toast.makeText(this, comb, Toast.LENGTH_LONG).show();
            if(gc.isPresent()){
                try {
                    List<Address> list = gc.getFromLocation(latt,lng,1);
                    for(Address address : list) {
                        for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            strAddress += address.getAddressLine(i)+",";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mapSettings = mMap.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setTiltGesturesEnabled(true);
            mapSettings.setRotateGesturesEnabled(true);
            mapSettings.setMapToolbarEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            drawCircle(sydney);
            mMap.addMarker(new MarkerOptions().position(sydney).title(strAddress)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 16.0f));
        }else {
            if(gc.isPresent()){
                try {
                    List<Address> list = gc.getFromLocationName(CardAdapter.temp,1);
                    try {
                        Address address = list.get(0);
                       // Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
                        latt = address.getLatitude();
                        lng = address.getLongitude();
                       // String comb = Double.toString(latt)+","+Double.toString(lng);
                      //  Toast.makeText(this, comb, Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LatLng sydney = new LatLng(latt, lng);
           // String comb = Double.toString(latt)+","+Double.toString(lng);
           // Toast.makeText(this, comb, Toast.LENGTH_LONG).show();
            if(gc.isPresent()){
                try {
                    List<Address> list = gc.getFromLocation(latt,lng,1);
                    for(Address address : list) {
                        for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            strAddress += address.getAddressLine(i)+",";
                            //Toast.makeText(this, strAddress, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mapSettings = mMap.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setTiltGesturesEnabled(true);
            mapSettings.setRotateGesturesEnabled(true);
            mapSettings.setMapToolbarEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            drawCircle(sydney);
            mMap.addMarker(new MarkerOptions().position(sydney).title(strAddress)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 16.0f));
        }
        //String comb = Double.toString(latt)+","+Double.toString(lng);
       //Toast.makeText(this, comb, Toast.LENGTH_LONG).show();


    }
    private void drawCircle( LatLng location ) {
        CircleOptions options = new CircleOptions();
        options.center( location );
        //Radius in meters
        options.radius( 200 );
        options.fillColor( getResources()
                .getColor( R.color.fillcolor ) );
        options.strokeColor( getResources()
                .getColor( R.color.colorAccent ) );
        options.strokeWidth( 10 );
        mMap.addCircle(options);
    }
    @Override
    public void onBackPressed() {
        super.finish();
    }
}
