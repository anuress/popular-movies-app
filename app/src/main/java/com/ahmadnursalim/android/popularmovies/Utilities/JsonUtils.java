package com.ahmadnursalim.android.popularmovies.Utilities;

import com.ahmadnursalim.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public Movie[] parseJson(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");
        Movie[] movies = new Movie[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);

            String original_title = object.getString("original_title");
            String poster_path = object.getString("poster_path");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            String vote_average = object.getString("vote_average");

            movies[i] = new Movie(original_title, poster_path, overview, vote_average, release_date);
        }

        return movies;
    }
}
