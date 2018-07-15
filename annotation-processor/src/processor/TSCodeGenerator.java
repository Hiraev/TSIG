package processor;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class TSCodeGenerator {

    public String generate(AnnotatedClass annotatedClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("interface ");
        builder.append(annotatedClass.getName());
        builder.append(" {\n");
        for (AnnotatedClass.Field field : annotatedClass.getFields()) {
            builder.append("\t");
            builder.append(field.getName());
            builder.append(": ");
            //Check type
            builder.append(checkType(field.getType()));
            builder.append(";\n");
        }
        builder.append("}");
        return builder.toString();
    }

    private String checkType(final String type) {
        final String primitive = checkPrimitive(type);
        if (primitive != null) {
            return checkPrimitive(type);
        } else if (type.matches(".+\\]")) { //Is it an array?
            return checkArray(type);
        } else if (type.matches(".+>")) { //Is it a generic type?
            return checkGeneric(type);
        }
        return checkObject(type);
    }

    private String checkArray(String type) {
        int index = 0;
        StringBuilder dimension = new StringBuilder();
        boolean trigger = true;
        for (int i = type.length() - 1; i >= 0; i--) {
            final char sym = type.charAt(i);
            if (sym != '[' && sym != ']') break;
            index = i;
            if (trigger) dimension.append("[]");
            trigger = !trigger;
        }
        return checkType(type.substring(0, index)) + dimension.toString();
    }

    private String checkPrimitive(String type) {
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "float":
            case "long":
            case "double":

            case "Byte":
            case "Short":
            case "Integer":
            case "Float":
            case "Long":
            case "Double":
                return "number";
            case "char":
            case "Character":
                return "string";
            case "boolean":
            case "Boolean":
                return "boolean";
        }
        return null;
    }

    private String checkGeneric(String type) {
        final int firstScope = type.indexOf('<');
        final String basicType = checkObject(type.substring(0, firstScope));
        if ("Object".equals(basicType)) return basicType;
        StringBuilder innerTypes = new StringBuilder();
        final String generic = type.substring(firstScope + 1, type.length() - 1);
        final String[] genericTypes = generic.split(",");
        final int lastIndex = genericTypes.length - 1;
        for (int i = 0; i < genericTypes.length; i++) {
            innerTypes.append(checkType(genericTypes[i].trim()));
            if (i != lastIndex) innerTypes.append(", ");
        }
        return basicType + "<" + innerTypes.toString() + ">";
    }

    private String checkObject(String type) {
        if (String.class.getCanonicalName().equals(type)) return "String";
        else if (List.class.getCanonicalName().equals(type) ||
                Queue.class.getCanonicalName().equals(type)) return "Array";
        else if (Map.class.getCanonicalName().equals(type)) return "Map";
        else if (Set.class.getCanonicalName().equals(type)) return "Set";
        else return "Object";
    }
}