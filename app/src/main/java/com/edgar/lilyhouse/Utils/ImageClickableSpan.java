package com.edgar.lilyhouse.Utils;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;

import com.edgar.lilyhouse.Activities.CommentImageActivity;
import com.edgar.lilyhouse.R;

public class ImageClickableSpan extends ClickableSpan {

    private static final String TAG = "======================" + ImageClickableSpan.class.getSimpleName();
    private Context context;
    private String urlString;

    public ImageClickableSpan(Context context, String urlString) {
        this.context = context;
        this.urlString = urlString;
    }

    @Override
    public void onClick(View widget) {
//        Log.d(TAG, "onClick: Image is Clicked");
        Intent imageIntent = new Intent(context, CommentImageActivity.class);
        imageIntent.putExtra(context.getString(R.string.info_url_string_extra), urlString);
        context.startActivity(imageIntent);
    }
}
