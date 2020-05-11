package com.leafye.annotationlib.imp;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

/**
 * Created by leafye on 2020/4/19.
 */
public interface BaseElement {


    Name getName();

    TypeMirror getFieldType();
}
