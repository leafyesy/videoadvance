package com.leafye.annotationlib.imp;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

/**
 * Created by leafye on 2020/4/19.
 */
public abstract class BaseAnnotationClass {

    abstract TypeSpec.Builder append(TypeSpec.Builder builder, ClassName className);

    abstract void add(BaseElement element);

    abstract String getPackageName();

}
