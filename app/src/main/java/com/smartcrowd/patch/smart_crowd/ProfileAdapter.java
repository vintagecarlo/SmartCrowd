package com.smartcrowd.patch.smart_crowd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by user on 18/02/2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    List<Profiledetails> details;
    private Context context;
    private ImageLoader imageLoader;
    public static String temp;
    public ProfileAdapter(List<Profiledetails> details, Context context) {
        this.details = details;
        this.context = context;
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        Profiledetails pro = details.get(position);
        String forImage = Config.image_url+pro.getProfImage();
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(forImage, ImageLoader.getImageListener(holder.prof,R.mipmap.ic_launcher,R.drawable.images));
        holder.prof.setImageUrl(forImage,imageLoader);
        holder.name.setText(pro.getName());
        holder.rate.setRating(new Integer(pro.getRating()));
        holder.address.setText(pro.getAddress());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView prof;
        public TextView name;
        public RatingBar rate;
        public TextView address;
        public ViewHolder(View itemView) {
            super(itemView);
            prof = (NetworkImageView) itemView.findViewById(R.id.prof_photos);
            name = (TextView) itemView.findViewById(R.id.textView1);
            rate = (RatingBar) itemView.findViewById(R.id.ratingBar1);
            address = (TextView) itemView.findViewById(R.id.textView15);
        }
    }
}
