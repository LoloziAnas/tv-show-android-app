package com.lzitech.movies_shows_app.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lzitech.movies_shows_app.R;
import com.lzitech.movies_shows_app.adapters.EpisodeAdapter;
import com.lzitech.movies_shows_app.adapters.ImageSliderAdapter;
import com.lzitech.movies_shows_app.constants.Utils;
import com.lzitech.movies_shows_app.databinding.ActivityTvshowDetailsBinding;
import com.lzitech.movies_shows_app.databinding.LayoutEpisodeBottomSheetBinding;
import com.lzitech.movies_shows_app.models.TVShow;
import com.lzitech.movies_shows_app.viewModels.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodeBottomSheetBinding layoutEpisodeBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageButtonBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra(Utils.TV_SHOW);
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailableInWatchlist = true;
                    activityTvshowDetailsBinding.imageButtonWatchList.setImageResource(R.drawable.ic_check);
                    compositeDisposable.dispose();
                }));
    }

    private void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        long id = tvShow.getId();
        tvShowDetailsViewModel.getTVShowDetails(id).observe(this, tvShowDetailsResponse -> {
            activityTvshowDetailsBinding.setIsLoading(false);
            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                activityTvshowDetailsBinding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                activityTvshowDetailsBinding.imageViewTvShow.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.setTvShowDescription(
                        String.valueOf(
                                HtmlCompat
                                        .fromHtml(tvShowDetailsResponse.getTvShowDetails().getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        )
                );
                activityTvshowDetailsBinding.textViewDescription.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.textViewReadMe.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.textViewReadMe.setOnClickListener(v -> {
                    if (activityTvshowDetailsBinding.textViewReadMe.getText().equals(Utils.READ_ME)) {
                        activityTvshowDetailsBinding.textViewDescription.setMaxLines(Integer.MAX_VALUE);
                        activityTvshowDetailsBinding.textViewDescription.setEllipsize(null);
                        activityTvshowDetailsBinding.textViewReadMe.setText(R.string.read_less);
                    } else {
                        activityTvshowDetailsBinding.textViewDescription.setMaxLines(4);
                        activityTvshowDetailsBinding.textViewDescription.setEllipsize(TextUtils.TruncateAt.END);
                        activityTvshowDetailsBinding.textViewReadMe.setText(R.string.read_more);
                    }
                });
                activityTvshowDetailsBinding.setTvShowRating(
                        String.format(Locale.getDefault(), "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating()))
                );
                if (tvShowDetailsResponse.getTvShowDetails().getGenres()!= null){
                    activityTvshowDetailsBinding.setTvShowGenre(
                            tvShowDetailsResponse.getTvShowDetails().getGenres()[0]
                    );
                }else{
                    activityTvshowDetailsBinding.setTvShowGenre("N/A");
                }
                activityTvshowDetailsBinding.setTvShowRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                activityTvshowDetailsBinding.viewDivider.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.buttonWebsite.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });
                activityTvshowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener(v -> {
                    if (episodeBottomSheetDialog == null) {
                        episodeBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                        layoutEpisodeBottomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(TVShowDetailsActivity.this),
                                R.layout.layout_episode_bottom_sheet,
                                findViewById(R.id.episodes_container),
                                false
                        );
                        episodeBottomSheetDialog.setContentView(layoutEpisodeBottomSheetBinding.getRoot());
                        layoutEpisodeBottomSheetBinding.episodesRecyclerView.setAdapter(
                                new EpisodeAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                        );
                        layoutEpisodeBottomSheetBinding.textViewTitle.setText(
                                String.format("Episodes | %s", tvShow.getName())
                        );
                        layoutEpisodeBottomSheetBinding.imageButtonClose.setOnClickListener(v1 -> episodeBottomSheetDialog.dismiss());
                    }
                    // optional section start
                    FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                            com.google.android.material.R.id.design_bottom_sheet
                    );
                    if (frameLayout != null) {
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    // optional section end
                    episodeBottomSheetDialog.show();
                });
                activityTvshowDetailsBinding.imageButtonWatchList.setOnClickListener(v -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTVShowAvailableInWatchlist) {
                        compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isTVShowAvailableInWatchlist = false;
                                    Utils.IS_WATCHLIST_UPDATED = true;
                                    activityTvshowDetailsBinding.imageButtonWatchList.setImageResource(R.drawable.ic_watchlist);
                                    Toast.makeText(getApplicationContext(), "Removed from  Watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));
                    } else {
                        compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    Utils.IS_WATCHLIST_UPDATED = true;
                                    activityTvshowDetailsBinding.imageButtonWatchList.setImageResource(R.drawable.ic_check);
                                    Toast.makeText(getApplicationContext(), "Added to Watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));

                    }
                });


                activityTvshowDetailsBinding.imageButtonWatchList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));

            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvshowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderIndicators.getChildAt(position);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));
            }
        }
    }

    private void loadBasicTVShowDetails() {
        String name = tvShow.getName();
        String country = tvShow.getCountry();
        String startDate = tvShow.getStartDate();
        String status = tvShow.getStatus();
        String network = tvShow.getNetwork();
        activityTvshowDetailsBinding.textViewName.setText(name);
        activityTvshowDetailsBinding.textViewName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textViewStartDate.setText(startDate);
        activityTvshowDetailsBinding.textViewStartDate.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textViewStatus.setText(status);
        activityTvshowDetailsBinding.textViewStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textViewCountryNetwork.setText(network.concat(" (").concat(country).concat(")"));
        activityTvshowDetailsBinding.textViewCountryNetwork.setVisibility(View.VISIBLE);
    }
}