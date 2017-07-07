package com.ahmadnursalim.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ahmadnursalim.android.popularmovies.Utilities.JsonUtils;
import com.ahmadnursalim.android.popularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter movieAdapter;
    private ProgressBar loadingIndicator;
    private ArrayList<Movie> movieList;

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("poster", movie.getPoster());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("release_date", movie.getReleaseDate());

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        movieAdapter = new MovieAdapter(this, this);
        recyclerView.setAdapter(movieAdapter);

        loadingIndicator = (ProgressBar) findViewById(R.id.pb_movie_list_progressbar);

        // check for any savedInstanceState
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            loadMovie("popular");
        } else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
            if (movieList != null) {
                movieAdapter.setMovieList(movieList);
            }
        }
    }

    // implement savedInstanceState to save application's current state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        switch(selectedItem) {
            case R.id.action_sort_by_popularity :
                loadMovie("popular");
                break;
            case R.id.action_sort_by_rating :
                loadMovie("top_rated");
                break;
        }

        return true;
    }

    private class FetchMovie extends AsyncTask<String, Void, Movie[]> {
        NetworkUtils networkUtils = new NetworkUtils();
        JsonUtils jsonUtils = new JsonUtils();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            URL url = networkUtils.buildUrl(params[0]);

            String response;
            Movie[] parsedJson = null;

            try {
                response = networkUtils.getResponse(url);
                parsedJson = jsonUtils.parseJson(response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parsedJson;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                movieList = new ArrayList<>(Arrays.asList(movies));
                movieAdapter.setMovieList(movieList);
            }
        }
    }

    public void loadMovie(String sortBy) {
        if (isOnline()) {
            new FetchMovie().execute(sortBy);
        } else {
            Toast.makeText(this, "Please connect into internet", Toast.LENGTH_LONG).show();
        }
    }

    // check if device is connected into internet
    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
