package com.grishberg.graphreporter.data.rest;

/**
 * Created by grishberg on 12.01.17.
 */
public class RestConst {
    private static final String TAG = RestConst.class.getSimpleName();
    public final static String END_POINT = "http://127.0.0.1:5000";
    //public final static String END_POINT = "https://Grishberg.pythonanywhere.com";
    public static final String API = END_POINT + "/analytics/api/v1.0/";

    // Ошибки
    public static final class Errors {
        public static final int WRONG_CREDENTIALS = 1000;
        public static final int TOKEN_INVALID = 1002;
    }
    private RestConst(){
    }

}
