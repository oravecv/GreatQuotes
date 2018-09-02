package com.logamic.greatquotes.view;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.logamic.greatquotes.R;
import com.logamic.greatquotes.model.QuotesDatabase;
import com.logamic.greatquotes.QuotesXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_quote);

    }

}
