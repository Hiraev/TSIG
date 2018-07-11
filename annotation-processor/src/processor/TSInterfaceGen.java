package processor;

import annotations.TSIgnore;
import annotations.TSInterface;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class TSInterfaceGen extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private List<AnnotatedClass> annotatedClasses;
    private TSCodeGenerator codeGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        annotatedClasses = new ArrayList<>();
        codeGenerator = new TSCodeGenerator();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(TSInterface.class.getCanonicalName());
        annotations.add(TSIgnore.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(TSInterface.class)) {
            //if TSInterface is used for something is non class

            if (annotatedElement.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "TSInterface is available only for classes!");
                return true; //Exit
            } else {
                AnnotatedClass annotatedClass = new AnnotatedClass((TypeElement) annotatedElement, messager);
                annotatedClasses.add(annotatedClass);
            }
        }
        try {
            //TODO - СОХРАНЯЕТ ХРЕН ЗНАЕТ ГДЕ
            File path = new File("TSInterfaces");
            messager.printMessage(Diagnostic.Kind.WARNING, "Output files are saved in: " + path.getAbsolutePath());
            for (AnnotatedClass annotatedClass : annotatedClasses) {
                File file = new File(path.getPath() + "/" + annotatedClass.getName() + ".ts");
                if (!file.createNewFile()) messager.printMessage(Diagnostic.Kind.ERROR, "Cannot create file:" +
                        annotatedClass.getName() + ".ts");
                Writer writer = new FileWriter(file);
                writer.write(codeGenerator.generate(annotatedClass));
                writer.close();
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Can't create new TypeScript file");
        }
        //DEBUG
        messager.printMessage(Diagnostic.Kind.NOTE, "Process have been finished");
        return false;
    }
}
