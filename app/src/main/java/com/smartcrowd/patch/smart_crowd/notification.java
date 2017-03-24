package com.smartcrowd.patch.smart_crowd;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 12/02/2017.
 */

public class notification extends Service {
    private SQLiteHandler db;
    String holder;
    String title,contens,names;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // return super.onStartCommand(intent, flags, startId);
        getNotification();
        return Service.START_STICKY;
    }
    private void getNotification() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.verify_url+holder,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(getContext(), (CharSequence) response,Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        serverNotif(response);
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

    private void serverNotif(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
           /* String x =  Integer.toString(array.length());
            Toast.makeText(this,x,Toast.LENGTH_SHORT).show();*/
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                names = json.getString("name");
                title = json.getString("title");
                contens = json.getString("content");
            } catch (JSONException e) {;
                e.printStackTrace();
            }
            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle(names).setContentText(contens).
                    setContentTitle(title).setSmallIcon(R.drawable.images).build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(i, notify);
        }
    }
}
