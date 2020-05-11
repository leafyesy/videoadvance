package com.leafye.annotationlib.imp;

import com.google.auto.service.AutoService;
import com.leafye.apilib.BindView;
import com.leafye.apilib.OnClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by leafye on 2020/4/18.
 */
@AutoService(Processor.class)
public class AAbstractProcessor extends AbstractProcessor {

    private Filer mFiler;//文件相关辅助类
    private Elements mElementUtils;
    private Messager mMessager;
    private Map<String, List<BaseAnnotationClass>> mAnnotatedClassMap;

    /**
     * 这相当于每个处理器的主函数main()。 在这里写扫描、评估和处理注解的代码，以及生成Java文件。输入参数RoundEnviroment，可以让查询出包含特定注解的被注解元素。
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mAnnotatedClassMap.clear();
        try {
            process(roundEnvironment);
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
        for (Map.Entry<String, List<BaseAnnotationClass>> entry : mAnnotatedClassMap.entrySet()) {
            String key = entry.getKey();
            List<BaseAnnotationClass> value = entry.getValue();
            String packageName = key.split("_")[0];
            String typeName = key.split("_")[1];
            ClassName generatedClassName = ClassName
                    .get(packageName, typeName + "$$ViewBinder");
            try {
                TypeSpec.Builder builder = TypeSpec.classBuilder(generatedClassName);
                for (BaseAnnotationClass annotationClass : value) {
                    builder = annotationClass
                            .append(builder, generatedClassName);
                }
                TypeSpec injectClass = builder.build();
                JavaFile.builder(packageName, injectClass).build().writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    private void process(RoundEnvironment roundEnv) throws IllegalArgumentException {
        //优先添加方法生成
        addOnClick(roundEnv);
        addBind(roundEnv);
    }

    private void addBind(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String fullName = getFullClassName(element);
            List<BaseAnnotationClass> annotatedClassList = mAnnotatedClassMap.get(fullName);
            if (annotatedClassList == null) {
                annotatedClassList = new ArrayList<>();
                mAnnotatedClassMap.put(fullName, annotatedClassList);
            }
            AnnotatedBindClass annotatedClass = new AnnotatedBindClass(typeElement, mElementUtils);
            annotatedClassList.add(annotatedClass);
            BindViewField bindViewField = new BindViewField(element);
            annotatedClass.add(bindViewField);
        }
    }

    private void addOnClick(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String fullName = getFullClassName(element);
            List<BaseAnnotationClass> annotatedClassList = mAnnotatedClassMap.get(fullName);
            if (annotatedClassList == null) {
                annotatedClassList = new ArrayList<>();
                mAnnotatedClassMap.put(fullName, annotatedClassList);
            }
            AnnotationOnClick annotatedClass = new AnnotationOnClick(typeElement, mElementUtils);
            annotatedClassList.add(annotatedClass);
            OnClickMethod method = new OnClickMethod(element);
            annotatedClass.add(method);
        }
    }

    private String getFullClassName(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        return packageName + "_" + typeElement.getSimpleName().toString();
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    /**
     * 用来指定你使用的Java版本。
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。换句话说，在这里定义你的注解处理器注册到哪些注解上。
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                BindView.class.getCanonicalName(),
                OnClick.class.getCanonicalName()));
//        Set<String> types = new LinkedHashSet<>();
//        types.add(Arrays.asList(BindView.class.getCanonicalName(),OnClick.class.getCanonicalName()));
//        return types;
    }

    /**
     * 每一个注解处理器类都必须有一个空的构造函数。然而，这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnviroment参数。ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer。
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mAnnotatedClassMap = new TreeMap<>();
    }
}
