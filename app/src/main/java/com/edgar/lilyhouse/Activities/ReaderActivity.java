package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.edgar.lilyhouse.Adapters.ReaderAdapter;
import com.edgar.lilyhouse.Controllers.ReaderController;
import com.edgar.lilyhouse.Items.ReaderImageItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.ZoomListView;
import com.google.gson.Gson;

public class ReaderActivity extends AppCompatActivity {

    private String queryUrl;
    private String jsonString;

    private ReaderAdapter adapter;
    private ZoomListView listView;
//    private ScrollZoomListView listView;

    private ReaderImageItem readerImageItem;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        queryUrl = getIntent().getStringExtra(getString(R.string.info_url_string_extra));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        readFromNetwork(queryUrl, true);

    }

    @SuppressLint("HandlerLeak")
    private void readFromNetwork(String queryUrl, boolean isVertical) {

        if (isVertical) {

            listView = (ZoomListView) findViewById(R.id.zlv_reader_layout);
//            listView = (ScrollZoomListView) findViewById(R.id.zlv_reader_layout);

            Handler getImageUrlsHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {

                        case R.integer.get_data_success:

                            jsonString = ReaderController.getInstance().getJsonString();
                            if (jsonString.charAt(0) != '{' || jsonString.charAt(jsonString.length()-1) != '}') return;

                            Gson gson = new Gson();
                            readerImageItem = gson.fromJson(jsonString, ReaderImageItem.class);
                            adapter = new ReaderAdapter(ReaderActivity.this,
                                    readerImageItem.getPage_url(), width);
                            listView.setAdapter(adapter);
                            break;

                        case R.integer.get_data_failed:
                            Snackbar.make(listView, "Network Error!", Snackbar.LENGTH_SHORT).show();
                            break;

                        default:
                            break;
                    }
                }
            };

            ReaderController.getInstance().setupImageUrl(queryUrl, getImageUrlsHandler);

        }
    }

}
