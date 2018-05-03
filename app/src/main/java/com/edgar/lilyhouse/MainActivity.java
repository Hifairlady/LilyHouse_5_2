package com.edgar.lilyhouse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.edgar.lilyhouse.Activities.MangaActivity;
import com.edgar.lilyhouse.Activities.SearchActivity;
import com.edgar.lilyhouse.Adapters.MangaAdapter;
import com.edgar.lilyhouse.Controllers.MainController;
import com.edgar.lilyhouse.Items.MangaItem;
import com.edgar.lilyhouse.Utils.PermissionRequester;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int curStatusCode = 0, curRegionCode = 0, curOrderCode = 0, curPageNum = 0;
    private static boolean isExit = false;

    private static final int REQUEST_PERMISSION_STORAGE_CODE = 105;

    private String curStatusString = "全部";
    private String curRegionString = "全部";
    private String curOrderString = "默认";

    private TextView tvFilterStatus;
    private TextView tvFilterRegion;
    private TextView tvFilterOrder;

    private LinearLayout btnStatus;
    private LinearLayout btnOrder;
    private LinearLayout btnRegion;

    private FloatingActionButton fabTop;

    private Dialog filterDialog;
    private String queryUrl;
    private boolean isLoadingNextPage = false;

    private int scrollDistance = 0;

    private UltimateRecyclerView recyclerView;
    private MangaAdapter listAdapter;
    private ArrayList<MangaItem> mangaItems = new ArrayList<>();

    private boolean isGridOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_app_logo);

        fabTop = (FloatingActionButton)findViewById(R.id.my_fab);
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.layout_popup_filter);
        filterDialog.setCanceledOnTouchOutside(true);

        btnStatus = filterDialog.findViewById(R.id.btn_filter_by_status);
        btnOrder = filterDialog.findViewById(R.id.btn_filter_by_order);
        btnRegion = filterDialog.findViewById(R.id.btn_filter_by_region);

        btnStatus.setOnClickListener(mOnClickListener);
        btnOrder.setOnClickListener(mOnClickListener);
        btnRegion.setOnClickListener(mOnClickListener);

        fabTop.bringToFront();
        fabTop.hide();
        fabTop.setOnClickListener(mOnClickListener);

        setupFilters();

        Button btnFilterDismiss = filterDialog.findViewById(R.id.btn_filter_dismiss);
        Button btnFilterApply = filterDialog.findViewById(R.id.btn_filter_apply);

        btnFilterDismiss.setOnClickListener(mOnClickListener);
        btnFilterApply.setOnClickListener(mOnClickListener);

        recyclerView = (UltimateRecyclerView)findViewById(R.id.rv_main_manga);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new MangaAdapter(this, mangaItems, true);
        setupRecyclerView();

        queryUrl = MainController.getInstance().getSortUrl(curStatusCode, curRegionCode, curOrderCode, curPageNum);
        MainController.getInstance().loadMoreData(queryUrl, loadMoreHandler);

        PermissionRequester.request(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getString(R.string.request_rationale_string), REQUEST_PERMISSION_STORAGE_CODE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.main_menu_action_filter:
                filterDialog.show();
                break;

            case R.id.main_menu_action_grid:
                isGridOn = !isGridOn;
                scrollDistance = 0;
                fabTop.hide();
                if (!isGridOn) {
                    item.setIcon(R.drawable.ic_grid_on);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    listAdapter = new MangaAdapter(MainActivity.this, mangaItems, isGridOn);
                } else {
                    item.setIcon(R.drawable.ic_grid_off);
                    GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
                    recyclerView.setLayoutManager(layoutManager);
                    listAdapter = new MangaAdapter(MainActivity.this, mangaItems, isGridOn);
                }

                setupRecyclerView();
                break;

            case R.id.main_menu_action_search:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private MangaAdapter.ItemClickListener itemClickListener = new MangaAdapter.ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent infoIntent = new Intent(MainActivity.this, MangaActivity.class);
            infoIntent.putExtra(getString(R.string.info_title_string_extra), mangaItems.get(position).getName());
            String urlString = "https://m.dmzj.com/info/" +
                    mangaItems.get(position).getId() + ".html";
            String backupUrl = "https://m.dmzj.com/info/" +
                    mangaItems.get(position).getComic_py() + ".html";
            infoIntent.putExtra(getString(R.string.info_url_string_extra), urlString);
            infoIntent.putExtra(getString(R.string.back_up_url_string_extra), backupUrl);
            startActivity(infoIntent);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.my_fab:
                    recyclerView.getLayoutManager().scrollToPosition(0);
                    scrollDistance = 0;
                    fabTop.hide();
                    break;

                case R.id.btn_filter_by_status:
                    PopupMenu popupStatusMenu = new PopupMenu(MainActivity.this, btnStatus);
                    popupStatusMenu.getMenuInflater().inflate(R.menu.filter_status_menu, popupStatusMenu.getMenu());
                    popupStatusMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            tvFilterStatus.setText(item.getTitle());
                            curStatusString = item.getTitle().toString();
                            curStatusCode = item.getOrder();
                            return true;
                        }
                    });
                    popupStatusMenu.show();
                    break;

                case R.id.btn_filter_by_region:
                    PopupMenu popupRegionMenu = new PopupMenu(MainActivity.this, btnRegion);
                    popupRegionMenu.getMenuInflater().inflate(R.menu.filter_region_menu, popupRegionMenu.getMenu());
                    popupRegionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            tvFilterRegion.setText(item.getTitle());
                            curRegionString = item.getTitle().toString();
                            curRegionCode = item.getOrder();
                            return true;
                        }
                    });
                    popupRegionMenu.show();
                    break;

                case R.id.btn_filter_by_order:
                    PopupMenu popupOrderMenu = new PopupMenu(MainActivity.this, btnOrder);
                    popupOrderMenu.getMenuInflater().inflate(R.menu.filter_order_menu, popupOrderMenu.getMenu());
                    popupOrderMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            tvFilterOrder.setText(item.getTitle());
                            curOrderString = item.getTitle().toString();
                            curOrderCode = item.getOrder();
                            return true;
                        }
                    });
                    popupOrderMenu.show();
                    break;

                case R.id.btn_filter_dismiss:
                    setupFilters();
                    filterDialog.dismiss();
                    break;

                case R.id.btn_filter_apply:
                    applyFilters();
                    filterDialog.dismiss();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO: 2018\4\30 remove this after test
        super.onDestroy();
        MainController.getInstance().clearSharedPreference(this);
    }

    private void setupFilters() {

        curStatusCode = MainController.getInstance().getStoredIndices(MainActivity.this, "curStatusCode");
        curRegionCode = MainController.getInstance().getStoredIndices(MainActivity.this, "curRegionCode");
        curOrderCode = MainController.getInstance().getStoredIndices(MainActivity.this, "curOrderCode");

        curStatusString = MainController.getInstance().getStoredNames(MainActivity.this, "curStatusString");
        curRegionString = MainController.getInstance().getStoredNames(MainActivity.this, "curRegionString");
        curOrderString = MainController.getInstance().getStoredNames(MainActivity.this, "curOrderString");

        curStatusString = (curStatusString == null ? "全部" : curStatusString);
        curRegionString = (curRegionString == null ? "全部" : curRegionString);
        curOrderString = (curOrderString == null ? "默认" : curOrderString);

        tvFilterStatus = filterDialog.findViewById(R.id.filter_status_textview);
        tvFilterRegion = filterDialog.findViewById(R.id.filter_region_textview);
        tvFilterOrder = filterDialog.findViewById(R.id.filter_order_textview);

        tvFilterStatus.setText(curStatusString);
        tvFilterRegion.setText(curRegionString);
        tvFilterOrder.setText(curOrderString);


    }

    private void applyFilters() {
        curPageNum = 0;
        queryUrl = MainController.getInstance().getSortUrl(curStatusCode, curRegionCode,
                curOrderCode, curPageNum);

        String[] filterNames = {curStatusString, curRegionString, curOrderString};
        int[] filterIndices = {curStatusCode, curRegionCode, curOrderCode};

        MainController.getInstance().storeFilterNames(MainActivity.this, filterNames,
                MainController.storeStringKeys);
        MainController.getInstance().storeFilterIndices(MainActivity.this, filterIndices,
                MainController.storeIntegerKeys);

        queryUrl = MainController.getInstance().getSortUrl(curStatusCode, curRegionCode, curOrderCode, curPageNum);
        MainController.getInstance().refreshData(queryUrl, refreshHandler);

    }

    private void setupRecyclerView() {

        listAdapter.setOnItemClickListener(itemClickListener);
        listAdapter.setHasStableIds(true);
        recyclerView.setAdapter(listAdapter);
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
                    curPageNum++;
                    isLoadingNextPage = true;
//                    Snackbar.make(recyclerView, "Loading next page...", Snackbar.LENGTH_SHORT).show();
                    queryUrl = MainController.getInstance().getSortUrl(curStatusCode, curRegionCode,
                            curOrderCode, curPageNum);
                    MainController.getInstance().loadMoreData(queryUrl, loadMoreHandler);
                }
            }
        });

        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPageNum = 0;
                queryUrl = MainController.getInstance().getSortUrl(curStatusCode, curRegionCode,
                        curOrderCode, curPageNum);
                MainController.getInstance().refreshData(queryUrl, refreshHandler);
            }
        });

        recyclerView.setDefaultSwipeToRefreshColorScheme(0x000000);

    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (isLoadingNextPage) return false;
        if (recyclerView == null) return false;
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange();
    }

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isLoadingNextPage = false;
            switch (msg.what) {
                case R.integer.get_data_success:
                    String jsonString = (String)msg.obj;
                    recyclerView.setRefreshing(false);
                    ArrayList<MangaItem> items = MainController.getInstance().getDataFromJson(jsonString);
                    if (items != null) {

                        mangaItems = new ArrayList<>(items);
                        listAdapter = new MangaAdapter(MainActivity.this, mangaItems, isGridOn);
                        listAdapter.setOnItemClickListener(itemClickListener);
                        listAdapter.setHasStableIds(true);
                        recyclerView.setAdapter(listAdapter);
                    } else {
                        Snackbar.make(recyclerView, R.string.no_data_string, Snackbar.LENGTH_SHORT).show();
                    }
                    break;

                case R.integer.get_data_failed:
                    String errorMsg = (String)msg.obj;
                    recyclerView.setRefreshing(false);
                    Snackbar.make(recyclerView, errorMsg, Snackbar.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak") Handler loadMoreHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case R.integer.get_data_success:
                    String jsonString = (String)msg.obj;
                    isLoadingNextPage = false;
                    ArrayList<MangaItem> items = MainController.getInstance().getDataFromJson(jsonString);
                    if (items != null) {
                        mangaItems.addAll(items);
                        listAdapter.notifyDataSetChanged();
                    } else {
                        isLoadingNextPage = true;
                        Snackbar.make(recyclerView, R.string.has_loaded_all_string, Snackbar.LENGTH_SHORT).show();
                    }
                    break;

                case R.integer.get_data_failed:
                    String errorMsg = (String)msg.obj;
                    isLoadingNextPage = false;
                    Snackbar.make(recyclerView, errorMsg, Snackbar.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    //handle double click return key to exit
    @SuppressLint("HandlerLeak")
    private Handler exitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == R.integer.exit_program) {
                isExit = false;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Snackbar.make(recyclerView, R.string.exit_double_click_string, Snackbar.LENGTH_SHORT).show();
            // exit after 2 seconds
            exitHandler.sendEmptyMessageDelayed(R.integer.exit_program, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE_CODE) {
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, R.string.request_permission_failed_string, Toast.LENGTH_SHORT).show();
                Snackbar.make(recyclerView, R.string.request_permission_failed_string, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
