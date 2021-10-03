package com.lzitech.movies_shows_app.responses;

import com.google.gson.annotations.SerializedName;
import com.lzitech.movies_shows_app.models.TVShowDetails;

public class TVShowDetailsResponse {
    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
