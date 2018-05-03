package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.WindowManager;

import com.edgar.lilyhouse.Adapters.ReaderAdapter;
import com.edgar.lilyhouse.Controllers.ReaderController;
import com.edgar.lilyhouse.Items.ReaderImageItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.FullScreenUtil;
import com.edgar.lilyhouse.Views.ZoomRecyclerView;
import com.google.gson.Gson;

public class ReaderActivity extends AppCompatActivity {

    private String queryUrl;
    private String jsonString;

    private ZoomRecyclerView recyclerView;
    private ReaderAdapter readerAdapter;

    private ReaderImageItem readerImageItem;
    private int width, height;
    private FullScreenUtil fullScreenUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        queryUrl = getIntent().getStringExtra(getString(R.string.info_url_string_extra));
        readFromNetwork(queryUrl, true);

        fullScreenUtil = new FullScreenUtil(ReaderActivity.this, getWindow(), recyclerView);
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
}
