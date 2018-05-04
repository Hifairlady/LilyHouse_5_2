package com.edgar.lilyhouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edgar.lilyhouse.Items.SearchResultItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.GlideUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private ArrayList<SearchResultItem> resultItems;
    private SearchItemClickListener listener;

    public SearchAdapter(Context context, ArrayList<SearchResultItem> resultItems) {
        this.context = context;
        this.resultItems = resultItems;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_search_result,
                parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        SearchResultItem resultItem = resultItems.get(position);
        String urlString = resultItem.getCover();
        if (!urlString.startsWith("https")) {
            urlString = "https://images.dmzj.com/" + urlString;
        }
        GlideUtil.setImageView(context, holder.ivCover, urlString);

        holder.tvTitle.setText(resultItem.getName());
        holder.tvAuthors.setText(resultItem.getAuthors());
        holder.tvTypes.setText(resultItem.getTypes());
        holder.tvChapter.setText(resultItem.getLast_update_chapter_name());
        String dateString = getDateString(resultItem.getLast_updatetime());
        holder.tvTime.setText(dateString);

        if (resultItem.getStatus().equals("连载中")) {
            holder.ivFinishedLogo.setVisibility(View.GONE);
        } else {
            holder.ivFinishedLogo.setVisibility(View.VISIBLE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultItems.size();
    }

    protected class SearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCover, ivFinishedLogo;
        private TextView tvTitle, tvAuthors, tvTypes, tvTime, tvChapter;
        private CardView cardView;

        public SearchViewHolder(View itemView) {
            super(itemView);

            ivCover = (ImageView)itemView.findViewById(R.id.cover_image);
            ivFinishedLogo = (ImageView)itemView.findViewById(R.id.cover_finished_logo);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_search_item_title);
            tvAuthors = (TextView)itemView.findViewById(R.id.tv_search_item_authors);
            tvTypes = (TextView)itemView.findViewById(R.id.tv_search_item_types);
            tvTime = (TextView)itemView.findViewById(R.id.tv_search_item_time);
            tvChapter = (TextView)itemView.findViewById(R.id.tv_search_item_chapter);
            cardView = (CardView)itemView.findViewById(R.id.search_item_root);

        }
    }

    private String getDateString(long lastUpdateTime) {
        long times = lastUpdateTime * 1000;
        Date date = new Date(times);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public interface SearchItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(SearchItemClickListener listener) {
        this.listener = listener;
    }



}
