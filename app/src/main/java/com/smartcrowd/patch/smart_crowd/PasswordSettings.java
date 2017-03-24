package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 11/02/2017.
 */
public class PasswordSettings extends AppCompatActivity implements View.OnClickListener {
    EditText oldP;
    EditText newP;
    EditText confirmNewP;
    Button updatePassword;
    private ProgressDialog loading;
    String holder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_settings_layout);
        oldP = (EditText) findViewById(R.id.editText15);
        newP = (EditText) findViewById(R.id.editText16);
        confirmNewP = (EditText) findViewById(R.id.editText17);
        updatePassword = (Button) findViewById(R.id.button3);
        Bundle b = getIntent().getExtras();
        holder = b.getString("id");
        updatePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        updateUserPassword();
    }

    private void updateUserPassword() {
        final String oldpass = oldP.getText().toString().trim();
        final String newpass = newP.getText().toString().trim();
        String confirmpass = confirmNewP.getText().toString().trim();
        if((newpass.equals(""))&&(confirmpass.equals(""))&&(oldpass.equals(""))){
            oldP.setError("Fill in this field");
            newP.setError("Fill in this field");
            confirmNewP.setError("Fill in this field");
        }else if((newpass.equals(""))&&(confirmpass.equals(""))){
            confirmNewP.setError("Fill in this field");
            newP.setError("Fill in this field");
        }else if(newpass.equals(confirmpass)){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.update_password_url+holder,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(PasswordSettings.this,response,Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PasswordSettings.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("oldpass",oldpass);
                    params.put("newpass",newpass);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else{
            confirmNewP.setError("Password doesnt match !");
        }
    }
    @Override
    public void onBackPressed() {
        super.finish();
    }
}
