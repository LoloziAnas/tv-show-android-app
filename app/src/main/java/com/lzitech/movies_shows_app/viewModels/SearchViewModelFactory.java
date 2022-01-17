package com.lzitech.movies_shows_app.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lzitech.movies_shows_app.repositories.SearchTVShowRepository;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final SearchTVShowRepository searchTVShowRepository;

    public SearchViewModelFactory(SearchTVShowRepository searchTVShowRepository) {
        this.searchTVShowRepository = searchTVShowRepository;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SearchViewModel(searchTVShowRepository);
    }
}
