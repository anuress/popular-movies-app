package com.ahmadnursalim.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView movieDetailPoster;

    TextView movieDetailTitle;
    TextView movieDetailRating;
    TextView movieDetailOverview;
    TextView movieDetailReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieDetailPoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);

        movieDetailTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        movieDetailRating = (TextView) findViewById(R.id.tv_movie_detail_rating);
        movieDetailOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);
        movieDetailReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);

        Intent intent = getIntent();

        Picasso.with(this).load(intent.getStringExtra("poster")).into(movieDetailPoster);

        movieDetailTitle.setText(intent.getStringExtra("title"));
        movieDetailRating.setText(intent.getStringExtra("rating"));
        movieDetailOverview.setText(intent.getStringExtra("overview"));
        movieDetailReleaseDate.setText(intent.getStringExtra("release_date"));
    }

    // Override functionality of default back button
    // to call onSaveInstanceState when user want to go back into MainActivity (movie list)

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
