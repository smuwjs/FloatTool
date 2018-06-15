package me.jeeson.android.floattool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jeeson on 2018/5/18.
 */

public class FloatManager implements Application.ActivityLifecycleCallbacks {

    private static volatile FloatManager instance;

    FloatTool floatTool;
    private Context context;
    private View mView;

    public boolean showMenu() {
        return showMenu(0, 0);
    }

    public boolean showMenu(int x, int y) {
        if (context == null) {
            Toast.makeText(context, "Please initialize in Application.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                requestPermission(context);
                Toast.makeText(context, "After grant this permission, re-enable FloatTool.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (floatTool == null) {
            floatTool = new FloatTool(mView, x, y);
        }
        floatTool.show();
        return true;
    }

    public Point dismissMenu() {
        if (floatTool != null) {
            Point p = floatTool.dismiss();
            floatTool = null;
            return p;
        }
        return new Point(-1, -1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private int visibleActivityCount;
    private int dismissY = -1;
    private int dismissX = -1;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        visibleActivityCount++;
        if (visibleActivityCount == 1 && dismissX >= 0 && dismissY >= 0) {
            showMenu(dismissX, dismissY);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        visibleActivityCount--;
        if (visibleActivityCount == 0) {
            Point point = dismissMenu();
            dismissY = point.y;
            dismissX = point.x;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void initialize(View view) {
        this.context = view.getContext();
        this.mView = view;
    }

    public static FloatManager getInstance() {
        if (instance == null) {
            synchronized (FloatManager.class) {
                if (instance == null) {
                    instance = new FloatManager();
                }
            }
        }
        return instance;
    }
}
