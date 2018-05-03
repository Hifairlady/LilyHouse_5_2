package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.edgar.lilyhouse.Adapters.ReaderAdapter;
import com.edgar.lilyhouse.Controllers.ReaderController;
import com.edgar.lilyhouse.Items.ReaderImageItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.FullScreenUtil;
import com.edgar.lilyhouse.Views.ZoomRecyclerView;
import com.google.gson.Gson;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "=================" + ReaderActivity.class.getSimpleName();

    private String queryUrl, fullTitleString;
    private String[] authorsStrings;
    private String jsonString;

    private ZoomRecyclerView recyclerView;
    private ReaderAdapter readerAdapter;

    private ReaderImageItem readerImageItem;
    private int width;
    private ConstraintLayout constraintLayout;
    private DrawerLayout drawerLayout;

    private TextView tvFullTitle, tvAuthor, tvTranslator;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        queryUrl = getIntent().getStringExtra(getString(R.string.info_url_string_extra));
        fullTitleString = getIntent().getStringExtra(getString(R.string.full_title_string_extra));
        authorsStrings = getIntent().getStringArrayExtra(getString(R.string.authors_string_extra));
        readFromNetwork(queryUrl, true);

        FullScreenUtil fullScreenUtil = new FullScreenUtil(ReaderActivity.this, getWindow(), recyclerView);

        tvFullTitle = (TextView)findViewById(R.id.tv_drawer_chapter_title);
        tvAuthor = (TextView)findViewById(R.id.tv_drawer_author);
        tvTranslator = (TextView)findViewById(R.id.tv_drawer_translator);
        tvFullTitle.setText(fullTitleString);
        String authorsString = "";
        for (int i = 0; i < authorsStrings.length; i++) {
            authorsString = authorsString.concat(authorsStrings[i]);
        }
        tvAuthor.setText("Authors: " + authorsString);
        Log.d(TAG, "onCreate: " + authorsString);

        constraintLayout = (ConstraintLayout)findViewById(R.id.my_drawer_content_layout);
        drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private void readFromNetwork(String queryUrl, boolean isVertical) {

        if (isVertical) {

            recyclerView = (ZoomRecyclerView) findViewById(R.id.zlv_reader_layout);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            final Handler getImageUrlsHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {

                        case R.integer.get_data_success:

                            jsonString = ReaderController.getInstance().getJsonString();
                            if (jsonString.charAt(0) != '{' || jsonString.charAt(jsonString.length()-1) != '}') return;

                            Gson gson = new Gson();
                            readerImageItem = gson.fromJson(jsonString, ReaderImageItem.class);
                            readerAdapter = new ReaderAdapter(ReaderActivity.this,
                                    readerImageItem.getPage_url(), width);
                            tvTranslator.setText("Translator: " + readerImageItem.getTranslator());
                            readerAdapter.setHasStableIds(true);
                            recyclerView.setAdapter(readerAdapter);
                            break;

                        case R.integer.get_data_failed:
                            Snackbar.make(recyclerView, "Network Error!", Snackbar.LENGTH_SHORT).show();
                            break;

                        default:
                            break;
                    }
                }
            };

            ReaderController.getInstance().setupImageUrl(queryUrl, getImageUrlsHandler);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                GlideApp.get(ReaderActivity.this).clearDiskCache();
//            }
//        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && drawerLayout.isDrawerOpen(Gravity.END) ) {
            Log.d(TAG, "onKeyDown: key back down");
            drawerLayout.closeDrawer(Gravity.END);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
