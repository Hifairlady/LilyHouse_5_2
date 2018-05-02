package com.edgar.lilyhouse.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.edgar.lilyhouse.GlideApp;
import com.edgar.lilyhouse.R;

public class GlideUtil {

    public static void setScaledImage(Context context, String urlString, ImageView imageView, final int width) {
        GlideApp.with(context).load(getGlideUrl(urlString)).transform(new ScaledTransformation(width))
                .placeholder(R.drawable.lily_loading_black).error(R.drawable.error_white_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void setImageView(Context context, ImageView imageView, String urlString) {
        GlideApp.with(context).load(getGlideUrl(urlString)).centerCrop()
                .placeholder(R.drawable.ic_pure_white_bg).error(R.drawable.error_white_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void setCircularImage(Context context, ImageView imageView, String urlString) {
        GlideApp.with(context).load(getGlideUrl(urlString)).circleCrop()
                .placeholder(R.drawable.ic_pure_white_bg).error(R.drawable.error_white_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    private static GlideUrl getGlideUrl(String urlString) {
        GlideUrl url = new GlideUrl(urlString, new LazyHeaders.Builder()
                .addHeader("Cache-Control", "max-age=" + (60 * 60 * 24 * 365) )
                .addHeader("Referer", "https://m.dmzj.com/classify.html")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build());
        return url;
    }

    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int preloadImage(Context context, String[] urls, int position,
                                    int preloadNum, int width) {
        int loaded;
        for (loaded = 0; loaded < preloadNum; loaded++) {
            if (urls[position+loaded] == null || urls[position+loaded].length() == 0) break;
            GlideApp.with(context).load(getGlideUrl(urls[position+loaded]))
                    .placeholder(R.drawable.lily_loading_black).error(R.drawable.error_white_bg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .preload();
        }
        return loaded;

    }

}
