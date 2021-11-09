package com.lzitech.movies_shows_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.adapters.WatchlistAdapter;
import com.lzitech.movies_shows_app.constants.Utils;
import com.lzitech.movies_shows_app.databinding.ActivityWatchListBinding;
import com.lzitech.movies_shows_app.listeners.WatchListListener;
import com.lzitech.movies_shows_app.models.TVShow;
import com.lzitech.movies_shows_app.viewModels.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;

public class WatchListActivity extends AppCompatActivity implements WatchListListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchListViewModel watchListViewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);
        doInitialization();
    }

    private void doInitialization() {
        watchListViewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchListBinding.imageButtonBack.setOnClickListener(v -> onBackPressed());
        watchlist = new ArrayList<>();
        loadWatchList();
    }

    private void loadWatchList() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.loadWatchList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                    if (watchlist.size() > 0)
                        watchlist.clear();
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, this);
                    activityWatchListBinding.recyclerViewWatchList.setAdapter(watchlistAdapter);
                    activityWatchListBinding.recyclerViewWatchList.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.IS_WATCHLIST_UPDATED){
            loadWatchList();
            Utils.IS_WATCHLIST_UPDATED = false;
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra(Utils.TV_SHOW, tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(watchListViewModel.removeTVShowWatchlist(tvShow)
        .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchlist.remove(position);
                    watchlistAdapter.notifyItemRemoved(position);
                    watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                    compositeDisposableForDelete.dispose();
                }));
    }
}