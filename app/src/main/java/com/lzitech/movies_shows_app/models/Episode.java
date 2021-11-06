package com.lzitech.movies_shows_app.models;

import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("season")
    private String season;
    @SerializedName("episode")
    private String episode;
    @SerializedName("name")
    private String name;
    @SerializedName("air_date")
    private String airDate;

    public String getSeason() {
        return season;
    }

    public String getEpisode() {
        return episode;
    }

    public String getAirDate() {
        return airDate;
    }

    public String getName() {
        return name;
    }
}
