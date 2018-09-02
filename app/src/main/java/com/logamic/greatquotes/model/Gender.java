package com.logamic.greatquotes.model;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by oravecv on 01/09/18.
 */

public enum Gender {
    MALE(0),
    FEMALE(1),
    NOT_DEFINED(2);

    private int code;

    Gender(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
