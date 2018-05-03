package com.edgar.lilyhouse.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.GlideUtil;
import com.github.chrisbanes.photoview.PhotoView;

public class CommentImageActivity extends AppCompatActivity {

    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_image);

        urlString = getIntent().getStringExtra(getString(R.string.info_url_string_extra));

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("图片详情");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PhotoView photoView = (PhotoView)findViewById(R.id.pv_comment_image);
        GlideUtil.setScaledImage(this, urlString, photoView, width);

    }
}
