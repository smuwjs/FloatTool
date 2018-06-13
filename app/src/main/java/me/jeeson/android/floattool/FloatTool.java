package me.jeeson.android.floattool;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

/**
 * 让View浮动在界面上的工具类
 * Created by Jeeson on 2018/5/18.
 */

public class FloatTool {

    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private final WindowManager windowManager;
    private View mView;
    private int x;
    private int y;
    private int touchSlop;

    public FloatTool(final View view) {
        this(view, 0, 0);
    }

    public FloatTool(final View view, int x, int y) {
        this.mView = view;
        this.x = x;
        this.y = y;
        windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        touchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();

        mView.setOnTouchListener(new View.OnTouchListener() {
            private float downX, downY;
            private float lastY;
            private float lastX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        lastY = downY;
                        lastX = downX;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.y += event.getRawY() - lastY;
                        params.y = Math.max(0, params.y);
                        params.x += event.getRawX() - lastX;
                        params.x = Math.max(0, params.x);
                        windowManager.updateViewLayout(mView, params);
                        lastY = event.getRawY();
                        lastX = event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(event.getRawX() - downX) < touchSlop && Math.abs(event.getRawY() - downY) < touchSlop) {
                            try {
                                Field field = View.class.getDeclaredField("mListenerInfo");
                                field.setAccessible(true);
                                Object object = field.get(mView);
                                field = object.getClass().getDeclaredField("mOnClickListener");
                                field.setAccessible(true);
                                object = field.get(object);
                                if (object != null && object instanceof View.OnClickListener) {
                                    ((View.OnClickListener) object).onClick(mView);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void show() {
        try {
            windowManager.addView(mView, getWindowLayoutParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Point dismiss() {

        try {
            windowManager.removeView(mView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Point point = new Point();
        point.x = params.x;
        point.y = params.y;
        return point;
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {

        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = x;
        params.y = y;
        return params;
    }
}
