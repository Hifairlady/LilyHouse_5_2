package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.lilyhouse.Adapters.SearchAdapter;
import com.edgar.lilyhouse.Controllers.SearchController;
import com.edgar.lilyhouse.Items.SearchResultItem;
import com.edgar.lilyhouse.R;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private static final String QUOTE_URL = "https://github.com/Hifairlady/LilyHouse/blob/master/quote_text-file.json";

    private static final String SEARCH_PREFIX = "https://m.dmzj.com/search/";

    private String quoteTextString, quoteKeyString;
    private TextView tvQuote;
    private SearchView searchView;
    private TextView tvSearchCount;
    private LinearLayout lvResultContainer;
    private boolean isSearching = false;
    NestedScrollView nestedScrollView;

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;

    private String searchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tvQuote = findViewById(R.id.tv_search_quote);
        tvSearchCount = findViewById(R.id.tv_search_count);
        tvSearchCount.setVisibility(View.GONE);
        searchView = findViewById(R.id.my_search_view);
        recyclerView = findViewById(R.id.rv_search_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        setupQuoteText();
        customSearchView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (!isSearching) {
                    isSearching = true;
                    searchUrl = URLEncoder.encode(query);
                    searchUrl = SEARCH_PREFIX + searchUrl + ".html";
                    SearchController.getInstance().setupSearchData(searchUrl, getSearchHandler);
                } else {
                    Snackbar.make(lvResultContainer, R.string.searching_string, Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tvSearchCount.setVisibility(View.GONE);
                return false;
            }
        });

    }

    private void customSearchView() {
        AutoCompleteTextView searchTextView = searchView.findViewById(R.id.search_src_text);
        if (searchTextView == null) {
            return;
        }

        searchTextView.setTextAppearance(SearchActivity.this, R.style.SearchTextStyle);
        searchTextView.setTextColor(getResources().getColor(R.color.primary_text));
        searchTextView.setHintTextColor(getResources().getColor(R.color.secondary_text));
        searchTextView.setHint(R.string.search_hint_string);
    }

    private String getDateString(long lastUpdateTime) {
        long times = lastUpdateTime * 1000;
        Date date = new Date(times);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    private String getQuoteText(String quoteKeyString) {
        SharedPreferences quoteLocalStorage = getSharedPreferences("quoteText", MODE_PRIVATE);
        return quoteLocalStorage.getString(quoteKeyString, null);
    }

    private void setupQuoteText() {
        String dateString;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateString = dateFormat.format(date);
        quoteKeyString = "QUOTE " + dateString;
        quoteTextString = getQuoteText(quoteKeyString);

        if (quoteTextString == null) {
            SearchController.getInstance().setupSearchUI(QUOTE_URL, getQuoteHandler);
            quoteTextString = getQuoteText("QUOTE DEFAULT");
            if (quoteTextString == null) {
                quoteTextString = getString(R.string.search_quote_text_string);
            }
            storeQuoteText(quoteKeyString, quoteTextString, true);
        } else {
            storeQuoteText(quoteKeyString, quoteTextString, false);
        }
        tvQuote.setText(quoteTextString);
    }

    private void storeQuoteText(String quoteKeyString, String quoteText, boolean isDefault) {
        SharedPreferences quoteLocalStorage = getSharedPreferences("quoteText", MODE_PRIVATE);
        SharedPreferences.Editor editor = quoteLocalStorage.edit();
        editor.putString("QUOTE DEFAULT", quoteText);
        if (!isDefault) {
            editor.putString(quoteKeyString, quoteText);
        }
        editor.apply();
    }

    //handle when get the quote text string

    @SuppressLint("HandlerLeak")
    private Handler getQuoteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case R.integer.get_data_success:
                    storeQuoteText(quoteKeyString, SearchController.getInstance().getQuoteText(), false);
                    tvQuote.setText(SearchController.getInstance().getQuoteText());

                    break;

                case R.integer.get_data_failed:
                    break;

                default:
                    break;
            }

        }
    };

    @SuppressLint("HandlerLeak")
    private Handler getSearchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case R.integer.get_data_success:
                    tvSearchCount.setText(getString(R.string.search_result_count_string,
                            SearchController.getInstance().getSearchResultItems().size()));
                    tvSearchCount.setVisibility(View.VISIBLE);
                    searchAdapter = new SearchAdapter(SearchActivity.this,
                            SearchController.getInstance().getSearchResultItems());
                    searchAdapter.setItemClickListener(itemClickListener);
                    recyclerView.setAdapter(searchAdapter);

                    isSearching = false;
                    break;

                case R.integer.get_data_failed:
                    break;

                default:
                    break;
            }
        }
    };

    private SearchAdapter.SearchItemClickListener itemClickListener = new SearchAdapter.SearchItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SearchResultItem resultItem = SearchController.getInstance().getSearchResultItems().get(position);
            Intent infoIntent = new Intent(SearchActivity.this, MangaActivity.class);
            infoIntent.putExtra(getString(R.string.info_title_string_extra), resultItem.getName());
            String urlString = "https://m.dmzj.com/info/" + resultItem.getId() + ".html";
            String backuUrl = "https://m.dmzj.com/info/" + resultItem.getComic_py() + ".html";
            infoIntent.putExtra(getString(R.string.info_url_string_extra), urlString);
            infoIntent.putExtra(getString(R.string.back_up_url_string_extra), backuUrl);
            startActivity(infoIntent);
        }
    };

}
