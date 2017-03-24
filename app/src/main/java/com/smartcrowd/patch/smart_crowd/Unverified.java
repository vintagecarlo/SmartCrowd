package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 18/02/2017.
 */
public class Unverified extends AppCompatActivity {
    public NetworkImageView profileView;
    public NetworkImageView contentView;
    public TextView userName;
    public RatingBar rating;
    public TextView textrate;
    public TextView contentTitle;
    public TextView postContent;
    public TextView location;
    public TextView tags;
    public TextView userid;
    public TextView status;
    String holder;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedcard_profile_layout);
        profileView = (NetworkImageView) this.findViewById(R.id.person_photo11);
        contentView = (NetworkImageView) this.findViewById(R.id.person_postphoto11);
        userName = (TextView) this.findViewById(R.id.person_name11);
        rating = (RatingBar) this.findViewById(R.id.person_rate11);
        textrate = (TextView) this.findViewById(R.id.person_rate111);
        contentTitle = (TextView) this.findViewById(R.id.person_post_title11);
        postContent = (TextView) this.findViewById(R.id.person_post_content11);
        location = (TextView) this.findViewById(R.id.person_post_location11);
        tags = (TextView) this.findViewById(R.id.person_tag11);
        userid = (TextView) this.findViewById(R.id.idhidden11);
        status = (TextView) this.findViewById(R.id.status11);
        Bundle b = getIntent().getExtras();
        holder = b.getString("postid");
        loadpostdata();
    }

    private void loadpostdata() {

        String url = Config.post_map_url+holder;
        //Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        String username = "";
        String rating= "";
        String textrate= "";
        String title= "";
        String content = "";
        String location= "";
        String tags = "";
        String status = "";
        String prof = "";
        String cont = "";
        try {
            JSONArray json = new JSONArray(response);
            for(int i=0;i<json.length();i++){
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(i);
                username = e.getString("name");
                rating = e.getString("rating");
                textrate = e.getString("type");
                title = e.getString("title");
                content = e.getString("content");
                location = e.getString("location");
                tags = e.getString("tags");
                status = e.getString("status");
                prof = e.getString("userpic");
                cont = e.getString("postpic");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String profImage = Config.image_url+prof;
        String coverImage = Config.image_url+cont;
        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
        imageLoader.get(profImage, ImageLoader.getImageListener(profileView, R.mipmap.ic_launcher,R.drawable.images));
        imageLoader.get(coverImage, ImageLoader.getImageListener(contentView,0,0));
        this.profileView.setImageUrl(profImage,imageLoader);
        this.contentView.setImageUrl(coverImage,imageLoader);
        this.userName.setText(username);
        this.rating.setRating(new Integer(rating));
        this.textrate.setText(textrate);
        this.contentTitle.setText(title);
        this.postContent.setText(content);
        this.location.setText(location);
        this.status.setText(status);
        this.tags.setText(tags);
        if(status.equals("unverified_2")){
            this.status.setText("unverified");
            this.status.setTextColor(Color.parseColor("#c74d38"));
        }else if(status.equals("unverified")){
            this.status.setTextColor(Color.parseColor("#c74d38"));
        }
        if((textrate.equals("admin"))||(textrate.equals("government"))){
            this.textrate.setVisibility(View.VISIBLE);
            this.rating.setVisibility(View.GONE);
        }else if((textrate.equals("user"))){
            this.rating.setVisibility(View.VISIBLE);
            this.textrate.setVisibility(View.GONE);
        }
    }
}
