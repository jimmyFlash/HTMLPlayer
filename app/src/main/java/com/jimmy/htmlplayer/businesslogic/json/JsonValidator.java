package com.jimmy.htmlplayer.businesslogic.json;

import org.json.JSONObject;

/**
 * Created by mvp on 2/26/2016.
 */
public class JsonValidator {

    public static  Boolean contains (JSONObject jsonObject, String key){

        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key);
    }
}
