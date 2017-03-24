package com.smartcrowd.patch.smart_crowd;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 18/02/2017.
 */
public class Reports extends Fragment {
    private SwipeRefreshLayout swipeContainer;
    private List<CardContent> feedcard;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    public String status;
    private SQLiteHandler db;
    String holder;
    String title,contens,names;
    public Reports(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_holder_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerVie2w);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        // Configure the refreshing colors
        db = new SQLiteHandler(getActivity().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holder = user.get("uid");
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Initializing our superheroes list
        feedcard = new ArrayList<>();

        /*feedcard.add(new CardContent(R.drawable.download,"Hannah jade carungay","22 years old","Namatay dinhi","nilayat kay wala sugta sa uyab dyes ang takos",R.drawable.report_menu,"PARDO dinhi sa Cebu","asdasd"));
        feedcard.add(new CardContent(R.drawable.download,"Hannah jade carungay","23 years old","Namatay dinhi","nilayat kay wala sugta sa uyab dyasdasdasdafffes ang takos",R.drawable.report_menu,"PARDO dasdasdinhi sa Cebu","asasddasd"));
        feedcard.add(new CardContent(R.drawable.report_menu,"Hannah jade carungay","22 years old","Namatay dinhsadsadi","nilayat kay wala sugta sa asdauyab dyes ang takos",R.drawable.report_menu,"PARDO dasdddinhi sa Cebu","asdasd"));
        adapter = new CardAdapter(getContext(),feedcard);*/

        //Adding adapter to recyclerview
        // recyclerView.setAdapter(adapter);
        getData();
        // getNotification();
        Toast.makeText(getContext(), CardAdapter.temp,Toast.LENGTH_LONG).show();
        return view;
    }
    private void getNotification() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Loading Data", "Please wait...",false,false);

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
            NotificationManager notif=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification.Builder
                    (getActivity().getApplicationContext()).setContentTitle(names).setContentText(contens).
                    setContentTitle(title).setSmallIcon(R.drawable.images).build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(i, notify);
        }
    }

    private void getData(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.PROF_FEED_URL+CardAdapter.temp,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(getContext(), (CharSequence) response,Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        feedcard.clear();
        for(int i = 0; i<array.length(); i++) {
            CardContent cards = new CardContent();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                //Toast.makeText(getContext(),"NI agi ko diri part 1",Toast.LENGTH_LONG).show();
                cards.setUser_id(json.getString("uid"));
                cards.setProfileImageUrl(json.getString("userpic"));
                cards.setName(json.getString("name"));
                cards.setRating(json.getString("rating"));
                cards.setTitle(json.getString("title"));
                cards.setContent(json.getString("content"));
                cards.setContentImageUrl(json.getString("postpic"));
                cards.setLocation(json.getString("location"));
                cards.setTags(json.getString("tags"));
                cards.setStatus(json.getString("status"));
                cards.setRateText(json.getString("type"));
                status = json.getString("status");
            } catch (JSONException e) {
                //Toast.makeText(getContext(),"NI agi ko diri part 3",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            //Toast.makeText(getContext(),"NI agi ko diri part 2",Toast.LENGTH_LONG).show();
            //
            feedcard.add(cards);
            swipeContainer.setRefreshing(false);
        }

        //Finally initializing our adapter
        //Toast.makeText(getContext(),"HI!",Toast.LENGTH_LONG).show();
        adapter = new CardAdapter(feedcard,getContext());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
