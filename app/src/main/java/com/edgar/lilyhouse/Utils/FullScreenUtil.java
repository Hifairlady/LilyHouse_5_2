package com.edgar.lilyhouse.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.edgar.lilyhouse.R;

public class FullScreenUtil {

    private Window window;
    private int width, height;
    private boolean isFullScreen = true;

    @SuppressLint("ClickableViewAccessibility")
    public FullScreenUtil(Context context, final Window window, RecyclerView recyclerView) {
        this.window = window;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }

        //handle fullscreen and exit fullscreen
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) return false;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int)event.getX();
                    int y = (int)event.getY();

                    if (x >= width/2-200 && x <= width/2+200
                            && y >= height/2-200 && y <= height/2+200) {
                        if (isFullScreen) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            fullscreenHandler.sendEmptyMessageDelayed(R.integer.fullscreen_message, 3000);

                        } else {
                            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                        isFullScreen = !isFullScreen;
                    }
                }

                return false;
            }
        });
    }

    //set fullscreen after 2000 milliseconds
    @SuppressLint("HandlerLeak")
    private Handler fullscreenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == R.integer.fullscreen_message) {
                if (!isFullScreen) {
                    isFullScreen = true;
                    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        }
    };


}
