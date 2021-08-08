package cn.roy.module.permission_ext;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/08/04
 * @Version: v1.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RequestPermissionProcessor extends AbstractProcessor {
    private Messager messager;
    private boolean hasProcess = false;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(RequestPermission.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (hasProcess) {
            log("********************Annotation has processed********************");
            return false;
        }
        messager = processingEnv.getMessager();
        log("********************Annotation process start********************");
        Iterator<? extends TypeElement> iterator = annotations.iterator();
        while (iterator.hasNext()) {
            TypeElement typeElement = iterator.next();
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(typeElement);
            log(String.format("需要处理的方法数目为：%d", elements.size()));
            for (Element element : elements) {
                log("element类型：" + element.getKind());
                log("待处理类名：" + element.getEnclosingElement().toString());
                log("待处理方法名：" + element.getSimpleName());
                ExecutableElement executableElement = (ExecutableElement) element;
                String returnType = executableElement.getReturnType().toString();
                log("方法返回类型：" + returnType);
                List<? extends VariableElement> parameters = executableElement.getParameters();
                if (!parameters.isEmpty()) {
                    for (VariableElement item : parameters) {
                        log("方法参数类型：" + item.asType());
                        log("方法参数名：" + item.getSimpleName());
                    }
                }
                List<? extends TypeParameterElement> typeParameters = executableElement.getTypeParameters();
                if (!typeParameters.isEmpty()) {
                    for (TypeParameterElement item : typeParameters) {
                        log("方法参数类型2：" + item.getSimpleName());
                    }
                }
                RequestPermission annotation = element.getAnnotation(RequestPermission.class);
                String[] permissions = annotation.permissions();
                boolean autoApply = annotation.autoApply();
                String applyPermissionTip = annotation.applyPermissionTip();
                int applyPermissionCode = annotation.applyPermissionCode();
                String lackPermissionTip = annotation.lackPermissionTip();

                String elementStr = element.toString();
                Set<Modifier> modifiers = element.getModifiers();
                Element enclosingElement = element.getEnclosingElement();
                List<? extends Element> enclosedElements = element.getEnclosedElements();
                List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            }
        }

        MethodSpec methodSpec = MethodSpec.methodBuilder("init")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("LoggerInit")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder("cn.roy.module.base", typeSpec)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
            log("发生异常" + e.getMessage());
        }
        log("********************Annotation process end********************");
        hasProcess = true;
        return true;
    }

    private void log(String text) {
        messager.printMessage(Diagnostic.Kind.NOTE, text + "\n");
    }

}