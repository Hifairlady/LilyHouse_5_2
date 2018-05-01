package com.edgar.lilyhouse.Controllers;

import android.os.Handler;

import com.edgar.lilyhouse.Utils.JsonGetter;

public class CommentDataController {

    private static final CommentDataController instance = new CommentDataController();

    private CommentDataController() {}

    public static CommentDataController getInstance() {
        return instance;
    }

    public void setupComments(String urlString, Handler handler) {

        JsonGetter jsonGetter = new JsonGetter(urlString, handler);
        jsonGetter.start();

    }

}
