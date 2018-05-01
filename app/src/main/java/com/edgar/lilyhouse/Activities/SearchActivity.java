package com.edgar.lilyhouse.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.lilyhouse.Items.SearchResultItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.ImageUtil;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static final String QUOTE_URL = "https://github.com/Hifairlady/YuriHouse/blob/master/quote_text_file.json";

    private static final String SEARCH_PREFIX = "https://m.dmzj.com/search/";

    private String quoteTextString, quoteKeyString;
    private TextView tvQuote;
    private SearchView searchView;
    private TextView tvSearchCount;
    private LinearLayout lvResultContainer;
    private ImageUtil imageUtil;
    private boolean isSearching = false;

//    private Handler getQuoteHandler, getSearchHandler;
    private ArrayList<SearchResultItem> searchResultItems = new ArrayList<>();

    private String searchUrl;
//    private ParseHtmlThread parseSearchThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
