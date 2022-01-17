package com.lzitech.movies_shows_app.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lzitech.movies_shows_app.repositories.MostPopularTVShowsRepository;
import com.lzitech.movies_shows_app.responses.TVShowResponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private final MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRepository = new MostPopularTVShowsRepository();
    }

    public MostPopularTVShowsViewModel(MostPopularTVShowsRepository mostPopularTVShowsRepository) {
        this.mostPopularTVShowsRepository = mostPopularTVShowsRepository;
    }

    public LiveData<TVShowResponse> getMostPopularTVShows(int page) {
        return mostPopularTVShowsRepository.getMostPopularTVShows(page);
    }
}
