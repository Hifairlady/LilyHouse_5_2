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
import com.edgar.lilyhouse.Controllers.MainDataController;
import com.edgar.lilyhouse.Controllers.MangaDataController;
import com.edgar.lilyhouse.Fragments.ChaptersFragment;
import com.edgar.lilyhouse.Fragments.CommentsFragment;
import com.edgar.lilyhouse.Fragments.RelatedFragment;
import com.edgar.lilyhouse.Items.CommentQueryArg;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.ImageUtil;

import java.util.ArrayList;
import java.util.Date;

public class MangaDetailActivity extends AppCompatActivity {

    private static final String TAG = MangaDetailActivity.class.getSimpleName() + "WWWWWWWWWWWWWWWWWW";

    private String urlString;

//    private String obj_id, authoruid, is_Original, comment_type, isToggle, dt;

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private LinearLayout authorContainer;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private TextView tvLabelTypes;
    private TextView tvLabelStatus;
    private TextView tvLabelTime;
    private ImageView ivCover;

    private ImageUtil imageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);

        imageUtil = new ImageUtil(this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String titleString = getIntent().getStringExtra(getString(R.string.info_title_string_extra));
        urlString = getIntent().getStringExtra(getString(R.string.info_url_string_extra));

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

        MangaDataController.getInstance().setupMangaInfos(urlString, getInfosHandler);
    }

    private void setupFragments() {
        fragments.clear();
        Log.d(TAG, "setupFragments: " + MangaDataController.getInstance().getRelatedUrl());
        fragments.add(RelatedFragment.newInstance(MangaDataController.getInstance().getRelatedUrl()));
        fragments.add(ChaptersFragment.newInstance(urlString));

        Date date = new Date();
        String dateString = String.valueOf(date.getTime());

        CommentQueryArg commentQueryArg = MangaDataController.getInstance().getCommentQueryArg();
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
                    tvLabelTime.setText(MangaDataController.getInstance().getTimeString());
                    tvLabelStatus.setText(MangaDataController.getInstance().getStatusString());
                    tvLabelTypes.setText(MangaDataController.getInstance().getTypeString());
                    imageUtil.setImageView(ivCover, MangaDataController.getInstance().getCoverUrl());
                    authorContainer.removeAllViews();
                    for (int i = 0; i < MangaDataController.getInstance().getAuthorItems().size(); i++) {
                        addAuthorsView(authorContainer, MangaDataController.getInstance().getAuthorItems().get(i).getAuthorName(),
                                MangaDataController.getInstance().getAuthorItems().get(i).getAuthorUrl());
                    }
                    setupFragments();
                    break;

                case R.integer.get_data_failed:
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
                Intent authorIntent = new Intent(MangaDetailActivity.this, AuthorDetailActivity.class);
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
