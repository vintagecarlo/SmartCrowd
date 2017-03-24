package com.smartcrowd.patch.smart_crowd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 14/01/2017.
 */
public class RegisterActivity  extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText userFname;
    EditText userLname;
    EditText userMname;
    EditText userUsername;
    EditText userPassword;
    EditText userMobilenumber;
    EditText userEmailaddress;
    EditText city;
    EditText brgy;
    EditText street;
    EditText birthday;
    Button userRegister;
    String userBday = "";
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    //Arraylist for Municipality
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
    String streetPush = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        userFname = (EditText) findViewById(R.id.editText1);
        userLname = (EditText) findViewById(R.id.editText3);
        userMname = (EditText) findViewById(R.id.editText2);
        userPassword = (EditText) findViewById(R.id.editText5);
        userEmailaddress = (EditText) findViewById(R.id.editText7);
        city = (EditText) findViewById(R.id.city);
        brgy = (EditText) findViewById(R.id.brgy);
        street = (EditText) findViewById(R.id.street);
        birthday = (EditText) findViewById(R.id.birthday);
        userRegister = (Button) findViewById(R.id.button);
        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        /*Drawable dr = getResources().getDrawable(R.drawable.download);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 20, 20, true));
        city.setCompoundDrawablesWithIntrinsicBounds(null,null,d,null);
        brgy.setCompoundDrawablesWithIntrinsicBounds(null,null,d,null);*/
        userRegister.setOnClickListener(this);
        birthday.setOnClickListener(this);
        city.setOnClickListener(this);
        brgy.setOnClickListener(this);
        getData();
    }
    private void register() {
        final String fname = userFname.getText().toString().trim();
        final String mname = userMname.getText().toString().trim();
        final String lname = userLname.getText().toString().trim();
        final String password = userPassword.getText().toString().trim();
        final String emailadd = userEmailaddress.getText().toString().trim();
        final String streets = street.getText().toString().trim();
        final String userBOD = userBday;
        final String finaladdress = brngyPush;
       /* Toast.makeText(RegisterActivity.this,finaladdress,Toast.LENGTH_LONG).show();
        Toast.makeText(RegisterActivity.this,streets,Toast.LENGTH_LONG).show();*/
        //userBday = year+"-"+month+"-"+day;
        //Toast.makeText(RegisterActivity.this,userBday,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fname",fname);
                params.put("mname",mname);
                params.put("lname",lname);
                params.put("password",password);
                params.put("email",emailadd);
                params.put("bday",userBOD);
                params.put("loc_id",brgy_id_controller);
                params.put("street",streets);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getData(){
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


    @Override
    public void onClick(View v) {
        if(v == userRegister) {
            if(userFname.equals("")){
                userFname.setError("Fill in this field");
            }else if(userMname.equals("")){
                userMname.setError("Fill in this field");
            }else if(userFname.equals("")){
                userFname.setError("Fill in this field");
            }else if(userPassword.equals("")){
                userPassword.setError("Fill in this field");
            }else if((userEmailaddress.equals(""))||!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmailaddress.getText().toString().trim()).matches()){
                userEmailaddress.setError("Fill in this field");
            }else if(userBday.equals("")){
                Toast.makeText(this,"Provide you birthday",Toast.LENGTH_SHORT).show();
            }else {
                register();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }else if(v == birthday){
            userboddate();
        }else if(v == city){
            cityAjax();
        }else if(v == brgy){
            brgyAjax();
        }
    }
    private void userboddate(){
        datePickerDialog = DatePickerDialog.newInstance(RegisterActivity.this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#009688"));
        datePickerDialog.setTitle("Select Date From DatePickerDialog");
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }
    private void brgyAjax() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Select Barangay");
        builder.setSingleChoiceItems(brngy.toArray(new String[brngy.size()]), -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                /*switch(item)
                {

                }*/
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Select City");
        builder.setSingleChoiceItems(items.toArray(new String[items.size()]), -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                /*switch(item)
                {
                    case 0:
                        select_brgy = ctid.get(0);
                        citifi.get(0);
                        city.setText(items.get(0));
                        loadBarangay();
                        break;
                    case 1:
                        select_brgy = ctid.get(1);
                        citifi.get(1);
                        city.setText(items.get(1));
                        loadBarangay();
                        break;
                    case 2:
                        select_brgy = ctid.get(2);
                        citifi.get(2);
                        city.setText(items.get(2));
                        loadBarangay();
                        break;
                    case 3:
                        select_brgy = ctid.get(3);
                        citifi.get(3);
                        city.setText(items.get(3));
                        loadBarangay();
                        break;
                    case 4:
                        select_brgy = ctid.get(4);
                        citifi.get(4);
                        city.setText(items.get(4));
                        loadBarangay();
                        break;
                    case 5:
                        select_brgy = ctid.get(5);
                        citifi.get(5);
                        city.setText(items.get(5));
                        loadBarangay();
                        break;
                    case 6:
                        select_brgy = ctid.get(6);
                        citifi.get(6);
                        city.setText(items.get(6));
                        loadBarangay();
                        break;
                    case 7:
                        select_brgy = ctid.get(7);
                        citifi.get(7);
                        city.setText(items.get(7));
                        loadBarangay();
                        break;
                    case 8:
                        select_brgy = ctid.get(8);
                        citifi.get(8);
                        city.setText(items.get(8));
                        loadBarangay();
                        break;

                }*/

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

    private void citygetfromserver(JSONArray array) {
        for(int i = 0; i<array.length(); i++) {
           /* String x =  Integer.toString(array.length());
            Toast.makeText(this,x,Toast.LENGTH_SHORT).show();*/
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        userBday = date;
        birthday.setText(userBday);
       // Toast.makeText(this,userBday,Toast.LENGTH_SHORT).show();
    }
}
