package com.leafye.annotationlib.imp;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by leafye on 2020/4/20.
 */
public class EmptyAnnotationOnClick extends AnnotationOnClick {
    public EmptyAnnotationOnClick(TypeElement mTypeElement, Elements mElements) {
        super(mTypeElement, mElements);
    }

    @Override
    void add(BaseElement element) {

    }

}
