package com.smartcrowd.patch.smart_crowd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
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
 * Created by user on 18/02/2017.
 */

public class profileMainDisplay extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    NetworkImageView coverphoto;
    NetworkImageView profilephoto;
    TextView name;
    TextView email;
    TextView rating1;
    TextView address;
    TextView ver;
    TextView unver;
    TextView ttl;
    RatingBar rate;
    private ProgressDialog loading;
    private ImageLoader imageLoader;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    // private SwipeRefreshLayout swipeContainer;
    private List<CardContents> feedcards;
    private SQLiteHandler db;
    String holders;
    String idholder,image;
    AlertDialog alertDialog1;
    final CharSequence[] items = {"  "," Medium "," Hard "," Very Hard "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        //coverphoto = (NetworkImageView) findViewById(R.id.cover);
        profilephoto = (NetworkImageView) findViewById(R.id.prof_photos);
        name = (TextView) findViewById(R.id.textView1);
        rate = (RatingBar) findViewById(R.id.ratingBar1);
        address = (TextView) findViewById(R.id.textView15);
        ver = (TextView) findViewById(R.id.textView8);
        unver = (TextView) findViewById(R.id.textView9);
        ttl = (TextView) findViewById(R.id.textView11);
        profilephoto.setOnClickListener(this);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        feedcards = new ArrayList<>();
        rate.setOnTouchListener(this);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holders = user.get("uid");
        HashMap<String, String> user1 = db.getUserDID();
        String email1 = user1.get("email");
        idholder = user1.get("userid");
        loadprofdata();
        getData();
    }

    private void getData() {
        //final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);
        //Toast.makeText(this,Config.PROF_FEED_URL+CardAdapter.temp,Toast.LENGTH_LONG).show();
        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.PROF_FEED_URL+idholder,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        // Toast.makeText(this,new String(response),Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        parseData(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(this,,Toast.LENGTH_LONG).show();
                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response) {
        feedcards.clear();
        for(int i = 0; i<response.length(); i++) {
            CardContents cards = new CardContents();
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
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
            } catch (JSONException e) {
                //Toast.makeText(getContext(),"NI agi ko diri part 3",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            //Toast.makeText(getContext(),"NI agi ko diri part 2",Toast.LENGTH_LONG).show();
            //
            feedcards.add(cards);
            // swipeContainer.setRefreshing(false);
        }

        //Finally initializing our adapter
        // Toast.makeText(this,"HI!",Toast.LENGTH_LONG).show();
        adapter = new CardAdapters(feedcards,this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    private void loadprofdata() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url = Config.PROF_URL+idholder;
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
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profilephoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showJSON(String response) {
        String emailAdd="";
        String firstname="";
        String lastname = "";
        String middlename = "";
        String prof_pic = "";
        String rating= "";
        String address="";
        String ver ="";
        String unver= "";
        String ttl = "";
        //Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        try {

            JSONArray json = new JSONArray(response);
            for(int i=0;i<json.length();i++){
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(i);
                emailAdd = e.getString(Config.KEY_EMAIL);
                firstname = e.getString(Config.KEY_FNAME);
                lastname = e.getString(Config.KEY_LNAME);
                middlename = e.getString(Config.KEY_MNAME);
                prof_pic = e.getString("pic_loc");
                rating = e.getString("rating");
                address = e.getString("location");
                ver = e.getString("ver");
                unver = e.getString("unv");
                ttl = e.getString("ttl");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String profImage = Config.image_url+prof_pic;
        String coverImage = Config.image_url+prof_pic;
        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
        imageLoader.get(profImage, ImageLoader.getImageListener(profilephoto, R.mipmap.ic_launcher,R.drawable.images));
        //imageLoader.get(coverImage, ImageLoader.getImageListener(coverphoto,0,0));
        String fullname = firstname+" "+middlename+" "+lastname;
        this.name.setText(fullname);
        this.rate.setRating(new Integer(rating));
        this.profilephoto.setImageUrl(profImage, imageLoader);
        this.address.setText(address);
        this.ver.setText(ver);
        this.unver.setText(unver);
        this.ttl.setText(ttl);
        //this.coverphoto.setImageUrl(coverImage, imageLoader);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == rate){
            final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
            final RatingBar rating = new RatingBar(this);
            rating.setMax(5);

            popDialog.setIcon(android.R.drawable.btn_star_big_on);
            popDialog.setTitle("Rate me!! ");
            popDialog.setView(rating);

            // Button OK
            popDialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rate.setRating(rating.getProgress());
                            dialog.dismiss();
                        }

                    })

                    // Button Cancel
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            popDialog.create();
            popDialog.show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == profilephoto){
            showFileChooser();
            new AlertDialog.Builder(this)
                    .setTitle("Profile picture")
                    .setMessage("Change profile picture")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            senddata();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void senddata() {
         if(loading !=null)
            {
                loading = null;
            }
            loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.profpic_url+idholder,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                                Toast.makeText(profileMainDisplay.this, s, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();

                            //Showing toast
                            Toast.makeText(profileMainDisplay.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    image = getStringImage(bitmap);
                    Map<String,String> params = new Hashtable<String, String>();
                    //Adding parameters
                    params.put("image", image);
                    return params;
                }
            };
            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Adding request to the queue
            requestQueue.add(stringRequest);
            // clearPost();
    }
}
