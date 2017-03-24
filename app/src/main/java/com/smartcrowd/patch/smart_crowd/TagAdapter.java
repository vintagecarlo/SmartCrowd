package com.smartcrowd.patch.smart_crowd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by user on 05/03/2017.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> implements Filterable{
    List<Tags> tagscontent;
    List<Tags> tagscontent1;
    List<Tags> tagscontent2;
    String tagholder;
    private Context context;
    private CustomFilter mFilter;
    public TagAdapter(List<Tags> tagscontent, Context context) {
        this.tagscontent = tagscontent;
        this.context = context;
    }

    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagAdapter.ViewHolder holder, int position) {
        Tags tagscontent1 = tagscontent.get(position);
        holder.tags.setText(tagscontent1.getTag());
    }

    @Override
    public int getItemCount() {
        return tagscontent.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tags;
        public ViewHolder(View itemView) {
            super(itemView);
            tags = (TextView) itemView.findViewById(R.id.tagpop);
        }
    }

    private class CustomFilter extends Filter {
        private TagAdapter mAdapter;

        public CustomFilter(TagAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            tagscontent1.clear();
            final FilterResults results = new FilterResults();
            if(constraint.length() == 0){
                tagscontent1.addAll(tagscontent);
            }else{
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Tags mWords : tagscontent) {
                    if (mWords.getTag().toLowerCase().startsWith(filterPattern)) {
                        tagscontent1.add(mWords);
                    }
                }
            }
            results.values = tagscontent1;
            results.count = tagscontent1.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
