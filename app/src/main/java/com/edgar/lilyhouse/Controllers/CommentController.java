package com.edgar.lilyhouse.Controllers;

import android.os.Handler;

import com.edgar.lilyhouse.Items.CommentItem;
import com.edgar.lilyhouse.Utils.JsonGetter;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CommentController {

    private static final CommentController instance = new CommentController();

    private CommentController() {}

    public static CommentController getInstance() {
        return instance;
    }

    public void setupComments(String urlString, Handler handler) {

        JsonGetter jsonGetter = new JsonGetter(urlString, handler);
        jsonGetter.start();

    }

    public void setupCommentsList(String urlString, Handler handler) {

        JsonGetter jsonGetter = new JsonGetter(urlString, handler);
        jsonGetter.start();

    }

    public ArrayList<CommentItem> getCommentItems(String jsonString) {
        Type listType = new TypeToken<ArrayList<CommentItem>>() {}.getType();
        ArrayList<CommentItem> items = new GsonBuilder().create().fromJson(jsonString, listType);
        return items;
    }


}
