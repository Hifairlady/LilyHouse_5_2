package com.edgar.lilyhouse.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.edgar.lilyhouse.R;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Edgar on 2018/5/1.
 */

public class ImageUtil {

    private Picasso picasso;

    public ImageUtil(Context context, int cacheSize) {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Cache-Control", "max-age=" + (60 * 60 * 24 * 365) )
                        .addHeader("Referer", "https://m.dmzj.com/classify.html")
                        .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        this.picasso = new Picasso.Builder(context).memoryCache(new LruCache(cacheSize * 1000000))
                .downloader(new OkHttp3Downloader(client))
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    public ImageUtil(Context context) {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Cache-Control", "max-age=" + (60 * 60 * 24 * 365) )
                        .addHeader("Referer", "https://m.dmzj.com/classify.html")
                        .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        this.picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    public void setScaledImage(ImageView imageView, String urlString, final int width) {
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {

                if (source.getWidth() == 0) {
                    return source;
                }

                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (width * aspectRatio);
                if (targetHeight != 0 && width != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }

            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        picasso.load(urlString).transform(transformation).config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.ic_pure_white_bg)
                .error(R.drawable.error_white_bg).into(imageView);
    }

    public void setImageView(ImageView imageView, String urlString) {
        picasso.load(urlString).placeholder(R.drawable.loading_white_bg)
                .error(R.drawable.error_white_bg).into(imageView);
    }

    public void setCircularImage(ImageView imageView, String urlString) {

        Transformation circularTransform = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap,
                        BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circle";
            }
        };

        picasso.load(urlString).transform(circularTransform).config(Bitmap.Config.ARGB_4444)
                .placeholder(R.drawable.ic_authors_circle)
                .error(R.drawable.ic_authors_circle).into(imageView);
    }

    public void shutdownPicasso() {
        picasso.shutdown();
    }


}

