package com.ahmadnursalim.android.popularmovies.DataModel;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ahmadnursalim.android.popularmovies.MainActivity;

public class Movie implements Parcelable {
    public Movie(String id, String title, String poster, String overview, String rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel source) {
        id = source.readString();
        title = source.readString();
        poster = source.readString();
        overview = source.readString();
        rating = source.readString();
        releaseDate = source.readString();
    }

    public Movie(Cursor cursor) {
        this.id = cursor.getString(MainActivity.COL_MOVIE_ID);
        this.title = cursor.getString(MainActivity.COL_TITLE);
        this.poster = cursor.getString(MainActivity.COL_IMAGE);
        this.overview = cursor.getString(MainActivity.COL_OVERVIEW);
        this.rating = cursor.getString(MainActivity.COL_RATING);
        this.releaseDate = cursor.getString(MainActivity.COL_DATE);
    }

    private String id;
    private String title;
    private String poster;
    private String overview;
    private String rating;
    private String releaseDate;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getRating() {
        return "★ " + rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
