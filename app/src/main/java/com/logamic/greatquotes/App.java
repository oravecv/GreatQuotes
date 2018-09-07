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

    private Quote currentQuote;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static App get() {
        return INSTANCE;
    }

    public void loadData() {
        database = Room.databaseBuilder(getApplicationContext(), QuotesDatabase.class, DATABASE_NAME).build();

        if (getSP().getBoolean(KEY_XML_LOADED, false) == false) {
            loadXml();
        }
        loadDatabase();

    }

    public QuotesDatabase getDatabase() {
        return database;
    }


    private void loadXml() {
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

    private void loadDatabase() {
        quotesList = getDatabase().quoteDao().getAll();
        Log.d(App.this.getClass().getSimpleName(), "quotesList.size() = " + quotesList.size());

        if (quotesList.size() > 0) {
            selectRandomQuote();
        }

    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }

    public void selectRandomQuote() {
        if (quotesList != null && quotesList.size() > 0) {
            currentQuote = quotesList.get(new Random().nextInt(quotesList.size()));
        } else {
            currentQuote = null;
        }

    }

    public void selectCurrentQuote(int index) {
        if (quotesList != null && index < quotesList.size()) {
            currentQuote = quotesList.get(index);
        }
    }

    public Quote getCurrentQuote() {
        return currentQuote;
    }

    public void deleteCurrentQuote() {
        database.quoteDao().delete(currentQuote);
        quotesList = database.quoteDao().getAll();
        selectRandomQuote();
    }

    public List<Quote> getQuotesList() {
        return quotesList;
    }

    public int getQuotesListSize() {
        if (quotesList != null) {
            return quotesList.size();
        } else {
            return 0;
        }
    }

    public void addQuote(Quote quote) {
        database.quoteDao().insert(quote);
        quotesList.add(quote);
        currentQuote = quote;
    }
}
