package ru.guzeyst.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import ru.guzeyst.mymovies.adapters.MovieAdapter;
import ru.guzeyst.mymovies.data.MainViewModel;
import ru.guzeyst.mymovies.data.Movie;
import ru.guzeyst.mymovies.utils.JSONUtils;
import ru.guzeyst.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView rvPosters;
    private MovieAdapter adapter;
    private Switch switchSort;
    private TextView tvPopularity;
    private TextView tvTopRate;
    private MainViewModel viewModel;
    private static final int LOADER_ID = 133;
    private LoaderManager loaderManager;
    private static int methodOfSort;
    private static int page = 1;
    private static RecyclerView.LayoutManager layoutManager;
    private static boolean isLoading = false;
    ProgressBar loading;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels/displayMetrics.density);
        return width/185 > 2 ? width/185 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvPosters = findViewById(R.id.rvPosters);
        switchSort = findViewById(R.id.switchSort);
        tvPopularity = findViewById(R.id.textViewPopularity);
        tvTopRate = findViewById(R.id.textViewTopRate);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        loaderManager = LoaderManager.getInstance(this);
        loading = findViewById(R.id.pbLoading);

        adapter = new MovieAdapter();
        layoutManager = new GridLayoutManager(this, getColumnCount());
        rvPosters.setLayoutManager(layoutManager);
        rvPosters.setAdapter(adapter);

        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                page = 1;
                setMethodOfSort(b);
            }

        });

        switchSort.setChecked(false);

        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = adapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(getResources().getString(R.string.key_id), movie.get_id());
                startActivity(intent);
            }
        });

        adapter.setOnReachEndListener(new MovieAdapter.onReachEndListener() {
            @Override
            public void onReachEnd() {

            }
        });

        rvPosters.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    downloadData(methodOfSort, page);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(page == 1){
                    adapter.setMovies(movies);
                }
            }
        });
    }

    public void onClickPopularity(View view) {
        switchSort.setChecked(false);
        //setMethodOfSort(false);
    }

    public void OnClickTopRate(View view) {
        switchSort.setChecked(true);
        //setMethodOfSort(true);
    }

    private void setMethodOfSort(boolean isTopRated) {
        if (isTopRated) {
            methodOfSort = NetworkUtils.TOP_RATED;
            setColorTextView(tvTopRate, tvPopularity);
        } else {
            methodOfSort = NetworkUtils.POPULARITY;
            setColorTextView(tvPopularity, tvTopRate);
        }
        downloadData(methodOfSort, page);
    }

    private void setColorTextView(TextView enableTV, TextView disableTV) {
        enableTV.setTextColor(getResources().getColor(R.color.teal_700));
        disableTV.setTextColor(getResources().getColor(R.color.white));
    }

    private void downloadData(int methodOfSort, int page) {
        URL ulr = NetworkUtils.buldURL(methodOfSort, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", ulr.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.onStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
                loading.setVisibility(View.VISIBLE);
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        JSONObject jsonObject = data;
        List<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);

        if (movies != null && !movies.isEmpty()) {
            if(page == 1) {
                viewModel.deleteAllMovies();
                adapter.clearMovie();
            }
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            adapter.addMovies(movies);
            page++;
        }
        //movies = viewModel.getMovies().getValue();

        isLoading = false;
        loading.setVisibility(View.GONE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}
