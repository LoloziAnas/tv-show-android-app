package com.lzitech.movies_shows_app.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lzitech.movies_shows_app.repositories.SearchTVShowRepository;
import com.lzitech.movies_shows_app.responses.TVShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        this.searchTVShowRepository = new SearchTVShowRepository();
    }

    public SearchViewModel(SearchTVShowRepository searchTVShowRepository) {
        this.searchTVShowRepository = searchTVShowRepository;
    }

    public LiveData<TVShowResponse> searchTVShow(String query, int page) {
        return searchTVShowRepository.searchTVShow(query, page);
    }
}
