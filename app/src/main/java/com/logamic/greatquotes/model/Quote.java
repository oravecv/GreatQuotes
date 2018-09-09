package com.logamic.greatquotes.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by oravecv on 01/09/18.
 */

@Entity
public class Quote {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "quote_id")
    private long quoteId;

    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;

    @TypeConverters(GenderConverter.class)
    @ColumnInfo(name = "gender")
    private Gender gender;

    @ColumnInfo(name = "quote")
    private String quote;


    public Quote(String firstName, String lastName, Gender gender, String quote) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.quote = quote;
    }

    public long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(long quoteId) {
        this.quoteId = quoteId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + gender + "): " + "'" + quote + "'";
    }

    public String getAuthor() {
        return firstName + " " + lastName;
    }
}
