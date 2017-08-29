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


class TrailerAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private ArrayList<Trailer> trailerList;
    private final TrailerAdapterOnClickHandler onClickHandler;

    interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    TrailerAdapter(Context mContext, ArrayList<Trailer> trailerList, TrailerAdapterOnClickHandler onClickHandler) {
        this.mContext = mContext;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.trailerList = trailerList;
        this.onClickHandler = onClickHandler;
    }

    private void remove() {
        if (trailerList.size() > 0) {
            trailerList.clear();
        }
    }

    void add(ArrayList<Trailer> newTrailerList) {
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

    private Context getmContext() {
        return mContext;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootview = view;
        ViewHolder viewHolder;

        final int position = i;

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

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trailer selectedMovie = trailerList.get(position);

                onClickHandler.onClick(selectedMovie);
            }
        });

        return rootview;
    }

    private static class ViewHolder {
        final ImageView imageView;
        final TextView nameView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }
}
