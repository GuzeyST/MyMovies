package ru.guzeyst.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorite_movies")
public class FavoriteMovie extends Movie{
    public FavoriteMovie(int _id, int vote_count, String title, String originalTitle,double popularity, String overview, String posterPath, String bigPosterPath,
                         String backdropPath, double voteAverage, String releaseDate) {
        super(_id, vote_count, title, originalTitle, popularity, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate);
    }

    @Ignore
    public FavoriteMovie(Movie movie){
        super(movie.get_id(), movie.getVote_count(), movie.getTitle(), movie.getOriginalTitle(), movie.getPopularity(), movie.getOverview(), movie.getPosterPath(),
                movie.getBigPosterPath(), movie.getBackdropPath(), movie.getVoteAverage(), movie.getReleaseDate());
    }
}
