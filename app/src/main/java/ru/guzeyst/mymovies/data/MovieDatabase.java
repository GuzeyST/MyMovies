package ru.guzeyst.mymovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, FavoriteMovie.class}, version = 7, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase database;
    public static final String NAME_DB = "movies.db";
    public static final Object LOOK = new Object();

    public static MovieDatabase getInstance(Context context) {
        synchronized (LOOK) {
            if (database == null) {
                database = Room.databaseBuilder(context, MovieDatabase.class, NAME_DB).fallbackToDestructiveMigration().build();
            }
        }
        return database;
    }

    public abstract MovieDao movieDao();
}
