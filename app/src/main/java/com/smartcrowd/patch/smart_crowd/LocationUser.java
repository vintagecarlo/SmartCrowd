package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by user on 28/02/2017.
 */
public class LocationUser extends AppCompatActivity implements View.OnClickListener {
    EditText city;
    EditText brgy;
    EditText street;
    Button update;
    List<String> items = new ArrayList<String>();
    List<String> ctid = new ArrayList<String>();
    List<String> citifi = new ArrayList<String>();
    //Arraylist for Barangay
    List<String> brngy = new ArrayList<String>();
    List<String> brid = new ArrayList<String>();
    AlertDialog alertDialog1;
    String select_brgy = "";
    String brgy_id_controller = "";
    String cityPush = "";
    String brngyPush = "";
    private SQLiteHandler db;
    String holders;
    String idholder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        city = (EditText) findViewById(R.id.city);
        brgy = (EditText) findViewById(R.id.brgy);
        street = (EditText) findViewById(R.id.street);
        update = (Button) findViewById(R.id.button);
        update.setOnClickListener(this);
        city.setOnClickListener(this);
        brgy.setOnClickListener(this);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holders = user.get("uid");
        HashMap<String, String> user1 = db.getUserDID();
        String email1 = user1.get("email");
        idholder = user1.get("userid");
        getData();
    }

    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.address_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(getContext(), (CharSequence) response,Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        citygetfromserver(response);
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

    private void citygetfromserver(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
           /* String x =  Integer.toString(array.length());
            Toast.makeText(this,x,Toast.LENGTH_SHORT).show();*/
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                String cid = json.getString("id");
                String munic = json.getString("municipal");
                String citif = json.getString("citifier");
                ctid.add(cid);
                items.add(munic);
                citifi.add(citif);
            } catch (JSONException e) {;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == update){
            updateAddress();
        }else if(v == city ){
            cityAjax();
        }else if(v == brgy){
            brgyAjax();
        }
    }

    private void updateAddress() {
        final String streets = street.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.updateaddress_url+idholder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(LocationUser.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LocationUser.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("loc_id",brgy_id_controller);
                params.put("street",streets);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void brgyAjax() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationUser.this);
        builder.setTitle("Select Barangay");
        builder.setSingleChoiceItems(brngy.toArray(new String[brngy.size()]), -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                for(int x = 0; x < brngy.size()-1; x++) {
                    if (item==x) {
                        brgy_id_controller = brid.get(x);
                        brngyPush = brngy.get(x);
                        brgy.setText(brngy.get(x));
                        break;

                    }
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void cityAjax() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationUser.this);
        builder.setTitle("Select City");
        builder.setSingleChoiceItems(items.toArray(new String[items.size()]), -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                for(int x = 0; x < items.size()-1; x++) {
                    if (item==x) {
                        select_brgy = ctid.get(x);
                        citifi.get(x);
                        cityPush = items.get(x);
                        city.setText(items.get(x));
                        if(items.size()>0) {
                            brngy.clear();
                        }
                        loadBarangay();
                        break;

                    }
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void loadBarangay() {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.brgy_url+select_brgy,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(getContext(), (CharSequence) response,Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        brngygetfromserver(response);
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

    private void brngygetfromserver(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
           /* String x =  Integer.toString(array.length());
            Toast.makeText(this,x,Toast.LENGTH_SHORT).show();*/
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                String bid = json.getString("id");
                String name = json.getString("name");
                String loc_id = json.getString("loc_id");
                brngy.add(name);
                brid.add(bid);
            } catch (JSONException e) {;
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
