package processor;

import annotations.TSIgnore;
import annotations.TSInterface;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedClass {

    private String name;
    private List<Field> fields;
    private TSInterface.AccessLevel[] accessLevel;

    public class Field {

        private String name; //Field name
        private String type; //Full type name
        private boolean isPrimitive;

        private Field(String name, String type, boolean isPrimitive) {
            this.name = name;
            this.type = type;
            this.isPrimitive = isPrimitive;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isPrimitive() {
            return isPrimitive;
        }
    }


    //Проверяем есть ли у поля необходимый модификатор
    private boolean checkModifiers(Set<Modifier> modifiers) {
        for (TSInterface.AccessLevel accessLevel : accessLevel) {
            if (accessLevel == TSInterface.AccessLevel.NO) {
                if (!modifiers.contains(Modifier.PUBLIC) &&
                        !modifiers.contains(Modifier.PRIVATE) &&
                        !modifiers.contains(Modifier.PROTECTED)) {
                    return true;
                }
            } else if (modifiers.contains(accessLevel.getModifier())) return true;
        }
        return false;
    }

    public AnnotatedClass(TypeElement element, Messager messager) {
        this.name = element.getQualifiedName().toString();
        //получаем все указанные в аннотации модификаторы доступа
        this.accessLevel = element.getAnnotation(TSInterface.class).accessLevel();
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        fields = enclosedElements.stream()
                .filter(it -> it.getKind().isField())
                .filter(it -> checkModifiers(it.getModifiers()))
                .filter(it -> it.getAnnotation(TSIgnore.class) == null)
                .map(it -> (VariableElement) it)
                .map(it -> new Field(it.getSimpleName().toString(),
                        it.asType().toString(),
                        it.asType().getKind().isPrimitive()
                ))
                .collect(Collectors.toList());

        fields.forEach(it -> messager.printMessage(Diagnostic.Kind.NOTE,
                name + "\t" + it.name + "\t" + it.type + "\t" + it.isPrimitive));
    }

    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }
}