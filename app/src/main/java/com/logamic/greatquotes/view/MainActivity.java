package com.logamic.greatquotes.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.logamic.greatquotes.App;
import com.logamic.greatquotes.R;
import com.logamic.greatquotes.model.Quote;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView quotesListView;
    private DrawerLayout drawerLayout;
    private QuoteListAdapter quoteListAdapter;

    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;

    TextView randomQuoteTextView;
    TextView authorTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomQuoteTextView = findViewById(R.id.quote_text_view);
        authorTextView = findViewById(R.id.author_text_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        quotesListView = findViewById(R.id.quotes_list);
        quoteListAdapter = new QuoteListAdapter(this);
        quotesListView.setAdapter(quoteListAdapter);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        if (App.get().getDatabase() == null) {
            new LoadDatabaseTask().execute();
        } else {
            showRandomQuote();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private class LoadDatabaseTask extends AsyncTask<Void, Void, Void> {

       protected Void doInBackground(Void... params) {
           App.get().loadData();
           return null;
       }

        protected void onPostExecute(Void result) {
            showRandomQuote();
            quoteListAdapter.notifyDataSetChanged();
        }
    }

    private void showRandomQuote() {
        Quote randomQuote = App.get().getRandomQuote();
        if (randomQuote != null) {
            randomQuoteTextView.setText(randomQuote.getQuote());
            authorTextView.setText(randomQuote.getAuthor());
            authorTextView.setTextColor(getResources().getColor(randomQuote.getGender().getColorResourceId()));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerToggle extends ActionBarDrawerToggle {

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public void onDrawerOpened (View drawerView) {
            super.onDrawerOpened(drawerView);
            getSupportActionBar().setTitle(MainActivity.this.getResources().getString(R.string.quotes));
            invalidateOptionsMenu();
        }
        
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            getSupportActionBar().setTitle(MainActivity.this.getResources().getString(R.string.app_name));
            invalidateOptionsMenu();
        }
    }
}
