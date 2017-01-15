package com.grishberg.graphreporter.data.rest;

/**
 * Created by grishberg on 12.01.17.
 */
public final class RestConst {
    public static final String API = "/analytics/api/v1.0/";

    // Ошибки
    public static final class Errors {
        public static final int WRONG_CREDENTIALS = 1000;
        public static final int TOKEN_EXPIRED = 1001;

        private Errors() {
        }
    }

    private RestConst() {
    }
}
