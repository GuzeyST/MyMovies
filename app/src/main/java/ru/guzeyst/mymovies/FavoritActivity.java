package ru.guzeyst.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ru.guzeyst.mymovies.adapters.MovieAdapter;
import ru.guzeyst.mymovies.data.FavoriteMovie;
import ru.guzeyst.mymovies.data.MainViewModel;
import ru.guzeyst.mymovies.data.Movie;

public class FavoritActivity extends AppCompatActivity {

    RecyclerView rvFavoriteMovie;
    MovieAdapter adapter;
    MainViewModel viewModel;

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
        setContentView(R.layout.activity_favorit);
        rvFavoriteMovie = findViewById(R.id.rvFavoriteMovie);
        rvFavoriteMovie.setLayoutManager(new GridLayoutManager(this, 2));
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        LiveData<List<FavoriteMovie>> liveData = viewModel.getFavoriteMovies();
        adapter = new MovieAdapter();
        rvFavoriteMovie.setAdapter(adapter);
        liveData.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                List<Movie> movies= new ArrayList<Movie>();
                if (favoriteMovies != null) {
                    movies.addAll(favoriteMovies);
                }
                adapter.setMovies(movies);
            }
        });
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie favoriteMovie = adapter.getMovies().get(position);
                Intent intent = new Intent(FavoritActivity.this, DetailActivity.class);
                intent.putExtra(getResources().getString(R.string.key_id), favoriteMovie.get_id());
                startActivity(intent);
            }
        });
    }
}