package com.lzitech.movies_shows_app.Dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.lzitech.movies_shows_app.models.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvShow")
    Flowable<List<TVShow>> getWatchList();

    @Insert(onConflict = REPLACE)
    Completable addToWatchList(TVShow tvShow);

    @Delete
    Completable removeFromWatchList(TVShow tvShow);

}
