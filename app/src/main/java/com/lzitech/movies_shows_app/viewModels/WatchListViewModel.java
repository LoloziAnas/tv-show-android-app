package com.lzitech.movies_shows_app.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.lzitech.movies_shows_app.database.TVShowDatabase;
import com.lzitech.movies_shows_app.models.TVShow;

import java.util.List;

import io.reactivex.Flowable;

public class WatchListViewModel extends AndroidViewModel {
    private TVShowDatabase tvShowDatabase;
    public WatchListViewModel(@NonNull Application application) {
        super(application);
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }
    public Flowable<List<TVShow>> loadWatchList(){
        return tvShowDatabase.tvShowDao().getWatchList();
    }
}
