package com.example.evoliris.hypesql.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.evoliris.hypesql.db.DbHelper;
import com.example.evoliris.hypesql.models.Movie;

import java.util.Locale;

public class MovieDAO {
    public static String TABLE_NAME = "listeDeFilms";

    public static final String COL_ID = "movie_id";
    public static final String COL_TITLE = "movie_title";
    public static final String COL_SYNOPSIS = "movie_synopsis";
    public static final String COL_DIRECTOR = "movie_director";

    public static final String CREATE = "CREATE TABLE movie(" +
            COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_TITLE + "VARCHAR(50) NOT NULL," +
            COL_SYNOPSIS + "VARCHAR(255) NOT NULL," +
            COL_DIRECTOR+ "VARCHAR(50);";
    public static final String UPDATE = "DROP TABLE movie;";
    public static final String DROP = "DROP TABLE movie;";

    private DbHelper helper;
    private Context context;
    private SQLiteDatabase db;

    public MovieDAO(Context context){
        this.context = context;
        this.helper = new DbHelper(this.context);
    }

    public MovieDAO openReadable(){
        this.db = this.helper.getReadableDatabase(); //TODO a vérifier
        return this;
    }

    public MovieDAO openWriteable(){ // TODO il manque quelque chose ?
        this.db = this.helper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.db.close();
        this.helper.close();
    }

    public void update(Movie movie){
        ContentValues values = new ContentValues();
        values.put(COL_ID, movie.getId());
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_SYNOPSIS, movie.getSynopsis());
        values.put(COL_DIRECTOR, movie.getDirector());

        db.update(TABLE_NAME, values, String.format(Locale.FRENCH, "%s = %d", COL_ID, movie.getId()), null);
    }

    public long insert(Movie movie){
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_SYNOPSIS, movie.getSynopsis());
        values.put(COL_DIRECTOR, movie.getDirector());

        long idInserted = db.insert(TABLE_NAME, null, values);
        movie.setId(idInserted);
        return idInserted;
    }

    public Movie findById(long id){
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        //TODO vérification condition
        if (cursor == null) return null;
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();

        return cursorToMovie(cursor);
    }

    private Movie cursorToMovie(Cursor cursor){
        Movie movie = new Movie();

        movie.setId(cursor.getLong(cursor.getColumnIndex(COL_ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
        movie.setSynopsis(cursor.getString(cursor.getColumnIndex(COL_SYNOPSIS)));
        movie.setDirector(cursor.getString(cursor.getColumnIndex(COL_DIRECTOR)));

        return movie;

    }
}
