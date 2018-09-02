package com.logamic.greatquotes;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.logamic.greatquotes.R;
import com.logamic.greatquotes.model.Gender;
import com.logamic.greatquotes.model.Quote;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oravecv on 01/09/18.
 */

public class QuotesXmlParser {

    private static final String ns = null;
    private Context context;

    public QuotesXmlParser(Context context) {
        this.context = context;
    }

    public List<Quote> getQuotesListFromXml() throws IOException, XmlPullParserException{
        return parse(context.getResources().openRawResource(R.raw.quotes));
    }

    private List<Quote> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readQuotes(parser);
        } finally {
            in.close();
        }
    }


    private List<Quote> readQuotes(XmlPullParser parser) throws XmlPullParserException, IOException {
        List quotes = new ArrayList<Quote>();

        parser.require(XmlPullParser.START_TAG, ns, getResourceString(R.string.xml_root_element));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            parser.require(XmlPullParser.START_TAG, ns, getResourceString(R.string.xml_quote_element));
            quotes.add(readQuote(parser));

        }
        return quotes;
    }

    private Quote readQuote(XmlPullParser parser) throws IOException, XmlPullParserException {
        String firstName = parser.getAttributeValue(null, getResourceString(R.string.xml_first_name_attribute));
        String lastName = parser.getAttributeValue(null, getResourceString(R.string.xml_last_name_attribute));

        Gender gender;
        String genderString = parser.getAttributeValue(null, getResourceString(R.string.xml_gender_attribute));
        if (getResourceString(R.string.xml_gender_male).equals(genderString)) {
            gender = Gender.MALE;
        } else if (getResourceString(R.string.xml_gender_female).equals(genderString)) {
            gender = Gender.FEMALE;
        } else {
            gender = Gender.NOT_DEFINED;
        }

        String quoteString = readText(parser);

        Quote quote = new Quote(firstName, lastName, gender, quoteString);
        Log.d(getClass().getSimpleName(), "readQuote() '" + quote.toString() + "'");

        return quote;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String getResourceString(int resourceId) {
        return context.getResources().getString(resourceId);
    }
}
