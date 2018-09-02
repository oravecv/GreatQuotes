package com.logamic.greatquotes.model;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by oravecv on 02/09/18.
 */

@Database(entities = {Quote.class}, version = 1)
public abstract class QuotesDatabase extends RoomDatabase {
    public abstract QuoteDao quoteDao();
}
