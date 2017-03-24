package com.smartcrowd.patch.smart_crowd;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
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
import java.util.Map;


/**
 * Created by user on 14/01/2017.
 */
/*public class FourFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView lv;
   // RecyclerView recyclerView;
    SettingaAdapter adapter;
    ArrayList<AppSettings> list = new ArrayList<AppSettings>();
    private SQLiteHandler db;
    private SessionManager session;
    String holder;
    String status;
    String type;
    public FourFragment() {
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
        View view= inflater.inflate(R.layout.fragment_one, container, false);
        //recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lv = (ListView) view.findViewById(R.id.list);
        list.clear();
        status = "Deactivate My Account";
        list.add(new AppSettings(android.R.drawable.spinner_dropdown_background,"Your Profile"));
        list.add(new AppSettings(R.drawable.download,"Account Settings"));
        list.add(new AppSettings(R.drawable.download,"Location Settings"));
        list.add(new AppSettings(R.drawable.download,"Password Settings"));
        list.add(new AppSettings(R.drawable.download,status));
        list.add(new AppSettings(R.drawable.download,"Log out"));
        adapter = new SettingaAdapter(list,getContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holder = user.get("uid");
        getType();
        return view;
    }

    private void getType() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL+holder,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(getContext(), (CharSequence) response,Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        knowtype(response);
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

    private void knowtype(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
           *//* String x =  Integer.toString(array.length());
            Toast.makeText(this,x,Toast.LENGTH_SHORT).show();*//*
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                type = json.getString("type");
            } catch (JSONException e) {;
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch(position){
                case 0:
                    Toast.makeText(getContext(),"1st", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Intent intent = new Intent(getContext(), AccountSettings.class);
                    Bundle b = new Bundle();
                    b.putString("id",holder);
                    intent.putExtras(b);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(getContext(),"3rd", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Intent intent2 = new Intent(getContext(), PasswordSettings.class);
                    Bundle b2 = new Bundle();
                    b2.putString("id",holder);
                    intent2.putExtras(b2);
                    startActivity(intent2);
                    break;
                case 4:
                    deactivate();
                    if(type.equals("user")){
                        status = new String("Deactivate My Account");
                    }else if(type.equals("deactivated")) {
                        status = new String("Activate My Account");
                    }
                    break;
                case 5:
                    logoutUser();
                    break;
            }

    }

    private void deactivate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.deactivate_url+holder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
}*/
public class FourFragment extends Fragment {
    private List<AppSettings> applist;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SQLiteHandler db;
    private SessionManager session;
    String holder;
    String status;
    String type;
    final CharSequence[] items = {"Deactivate"," Activate"};
    AlertDialog levelDialog;
    public FourFragment() {
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
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        applist = new ArrayList<>();
        applist.add(new AppSettings(R.drawable.ic_person_black_24dp,"Your Profile"));
        applist.add(new AppSettings(R.drawable.ic_search_black_24dp,"Search Posts"));
        applist.add(new AppSettings(R.drawable.ic_settings_black_24dp,"Account Settings"));
        applist.add(new AppSettings(R.drawable.ic_location_on_black_24dp,"Location Settings"));
        applist.add(new AppSettings(R.drawable.ic_map_black_24dp,"View Map"));
        applist.add(new AppSettings(R.drawable.ic_vpn_key_black_24dp,"Password Settings"));
        applist.add(new AppSettings(R.drawable.ic_perm_identity_black_24dp,"Deactivate My Account"));
        applist.add(new AppSettings(R.drawable.ic_keyboard_tab_black_24dp,"Log out"));
        adapter = new SettingaAdapter(applist, new SettingaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppSettings item) {
                if(item.getSettingsname().equals("Search Posts")){
                    Intent intent = new Intent(getContext(),SearchPost.class);
                    startActivity(intent);
                }else if(item.getSettingsname().equals("Your Profile")){
                   // Toast.makeText(getContext(),"1st", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), profileMainDisplay.class);
                    startActivity(intent);
                }else if(item.getSettingsname().equals("Account Settings")){
                    Intent intent = new Intent(getContext(), AccountSettings.class);
                    Bundle b = new Bundle();
                    b.putString("id",holder);
                    intent.putExtras(b);
                    startActivity(intent);
                }else if(item.getSettingsname().equals("Location Settings")){
                    Intent intent = new Intent(getContext(), LocationUser.class);
                    startActivity(intent);
                }else if(item.getSettingsname().equals("View Map")){
                    Intent intent = new Intent(getContext(), AlphaMap.class);
                    startActivity(intent);
                }else if(item.getSettingsname().equals("Password Settings")){
                    Intent intent2 = new Intent(getContext(), PasswordSettings.class);
                    Bundle b2 = new Bundle();
                    b2.putString("id",holder);
                    intent2.putExtras(b2);
                    startActivity(intent2);
                }else if(item.getSettingsname().equals("Deactivate My Account")){
                    /*deactivate();
                    if(type.equals("user")){
                        status = new String("Deactivate My Account");
                    }else if(type.equals("deactivated")) {
                        status = new String("Activate My Account");
                    }*/
                    // Creating and Building the Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Account");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {


                            switch(item)
                            {
                                case 0:
                                    // Your code when first option seletced
                                    deactivate();
                                    break;
                                case 1:
                                    // Your code when 2nd  option seletced
                                    break;
                            }
                            levelDialog.dismiss();
                        }
                    });
                    levelDialog = builder.create();
                    levelDialog.show();
                }else if(item.getSettingsname().equals("Log out")){
                    logoutUser();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holder = user.get("uid");
        getType();
        return view;
    }

    private void deactivate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.deactivate_url+holder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getType() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL+holder,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(getContext(), (CharSequence) response,Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        knowtype(response);
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

    private void knowtype(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                type = json.getString("type");
            } catch (JSONException e) {;
                e.printStackTrace();
            }
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


}