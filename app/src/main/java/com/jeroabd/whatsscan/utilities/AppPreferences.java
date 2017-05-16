package com.jeroabd.whatsscan.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ralphchan on 7/5/2017.
 */

public class AppPreferences {
    public static final String PREFS_NAME = "WhatScanPrefsFile";
    public static final String FIRST_LOAD_KEY = "isFirstLoad";
    public static final String ERROR_CODE_KEY = "code";
    public static final String ERROR_TEXT_KEY = "text";
    public static final String USER_ID_KEY = "userId";
    public static final String POINT_KEY = "point";
    public static final String TOKEN_KEY = "point";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String POINTS_EARNED = "pointsEarned";

    private static final String ARE_ADS_REMOVED = "areAdsRemoved";

    public static void initialize(Context context) {
        final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        boolean isFirstLoad = settings.getBoolean(FIRST_LOAD_KEY, true);

        if (isFirstLoad) {
            RestClient.get("get_error_codes.php", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray("data");
                        SharedPreferences.Editor editor = settings.edit();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            editor.putString(row.getString(ERROR_CODE_KEY), row.getString(ERROR_TEXT_KEY));
                        }
                        editor.putBoolean(FIRST_LOAD_KEY, false);
                        editor.commit();
                    } catch (JSONException e) {

                    }
                }
            });
        }
    }

    public static String getUserId(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(USER_ID_KEY, null);
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.putInt(POINT_KEY, 0);
        editor.commit();
    }

    public static int getTotalPoints(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0).getInt(POINT_KEY, 0);
    }

    public static String getErrorText(Context context, int code) {
        return context.getSharedPreferences(PREFS_NAME, 0).getString(Integer.toString(code), "Internal Server Error");
    }

    public static void removeAds(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(ARE_ADS_REMOVED, true);
        editor.commit();
    }

    public static boolean areAdsRemoved(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(ARE_ADS_REMOVED, false);
    }
}