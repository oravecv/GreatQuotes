package com.logamic.greatquotes.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by oravecv on 02/09/18.
 */

@Dao
public interface QuoteDao {

    @Query("SELECT * FROM quote")
    List<Quote> getAll();

    @Update
    void update(Quote quote);

    @Insert
    void insertAll(List<Quote> quotes);

    @Delete
    void delete(Quote quote);
}
