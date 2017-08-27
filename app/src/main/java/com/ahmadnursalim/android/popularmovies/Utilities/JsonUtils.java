package com.ahmadnursalim.android.popularmovies.Utilities;

import com.ahmadnursalim.android.popularmovies.DataModel.Movie;
import com.ahmadnursalim.android.popularmovies.DataModel.Review;
import com.ahmadnursalim.android.popularmovies.DataModel.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public Movie[] parseMovieJson(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");
        Movie[] movies = new Movie[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);

            String id = object.getString("id");
            String original_title = object.getString("original_title");
            String poster_path = object.getString("poster_path");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            String vote_average = object.getString("vote_average");

            movies[i] = new Movie(id, original_title, poster_path, overview, vote_average, release_date);
        }

        return movies;
    }

    public Review[] parseReviewJson(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");
        Review[] review = new Review[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);

            String id = object.getString("id");
            String author = object.getString("author");
            String content = object.getString("content");

            review[i] = new Review(id, author, content);
        }

        return review;
    }

    public Trailer[] parseTrailerJson(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");
        Trailer[] trailer = new Trailer[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);

            String id = object.getString("id");
            String key = object.getString("key");
            String name = object.getString("name");
            String type = object.getString("type");
            String site = object.getString("site");

            trailer[i] = new Trailer(id, key, name, type, site);
        }

        return trailer;
    }
}
