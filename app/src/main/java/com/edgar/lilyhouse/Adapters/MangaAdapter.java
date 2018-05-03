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
import com.edgar.lilyhouse.Utils.GlideUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

public class MangaAdapter extends UltimateViewAdapter<MangaAdapter.MangaViewHolder> {

    private ArrayList<MangaItem> mangaItems;
    private Context context;
    private ItemClickListener itemClickListener;
    private boolean isGridOn = false;

    public MangaAdapter(Context context, ArrayList<MangaItem> mangaItems, boolean isGridOn) {
        this.mangaItems = mangaItems;
        this.context = context;
        this.isGridOn = isGridOn;
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
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {

        if (getItemViewType(position) != VIEW_TYPES.NORMAL) return;

        final int curPos = position;
        final MangaItem mangaItem = mangaItems.get(position);

        if (!isGridOn) {
            holder.tvTitle.setText(mangaItem.getName());
            holder.tvAuthor.setText(context.getString(R.string.cover_author_string, mangaItem.getAuthors()));
            holder.tvType.setText(context.getString(R.string.cover_types_string, mangaItem.getTypes()));
        } else {
            holder.tvTitle.setText(mangaItem.getName());
            holder.tvAuthor.setText(context.getString(R.string.cover_author_string, mangaItem.getAuthors()));
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
//        imageUtil.setImageView(holder.ivCover, urlString);
        GlideUtil.setImageView(context, holder.ivCover, urlString);


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

    @Override
    public long getItemId(int position) {
        return mangaItems.get(position).getId();
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
