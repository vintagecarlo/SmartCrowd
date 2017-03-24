package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 14/01/2017.
 */
public class ThreeFragment extends Fragment {
    private SwipeRefreshLayout swipeContainer;
    private List<Verification> verifycard;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SQLiteHandler db;
    String holder = "";
    String status;
    String uid;
    String resp;
    String appr,disap,didntresp;
    int total;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer1);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holder = user.get("uid");
        //Initializing our superheroes list
        verifycard = new ArrayList<>();
        getData();
        return  view;

    }
    private void getData(){
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
        verifycard.clear();
        for(int i = 0; i<array.length(); i++) {
            Verification cards = new Verification();
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
                cards.setApproved(json.getString("app"));
                cards.setDisapproved(json.getString("dis"));
                cards.setPsid(json.getString("psid"));
                cards.setResponse(json.getString("resp"));
                status = json.getString("status");
                uid = json.getString("psid");
                resp = json.getString("resp");
                appr = json.getString("app");
                disap = json.getString("dis");
                didntresp = json.getString("ttl");
                total = new Integer(didntresp)-(new Integer(appr) + new Integer(disap));
                cards.setTotal(json.getString("ttl"));;
                cards.setDidnotrespond(new Integer(total).toString());
            } catch (JSONException e) {
                //Toast.makeText(getContext(),"NI agi ko diri part 3",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            //Toast.makeText(getContext(),"NI agi ko diri part 2",Toast.LENGTH_LONG).show();
            //
            if(status.equals("unverified")){
                verifycard.add(cards);
                swipeContainer.setRefreshing(false);
            }
            swipeContainer.setRefreshing(false);
        }

        //Finally initializing our adapter
        //Toast.makeText(getContext(),"HI!",Toast.LENGTH_LONG).show();
        adapter = new VerififyAdapter(verifycard,getContext());
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
