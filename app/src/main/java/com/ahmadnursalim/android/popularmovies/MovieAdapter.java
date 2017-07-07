package com.ahmadnursalim.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewholder> {
    private ArrayList<Movie> movieList;
    private LayoutInflater layoutInflater;
    private Context context;
    private final MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    MovieAdapter(Context context, MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.movieList = new ArrayList<>();
        this.movieAdapterOnClickHandler = movieAdapterOnClickHandler;
    }

    @Override
    public MovieViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);

        return new MovieViewholder(view);
    }

    @Override
    public int getItemCount() {
        if (movieList != null) return movieList.size();
        return 0;
    }

    @Override
    public void onBindViewHolder(MovieViewholder holder, int position) {
        Movie movie = movieList.get(position);

        Picasso.with(context).load(movie.getPoster()).into(holder.moviePoster);
    }

    void setMovieList(ArrayList<Movie> list) {
        this.movieList.clear();
        this.movieList.addAll(list);

        notifyDataSetChanged();
    }

    class MovieViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;

        MovieViewholder(View view) {
            super(view);
            moviePoster = (ImageView) view.findViewById(R.id.movie_item);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = movieList.get(adapterPosition);

            movieAdapterOnClickHandler.onClick(selectedMovie);
        }
    }
}
