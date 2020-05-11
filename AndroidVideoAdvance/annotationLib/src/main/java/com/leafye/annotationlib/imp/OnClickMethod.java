package com.leafye.annotationlib.imp;

import com.leafye.apilib.OnClick;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

/**
 * Created by leafye on 2020/4/19.
 */
public class OnClickMethod implements BaseElement {

    private ExecutableElement mExecutableElement;
    private int mResId;

    public OnClickMethod(Element element) {
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(String.format("only methods can be annotated with @%s", OnClick.class.getSimpleName()));
        }
        mExecutableElement = (ExecutableElement) element;
        OnClick annotation = mExecutableElement.getAnnotation(OnClick.class);
        mResId = annotation.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(String.format("value() in %s for method %s is not valid!", OnClick.class.getSimpleName(), mExecutableElement.getSimpleName()));
        }
    }

    @Override
    public Name getName() {
        return mExecutableElement.getSimpleName();
    }

    int getResId() {
        return mResId;
    }

    @Override
    public TypeMirror getFieldType() {
        return mExecutableElement.asType();
    }

}
