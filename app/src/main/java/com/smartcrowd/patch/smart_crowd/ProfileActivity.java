package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 28/01/2017.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
   /* TextView email;
    TextView fname;
    TextView lname;
    TextView mname;
    TextView rating;
    TextView uid;
    private ProgressDialog loading;
    String id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        email = (TextView) findViewById(R.id.textView2);
        fname = (TextView) findViewById(R.id.textView5);
        lname = (TextView) findViewById(R.id.textView6);
        mname = (TextView) findViewById(R.id.textView7);
        rating = (TextView) findViewById(R.id.textView8);
        uid = (TextView) findViewById(R.id.textView9);
       // Intent intent = getIntent();
//        textView.setText("Welcome User " + intent.getStringExtra(LoginActivity.KEY_USERNAME));
        //id_user = intent.getStringExtra(CardAdapter.id123);
        //Toast.makeText(this,CardAdapter.id123,Toast.LENGTH_LONG).show();
        getData();
    }
    private void getData() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url = Config.PROF_URL+CardAdapter.temp;
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
    private void showJSON(String response){
        String emailAdd="";
        String firstname="";
        String lastname = "";
        String middlename = "";
        String userrating = "";
        String userId = "";
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
                userrating = e.getString(Config.KEY_RATING);
                userId = e.getString(Config.KEY_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.email.setText(emailAdd);
        this.fname.setText(firstname);
        this.lname.setText(lastname);
        this.mname.setText(middlename);
        this.rating.setText(userrating);
        this.uid.setText(userId);
    }*/
    NetworkImageView coverphoto;
    NetworkImageView profilephoto;
    TextView name;
    TextView email;
    TextView rating1;
    TextView address;
    TextView ver;
    TextView unver;
    TextView ttl;
    TextView rateme;
    RatingBar rate;
    TextView typehidden;
    private ProgressDialog loading;
    private ImageLoader imageLoader;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
   // private SwipeRefreshLayout swipeContainer;
    private List<CardContents> feedcards;
    AlertDialog alertDialog1;
    String type;
    private SQLiteHandler db;
    String holders;
    String idholder;
    String myrate;
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
       rateme = (TextView) findViewById(R.id.rateme);
       typehidden= (TextView) findViewById(R.id.typehidden);
       recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView2);
       recyclerView.setHasFixedSize(true);
       layoutManager = new LinearLayoutManager(this);
       recyclerView.setLayoutManager(layoutManager);
       feedcards = new ArrayList<>();
       rateme.setOnClickListener(this);
       db = new SQLiteHandler(getApplicationContext());
       HashMap<String, String> user = db.getUserDetails();
       String email = user.get("email");
       holders = user.get("uid");
       HashMap<String, String> user1 = db.getUserDID();
       String email1 = user1.get("email");
       idholder = user1.get("userid");
       //rate.setOnTouchListener(this);
       loadprofdata();
       getData();
   }

    private void getData() {
        //final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);
        //Toast.makeText(this,Config.PROF_FEED_URL+CardAdapter.temp,Toast.LENGTH_LONG).show();
        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.PROF_FEED_URL+CardAdapter.temp,
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
        String url = Config.PROF_URL+CardAdapter.temp;
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
                type = e.getString("type");
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
        this.profilephoto.setImageUrl(profImage, imageLoader);
        this.address.setText(address);
        this.ver.setText(ver);
        this.unver.setText(unver);
        this.ttl.setText(ttl);
        this.rate.setRating(new Integer(rating));
        this.typehidden.setText(type);
        //this.coverphoto.setImageUrl(coverImage, imageLoader);
        if((type.equals("government"))||type.equals("government")) {
            rate.setVisibility(View.GONE);
            typehidden.setVisibility(View.VISIBLE);
            rateme.setVisibility(View.GONE);
        }else if(CardAdapter.temp.equals(idholder)){
            rateme.setVisibility(View.GONE);
        }else {
            rate.setVisibility(View.VISIBLE);
            typehidden.setVisibility(View.GONE);
        }
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
            rating.setNumStars(5);
            rating.setStepSize(1);
            rating.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout parent = new LinearLayout(this);
            parent.setGravity(Gravity.CENTER);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            parent.addView(rating);
            popDialog.setIcon(android.R.drawable.btn_star_big_on);
            popDialog.setTitle("Rate me!! ");
            popDialog.setView(rating);

            // Button OK
            popDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rateme.setText(rating.getProgress());
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

    /*private void rateuser() {

    }*/

    @Override
    public void onClick(View v) {
        if(v == rateme) {
            final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
            final RatingBar rating = new RatingBar(this);
            rating.setMax(5);
            rating.setNumStars(5);

            popDialog.setIcon(android.R.drawable.btn_star_big_on);
            popDialog.setTitle("Rate me!! ");
            popDialog.setView(rating);

            // Button OK
            popDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rate.setRating(rating.getProgress());
                            myrate = Integer.toString(rating.getProgress());
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.rate_url + CardAdapter.temp,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", idholder);
                                    params.put("rating", myrate);
                                    return params;
                                }

                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
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
            rating.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }
}
