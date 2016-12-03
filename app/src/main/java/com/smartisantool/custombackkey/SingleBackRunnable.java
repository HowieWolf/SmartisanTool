package com.smartisantool.custombackkey;

import android.accessibilityservice.AccessibilityService;

/**
 * Created by HowieWang on 2016/12/2.
 */

public class SingleBackRunnable implements Runnable{

    CustomBackKeyAccessibilityService service;

    public SingleBackRunnable(CustomBackKeyAccessibilityService service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.setFirstBack(true);
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        //Log.i("MYTAG" , "on single back pressed");
    }
}
