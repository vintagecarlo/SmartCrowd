package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by user on 24/02/2017.
 */
public class AlphaMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    private ClusterManager<MyItem> mClusterManager;
    private GoogleMap mMap;
    GPSTracker gps;
    UiSettings mapSettings;
    String lat,longs;
    double latitudes,longitudes;
    String strAddress = "";
    String address,title1,content1,id,status;
    double latt, lng;
    String strt,brngy,cty;
    String brngycty;
    String titleall;
    TextView tv;
    public static float me;
    ArrayList<Listaddress> addresslist;
    EditText search;
    Button searchadd;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        search = (EditText) findViewById(R.id.searchadd);
        searchadd = (Button) findViewById(R.id.button);
        searchadd.setOnClickListener(this);
         tv = new TextView(this);
        gps = new GPSTracker(this);
        addresslist = new ArrayList<>();
        if(gps.canGetLocation()){

            latitudes = gps.getLatitude();
            longitudes = gps.getLongitude();
            //lat = new Double(latitudes).toString();
           // longs = new Double(longitudes).toString();

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitudes + "\nLong: " + longitudes, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        getData();
    }
    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.FEED_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                address = json.getString("location");
                title1 = json.getString("title");
                content1 = json.getString("content");
                id = json.getString("id");
                status = json.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addresslist.add(new Listaddress(address));
            geocodeready(address,title1,content1,id,status);
        }

    }

    private void geocodeready(String addresser, String title1, String content1,String id,String status) {
        final float COORDINATE_OFFSET = 0.00002f;
        String comps;
        double newlat,newlong;
        LatLng newone;
        Geocoder gc = new Geocoder(this);
       /* if(gc.isPresent()){
            try {
                List<Address> list = gc.getFromLocationName(addresser,1);
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
                        Toast.makeText(this, strAddress, Toast.LENGTH_LONG).show();
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
        mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(title1)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 16.0f));*/
        // Add a marker in Sydney and move the camera
        if(gc.isPresent()){
            try {
                List<Address> list = gc.getFromLocationName(addresser,1);
                try {
                    Address address = list.get(0);
                    // Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();
                    latt = address.getLatitude();
                    lng = address.getLongitude();
                    //String comb = Double.toString(latt)+","+Double.toString(lng);
                    //Toast.makeText(this, comb, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if((latt == 0.0)||(lng == 0.0)){
            String[] loc = addresser.split(",");
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
            titleall = title1+"\n"+content1;

            me = new Float(id);
            mapSettings = mMap.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setTiltGesturesEnabled(true);
            mapSettings.setRotateGesturesEnabled(true);
            mapSettings.setMapToolbarEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            /*for(int i = 0;i < addresslist.size();i++){
                comps = addresslist.get(i).getAddress();
                if(comps.equals("")){
                    Toast.makeText(this,"HI BITCH!", Toast.LENGTH_LONG).show();
                    break;
                }else if(comps.equals(addresser)){
                    *//*Toast.makeText(this,"Hi list"+comps, Toast.LENGTH_LONG).show();
                    Toast.makeText(this,"HI address!"+addresser, Toast.LENGTH_LONG).show();*//*
                    newlat = latt * COORDINATE_OFFSET;
                    newlong = lng * COORDINATE_OFFSET;
                    newone = new LatLng(newlat,newlong);
                    if(status.equals("unverified_2")) {
                        //Toast.makeText(this, newone.toString(), Toast.LENGTH_LONG).show();
                        mMap.addMarker(new MarkerOptions().position(newone).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
                    }else{
                       // Toast.makeText(this, newone.toString(), Toast.LENGTH_LONG).show();
                        mMap.addMarker(new MarkerOptions().position(newone).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newone));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newlat, newlong), 16.0f));
                }
            }*/
           if((status.equals("unverified_2"))||(status.equals("unverified"))) {
                mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
            }else if(status.equals("event")){
               mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
           }else{
                mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 16.0f));
            mMap.setOnInfoWindowClickListener(this);

        }else {
            if(gc.isPresent()){
                try {
                    List<Address> list = gc.getFromLocationName(addresser,1);
                    try {
                        Address address = list.get(0);
                        latt = address.getLatitude();
                        lng = address.getLongitude();

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
            titleall = title1+"\n"+content1;
            me = new Float(id);
            mapSettings = mMap.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setTiltGesturesEnabled(true);
            mapSettings.setRotateGesturesEnabled(true);
            mapSettings.setMapToolbarEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
           /* for(int i = 0;i < addresslist.size();i++){
                comps = addresslist.get(i).getAddress();
                if(comps.equals("")){
                    Toast.makeText(this,"HI BITCH!", Toast.LENGTH_LONG).show();
                    break;
                }else if(comps.equals(addresser)){
                   *//* Toast.makeText(this,"Hi list"+comps, Toast.LENGTH_LONG).show();
                    Toast.makeText(this,"HI address!"+addresser, Toast.LENGTH_LONG).show();*//*
                    newlat = latt * COORDINATE_OFFSET;
                    newlong = lng * COORDINATE_OFFSET;
                    newone = new LatLng(newlat,newlong);
                    if((status.equals("unverified_2"))||(status.equals("unverified"))) {
                        //Toast.makeText(this, newone.toString(), Toast.LENGTH_LONG).show();
                        mMap.addMarker(new MarkerOptions().position(newone).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
                    }else{
                        //Toast.makeText(this, newone.toString(), Toast.LENGTH_LONG).show();
                        mMap.addMarker(new MarkerOptions().position(newone).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newone));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newlat, newlong), 16.0f));
                }
            }*/
            if((status.equals("unverified_2"))||(status.equals("unverified"))) {
                mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
            }else if(status.equals("event")){
                mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
            }else{
                mMap.addMarker(new MarkerOptions().position(sydney).title(addresser).snippet(titleall).zIndex(me).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 16.0f));
            mMap.setOnInfoWindowClickListener(this);
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
                List<Address> list = gc.getFromLocation(latitudes,longitudes,1);
                for(Address address : list) {
                    for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        strAddress += address.getAddressLine(i)+",";
                        Toast.makeText(this, strAddress, Toast.LENGTH_LONG).show();
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
        LatLng sydney = new LatLng(latitudes, longitudes);
        mMap.addMarker(new MarkerOptions().position(sydney).title(strAddress).snippet("You are here!")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudes, longitudes), 14.0f));
        mClusterManager = new ClusterManager<MyItem>(this,mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            for (MyItem item : items) {
                LatLng position = item.getPosition();
                double lat = position.latitude + offset;
                double lng = position.longitude + offset;
                MyItem offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if(marker.getZIndex() == 0){
            new AlertDialog.Builder(this)
                    .setTitle(marker.getTitle())
                    .setMessage(marker.getSnippet())
                    .setMessage(marker.getSnippet())
                    .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle(marker.getTitle())
                    .setMessage(marker.getSnippet())
                    .setMessage(marker.getSnippet())
                    .setNeutralButton("Click here for more details", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String pass = Float.toString(marker.getZIndex());
                                Intent intent = new Intent(AlphaMap.this, Unverified.class);
                                String[] pass1 = pass.split("\\.");
                                String finalpass = pass1[0];
                                String dummy = pass1[1];
                                Toast.makeText(getApplicationContext(), finalpass, Toast.LENGTH_SHORT).show();
                                Bundle b = new Bundle();
                                b.putString("postid", finalpass);
                                intent.putExtras(b);
                                startActivity(intent);
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        float temp = BitmapDescriptorFactory.HUE_GREEN;
        double latt1 = 0, lng1 = 0;
        LatLng addresssearch = null;
        String strAddress1 = "";
        String address1 = search.getText().toString().trim();
        if (v == searchadd) {
            if (address1.equals("")) {
                Toast.makeText(this, "Input Address to Search", Toast.LENGTH_LONG).show();
            } else {
                Geocoder gc = new Geocoder(this);
                if (gc.isPresent()) {
                    try {
                        List<Address> list = gc.getFromLocationName(address1, 1);
                        try {
                            Address address = list.get(0);
                            latt1 = address.getLatitude();
                            lng1 = address.getLongitude();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addresssearch = new LatLng(latt1, lng1);
                }
                if (gc.isPresent()) {
                    try {
                        List<Address> list = gc.getFromLocation(latt1, lng1, 1);
                        for (Address address : list) {
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                strAddress1 += address.getAddressLine(i) + ",";
                                Toast.makeText(this, strAddress1, Toast.LENGTH_LONG).show();
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
                if(marker == null){
                    marker = mMap.addMarker(new MarkerOptions().position(addresssearch).title(strAddress1).icon(BitmapDescriptorFactory.defaultMarker(temp)));
                }else{
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(addresssearch).title(strAddress1).icon(BitmapDescriptorFactory.defaultMarker(temp)));
                }
                // mMap.addMarker(new MarkerOptions().position(addresssearch).title(strAddress1).icon(BitmapDescriptorFactory.defaultMarker(temp))).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(addresssearch));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt1, lng1), 14.0f));
            }
        }
    }
}
