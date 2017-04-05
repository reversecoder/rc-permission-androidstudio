package com.reversecoder.permission.util;

import android.util.Log;

import java.util.EnumSet;

/**
 * Created by rashed on 4/5/17.
 */

public class EnumManager {

    /*
    * usage:
    *
    *  EnumManager.enumValues(PermissionRequestStatus.class);
    * */
    public static <T extends Enum<T>> void enumValues(Class<T> enumType) {
        for (T c : enumType.getEnumConstants()) {
            Log.d("Enum name is: ", c.name());
        }
    }

    public static <E extends Enum<E>> String getEnumString(Class<E> clazz, String s) {
        for (E en : EnumSet.allOf(clazz)) {
            if (en.name().equalsIgnoreCase(s)) {
                return en.name();
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }
}
