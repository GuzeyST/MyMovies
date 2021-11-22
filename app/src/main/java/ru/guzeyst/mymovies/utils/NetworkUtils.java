package ru.guzeyst.mymovies.utils;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_URL_REVIEWS = " https://api.themoviedb.org/3/movie/%s/reviews";

    private static final String PAR_API_KEY = "api_key";
    private static final String PAR_LANGUAGE = "language";
    private static final String PAR_SORT_BY = "sort_by";
    private static final String PAR_PAGE = "page";
    private static final String PAR_VOTE_COUNT = "vote_count.gte";
    private static final String MIN_VOTE_COUNTE = "1000";

    private static final String API_KEY = "fde80e70e0306f31eaf86e6cf0d706e3";
    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    public static URL buldURL(int sortBy, int page){
        String methodOfSort;
        URL url = null;
        if (sortBy == POPULARITY){
            methodOfSort = SORT_BY_POPULARITY;
        }else {
            methodOfSort = SORT_BY_TOP_RATED;
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PAR_API_KEY, API_KEY)
                .appendQueryParameter(PAR_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PAR_SORT_BY, methodOfSort)
                .appendQueryParameter(PAR_VOTE_COUNT, MIN_VOTE_COUNTE)
                .appendQueryParameter(PAR_PAGE, Integer.toString(page))
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURLToVideos(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id))
                .buildUpon()
                .appendQueryParameter(PAR_API_KEY, API_KEY)
                .appendQueryParameter(PAR_LANGUAGE, LANGUAGE_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURLToReviews(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id))
                .buildUpon()
                .appendQueryParameter(PAR_API_KEY, API_KEY)
                //.appendQueryParameter(PAR_LANGUAGE, LANGUAGE_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static JSONObject getJSONReviews(int id){
        JSONObject jsonObject = null;
        URL url = buildURLToReviews(id);
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getJSONForVideos(int id){
        JSONObject result = null;
        URL url = buildURLToVideos(id);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONObject(int sortBy, int page){
        JSONObject result = null;
        URL url = buldURL(sortBy, page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;

            if(urls == null || urls.length == 0){
                return result;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = bufferedReader.readLine();
                StringBuilder builder = new StringBuilder();
                while (line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(builder.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private onStartLoadingListener onStartLoadingListener;

        public interface onStartLoadingListener{
            void onStartLoading();
        }

        public void setOnStartLoadingListener(JSONLoader.onStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(onStartLoadingListener != null){
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            JSONObject result = null;
            if(bundle == null) return result;

            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if(url == null){
                return result;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = bufferedReader.readLine();
                StringBuilder builder = new StringBuilder();
                while (line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(builder.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return result;
        }
    }

}
