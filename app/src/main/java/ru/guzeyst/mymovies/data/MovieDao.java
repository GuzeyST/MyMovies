package ru.guzeyst.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE _id == :id")
    Movie getMovieById(int id);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<FavoriteMovie>> getAllFavoriteMoves();

    @Insert
    void insetrFavoriteMovie(FavoriteMovie favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM favorite_movies WHERE _id == :id")
    FavoriteMovie getFavoriteMovieById(int id);
}
