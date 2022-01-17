package com.lzitech.movies_shows_app.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lzitech.movies_shows_app.repositories.MostPopularTVShowsRepository;

public class MostPopularTVShowViewModelFactory implements ViewModelProvider.Factory {
    private final MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowViewModelFactory(MostPopularTVShowsRepository mostPopularTVShowsRepository) {
        this.mostPopularTVShowsRepository = mostPopularTVShowsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MostPopularTVShowsViewModel(mostPopularTVShowsRepository);
    }
}
