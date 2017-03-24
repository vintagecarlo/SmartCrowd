package com.smartcrowd.patch.smart_crowd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 05/03/2017.
 */
public class SearchPost extends AppCompatActivity implements TextWatcher {
    EditText search;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    // private SwipeRefreshLayout swipeContainer;
   private SimpleItemRecyclerViewAdapter mAdapter;
    private static List<Tags> posttags;
    private static List<Tags> posttagfilter;
    private ProgressDialog loading;
    private SQLiteHandler db;
    String holders;
    String idholder,image;
    public static String x;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchpost_layout);
        search = (EditText) findViewById(R.id.search);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        posttags = getTagst();
        posttagfilter = new ArrayList<Tags>();
        posttagfilter.addAll(posttags);
       // posttags = new ArrayList<>();
        mAdapter = new SimpleItemRecyclerViewAdapter(posttagfilter, new SimpleItemRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tags item) {
                for(int i = 0;i<posttagfilter.size();i++){
                    if(item.getTag().equals(posttagfilter.get(i).getTag())){
                        x = item.getTag();
                        Intent intent = new Intent(SearchPost.this,TagPost.class);
                        Bundle b = new Bundle();
                        b.putString("tag",x);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        holders = user.get("uid");
        HashMap<String, String> user1 = db.getUserDID();
        String email1 = user1.get("email");
        idholder = user1.get("userid");
        search.addTextChangedListener(this);
        //getData();
    }
    private List<Tags> getTagst(){
        List<Tags> listV = new ArrayList<Tags>();
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);
        //Toast.makeText(this,Config.PROF_FEED_URL+CardAdapter.temp,Toast.LENGTH_LONG).show();
        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.tag_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //Toast.makeText(this,response.toString(),Toast.LENGTH_LONG).show();
                        //calling method to parse json array
                        posttags.clear();
                        for(int i = 0; i<response.length(); i++) {
                            Tags tagcards = new Tags();
                            JSONObject json = null;
                            try {
                                json = response.getJSONObject(i);
                                tagcards.setTag(json.getString("tag"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            posttags.add(tagcards);
                        }
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
        return listV;
    }
    private void getData() {
      final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);
        //Toast.makeText(this,Config.PROF_FEED_URL+CardAdapter.temp,Toast.LENGTH_LONG).show();
        //Creating a json array request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.tag_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                       loading.dismiss();
                         //Toast.makeText(this,response.toString(),Toast.LENGTH_LONG).show();
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
        posttags.clear();
        for(int i = 0; i<response.length(); i++) {
            Tags tagcards = new Tags();
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                tagcards.setTag(json.getString("tag"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            posttags.add(tagcards);
        }
        adapter = new TagAdapter(posttags,this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {
        public interface OnItemClickListener {

            void onItemClick(Tags item);

        }
        private List<Tags> mValues;
        private CustomFilter mFilter;
        private final OnItemClickListener listener;
        public SimpleItemRecyclerViewAdapter(List<Tags> mValues,OnItemClickListener listener) {
            this.mValues = mValues;
            mFilter = new CustomFilter(SimpleItemRecyclerViewAdapter.this);
            this.listener = listener;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.bind(mValues.get(position), listener);
            Tags gs = mValues.get(position);
            holder.tags.setText(gs.getTag());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
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
            public void bind(final Tags item, final OnItemClickListener listener) {
                final String temp = item.getTag();
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }
    }

    private static class CustomFilter extends Filter{
        private SimpleItemRecyclerViewAdapter mAdapter;
        public CustomFilter(SimpleItemRecyclerViewAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            posttagfilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                posttagfilter.addAll(posttags);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Tags mWords : posttags) {
                    if (mWords.getTag().toLowerCase().contains(filterPattern)) {
                        posttagfilter.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + posttagfilter.size());
            results.values = posttagfilter;
            results.count = posttagfilter.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<Tags>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
