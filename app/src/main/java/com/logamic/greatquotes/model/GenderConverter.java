package com.logamic.greatquotes.model;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by oravecv on 02/09/18.
 */

public class GenderConverter {

    @TypeConverter
    public static Gender toGender(int code) {
        if (code == Gender.MALE.getCode()) {
            return Gender.MALE;
        }
        else if  (code == Gender.FEMALE.getCode()) {
            return Gender.FEMALE;
        }
        else {
            return Gender.NOT_DEFINED;
        }
    }

    @TypeConverter
    public static int toInteger(Gender gender) {
        return gender.getCode();
    }

}
