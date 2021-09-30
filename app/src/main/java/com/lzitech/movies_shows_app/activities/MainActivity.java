package com.lzitech.movies_shows_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.viewModels.MostPopularTVShowsViewModel;

public class MainActivity extends AppCompatActivity {

    private MostPopularTVShowsViewModel mostPopularTVShowsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mostPopularTVShowsViewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        mostPopularTVShowsViewModel.getMostPopularTVShows(1).observe(this, mostPopularTvShowResponse -> {
            Toast.makeText(getApplicationContext(), "Total pages: " + mostPopularTvShowResponse.getPages(), Toast.LENGTH_LONG).show();
        });
    }
}