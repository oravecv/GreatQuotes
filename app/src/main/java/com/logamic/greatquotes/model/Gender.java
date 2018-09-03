package com.logamic.greatquotes.model;

import com.logamic.greatquotes.R;

public enum Gender {
    MALE(0, R.color.male_color),
    FEMALE(1, R.color.female_color),
    NOT_DEFINED(2, R.color.not_defined_color);

    private int code;
    private int colorResourceId;

    Gender(int code, int colorResourceId) {
        this.code = code;
        this.colorResourceId = colorResourceId;
    }

    public int getCode() {
        return code;
    }

    public int getColorResourceId() {
        return colorResourceId;
    }
}
