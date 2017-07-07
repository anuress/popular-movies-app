package com.ahmadnursalim.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public Movie(String title, String poster, String overview, String rating, String releaseDate) {
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel source) {
        title = source.readString();
        poster = source.readString();
        overview = source.readString();
        rating = source.readString();
        releaseDate = source.readString();
    }

    private String title;
    private String poster;
    private String overview;
    private String rating;
    private String releaseDate;

    String getTitle() {
        return title;
    }

    String getPoster() {
        return "http://image.tmdb.org/t/p/w185/" + poster;
    }

    String getOverview() {
        return overview;
    }

    String getRating() {
        return "★ " + rating;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
