package com.ahmadnursalim.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadnursalim.android.popularmovies.Data.MovieContract;
import com.ahmadnursalim.android.popularmovies.DataModel.Review;
import com.ahmadnursalim.android.popularmovies.DataModel.Trailer;
import com.ahmadnursalim.android.popularmovies.Utilities.JsonUtils;
import com.ahmadnursalim.android.popularmovies.Utilities.NetworkUtils;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    ImageView movieDetailPoster;

    private TextView movieDetailTitle;
    private TextView movieDetailRating;
    private TextView movieDetailOverview;
    private TextView movieDetailReleaseDate;
    private LinearListView movieTrailers;
    private LinearListView reviewTrailers;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    Context context;

    @Override
    protected void onStart() {
        super.onStart();
        String id = getIntent().getStringExtra("id");
        new FetchTrailerTask().execute(id);
        new FetchReviewTask().execute(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        context = this;

        movieDetailPoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);

        movieDetailTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        movieDetailRating = (TextView) findViewById(R.id.tv_movie_detail_rating);
        movieDetailOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);
        movieDetailReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        movieTrailers = (LinearListView) findViewById(R.id.detail_trailers);
        reviewTrailers = (LinearListView) findViewById(R.id.detail_review);

        trailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>(), this);
        movieTrailers.setAdapter(trailerAdapter);

        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewTrailers.setAdapter(reviewAdapter);

        Intent intent = getIntent();

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("poster")).into(movieDetailPoster);

        movieDetailTitle.setText(intent.getStringExtra("title"));
        movieDetailRating.setText(intent.getStringExtra("rating"));
        movieDetailOverview.setText(intent.getStringExtra("overview"));
        movieDetailReleaseDate.setText(intent.getStringExtra("release_date"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        final MenuItem action_favorite = menu.findItem(R.id.action_favorite);

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return isFavorited(Integer.parseInt(getIntent().getStringExtra("id")));
            }

            @Override
            protected void onPostExecute(Integer isFavorited) {

                action_favorite.setTitle(isFavorited == 1 ?
                        "Unfavorite" :
                        "Favorite");
            }
        }.execute();

        return true;
    }


    // Override functionality of default back button
    // to call onSaveInstanceState when user want to go back into MainActivity (movie list)

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                final Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            case R.id.action_favorite:
                final int id = Integer.parseInt(getIntent().getStringExtra("id"));
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        return isFavorited(id);
                    }

                    @Override
                    protected void onPostExecute(Integer isFavorited) {
                        if (isFavorited == 1) {
                            new AsyncTask<Void, Void, Integer>() {
                                @Override
                                protected Integer doInBackground(Void... params) {
                                    return getApplicationContext().getContentResolver().delete(
                                            MovieContract.MovieEntry.CONTENT_URI,
                                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                            new String[]{Integer.toString(id)}
                                    );
                                }

                                @Override
                                protected void onPostExecute(Integer rowsDeleted) {
                                    item.setTitle("Favorite");
                                    Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                                }
                            }.execute();
                        }
                        else {
                            // add to favorites
                            new AsyncTask<Void, Void, Uri>() {
                                @Override
                                protected Uri doInBackground(Void... params) {
                                    Intent intent = getIntent();
                                    ContentValues values = new ContentValues();

                                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, intent.getStringExtra("id"));
                                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, intent.getStringExtra("title"));
                                    values.put(MovieContract.MovieEntry.COLUMN_IMAGE, intent.getStringExtra("poster"));
                                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, intent.getStringExtra("overview"));
                                    values.put(MovieContract.MovieEntry.COLUMN_RATING, intent.getStringExtra("rating"));
                                    values.put(MovieContract.MovieEntry.COLUMN_DATE, intent.getStringExtra("date"));

                                    return getApplicationContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                                            values);
                                }

                                @Override
                                protected void onPostExecute(Uri returnUri) {
                                    item.setTitle("Unfavorite");
                                    Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                                }
                            }.execute();
                        }
                    }
                }.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Trailer trailer) {
        String key = trailer.getKey();
        final String BASE_URL = "http://www.youtube.com/watch";
        final String PARAMS = "v";

        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAMS, key).build();

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    class FetchTrailerTask extends AsyncTask<String, Void, Trailer[]> {
        NetworkUtils networkUtils = new NetworkUtils();
        JsonUtils jsonUtils = new JsonUtils();

        @Override
        protected Trailer[] doInBackground(String... strings) {
            URL url = networkUtils.buildUrlTrailer(strings[0]);

            String response = "";
            Trailer[] parsedJson = null;

            try {
                response = networkUtils.getResponse(url);
                parsedJson = jsonUtils.parseTrailerJson(response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parsedJson;
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            super.onPostExecute(trailers);

            trailerAdapter.add(new ArrayList<>(Arrays.asList(trailers)));
        }
    }

    class FetchReviewTask extends AsyncTask<String, Void, Review[]> {
        NetworkUtils networkUtils = new NetworkUtils();
        JsonUtils jsonUtils = new JsonUtils();

        @Override
        protected Review[] doInBackground(String... strings) {
            URL url = networkUtils.buildUrlReview(strings[0]);

            String response = "";
            Review[] parsedJson = null;

            try {
                response = networkUtils.getResponse(url);
                parsedJson = jsonUtils.parseReviewJson(response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parsedJson;
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            super.onPostExecute(reviews);

            reviewAdapter.add(new ArrayList<>(Arrays.asList(reviews)));
        }
    }

    public int isFavorited(int id) {
        Cursor cursor = this.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null    // sort order
        );
        int numRows = 0;
        if (cursor != null) {
            numRows = cursor.getCount();
        }
        cursor.close();
        return numRows;
    }
}
