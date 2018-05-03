package com.edgar.lilyhouse.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.lilyhouse.Adapters.ViewPagerAdapter;
import com.edgar.lilyhouse.Controllers.MangaController;
import com.edgar.lilyhouse.Fragments.ChaptersFragment;
import com.edgar.lilyhouse.Fragments.CommentsFragment;
import com.edgar.lilyhouse.Fragments.RelatedFragment;
import com.edgar.lilyhouse.Items.CommentQueryArg;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.GlideUtil;

import java.util.ArrayList;
import java.util.Date;

public class MangaActivity extends AppCompatActivity {

    private static final String TAG = MangaActivity.class.getSimpleName() + "WWWWWWWWWWWWWWWWWW";

    private String urlString, backupUrl, titleString;

    private boolean firstTry = true;

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private LinearLayout authorContainer;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private TextView tvLabelTypes;
    private TextView tvLabelStatus;
    private TextView tvLabelTime;
    private ImageView ivCover;

    private String[] authorsStrings = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        titleString = getIntent().getStringExtra(getString(R.string.info_title_string_extra));
        urlString = getIntent().getStringExtra(getString(R.string.info_url_string_extra));
        backupUrl = getIntent().getStringExtra(getString(R.string.back_up_url_string_extra));

        toolbar.setTitle(titleString);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        authorContainer = findViewById(R.id.basic_info_authors_container);

        tvLabelStatus = findViewById(R.id.basic_info_status);
        tvLabelTypes = findViewById(R.id.basic_info_types);
        tvLabelTime = findViewById(R.id.basic_info_time);
        ivCover = findViewById(R.id.info_cover_image);

        viewPager = (ViewPager) findViewById(R.id.info_view_pager);


        Log.d(TAG, "onCreate: " + urlString);
        MangaController.getInstance().setupMangaInfos(urlString, getInfosHandler);
    }

    private void setupFragments() {
        fragments.clear();
        Log.d(TAG, "setupFragments: " + MangaController.getInstance().getRelatedUrl());
        fragments.add(RelatedFragment.newInstance(MangaController.getInstance().getRelatedUrl()));
        fragments.add(ChaptersFragment.newInstance(urlString, titleString, authorsStrings));

        Date date = new Date();
        String dateString = String.valueOf(date.getTime());

        CommentQueryArg commentQueryArg = MangaController.getInstance().getCommentQueryArg();
        String allCommentUrl = "https://interface.dmzj.com/api/NewComment2/list?callback=comment_list_s&type=";
        allCommentUrl = allCommentUrl + commentQueryArg.getComment_type() + "&obj_id=" +
                commentQueryArg.getObj_id() + "&hot=0&page_index=1";
        allCommentUrl = allCommentUrl + "&_=" + dateString;

        String hotCommentUrl = "https://interface.dmzj.com/api/NewComment2/list?callback=hotComment_s&type=";
        hotCommentUrl = hotCommentUrl + commentQueryArg.getComment_type() + "&obj_id=" + commentQueryArg.getObj_id() + "&hot=1&page_index=1";
        hotCommentUrl = hotCommentUrl + "&_=" + dateString;

        fragments.add(CommentsFragment.newInstance(allCommentUrl, hotCommentUrl));

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.info_tab_layout);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.info_related_chapter_title);
        tabLayout.getTabAt(1).setText(R.string.info_chapter_list_title);
        tabLayout.getTabAt(2).setText(R.string.info_comment_title);

        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);

    }

    @SuppressLint("HandlerLeak")
    private Handler getInfosHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case R.integer.get_data_success:
                    tvLabelTime.setText(MangaController.getInstance().getTimeString());
                    tvLabelStatus.setText(MangaController.getInstance().getStatusString());
                    tvLabelTypes.setText(MangaController.getInstance().getTypeString());
//                    imageUtil.setImageView(ivCover, MangaController.getInstance().getCoverUrl());
                    GlideUtil.setImageView(MangaActivity.this, ivCover, MangaController.getInstance().getCoverUrl());
                    authorContainer.removeAllViews();
                    authorsStrings = new String[MangaController.getInstance().getAuthorItems().size()];
                    for (int i = 0; i < MangaController.getInstance().getAuthorItems().size(); i++) {
                        addAuthorsView(authorContainer, MangaController.getInstance().getAuthorItems().get(i).getAuthorName(),
                                MangaController.getInstance().getAuthorItems().get(i).getAuthorUrl());
                        authorsStrings[i] = MangaController.getInstance().getAuthorItems().get(i).getAuthorName();
                    }
                    setupFragments();
                    break;

                case R.integer.get_data_failed:
                    if (firstTry) {
                        firstTry = false;
                        Log.d(TAG, "handleMessage: " + backupUrl);
                        if (backupUrl == null || backupUrl.length() == 0) break;
                        MangaController.getInstance().setupMangaInfos(backupUrl, getInfosHandler);
                    }

                    break;

                default:
                    break;
            }
        }
    };

    private void addAuthorsView(@NonNull LinearLayout container, @NonNull final String authorName,
                                @NonNull final String authorUrl) {

        View view = LayoutInflater.from(this).inflate(R.layout.button_author_name, null);
        TextView tvAuthor = view.findViewById(R.id.tv_author_button);
        tvAuthor.setText(authorName);
        tvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent authorIntent = new Intent(MangaActivity.this, AuthorActivity.class);
                authorIntent.putExtra(getString(R.string.info_url_string_extra), authorUrl);
                authorIntent.putExtra(getString(R.string.info_title_string_extra), authorName);
                startActivity(authorIntent);
            }
        });
        container.addView(view);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            fragmentTransaction.remove(fragments.get(i));
        }
        fragmentTransaction.commit();
        super.recreate();
    }
}
