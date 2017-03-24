package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 11/02/2017.
 */
public class AccountSettings extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText eFname;
    EditText eLname;
    EditText eMname;
    EditText eBday;
    Button eSubmit;
    private ProgressDialog loading;
    String holder;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    public static String idholder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings_layout);
        eFname = (EditText) findViewById(R.id.editText8);
        eLname = (EditText) findViewById(R.id.editText9);
        eMname = (EditText) findViewById(R.id.editText10);
        eLname = (EditText) findViewById(R.id.editText9);
        eBday = (EditText) findViewById(R.id.editText14);
        eSubmit = (Button) findViewById(R.id.button2);
        eBday.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        holder = b.getString("id");
        eSubmit.setOnClickListener(this);
        getdata();
    }

    private void getdata() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url = Config.DATA_URL+holder;
        //Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(),error.getMessage()+"",Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        String firstname="";
        String lastname = "";
        String middlename = "";
        String bday = "";
        //Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        try {

            JSONArray json = new JSONArray(response);
            for(int i=0;i<json.length();i++){
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(i);
                firstname = e.getString(Config.KEY_FNAME);
                lastname = e.getString(Config.KEY_LNAME);
                middlename = e.getString(Config.KEY_MNAME);
                bday = e.getString("bday");
                idholder = e.getString("uid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.eFname.setText(firstname);
        this.eLname.setText(lastname);
        this.eMname.setText(middlename);
        this.eBday.setText(bday);
    }

    @Override
    public void onClick(View v) {
        if(v == eSubmit) {
            updateUserdata();
        }else if(v == eBday){
            userboddate();
        }
    }

    private void updateUserdata() {
        final String efname = eFname.getText().toString().trim();
        final String emname = eMname.getText().toString().trim();
        final String elname = eLname.getText().toString().trim();
        final String ebday = eBday.getText().toString().trim();
        Toast.makeText(AccountSettings.this,emname,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.update_url+holder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AccountSettings.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountSettings.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fname",efname);
                params.put("mname",emname);
                params.put("lname",elname);
                params.put("bday",ebday);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void userboddate(){
        datePickerDialog = DatePickerDialog.newInstance(AccountSettings.this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#009688"));
        datePickerDialog.setTitle("Select Date From DatePickerDialog");
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        eBday.setText(date);
    }
    @Override
    public void onBackPressed() {
        super.finish();
    }
}
