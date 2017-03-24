package com.smartcrowd.patch.smart_crowd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 26/02/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Context context;
    List<Post> post;
    String hold1,hold2;
    String majorhold = "";
    double latitudes,longitudes;
    String title,contents,tags,image;
    GPSTracker gps;
    ProgressDialog loading;
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
    String holder = "";
    String holderid= "";
    private SQLiteHandler db;
    String type;


    public PostAdapter(Context context, List<Post> post) {
        this.context = context;
        this.post = post;
    }
    public interface OnClickImageListener{
        void onClick();
    }
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_two, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        db = new SQLiteHandler(context.getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holder = user.get("uid");
        //Toast.makeText(getContext(), holder , Toast.LENGTH_LONG).show();
        getType();
        getdata();
        return viewHolder;
    }
    private void getType() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(context,"Loading Data", "Please wait...",false,false);

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void knowtype(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
           /* String x =  Integer.toString(array.length());
            Toast.makeText(this,x,Toast.LENGTH_SHORT).show();*/
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                type = json.getString("type");
            } catch (JSONException e) {;
                e.printStackTrace();
            }
        }
    }
    private void getdata() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(context,"Loading Data", "Please wait...",false,false);

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        final Post p = post.get(position);
    }
    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button postnow,geltoc;
        public EditText postTitle;
        public EditText postContent;
        public EditText postTag;
        public EditText city;
        public EditText brgy;
        public EditText street;
        public ImageView postimage;
        public ViewHolder(View itemView) {
            super(itemView);
            postnow = (Button) itemView.findViewById(R.id.button4);
            postimage = (ImageView) itemView.findViewById(R.id.imageView);
            postTitle = (EditText) itemView.findViewById(R.id.editText11);
            postContent = (EditText) itemView.findViewById(R.id.editText12);
            postTag = (EditText) itemView.findViewById(R.id.editText13);
            city = (EditText) itemView.findViewById(R.id.city);
            brgy = (EditText) itemView.findViewById(R.id.brgy);
            street = (EditText) itemView.findViewById(R.id.street);
            postnow.setOnClickListener(this);
            postimage.setOnClickListener(this);
            city.setOnClickListener(this);
            brgy.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            gps = new GPSTracker(context);
            if(v == city) {
                cityAjax();
            }else if(v == brgy){
                brgyAjax();
            }
            else if(v == postimage){

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                context.startActivity(Intent.createChooser(intent,"Select Picture"));
                Uri filePath = intent.getData();
                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                    postimage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(v == postnow){
                if(type.equals("deactivated")){
                    Toast.makeText(context,"You are not able to post because your account is deactivated", Toast.LENGTH_LONG).show();
                }else if(type.equals("user")){
                    postreport();
                }
            }
        }
        private void brgyAjax() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            final ProgressDialog loading = ProgressDialog.show(context,"Loading Data", "Please wait...",false,false);

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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
        private void postreport() {
            //seeString();
            title = postTitle.getText().toString().trim();
            contents = postContent.getText().toString().trim();
            tags = postTag.getText().toString().trim();
            final String streets = street.getText().toString().trim();
            final String finaladdress = brngyPush;
            //Showing the progress dialog
            if(loading !=null)
            {
                loading = null;
            }
            loading = ProgressDialog.show(context,"Uploading...","Please wait...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.POST_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                            //Showing toast message of the response
                            Toast.makeText(context, s , Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();

                            //Showing toast
                            Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //image = getStringImage(bitmap);
                    //Getting Image Name
                    //String name = editTextName.getText().toString().trim();
                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();
                    //Adding parameters
                    // params.put("image", image);
                    params.put("uid",holder);
                    params.put("title", title);
                    params.put("body",contents);
                    params.put("tags",tags);
                    params.put("loc_id",brgy_id_controller);
                    params.put("muni",select_brgy);
                    params.put("street",streets);
                    params.put("type",type);
                    //params.put();
                    //returning parameters
                    return params;
                }
            };
            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            //Adding request to the queue
            requestQueue.add(stringRequest);
            // clearPost();
        }
        private void seeString() {
            title = postTitle.getText().toString().trim();
            contents = postContent.getText().toString().trim();
            tags = postTag.getText().toString().trim();
            Toast.makeText(context, title+" "+contents+" "+tags, Toast.LENGTH_SHORT).show();
        }
        public String getStringImage(Bitmap bmp){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }


    }

}
