package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.edgar.lilyhouse.Adapters.CommentAdapter;
import com.edgar.lilyhouse.Controllers.CommentController;
import com.edgar.lilyhouse.Items.CommentItem;
import com.edgar.lilyhouse.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    private static final String TAG = "=======================" + CommentsActivity.class.getSimpleName();

    private boolean isActivityDestroyed = false;

    private String queryUrl;
    private int curPage = 1;
    private int scrollDistance = 0;

    private boolean isLoadingNextPage = false;

    private ArrayList<CommentItem> commentItems = new ArrayList<>();

    private UltimateRecyclerView recyclerView;
    private CommentAdapter adapter;
    private boolean fromRefresh = false;

    private FloatingActionButton fabTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        queryUrl = getIntent().getStringExtra("queryUrl");
        String titleString = getIntent().getStringExtra("titleString");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(titleString);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabTop = (FloatingActionButton)findViewById(R.id.fab_comments_top);
        fabTop.bringToFront();
        fabTop.setOnClickListener(mOnClickListener);
        fabTop.hide();

        recyclerView = (UltimateRecyclerView)findViewById(R.id.rv_comments_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new CommentAdapter(this, commentItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setDefaultSwipeToRefreshColorScheme(0x000000);
        recyclerView.setRefreshing(false);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setRefreshing(true);
                fromRefresh = true;
                curPage = 1;
                scrollDistance = 0;
                isLoadingNextPage = true;
                String urlString = getUrlString(queryUrl, curPage);
                CommentController.getInstance().setupCommentsList(urlString, getCommentsHandler);
                Log.d(TAG, "onRefresh: " + urlString);
                Snackbar.make(recyclerView, "Loading next page...", Snackbar.LENGTH_SHORT).show();

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDistance = scrollDistance + dy;

                if (scrollDistance > 10000 && !fabTop.isShown()) {
                    fabTop.show();
                } else if (scrollDistance <= 10000 && fabTop.isShown()) {
                    fabTop.hide();
                }

                if (isSlideToBottom(recyclerView) && !isLoadingNextPage) {
                    curPage++;
                    isLoadingNextPage = true;
                    String urlString = getUrlString(queryUrl, curPage);
                    CommentController.getInstance().setupCommentsList(urlString, getCommentsHandler);
                    Snackbar.make(recyclerView, R.string.loading_next_page_string, Snackbar.LENGTH_SHORT).show();

                }
            }
        });


        CommentController.getInstance().setupCommentsList(queryUrl, getCommentsHandler);

    }

    @SuppressLint("HandlerLeak")
    private Handler getCommentsHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isLoadingNextPage = false;
            recyclerView.setRefreshing(false);
            switch (msg.what) {

                case R.integer.get_data_success:

                    if (isActivityDestroyed) return;

                    String jsonString = (String)msg.obj;
                    int startPos = jsonString.indexOf("[");
                    int endPos = jsonString.lastIndexOf("]") + 1;
                    jsonString = jsonString.substring(startPos, endPos);
                    if (jsonString.charAt(0) != '[' && jsonString.charAt(jsonString.length()-1) != ']') {
                        return;
                    }
                    ArrayList<CommentItem> items = CommentController.getInstance().getCommentItems(jsonString);
                    if (items == null || items.size() == 0) {
                        Snackbar.make(recyclerView, getString(R.string.no_data_string), Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (fromRefresh) commentItems.clear();
                    commentItems.addAll(items);
                    adapter.notifyDataSetChanged();
                    fromRefresh = false;
                    break;

                case R.integer.get_data_failed:

                    if (isActivityDestroyed) return;
                    Snackbar.make(recyclerView, "Network Error!", Snackbar.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (isLoadingNextPage) return false;
        if (recyclerView == null) return false;
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange();
    }

    private String getUrlString(String srcUrl, int pageNum) {
        int startPos = srcUrl.indexOf("page_index=") + 11;
        int endPos = srcUrl.indexOf("&_=");
        String prefix = srcUrl.substring(0, startPos);
        String suffix = srcUrl.substring(endPos);
        return prefix + String.valueOf(pageNum) + suffix;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityDestroyed = true;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.fab_comments_top:
                    fabTop.hide();
                    scrollDistance = 0;
                    recyclerView.scrollVerticallyToPosition(0);
                    break;

                default:
                    break;
            }
        }
    };

}
