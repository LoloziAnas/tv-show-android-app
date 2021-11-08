package com.lzitech.movies_shows_app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.adapters.TVShowsAdapter;
import com.lzitech.movies_shows_app.constants.Utils;
import com.lzitech.movies_shows_app.databinding.ActivityMainBinding;
import com.lzitech.movies_shows_app.listeners.TVShowListener;
import com.lzitech.movies_shows_app.models.TVShow;
import com.lzitech.movies_shows_app.viewModels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListener {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel mostPopularTVShowsViewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter adapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        mostPopularTVShowsViewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        adapter = new TVShowsAdapter(tvShows, this);
        activityMainBinding.tvShowRecyclerView.setAdapter(adapter);
        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMainBinding.ivWatchlist.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), WatchListActivity.class)));
        getMostPopularTVShows();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getMostPopularTVShows() {
        toggleLoading();
        mostPopularTVShowsViewModel.getMostPopularTVShows(currentPage).observe(this, mostPopularTvShowResponse -> {
            toggleLoading();
            if (mostPopularTvShowResponse.getTvShows() != null) {
                int oldCount = tvShows.size();
                totalAvailablePages = mostPopularTvShowResponse.getPages();
                tvShows.addAll(mostPopularTvShowResponse.getTvShows());
                adapter.notifyItemRangeInserted(oldCount, tvShows.size());
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            activityMainBinding.setIsLoading(activityMainBinding.getIsLoading() == null || !activityMainBinding.getIsLoading());
        } else {
            activityMainBinding.setIsLoadingMore(activityMainBinding.getIsLoadingMore() == null || !activityMainBinding.getIsLoadingMore());
        }

    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra(Utils.TV_SHOW, tvShow);
        startActivity(intent);
    }
}