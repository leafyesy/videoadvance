package com.leafye.annotation;

import android.app.Activity;
import android.view.View;


/**
 * Created by leafye on 2020/4/18.
 */
public class ActivityViewFinder implements ViewFinder {

    @Override
    public View findView(Object object, int id) {
        return ((Activity)object).findViewById(id);
    }
}
