package com.smartcrowd.patch.smart_crowd;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by user on 11/02/2017.
 */

public class VerififyAdapter extends RecyclerView.Adapter<VerififyAdapter.ViewHolder> {
    private ImageLoader imageLoader;
    List<Verification> contentsCard;
    private Context context;
    public static String temp;
    private SQLiteHandler db;
    String holder = "";
    String approvepost;
    String resp;
    public String status1;
    public VerififyAdapter(List<Verification> contentsCard, Context context) {
        this.contentsCard = contentsCard;
        this.context = context;
    }

    @Override
    public VerififyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.verifycard_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        db = new SQLiteHandler(v.getContext().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holder = user.get("uid");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VerififyAdapter.ViewHolder holder, int position) {
        Verification usercontent = contentsCard.get(position);
        String forImage = Config.image_url+usercontent.getContentImageUrl();
        String profImage = Config.image_url+usercontent.getProfileImageUrl();
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(profImage, ImageLoader.getImageListener(holder.profileView, R.mipmap.ic_launcher,R.drawable.images));
        imageLoader.get(forImage, ImageLoader.getImageListener(holder.contentView,0,0));

        holder.profileView.setImageUrl(profImage, imageLoader);
        holder.contentView.setImageUrl(forImage, imageLoader);
        //holder.profileView.setImageResource(usercontent.getProfileImageUrl());
        //holder.contentView.setImageResource(usercontent.getContentImageUrl());
        holder.userName.setText(usercontent.getName());
        holder.rating.setText(usercontent.getRating());
        holder.contentTitle.setText(usercontent.getTitle());
        holder.postContent.setText(usercontent.getContent());
        holder.location.setText(usercontent.getLocation());
        holder.tags.setText(usercontent.getTags());
        holder.userid.setText(usercontent.getUser_id());
        holder.status.setText(usercontent.getStatus());
        holder.approve.setText(usercontent.getApproved());
        holder.disapprove.setText(usercontent.getDisapproved());
        holder.didnotrespond.setText(usercontent.getDidnotrespond());
        holder.psid.setText(usercontent.getPsid());
        holder.response.setText(usercontent.getResponse());
        holder.total.setText(usercontent.getTotal());
        resp = holder.response.getText().toString().trim();
        if(resp.equals("valid")){
            holder.btnapprove.setVisibility(View.GONE);
            holder.btndecline.setVisibility(View.GONE);
            holder.rtrnstmnt.setText("You Approved the post");
            holder.rtrnstmnt.setVisibility(View.VISIBLE);
            holder.rtrnstmnt.setTextColor(Color.parseColor("#c74d38"));
        }else if(resp.equals("fake")){
            holder.btnapprove.setVisibility(View.GONE);
            holder.btndecline.setVisibility(View.GONE);
            holder.rtrnstmnt.setVisibility(View.VISIBLE);
            holder.rtrnstmnt.setTextColor(Color.parseColor("#c74d38"));
        }
        status1 = usercontent.getStatus();
        if(status1.equals("unverified_2")){
            holder.status.setText("unverified");
            holder.status.setTextColor(Color.parseColor("#c74d38"));
        }else if(status1.equals("unverified")){
            holder.status.setTextColor(Color.parseColor("#c74d38"));
        }
    }

    @Override
    public int getItemCount() {
        return contentsCard.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NetworkImageView profileView;
        public NetworkImageView contentView;
        public TextView userName;
        public TextView rating;
        public TextView contentTitle;
        public TextView postContent;
        public TextView location;
        public TextView tags;
        public TextView userid;
        public TextView status;
        public TextView approve;
        public TextView disapprove;
        public TextView didnotrespond;
        public Button btnapprove;
        public  Button btndecline;
        public TextView psid;
        public TextView response;
        public TextView total;
        public TextView rtrnstmnt;
        public ViewHolder(View itemView) {
            super(itemView);
            profileView = (NetworkImageView) itemView.findViewById(R.id.person_photo);
            contentView = (NetworkImageView) itemView.findViewById(R.id.person_postphoto);
            userName = (TextView) itemView.findViewById(R.id.person_name);
            rating = (TextView) itemView.findViewById(R.id.person_rate);
            contentTitle = (TextView) itemView.findViewById(R.id.person_post_title);
            postContent = (TextView) itemView.findViewById(R.id.person_post_content);
            location = (TextView) itemView.findViewById(R.id.person_post_location);
            tags = (TextView) itemView.findViewById(R.id.person_tag);
            userid = (TextView) itemView.findViewById(R.id.idhidden);
            status = (TextView) itemView.findViewById(R.id.status);
            approve = (TextView) itemView.findViewById(R.id.person_approved_no);
            disapprove = (TextView) itemView.findViewById(R.id.person_disapproved_no);
            didnotrespond = (TextView) itemView.findViewById(R.id.person_didnotresp_no);
            btnapprove = (Button) itemView.findViewById(R.id.button5);
            btndecline = (Button) itemView.findViewById(R.id.button6);
            psid = (TextView) itemView.findViewById(R.id.idhiddenpsid);
            response = (TextView) itemView.findViewById(R.id.idhidden3);
            total = (TextView) itemView.findViewById(R.id.idhidden4);
            rtrnstmnt = (TextView) itemView.findViewById(R.id.idhidden5);
            btndecline.setOnClickListener(this);
            btnapprove.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            approvepost = psid.getText().toString().trim();

            if(v == btnapprove){
                 StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.approved_url+approvepost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(v.getContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(v.getContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
               params.put("uid",holder);
                return params;
            }

             };

        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
                btnapprove.setVisibility(View.GONE);
                btndecline.setVisibility(View.GONE);
                rtrnstmnt.setText("You Approved the post");
                rtrnstmnt.setVisibility(View.VISIBLE);
                rtrnstmnt.setTextColor(Color.parseColor("#c74d38"));
            }else if(v == btndecline){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.disapproved_url+approvepost,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(v.getContext(),response,Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(v.getContext(),error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("uid",holder);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
                btnapprove.setVisibility(View.GONE);
                btndecline.setVisibility(View.GONE);
                rtrnstmnt.setText("You Disapproved the post");
                rtrnstmnt.setVisibility(View.VISIBLE);
                rtrnstmnt.setTextColor(Color.parseColor("#c74d38"));
            }
        }

    }


}
