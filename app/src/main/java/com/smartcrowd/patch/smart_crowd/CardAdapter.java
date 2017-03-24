package com.smartcrowd.patch.smart_crowd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 21/01/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private ImageLoader imageLoader;
    List<CardContent> contentsCard;
    private Context context;
    private SQLiteHandler db;
    String holders;
    String idholder;
    public static String temp;
    public String status1;
    public String rate,type,idid;
    public String postid2,postid1;
    String sched,sched1,sched2;
    String[] schedsplit;
    String times,time2;
    String[] time;

    public CardAdapter(List<CardContent> contentsCard, Context context) {
        this.contentsCard = contentsCard;
        this.context = context;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedcard_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        db = new SQLiteHandler(v.getContext().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holders = user.get("uid");
        HashMap<String, String> user1 = db.getUserDID();
        String email1 = user1.get("email");
        idholder = user1.get("userid");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CardAdapter.ViewHolder holder, int position) {

       final CardContent usercontent = contentsCard.get(position);
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
        holder.rating.setRating(new Integer(usercontent.getRating()));
        holder.textrate.setText(usercontent.getRateText());
        holder.contentTitle.setText(usercontent.getTitle());
        holder.postContent.setText(usercontent.getContent());
        holder.location.setText(usercontent.getLocation());
        holder.tags.setText(usercontent.getTags());
        holder.userid.setText(usercontent.getUser_id());
        holder.status.setText(usercontent.getStatus());
        holder.postid.setText(usercontent.getPost_id());
        status1 = usercontent.getStatus();
        rate = usercontent.getRating();
        type = usercontent.getRateText();
        idid = usercontent.getUser_id();
        postid1 = usercontent.getPost_id();
        if(status1.equals("unverified_2")){
            holder.status.setText("unverified");
            holder.status.setTextColor(Color.parseColor("#c74d38"));
        }else if(status1.equals("unverified")){
            holder.status.setTextColor(Color.parseColor("#c74d38"));
        }
        if((type.equals("admin"))||(type.equals("government"))){
            holder.textrate.setVisibility(View.VISIBLE);
            holder.rating.setVisibility(View.GONE);
        }else if((type.equals("user"))){
            holder.rating.setVisibility(View.VISIBLE);
            holder.textrate.setVisibility(View.GONE);
        }
        if((idid.equals(idholder))){
            holder.option.setVisibility(View.VISIBLE);
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context,holder.option);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_user);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    postid2 = holder.postid.getText().toString().trim();
                                    new AlertDialog.Builder(context)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Delete")
                                            .setMessage("Are you sure you want delete this post?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    delete(postid2);

                                                }

                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                    break;
                                case R.id.menu2:
                                    //handle menu2 click
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }else if(((type.equals("government"))||(status1.equals("event")))){
            final String url = Config.post_map_url+postid1;
            holder.option.setVisibility(View.VISIBLE);
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context,holder.option);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu2:
                                    //handle menu2 click
                                    break;
                                case R.id.menu3:
                                        getCal(url);
                                        Calendar cal = Calendar.getInstance();
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        intent.putExtra("beginTime", time2);
                                        intent.putExtra("allDay", false);
                                        intent.putExtra("rrule", "FREQ=DAILY");
                                        intent.putExtra("endTime", times);
                                        intent.putExtra("title", usercontent.getContent());
                                        context.startActivity(intent);
                                        break;
                                    }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        } else{
            holder.option.setVisibility(View.GONE);
        }
    }

    private void getCal(String url) {
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        try{
            JSONArray json = new JSONArray(response);
            for(int i=0;i<json.length();i++){
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = json.getJSONObject(i);
                sched = e.getString("sched");
                //Toast.makeText(context,sched,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*schedsplit = sched.split("-");
        sched1 = schedsplit[0];
        time = sched1.split("T");
        time2 = time[1];
        sched = schedsplit[1];
        time = sched.split("T");
        times = time[1];*/
    }

    private void delete(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.delete_url+url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",idholder);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return contentsCard.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        public TextView option;
        public TextView postid;
        public ViewHolder(View itemView) {
            super(itemView);
            profileView = (NetworkImageView) itemView.findViewById(R.id.person_photo);
           contentView = (NetworkImageView) itemView.findViewById(R.id.person_postphoto);
            userName = (TextView) itemView.findViewById(R.id.person_name);
            rating = (RatingBar) itemView.findViewById(R.id.person_rate);
            textrate = (TextView) itemView.findViewById(R.id.person_rate1);
            contentTitle = (TextView) itemView.findViewById(R.id.person_post_title);
            postContent = (TextView) itemView.findViewById(R.id.person_post_content);
            location = (TextView) itemView.findViewById(R.id.person_post_location);
            tags = (TextView) itemView.findViewById(R.id.person_tag);
            userid = (TextView) itemView.findViewById(R.id.idhidden);
            status = (TextView) itemView.findViewById(R.id.status);
            option = (TextView) itemView.findViewById(R.id.textViewOptions);
           postid = (TextView) itemView.findViewById(R.id.idhiddenpost);
            userName.setOnClickListener(this);
            profileView.setOnClickListener(this);
            location.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if(v == userName){
                 temp = userid.getText().toString().trim();
                //Toast.makeText(v.getContext(),type,Toast.LENGTH_LONG).show();
                //Toast.makeText(v.getContext(),temp,Toast.LENGTH_SHORT).show();
                Toast.makeText(v.getContext(),idid,Toast.LENGTH_LONG);
                Intent profile = new Intent(v.getContext(), ProfileActivity.class);
                //profile.putExtra("uid", id123);
                v.getContext().startActivity(profile);
            }else if(v == profileView){
                temp = userid.getText().toString().trim();
                //Toast.makeText(v.getContext(),temp,Toast.LENGTH_SHORT).show();
                Intent profile = new Intent(v.getContext(), ProfileActivity.class);
                //profile.putExtra("uid", id123);
                v.getContext().startActivity(profile);
            }else if(v == location){
                temp = location.getText().toString().trim();
                //Toast.makeText(v.getContext(),temp,Toast.LENGTH_SHORT).show();
                Intent profile = new Intent(v.getContext(), BetaMap.class);
                v.getContext().startActivity(profile);
            }
        }
    }
}
