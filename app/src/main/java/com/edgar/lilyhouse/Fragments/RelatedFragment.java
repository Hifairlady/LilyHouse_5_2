package com.edgar.lilyhouse.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edgar.lilyhouse.Activities.MangaActivity;
import com.edgar.lilyhouse.Adapters.MangaAdapter;
import com.edgar.lilyhouse.Controllers.MangaController;
import com.edgar.lilyhouse.R;

public class RelatedFragment extends Fragment {

    private static final String ARG_QUERY_URL = "queryUrl";

    private String queryUrl;

    private boolean isFragDestroyed = false;

    private RecyclerView recyclerView1, recyclerView2;
    private TextView tvAuthor;

    private LinearLayout lvHasData;
    private CardView cvNoData;
    private Button btnRetry;

    public RelatedFragment() { }

    public static RelatedFragment newInstance(String relatedUrl) {
        RelatedFragment fragment = new RelatedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY_URL, relatedUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            queryUrl = getArguments().getString(ARG_QUERY_URL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_related, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView1 = view.findViewById(R.id.related_recycler_view1);
        recyclerView2 = view.findViewById(R.id.related_recycler_view2);
        tvAuthor = view.findViewById(R.id.tv_author_related_title);

        lvHasData = view.findViewById(R.id.lv_related_has_data);
        cvNoData = view.findViewById(R.id.cv_no_data_page);
        btnRetry = view.findViewById(R.id.btn_error_retry);
        btnRetry.setOnClickListener(mOnClickListener);

        MangaController.getInstance().setupRelated(queryUrl, getRelatedHandler);

    }

    @SuppressLint("HandlerLeak")
    private Handler getRelatedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case R.integer.get_data_success:

                    if (isFragDestroyed) return;

                    cvNoData.setVisibility(View.GONE);
                    lvHasData.setVisibility(View.VISIBLE);
                    if (MangaController.getInstance().getRelatedAuthorName() == null) {
                        cvNoData.setVisibility(View.VISIBLE);
                        return;
                    }
                    tvAuthor.setText(MangaController.getInstance().getRelatedAuthorName());
//                        btnRetry.setClickable(true);

                    MangaAdapter adapter1 = new MangaAdapter(getContext(), MangaController.getInstance().getMangaItems1(), true);
                    MangaAdapter adapter2 = new MangaAdapter(getContext(), MangaController.getInstance().getMangaItems2(), true);

                    adapter1.setHasStableIds(true);
                    adapter2.setHasStableIds(true);
                    GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 3);
                    recyclerView1.setLayoutManager(layoutManager1);
                    recyclerView1.setAdapter(adapter1);
                    adapter1.setOnItemClickListener(mItemClickListener1);

                    GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(), 3);
                    recyclerView2.setLayoutManager(layoutManager2);
                    recyclerView2.setAdapter(adapter2);
                    adapter2.setOnItemClickListener(mItemClickListener2);

                    break;

                case R.integer.get_data_failed:
                    lvHasData.setVisibility(View.GONE);
                    cvNoData.setVisibility(View.VISIBLE);
                    btnRetry.setClickable(true);
                    break;

                default:
                    break;
            }
        }
    };

    private MangaAdapter.ItemClickListener mItemClickListener1 = new MangaAdapter.ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent infoIntent = new Intent(getContext(), MangaActivity.class);
            infoIntent.putExtra(getContext().getString(R.string.info_title_string_extra),
                    MangaController.getInstance().getMangaItems1().get(position).getName());
            String urlString = MangaController.getInstance().getMangaItems1().get(position).getQueryInfoUrl();
            infoIntent.putExtra(getContext().getString(R.string.info_url_string_extra), urlString);
            getContext().startActivity(infoIntent);
        }
    };

    private MangaAdapter.ItemClickListener mItemClickListener2 = new MangaAdapter.ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent infoIntent = new Intent(getContext(), MangaActivity.class);
            infoIntent.putExtra(getContext().getString(R.string.info_title_string_extra),
                    MangaController.getInstance().getMangaItems2().get(position).getName());
            String urlString = MangaController.getInstance().getMangaItems2().get(position).getQueryInfoUrl();
            infoIntent.putExtra(getContext().getString(R.string.info_url_string_extra), urlString);
            getContext().startActivity(infoIntent);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_error_retry:
                    cvNoData.setVisibility(View.GONE);
                    lvHasData.setVisibility(View.VISIBLE);
                    MangaController.getInstance().setupRelated(queryUrl, getRelatedHandler);
                    btnRetry.setClickable(false);

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragDestroyed = true;
    }



}
