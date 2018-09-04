package com.logamic.greatquotes.view;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.logamic.greatquotes.App;
import com.logamic.greatquotes.R;
import com.logamic.greatquotes.model.Quote;


class QuoteListAdapter extends BaseAdapter {

    Context context;

    public QuoteListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return App.get().getQuotesListSize();
    }

    @Override
    public Object getItem(int position) {
        return App.get().getQuotesList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return App.get().getQuotesList().get(position).getQuoteId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.quote_item, parent, false);
        }

        final Quote quote = App.get().getQuotesList().get(position);

        TextView quoteTextView = (TextView) convertView.findViewById(R.id.quote_text);
        quoteTextView.setText(quote.getQuote());

        TextView authorTextView = (TextView) convertView.findViewById(R.id.author_text);
        authorTextView.setText(quote.getAuthor());
        authorTextView.setTextColor(context.getResources().getColor(quote.getGender().getColorResourceId()));

        return convertView;
    }

}
