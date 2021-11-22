package ru.guzeyst.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public LiveData<List<FavoriteMovie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(getApplication());
        movies = database.movieDao().getAllMovies();
        favoriteMovies = database.movieDao().getAllFavoriteMoves();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public FavoriteMovie getFavoriteMovieById(int id){
        try {
            return new getFavoriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getFavoriteMovieByIdTask extends AsyncTask<Integer, Void, FavoriteMovie>{

        @Override
        protected FavoriteMovie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return database.movieDao().getFavoriteMovieById(integers[0]);
            }
            return null;
        }
    }

    public void deleteFavoriteMove(FavoriteMovie movie){
        try {
            new deleteFavoriteMoveTask().execute(movie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class deleteFavoriteMoveTask extends AsyncTask<FavoriteMovie, Void, Void>{
        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            if(favoriteMovies != null && favoriteMovies.length > 0){
                database.movieDao().deleteFavoriteMovie(favoriteMovies[0]);
            }
            return null;
        }
    }

    public void insertFavoriteMove(FavoriteMovie movie){
        try {
            new insertFavoriteMovieTask().execute(movie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class insertFavoriteMovieTask extends AsyncTask<FavoriteMovie, Void, Void>{
        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            if(favoriteMovies != null && favoriteMovies.length > 0){
                database.movieDao().insetrFavoriteMovie(favoriteMovies[0]);
            }
            return null;
        }
    }

    public Movie getMovieById(int id){
        try {
            return new getMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getMovieTask extends AsyncTask<Integer, Void, Movie>{

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return database.movieDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    public void deleteAllMovies(){
        try {
            new deleteMoviesTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class deleteMoviesTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            database.movieDao().deleteAllMovies();
            return null;
        }
    }

    public void insertMovie(Movie movie){
        try {
            new insertMovieTask().execute(movie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class insertMovieTask extends AsyncTask<Movie, Void, Void>{
        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0){
                database.movieDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    public void deleteMovie(Movie movie){
        try {
            new deleteMovieTask().execute(movie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class deleteMovieTask extends AsyncTask<Movie, Void, Void>{
        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0){
                database.movieDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }
}
