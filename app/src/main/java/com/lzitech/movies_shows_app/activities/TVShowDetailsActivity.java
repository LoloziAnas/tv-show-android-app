package com.lzitech.movies_shows_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.databinding.ActivityTvshowDetailsBinding;
import com.lzitech.movies_shows_app.viewModels.TVShowDetailsViewModel;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        getTVShowDetails();
    }

    private void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        long id = getIntent().getLongExtra("id", -1);
        tvShowDetailsViewModel.getTVShowDetails(id).observe(this, tvShowDetailsResponse -> {
            activityTvshowDetailsBinding.setIsLoading(false);
            Toast.makeText(getApplicationContext(), "" + tvShowDetailsResponse.getTvShowDetails().getUrl(), Toast.LENGTH_LONG).show();
        });
    }
}