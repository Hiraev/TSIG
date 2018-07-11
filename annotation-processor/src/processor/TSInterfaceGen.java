package processor;

import annotations.TSIgnore;
import annotations.TSInterface;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TSInterfaceGen extends AbstractProcessor {

    private Messager messager;
    private List<AnnotatedClass> annotatedClasses;
    private TSCodeGenerator codeGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
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
            File path;
            //get normal output path
            URL url = getClass().getClassLoader().getResource(".");
            if (url != null) path = new File(url.getPath() + "TSInterfaces");
            else {
                messager.printMessage(Diagnostic.Kind.ERROR, "Cannot get a path from the classLoader");
                return true;
            }
            path.mkdir();
            for (AnnotatedClass annotatedClass : annotatedClasses) {
                File file = new File(path.getPath() + "/" + annotatedClass.getName() + ".ts");
                file.createNewFile();
                Writer writer = new FileWriter(file);
                writer.write(codeGenerator.generate(annotatedClass));
                writer.close();
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Can't create new TypeScript file");
        }
        //DEBUG
        messager.printMessage(Diagnostic.Kind.NOTE, "Process has been finished");
        return false;
    }
}
