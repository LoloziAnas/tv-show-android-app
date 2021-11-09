package com.lzitech.movies_shows_app.listeners;

import com.lzitech.movies_shows_app.models.TVShow;

public interface WatchListListener {
    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWatchlist(TVShow tvShow, int position);
}
