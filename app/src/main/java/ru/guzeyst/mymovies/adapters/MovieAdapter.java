package ru.guzeyst.mymovies.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.guzeyst.mymovies.R;
import ru.guzeyst.mymovies.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;

    private OnPosterClickListener onPosterClickListener;
    private onReachEndListener onReachEndListener;

    public interface OnPosterClickListener{
        void onPosterClick(int position);
    }

    public interface onReachEndListener{
        void onReachEnd();
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setOnReachEndListener(MovieAdapter.onReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public void clearMovie(){
        this.movies.clear();
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie>movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addMovie(Movie movie){
        this.movies.add(movie);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(movies.size() >= 20 && position == movies.size() - 4 && onReachEndListener != null){
            onReachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        ImageView imageView = holder.smallPoster;
        Picasso.get().load(movie.getPosterPath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView smallPoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            smallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onPosterClickListener != null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }

    }
}
