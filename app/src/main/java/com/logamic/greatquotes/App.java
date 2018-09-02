package com.logamic.greatquotes;

import android.app.AlertDialog;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.logamic.greatquotes.model.Quote;
import com.logamic.greatquotes.model.QuotesDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by oravecv on 02/09/18.
 */

public class App extends Application {

    private static final String DATABASE_NAME = "QUOTES_DATABASE";
    private QuotesDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(), QuotesDatabase.class, DATABASE_NAME).build();


        new Thread(new Runnable() {
            @Override
            public void run() {

                QuotesXmlParser parser = new QuotesXmlParser(getApplicationContext());
                List<Quote> quotesListFromXml = null;
                try {
                    quotesListFromXml = parser.getQuotesListFromXml();
                    Log.d(App.this.getClass().getSimpleName(), "quotesListFromXml.size() = " + quotesListFromXml.size());
                } catch (IOException | XmlPullParserException e) {
                    showAlertDialog(e.getClass().getSimpleName(), e.getMessage());
                }

                if (quotesListFromXml != null) {
                    database.quoteDao().insertAll(quotesListFromXml);
                }

                Log.d(App.this.getClass().getSimpleName(), "database.quoteDao().getAll().size() = " + database.quoteDao().getAll().size());

            }
        }).start();


    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }
}
