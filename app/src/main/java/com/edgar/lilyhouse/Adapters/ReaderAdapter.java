package com.edgar.lilyhouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.GlideUtil;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.ZoomViewHolder> {

    private static final String TAG = "=====================" + ReaderAdapter.class.getSimpleName();

    private Context context;
    private String[] imageUrls;
    private int width;
    private int preloadPosition = 0;

    public ReaderAdapter(Context context, String[] imageUrls, int width) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.width = width;
    }

    @NonNull
    @Override
    public ZoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_reader_image_view,
                parent, false);
        return new ZoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ZoomViewHolder holder, int position) {
        GlideUtil.setScaledImage(context, imageUrls[position], holder.imageView, width);
        if (preloadPosition < position) {
            preloadPosition += GlideUtil.preloadImage(context, imageUrls, position+1, 3, width);
        }
//        Log.d(TAG, "getView: " + preloadPosition + "  " + position);

    }

    @Override
    public int getItemCount() {
        return imageUrls.length;
    }

    protected class ZoomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;

        public ZoomViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_reader_image);
        }
    }

    @Override
    public long getItemId(int position) {
        return imageUrls[position].hashCode();
    }
}
