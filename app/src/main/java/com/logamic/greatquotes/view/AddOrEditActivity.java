package com.logamic.greatquotes.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.logamic.greatquotes.App;
import com.logamic.greatquotes.R;
import com.logamic.greatquotes.model.Gender;
import com.logamic.greatquotes.model.Quote;

public class AddOrEditActivity extends AppCompatActivity {

    private boolean isEditActivity;

    private ActionBar actionBar;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Spinner genderSpinner;
    private EditText quoteEditText;


    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        genderSpinner = findViewById(R.id.gender_spinner);
        genderSpinner.setAdapter(new ArrayAdapter<Gender>(this, R.layout.gender_spinner_layout, Gender.values()));

        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        quoteEditText = findViewById(R.id.quote_edit_text);

        doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new OnDoneButtonClickListener());

        isEditActivity = getIntent().getBooleanExtra(App.INTENT_EXTRA_ADD_EDIT, false);
        if (isEditActivity == true) {
            setTitle(R.string.title_edit_quote);

            Quote currentQuote = App.get().getCurrentQuote();

            firstNameEditText.setText(currentQuote.getFirstName());
            lastNameEditText.setText(currentQuote.getLastName());
            quoteEditText.setText(currentQuote.getQuote());
            genderSpinner.setSelection(currentQuote.getGender().getCode());

            firstNameEditText.setSelectAllOnFocus(false);
            lastNameEditText.setSelectAllOnFocus(false);
            quoteEditText.setSelectAllOnFocus(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class OnDoneButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            Gender gender = (Gender) genderSpinner.getSelectedItem();
            String quote = quoteEditText.getText().toString();

            if (firstName.length() < 5 || lastName.length() < 5) {
                App.showAlertDialog(AddOrEditActivity.this, getResources().getString(R.string.error_input_title), getResources().getString(R.string.error_name_min_length));
                if(firstName.length() < 5) {
                    firstNameEditText.requestFocus();
                } else {
                    lastNameEditText.requestFocus();
                }
                return;
            }
            if (quote.length() < 10) {
                App.showAlertDialog(AddOrEditActivity.this, getResources().getString(R.string.error_input_title), getResources().getString(R.string.error_quote_min_length));
                quoteEditText.requestFocus();
                return;
            }

            if (isEditActivity == false) {

                final Quote newQuote = new Quote(firstName, lastName, gender, quote);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        App.get().addQuote(newQuote);
                    }
                }).start();

            } else {

                final Quote currentQuote = App.get().getCurrentQuote();
                currentQuote.setFirstName(firstName);
                currentQuote.setLastName(lastName);
                currentQuote.setGender(gender);
                currentQuote.setQuote(quote);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        App.get().updateQuote(currentQuote);
                    }
                }).start();
            }

            onBackPressed();
        }
    }


}
