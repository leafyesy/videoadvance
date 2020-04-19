package com.leafye.annotation;

/**
 * Created by leafye on 2020/4/18.
 */
public interface ViewBinder <T>{

    void bindView(T host, Object object, ViewFinder finder);

    void unBindView(T host);

}
