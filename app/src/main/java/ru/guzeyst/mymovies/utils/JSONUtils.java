package ru.guzeyst.mymovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.guzeyst.mymovies.data.Movie;
import ru.guzeyst.mymovies.data.Review;
import ru.guzeyst.mymovies.data.Trailer;

public class JSONUtils {

    private static final String KEY_RESULTS = "results";

    //Для отызвов
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    //Для видео
    private static final String KEY_OF_VIDEO = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    //Информация о фильме
    private static final String KEY_ID = "id";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject){
        JSONArray  jsonArray = null;
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null){
            return result;
        }
        try {
            jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                int voteCount = objectMovie.getInt(KEY_VOTE_COUNT);
                String title = objectMovie.getString(KEY_TITLE);
                String originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
                double popularity = objectMovie.getDouble(KEY_POPULARITY);
                String overview = objectMovie.getString(KEY_OVERVIEW);
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String backdropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate = objectMovie.getString(KEY_RELEASE_DATE);
                result.add(new Movie(id, voteCount, title, originalTitle, popularity, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static ArrayList<Trailer> getTrailersFromJSON(JSONObject jsonObject){
        ArrayList<Trailer> arrayList = new ArrayList<>();

        if(jsonObject == null){
            return arrayList;
        }

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObjectFromArray = jsonArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + jsonObjectFromArray.getString(KEY_OF_VIDEO);
                String name = jsonObjectFromArray.getString(KEY_NAME);
                arrayList.add(new Trailer(key, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject){
        ArrayList<Review> arrayList = new ArrayList<>();

        if(jsonObject == null){
            return arrayList;
        }

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObjectFromArray = jsonArray.getJSONObject(i);
                String author = jsonObjectFromArray.getString(AUTHOR);
                String content = jsonObjectFromArray.getString(CONTENT);
                arrayList.add(new Review(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
