package com.smartisantool.custombackkey;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by HowieWang on 2016/12/2.
 */

public class CustomBackKeyAccessibilityService extends AccessibilityService {

    public static int DOUBLE_BACK_SPACE_MIN = 150;
    public static int DOUBLE_BACK_SPACE = 230;

    long firstBackTime = 0;
    boolean isFirstBack = true;

    Handler handler = null;
    SingleBackRunnable singleBackRunnable = null;

    @Override
    protected void onServiceConnected() {
        Toast.makeText(this , "双击back键查看后台已经开启" , Toast.LENGTH_SHORT).show();
        singleBackRunnable = new SingleBackRunnable(this);
        handler = new Handler();
        super.onServiceConnected();
    }

    public CustomBackKeyAccessibilityService() {
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            myOnBackUp();
            return true;
        }

        return super.onKeyEvent(event);
    }

    private void myOnBackUp() {
        long tempTime = System.currentTimeMillis();
        if (isFirstBack) {
            firstBackTime = tempTime;
            isFirstBack = false;
            handler.postDelayed(singleBackRunnable, DOUBLE_BACK_SPACE);
        } else {
            if (tempTime - firstBackTime < DOUBLE_BACK_SPACE) {
                handler.removeCallbacks(singleBackRunnable);
                isFirstBack = true;
                this.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                //Toast.makeText(this, "on double back pressed", Toast.LENGTH_SHORT).show();
                //Log.i("MYTAG" , "on double back pressed");
                //Log.i("MYTAG" , DOUBLE_BACK_SPACE + "   double back space  " + (tempTime- firstBackTime));
            }

        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        handler = null;
        singleBackRunnable = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    public void setFirstBack(boolean firstBack) {
        isFirstBack = firstBack;
    }
}
