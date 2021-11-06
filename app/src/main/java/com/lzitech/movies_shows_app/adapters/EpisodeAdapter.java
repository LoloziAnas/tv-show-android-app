package com.lzitech.movies_shows_app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.databinding.ItemContainerEpisodesBinding;
import com.lzitech.movies_shows_app.models.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private List<Episode> episodes;
    private LayoutInflater layoutInflater;

    public EpisodeAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_container_episodes, parent, false);

        return new EpisodeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindEpisode(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        ItemContainerEpisodesBinding itemContainerEpisodesBinding;

        public EpisodeViewHolder(ItemContainerEpisodesBinding itemContainerEpisodesBinding) {
            super(itemContainerEpisodesBinding.getRoot());
            this.itemContainerEpisodesBinding = itemContainerEpisodesBinding;
        }

        public void bindEpisode(Episode episode) {
            String season = episode.getSeason();
            String nbEpisode = episode.getEpisode();
            String title = "S".concat(season).concat(" E").concat(nbEpisode);
            itemContainerEpisodesBinding.setTitle(title);
            itemContainerEpisodesBinding.setName(episode.getName());
            itemContainerEpisodesBinding.setAirDate(episode.getAirDate());
        }

    }
}
