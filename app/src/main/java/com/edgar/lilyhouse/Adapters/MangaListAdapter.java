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

import com.edgar.lilyhouse.Items.MangaItem;
import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.ImageUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

public class MangaListAdapter extends UltimateViewAdapter<MangaListAdapter.MangaViewHolder> {

    private ArrayList<MangaItem> mangaItems;
    private Context context;
    private ItemClickListener itemClickListener;
    private ImageUtil imageUtil;
    private boolean isGridOn = false;

    public MangaListAdapter(Context context, ArrayList<MangaItem> mangaItems, boolean isGridOn) {
        this.mangaItems = mangaItems;
        this.context = context;
        this.isGridOn = isGridOn;
        this.imageUtil = new ImageUtil(context);
    }

    @Override
    public MangaViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MangaViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public MangaViewHolder onCreateViewHolder(ViewGroup parent) {
        if (isGridOn) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_grid_manga, parent, false);
            return new MangaViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_vertical_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        return (mangaItems == null ? 0 : mangaItems.size());
    }

    @Override
    public long generateHeaderId(int position) {
        return mangaItems.get(position).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {

        if (getItemViewType(position) != VIEW_TYPES.NORMAL) return;

        final int curPos = position;
        final MangaItem mangaItem = mangaItems.get(position);

        if (!isGridOn) {
            holder.tvTitle.setText("标题: " + mangaItem.getName());
            holder.tvAuthor.setText("作者: " + mangaItem.getAuthors());
            holder.tvType.setText("类别: " + mangaItem.getTypes());
        } else {
            holder.tvTitle.setText(mangaItem.getName());
            holder.tvAuthor.setText("作者: " + mangaItem.getAuthors());
        }

        if (mangaItem.getStatus().equals(context.getString(R.string.chapter_is_serializing))) {
            holder.ivFinished.setVisibility(View.GONE);
        } else {
            holder.ivFinished.setVisibility(View.VISIBLE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(curPos);
            }
        });

        String urlString = mangaItem.getCover();
        if (!urlString.startsWith("https")) {
            urlString = "https://images.dmzj.com/" + urlString;
        }
        imageUtil.setImageView(holder.ivCover, urlString);


    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    protected class MangaViewHolder extends UltimateRecyclerviewViewHolder {

        protected ImageView ivCover, ivFinished;
        protected TextView tvTitle, tvAuthor, tvType;
        protected CardView cardView;

        public MangaViewHolder(View itemView) {
            super(itemView);

            ivCover = (ImageView) itemView.findViewById(R.id.cover_image);
            ivFinished = (ImageView) itemView.findViewById(R.id.cover_finished_logo);
            tvTitle = (TextView) itemView.findViewById(R.id.cover_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.cover_author);
            cardView = (CardView) itemView.findViewById(R.id.rv_item_root);

            if (!isGridOn) {
                tvType = (TextView) itemView.findViewById(R.id.cover_class_types);
            }

        }
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
