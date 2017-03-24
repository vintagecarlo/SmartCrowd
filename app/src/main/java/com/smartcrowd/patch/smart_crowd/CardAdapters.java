package com.smartcrowd.patch.smart_crowd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by user on 18/02/2017.
 */

public class CardAdapters extends RecyclerView.Adapter<CardAdapters.ViewHolder> {
    private ImageLoader imageLoader;
    List<CardContents> contentsCard;
    private Context context;
    public static String temp;
    public String status1;
    public String rate,type;

    public CardAdapters(List<CardContents> contentsCard, Context context) {
        this.contentsCard = contentsCard;
        this.context = context;
    }

    @Override
    public CardAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedcard_profile_layout, parent, false);
        CardAdapters.ViewHolder viewHolder = new CardAdapters.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardAdapters.ViewHolder holder, int position) {
        CardContents usercontent = contentsCard.get(position);
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
        status1 = usercontent.getStatus();
        rate = usercontent.getRating();
        type = usercontent.getRateText();
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
        public ViewHolder(View itemView) {
            super(itemView);
            profileView = (NetworkImageView) itemView.findViewById(R.id.person_photo11);
            contentView = (NetworkImageView) itemView.findViewById(R.id.person_postphoto11);
            userName = (TextView) itemView.findViewById(R.id.person_name11);
            rating = (RatingBar) itemView.findViewById(R.id.person_rate11);
            textrate = (TextView) itemView.findViewById(R.id.person_rate111);
            contentTitle = (TextView) itemView.findViewById(R.id.person_post_title11);
            postContent = (TextView) itemView.findViewById(R.id.person_post_content11);
            location = (TextView) itemView.findViewById(R.id.person_post_location11);
            tags = (TextView) itemView.findViewById(R.id.person_tag11);
            userid = (TextView) itemView.findViewById(R.id.idhidden11);
            status = (TextView) itemView.findViewById(R.id.status11);
            userName.setOnClickListener(this);
            profileView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == userName){
                temp = userid.getText().toString().trim();
                //Toast.makeText(v.getContext(),type,Toast.LENGTH_LONG).show();
                //Toast.makeText(v.getContext(),temp,Toast.LENGTH_SHORT).show();
                Intent profile = new Intent(v.getContext(), ProfileActivity.class);
                //profile.putExtra("uid", id123);
                v.getContext().startActivity(profile);
            }else if(v == profileView){
                temp = userid.getText().toString().trim();
                //Toast.makeText(v.getContext(),temp,Toast.LENGTH_SHORT).show();
                Intent profile = new Intent(v.getContext(), ProfileActivity.class);
                //profile.putExtra("uid", id123);
                v.getContext().startActivity(profile);
            }
        }


    }
}
