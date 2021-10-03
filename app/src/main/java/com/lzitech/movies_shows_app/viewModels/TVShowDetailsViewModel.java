package com.lzitech.movies_shows_app.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lzitech.movies_shows_app.repositories.TVShowDetailsRepository;
import com.lzitech.movies_shows_app.responses.TVShowDetailsResponse;

public class TVShowDetailsViewModel extends ViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;

    public TVShowDetailsViewModel() {
        tvShowDetailsRepository = new TVShowDetailsRepository();
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(long tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
}
