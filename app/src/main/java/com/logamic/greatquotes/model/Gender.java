package com.logamic.greatquotes.model;

import com.logamic.greatquotes.App;
import com.logamic.greatquotes.R;

public enum Gender {
    MALE(0, R.color.male_color, App.get().getResources().getString(R.string.xml_gender_male)),
    FEMALE(1, R.color.female_color, App.get().getResources().getString(R.string.xml_gender_female)),
    NOT_DEFINED(2, R.color.not_defined_color, App.get().getResources().getString(R.string.xml_gender_not_defined));

    private int code;
    private int colorResourceId;
    private String name;

    Gender(int code, int colorResourceId, String name) {
        this.code = code;
        this.colorResourceId = colorResourceId;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public int getColorResourceId() {
        return colorResourceId;
    }


    @Override
    public String toString() {
        return name;
    }
}
