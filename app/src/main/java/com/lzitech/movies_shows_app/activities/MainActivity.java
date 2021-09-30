package com.lzitech.movies_shows_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.adapters.TVShowsAdapter;
import com.lzitech.movies_shows_app.databinding.ActivityMainBinding;
import com.lzitech.movies_shows_app.models.TVShow;
import com.lzitech.movies_shows_app.viewModels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel mostPopularTVShowsViewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        mostPopularTVShowsViewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        adapter = new TVShowsAdapter(tvShows);
        activityMainBinding.tvShowRecyclerView.setAdapter(adapter);
        getMostPopularTVShows();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getMostPopularTVShows() {
        activityMainBinding.setIsLoading(true);
        mostPopularTVShowsViewModel.getMostPopularTVShows(1).observe(this, mostPopularTvShowResponse -> {
            activityMainBinding.setIsLoading(false);
            if (mostPopularTvShowResponse.getTvShows() != null){
                tvShows.addAll(mostPopularTvShowResponse.getTvShows());
                adapter.notifyDataSetChanged();
            }
        });
    }
}