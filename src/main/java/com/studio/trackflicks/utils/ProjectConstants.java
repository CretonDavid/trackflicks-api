package com.studio.trackflicks.utils;

import java.util.Locale;

public class ProjectConstants {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Locale ENGLISH_LOCALE = new Locale.Builder().setLanguage("en").setRegion("US").build();

    public static final Locale FRENCH_LOCALE = new Locale.Builder().setLanguage("fr").setRegion("FR").build();

    private ProjectConstants() {

        throw new UnsupportedOperationException();
    }

}
