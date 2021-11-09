package com.lzitech.movies_shows_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.databinding.ItemContainerTvShowBinding;
import com.lzitech.movies_shows_app.listeners.WatchListListener;
import com.lzitech.movies_shows_app.models.TVShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {
    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private WatchListListener watchListListener;

    public WatchlistAdapter(List<TVShow> tvShows, WatchListListener watchListListener) {
        this.tvShows = tvShows;
        this.watchListListener = watchListListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_container_tv_show, parent, false);

        return new TVShowViewHolder(tvShowBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        TVShow tvShow = tvShows.get(position);
        holder.bindTVShow(tvShow);
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TVShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(v -> watchListListener.onTVShowClicked(tvShow));
            itemContainerTvShowBinding.imageButtonDelete.setOnClickListener(v -> {
                watchListListener.removeTVShowFromWatchlist(tvShow, getAdapterPosition());
            });
            itemContainerTvShowBinding.imageButtonDelete.setVisibility(View.VISIBLE);
        }
    }
}
