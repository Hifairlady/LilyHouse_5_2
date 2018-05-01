package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.lilyhouse.Controllers.SearchDataController;
import com.edgar.lilyhouse.Items.SearchResultItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.ImageUtil;

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
    private ImageUtil imageUtil;
    private ImageView ivBackground;
    private boolean isSearching = false;

    private String searchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        imageUtil = new ImageUtil(this);

        tvQuote = findViewById(R.id.tv_search_quote);
        tvSearchCount = findViewById(R.id.tv_search_count);
        tvSearchCount.setVisibility(View.GONE);
        lvResultContainer = findViewById(R.id.lv_search_result_container);
        ivBackground = findViewById(R.id.iv_search_bg);
        searchView = findViewById(R.id.my_search_view);

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
                    SearchDataController.getInstance().setupSearchData(searchUrl, getSearchHandler);
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SharedPreferences quoteLocalStorage = getSharedPreferences("quoteText", MODE_PRIVATE);
//        SharedPreferences.Editor editor = quoteLocalStorage.edit();
//        editor.clear();
//        editor.apply();
//    }

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
            SearchDataController.getInstance().setupSearchUI(QUOTE_URL, getQuoteHandler);
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
                    storeQuoteText(quoteKeyString, SearchDataController.getInstance().getQuoteText(), false);
                    tvQuote.setText(SearchDataController.getInstance().getQuoteText());

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
                    String countString = "共 " + SearchDataController.getInstance()
                            .getSearchResultItems().size() + " 条搜索结果";
                    tvSearchCount.setText(countString);
                    tvSearchCount.setVisibility(View.VISIBLE);
                    lvResultContainer.removeAllViews();
                    for (int i = 0; i < SearchDataController.getInstance()
                            .getSearchResultItems().size(); i++) {
                        addViewToContainer(lvResultContainer,
                                SearchDataController.getInstance().getSearchResultItems().get(i));
                    }
                    isSearching = false;
                    break;

                case R.integer.get_data_failed:
                    break;

                default:
                    break;
            }
        }
    };

    private void addViewToContainer(LinearLayout lvContainer, final SearchResultItem resultItem) {

        View view = LayoutInflater.from(this).inflate(R.layout.item_search_result, null);

        CardView cardView = view.findViewById(R.id.search_item_root);
        TextView tvTitle, tvAuthors, tvTypes, tvTime, tvChapter;
        ImageView ivCover, ivFinishedLogo;

        tvTitle = view.findViewById(R.id.tv_search_item_title);
        tvAuthors = view.findViewById(R.id.tv_search_item_authors);
        tvTypes = view.findViewById(R.id.tv_search_item_types);
        tvTime = view.findViewById(R.id.tv_search_item_time);
        tvChapter = view.findViewById(R.id.tv_search_item_chapter);
        ivCover = view.findViewById(R.id.cover_image);
        ivFinishedLogo = view.findViewById(R.id.cover_finished_logo);

        String urlString = resultItem.getCover();
        if (!urlString.startsWith("https")) {
            urlString = "https://images.dmzj.com/" + urlString;
        }
        imageUtil.setImageView(ivCover, urlString);

        tvTitle.setText(resultItem.getName());
        tvAuthors.setText(resultItem.getAuthors());
        tvTypes.setText(resultItem.getTypes());
        tvChapter.setText(resultItem.getLast_update_chapter_name());
        String dateString = getDateString(resultItem.getLast_updatetime());
        tvTime.setText(dateString);

        if (resultItem.getStatus().equals("连载中")) {
            ivFinishedLogo.setVisibility(View.GONE);
        } else {
            ivFinishedLogo.setVisibility(View.VISIBLE);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(SearchActivity.this, MangaDetailActivity.class);
                infoIntent.putExtra(getString(R.string.info_title_string_extra), resultItem.getName());
                String urlString = "https://m.dmzj.com/info/" + resultItem.getId() + ".html";
                infoIntent.putExtra(getString(R.string.info_url_string_extra), urlString);
                startActivity(infoIntent);
            }
        });

        lvContainer.addView(view);
    }
}
