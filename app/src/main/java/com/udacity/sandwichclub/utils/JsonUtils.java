package com.udacity.sandwichclub.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    @Nullable
    public static Sandwich parseSandwichJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject nameNode = json.getJSONObject(Sandwich.NAME);

            String mainName = nameNode.getString(Sandwich.MAIN_NAME);
            List<String> alsoKnownAs = parseStringArray(nameNode.getJSONArray(Sandwich.AKA));
            String placeOfOrigin = json.getString(Sandwich.ORIGIN);
            String description = json.getString(Sandwich.DESCRIPTION);
            String imageUrl = json.getString(Sandwich.IMAGE_URL);
            List<String> ingredients = parseStringArray(json.getJSONArray(Sandwich.INGREDIENTS));

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, imageUrl, ingredients);
        } catch (JSONException error) {
            logThrowable(error);
            return null;
        } catch (Error error) {
            logThrowable(error);
            return null;
        }
    }

    private static List<String> parseStringArray(JSONArray jsonArray) throws JSONException {
        List<String> strings = new ArrayList<>();

        if (null != jsonArray) {
            for (int index = 0; index < jsonArray.length(); index++) {
                strings.add(jsonArray.getString(index));
            }
        }

        return strings;
    }

    private static void logThrowable(Throwable throwable) {
        Log.e(TAG, throwable.getMessage());
        throwable.printStackTrace();
    }
}
