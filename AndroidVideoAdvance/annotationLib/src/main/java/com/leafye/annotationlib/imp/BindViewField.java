package com.leafye.annotationlib.imp;

import com.leafye.apilib.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by leafye on 2020/4/18.
 */
public class BindViewField {

    private VariableElement mVariableElement;
    private int mResId;

    public BindViewField(Element element) throws IllegalArgumentException{
        if (ElementKind.FIELD !=element.getKind()) {
            throw new IllegalArgumentException(String.format("only fields can be annotated with @%s", BindView.class.getSimpleName()));
        }
        mVariableElement = (VariableElement) element;
        BindView bindView = mVariableElement.getAnnotation(BindView.class);
        mResId = bindView.value();
        if (mResId<0){
            throw new IllegalArgumentException(String.format("value() in %s for field %s is not valid!", BindView.class.getSimpleName(),mVariableElement.getSimpleName()));
        }
    }

    Name getFieldName(){
        return mVariableElement.getSimpleName();
    }

    int getResId(){
        return mResId;
    }

    TypeMirror getFieldType(){
        return mVariableElement.asType();
    }


}
