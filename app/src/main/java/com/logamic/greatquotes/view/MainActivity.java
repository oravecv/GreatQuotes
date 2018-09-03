package com.logamic.greatquotes.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.logamic.greatquotes.App;
import com.logamic.greatquotes.R;
import com.logamic.greatquotes.model.Quote;

public class MainActivity extends AppCompatActivity {

    TextView randomQuoteTextView;
    TextView authorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomQuoteTextView = findViewById(R.id.quote_text_view);
        authorTextView = findViewById(R.id.author_text_view);

        if (App.get().getDatabase() == null) {
            new LoadDatabaseTask().execute();
        } else {
            showRandomQuote();
        }
    }


    private class LoadDatabaseTask extends AsyncTask<Void, Void, Void> {

       protected Void doInBackground(Void... params) {
           App.get().loadData();
           return null;
       }

        protected void onPostExecute(Void result) {
            showRandomQuote();
        }
    }

    private void showRandomQuote() {
        Quote randomQuote = App.get().getRandomQuote();
        if (randomQuote != null) {
            randomQuoteTextView.setText(randomQuote.getQuote());
            authorTextView.setText(randomQuote.getFirstName() + " " + randomQuote.getLastName());
            authorTextView.setTextColor(getResources().getColor(randomQuote.getGender().getColorResourceId()));
        }

    }
}
