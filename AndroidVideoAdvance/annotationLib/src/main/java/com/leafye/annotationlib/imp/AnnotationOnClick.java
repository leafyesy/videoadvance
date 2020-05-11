package com.leafye.annotationlib.imp;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by leafye on 2020/4/19.
 */
public class AnnotationOnClick extends BaseAnnotationClass {

    private static class TypeUtils {

        public static final String ANDROID_VIEW = "android.view";
        public static final String VIEW = "View";
        public static final String ANDROID_VIEW_ON_CLICK_LISTENER = "OnClickListener";
        public static final String ANDROID_VIEW_ON_CLICK = "onClick";
        public static final String BIND_ON_CLICKS = "bindOnClicks";
        public static final String ANDROID_ACTIVITY = "activity";
    }

    private TypeElement mTypeElement;
    private ArrayList<OnClickMethod> mMethodList;
    private Elements mElements;

    public AnnotationOnClick(TypeElement mTypeElement, Elements mElements) {
        this.mTypeElement = mTypeElement;
        this.mElements = mElements;
        mMethodList = new ArrayList<>();
    }

    @Override
    TypeSpec.Builder append(TypeSpec.Builder builder, ClassName className) {
        ClassName androidOnClickListenerClassName = ClassName.get(
                TypeUtils.ANDROID_VIEW,
                TypeUtils.VIEW,
                TypeUtils.ANDROID_VIEW_ON_CLICK_LISTENER);
        ClassName androidViewClassName = ClassName.get(
                TypeUtils.ANDROID_VIEW,
                TypeUtils.VIEW);
        MethodSpec.Builder bindOnClicksMethodBuilder = MethodSpec
                .methodBuilder(TypeUtils.BIND_ON_CLICKS)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.get(mTypeElement.asType()),
                        TypeUtils.ANDROID_ACTIVITY, Modifier.FINAL);
        for (OnClickMethod method : mMethodList) {
            TypeSpec onClickListenerClass = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(androidOnClickListenerClassName)
                    .addMethod(MethodSpec.methodBuilder(TypeUtils.ANDROID_VIEW_ON_CLICK)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(androidViewClassName, TypeUtils.VIEW)
                            .addStatement("$N.$N($N)",
                                    TypeUtils.ANDROID_ACTIVITY,
                                    method.getName(),
                                    TypeUtils.VIEW)
                            .returns(void.class)
                            .build())
                    .build();
            bindOnClicksMethodBuilder.addStatement("$N.findViewById($L).setOnClickListener($L)",
                    TypeUtils.ANDROID_ACTIVITY,
                    method.getResId(),
                    onClickListenerClass);
        }
        return builder.addMethod(bindOnClicksMethodBuilder.build());
    }

    @Override
    void add(BaseElement element) {
        mMethodList.add((OnClickMethod) element);
    }

    @Override
    String getPackageName() {
        return mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
    }

}
