package com.ahmadnursalim.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahmadnursalim.android.popularmovies.DataModel.Review;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {
    public ReviewAdapter(Context mContext, ArrayList<Review> reviewList) {
        this.mContext = mContext;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.reviewList = reviewList;
    }

    private final Context mContext;
    private final LayoutInflater mInflater;
    private ArrayList<Review> reviewList;

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Review getItem(int i) {
        return reviewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clear() {
        if (reviewList.size() > 0) {
            reviewList.clear();
        }
        notifyDataSetChanged();
    }

    public void add(ArrayList<Review> newReviewList) {
        clear();
        this.reviewList = newReviewList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootview = view;
        ViewHolder viewHolder;

        if (view == null) {
            rootview = mInflater.inflate(R.layout.item_review, viewGroup, false);
            viewHolder = new ViewHolder(rootview);
            rootview.setTag(viewHolder);
        }

        final Review review = getItem(i);

        viewHolder = (ViewHolder) rootview.getTag();

        viewHolder.authorView.setText(review.getAuthor());
        viewHolder.contentView.setText(review.getContent());

        return rootview;
    }

    public static class ViewHolder {
        public final TextView authorView;
        public final TextView contentView;

        public ViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
        }
    }
}
