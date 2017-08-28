package com.ahmadnursalim.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadnursalim.android.popularmovies.DataModel.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TrailerAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private ArrayList<Trailer> trailerList;

    public TrailerAdapter(Context mContext, ArrayList<Trailer> trailerList) {
        this.mContext = mContext;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.trailerList = trailerList;
    }

    public void remove() {
        if (trailerList.size() > 0) {
            trailerList.clear();
        }
    }

    public void add(ArrayList<Trailer> newTrailerList) {
        remove();
        this.trailerList = newTrailerList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Trailer getItem(int i) {
        return trailerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootview = view;
        ViewHolder viewHolder;

        if (view == null) {
            rootview = mInflater.inflate(R.layout.item_trailer, viewGroup, false);
            viewHolder = new ViewHolder(rootview);
            rootview.setTag(viewHolder);
        }

        final Trailer trailer = getItem(i);

        viewHolder = (ViewHolder) rootview.getTag();

        String yt_thumbnail_url = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Picasso.with(getmContext()).load(yt_thumbnail_url).into(viewHolder.imageView);

        viewHolder.nameView.setText(trailer.getName());

        return rootview;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView nameView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }
}
