package com.edgar.lilyhouse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.edgar.lilyhouse.R;
import com.edgar.lilyhouse.Utils.ImageUtil;

public class ReaderAdapter extends BaseAdapter {

    private Context context;
    private String[] imageUrls;
    private ImageUtil imageUtil;
    private int width;

    public ReaderAdapter(Context context, String[] imageUrls, int width) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.width = width;
        this.imageUtil = new ImageUtil(context, 250);
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return imageUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_reader_image_view,
                    parent, false);
        } else {
            itemView = convertView;
        }

        ImageView imageView = (ImageView)itemView.findViewById(R.id.iv_reader_image);
        imageUtil.setScaledImage(imageView, imageUrls[position], width);
//        ImageUtil.setScaledImage(context, imageView, imageUrls[position], width);

        return itemView;
    }

    public void stopPicasso() {
        imageUtil.shutdownPicasso();
    }

}
