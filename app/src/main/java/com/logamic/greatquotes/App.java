package com.logamic.greatquotes;

import android.app.AlertDialog;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.util.Log;

import com.logamic.greatquotes.model.Quote;
import com.logamic.greatquotes.model.QuotesDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class App extends Application {

    public static App INSTANCE;

    private static final String PREFERENCES = "RoomDemo.preferences";
    private static final String KEY_XML_LOADED = "xml_loaded";

    private static final String DATABASE_NAME = "QUOTES_DATABASE";
    private QuotesDatabase database;

    private List<Quote> quotesList;
    private Quote randomQuote = null;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(), QuotesDatabase.class, DATABASE_NAME).build();

        if (getSP().getBoolean(KEY_XML_LOADED, false) == false) {
            new Thread(new XmlLoader()).start();
        } else {
           loadDatabase();
        }

        INSTANCE = this;
    }

    class XmlLoader implements Runnable {

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
                getSP().edit().putBoolean(KEY_XML_LOADED, true).apply();

            }
        }

    }

    private void loadDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                quotesList = App.get().getDatabase().quoteDao().getAll();
                Log.d(App.this.getClass().getSimpleName(), "quotesList.size() = " + quotesList.size());

                if (quotesList.size() > 0) {
                    randomQuote = quotesList.get(new Random().nextInt(quotesList.size()));
                }

            }
        }).start();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }

    public static App get() {
        return INSTANCE;
    }

    public QuotesDatabase getDatabase() {
        return database;
    }

    public Quote getRandomQuote() {
        return randomQuote;
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }

}
