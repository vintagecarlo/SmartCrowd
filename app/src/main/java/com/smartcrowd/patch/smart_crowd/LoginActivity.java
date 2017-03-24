package com.smartcrowd.patch.smart_crowd;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 14/01/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOGIN_URL = "http://"+Config.URL+"/api/login";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static String hold1 = "";
    public static String hold2 = "";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String EMAIL_SHARED_PREF = "username";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView registerHere;
    private Button buttonLogin;
    private String username;
    private String password;
    private SessionManager session;
    private SQLiteHandler db;
    //private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        editTextUsername = (EditText) findViewById(R.id.editText);
        editTextPassword = (EditText) findViewById(R.id.editText3);
        registerHere = (TextView) findViewById(R.id.textView4);
        registerHere.setOnClickListener(this);
        buttonLogin = (Button) findViewById(R.id.button);
        buttonLogin.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }*/

    private void userLogin() {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if(response.trim().equals("success")){
                            openProfile();
                        }else{
                           // Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this,"DIRI ra ko kutob!",Toast.LENGTH_LONG).show();
                        }*/
                        if(response.trim().equals("failure, Credentials doesnt match!")){
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals("Account deactivated")){
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }else{
                            hold1 = response;
                            //userId.setId(hold);
                            //Toast.makeText(LoginActivity.this,hold,Toast.LENGTH_LONG).show();
                          /*  SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                            editor.putString(EMAIL_SHARED_PREF, username);
                            editor.commit();*/
                            splituser(hold1);
                            db.addUser(username,hold1);
                            db.addID(username,hold2);
                            session.setLogin(true);
                            openProfile();
                        }/*else{
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME,username);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void splituser(String splits) {
        String[] user = splits.split("/tidirt/");
        hold1 = user[0];
        hold2 = user[1];
    }

    private void openProfile(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            userLogin();
        }else if(view == registerHere){
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerIntent);
        }
    }
}
