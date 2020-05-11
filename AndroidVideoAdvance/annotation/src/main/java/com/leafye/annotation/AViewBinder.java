package com.leafye.annotation;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leafye on 2020/4/18.
 */
public class AViewBinder {
    private static final ActivityViewFinder ACTIVITY_VIEW_FINDER = new ActivityViewFinder();
    private static final Map<String, ViewBinder> BINDER_MAP = new LinkedHashMap<>();

    public static void bind(Activity activity) {
        bind(activity, activity, ACTIVITY_VIEW_FINDER);
    }

    private static void bind(Object host, Object object, ViewFinder viewFinder) {
        String className = host.getClass().getName();
        try {
            ViewBinder binder = BINDER_MAP.get(className);
            if (binder == null) {
                Class<?> aClass = Class.forName(className + "$$ViewBinder");
                binder = (ViewBinder) aClass.newInstance();
                BINDER_MAP.put(className, binder);
            }
            binder.bindView(host, object, viewFinder);
            //binder.bindOnClicks(host);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void unBind(Object host) {
        String className = host.getClass().getName();
        ViewBinder binder = BINDER_MAP.get(className);
        if (binder != null) {
            binder.unBindView(host);
        }
        BINDER_MAP.remove(className);
    }

}
