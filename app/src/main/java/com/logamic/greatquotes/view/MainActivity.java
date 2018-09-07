package com.logamic.greatquotes.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    TextView quoteTextView;
    TextView authorTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quote_text_view);
        authorTextView = findViewById(R.id.author_text_view);

        quotesListView = findViewById(R.id.quotes_list);
        quoteListAdapter = new QuoteListAdapter(this);
        quotesListView.setAdapter(quoteListAdapter);
        quotesListView.setOnItemClickListener(new OnQuotesListItemClickListener());

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        if (App.get().getDatabase() == null) {
            new LoadDatabaseTask().execute();
        } else {
            showCurrentQuote();
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
            MainActivity.this.onPostExecute();
        }
    }

    private class DeleteCurrentQuoteTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            App.get().deleteCurrentQuote();
            return null;
        }

        protected void onPostExecute(Void result) {
            MainActivity.this.onPostExecute();
        }
    }

    private void onPostExecute() {
        showCurrentQuote();
        quoteListAdapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }

    private void showCurrentQuote() {
        Quote quote = App.get().getCurrentQuote();
        if (quote != null) {
            showQuote(quote);
        } else {
            clearQuote();
        }
    }

    private void showQuote(Quote quote) {
        if (quote != null) {
            quoteTextView.setText(quote.getQuote());
            authorTextView.setText(quote.getAuthor());
            authorTextView.setTextColor(getResources().getColor(quote.getGender().getColorResourceId()));
        }
    }

    private void clearQuote() {
        quoteTextView.setText("");
        authorTextView.setText("");
    }

    private void selectQuote(int index) {
        App.get().selectCurrentQuote(index);
        showCurrentQuote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        menu.findItem(R.id.action_delete).setVisible(App.get().getCurrentQuote() != null);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_delete:
                new DeleteCurrentQuoteTask().execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class OnQuotesListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectQuote(position);
            quoteListAdapter.notifyDataSetChanged();
            //drawerLayout.closeDrawers();
        }
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
