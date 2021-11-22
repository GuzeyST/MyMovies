package ru.guzeyst.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import ru.guzeyst.mymovies.adapters.ReviewAdapter;
import ru.guzeyst.mymovies.adapters.TrailerAdapter;
import ru.guzeyst.mymovies.data.FavoriteMovie;
import ru.guzeyst.mymovies.data.MainViewModel;
import ru.guzeyst.mymovies.data.Movie;
import ru.guzeyst.mymovies.data.Review;
import ru.guzeyst.mymovies.data.Trailer;
import ru.guzeyst.mymovies.utils.JSONUtils;
import ru.guzeyst.mymovies.utils.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewAddFavorite;
    private ImageView bigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewDateRelease;
    private TextView textViewDesc;
    private String KEY_ID;
    private int id;
    private MainViewModel viewModel;
    private Movie movie;
    private FavoriteMovie favoriteMovie;
    private RecyclerView rvTrailers;
    private RecyclerView rvReviews;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavorite:
                Intent intentFavoite = new Intent(this, FavoritActivity.class);
                startActivity(intentFavoite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewDateRelease = findViewById(R.id.textViewDateRelease);
        textViewDesc = findViewById(R.id.textViewDesc);
        KEY_ID = getResources().getString(R.string.key_id);
        imageViewAddFavorite = findViewById(R.id.imageViewAddToFavorite);
        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(KEY_ID)){
            id = intent.getIntExtra(KEY_ID, -1);
        }else{
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id);
        Picasso.get().load(movie.getBigPosterPath()).into(bigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        textViewDateRelease.setText(movie.getReleaseDate());
        textViewDesc.setText(movie.getOverview());
        setFavorite();

        rvReviews = findViewById(R.id.rvReviews);
        rvTrailers = findViewById(R.id.rvTrailers);
        reviewAdapter = new ReviewAdapter();
        trailerAdapter = new TrailerAdapter();
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
        rvTrailers.setAdapter(trailerAdapter);
        JSONObject jsonObjectTrailers = NetworkUtils.getJSONForVideos(id);
        JSONObject jsonObjectReview = NetworkUtils.getJSONReviews(id);
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObjectTrailers);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReview);
        reviewAdapter.setListReviews(reviews);
        trailerAdapter.setListTrailers(trailers);

        trailerAdapter.setOnPlayClickListener(new TrailerAdapter.OnPlayClickListener() {
            @Override
            public void onPlayClick(String url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            }
        });
    }

    private void setFavorite(){
        favoriteMovie = viewModel.getFavoriteMovieById(id);
        if(favoriteMovie == null){
            imageViewAddFavorite.setImageResource(R.drawable.star_black);
        }else{
            imageViewAddFavorite.setImageResource(R.drawable.star_gold);
        }
    }

    public void onClickAddFavorite(View view) {
        if(favoriteMovie == null){
            viewModel.insertFavoriteMove(new FavoriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
        }else {
            viewModel.deleteFavoriteMove(favoriteMovie);
            Toast.makeText(this, R.string.delete_from_favorite, Toast.LENGTH_SHORT).show();
        }
        setFavorite();
    }
}