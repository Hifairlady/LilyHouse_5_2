package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edgar.lilyhouse.Adapters.MangaAdapter;
import com.edgar.lilyhouse.Controllers.AuthorController;
import com.edgar.lilyhouse.R;

public class AuthorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_detail);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String titleString = getIntent().getStringExtra(getString(R.string.info_title_string_extra));
        String queryUrl = getIntent().getStringExtra(getString(R.string.info_url_string_extra));
        toolbar.setTitle(titleString);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.rv_author_chapters_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        AuthorController.getInstance().setupMangaList(queryUrl, getMangasHandler);
    }

    MangaAdapter.ItemClickListener itemClickListener = new MangaAdapter.ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent infoIntent = new Intent(AuthorActivity.this, MangaActivity.class);
            infoIntent.putExtra(getString(R.string.info_title_string_extra),
                    AuthorController.getInstance().getMangaItems().get(position).getName());
            String urlString = AuthorController.getInstance().getMangaItems()
                    .get(position).getQueryInfoUrl();
            infoIntent.putExtra(getString(R.string.info_url_string_extra), urlString);
            startActivity(infoIntent);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler getMangasHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case R.integer.get_data_success:
                    MangaAdapter adapter = new MangaAdapter(AuthorActivity.this,
                            AuthorController.getInstance().getMangaItems(), true);
                    adapter.setHasStableIds(true);
                    adapter.setOnItemClickListener(itemClickListener);
                    recyclerView.setAdapter(adapter);
                    break;

                case R.integer.get_data_failed:
                    break;

                default:
                    break;
            }
        }
    };


}
