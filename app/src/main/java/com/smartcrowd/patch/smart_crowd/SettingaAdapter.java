package com.smartcrowd.patch.smart_crowd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by user on 11/02/2017.
 */
public class SettingaAdapter extends RecyclerView.Adapter<SettingaAdapter.ViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(AppSettings item);

    }

    List<AppSettings> appsetting;
    private Context context;
    private final OnItemClickListener listener;


    public SettingaAdapter(List<AppSettings> appsetting, OnItemClickListener listener) {
        this.appsetting = appsetting;
        this.listener = listener;
    }

    @Override
    public SettingaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SettingaAdapter.ViewHolder holder, int position) {
        holder.bind(appsetting.get(position), listener);
        AppSettings setApp = appsetting.get(position);
        holder.iv.setImageResource(setApp.getIv());
        holder.settings.setText(setApp.getSettingsname());
    }

    @Override
    public int getItemCount() {
         return appsetting.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView settings;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imageset);
            settings = (TextView) itemView.findViewById(R.id.textDesc);

        }

        public void bind(final AppSettings item, final OnItemClickListener listener) {
            final String temp = item.getSettingsname();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }

    /*Context context;
    ArrayList<AppSettings> list = new ArrayList<AppSettings>();
    LayoutInflater inflate;

    public SettingaAdapter(ArrayList<AppSettings> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Settings settings = null;
        if(convertView == null){
            convertView = inflate.inflate(R.layout.settings_layout,null);
            settings = new Settings();
            settings.iv = (ImageView) convertView.findViewById(R.id.imageView2);
            settings.settingsname = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(settings);
        }else settings = (Settings) convertView.getTag();
        settings.settingsname.setText(list.get(position).getSettingsname());
        settings.iv.setImageResource(list.get(position).getIv());
        return convertView;
    }
    private static class Settings{
        ImageView iv;
        TextView settingsname;
    }*/

}
