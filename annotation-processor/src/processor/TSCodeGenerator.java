package processor;

import java.util.Map;

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
            final String type = field.getType();
            if (field.isPrimitive()) {
                switch (type) {
                    case "byte":
                    case "short":
                    case "int":
                    case "float":
                    case "long":
                    case "double":
                        builder.append("number");
                        break;
                    case "char":
                        builder.append("string");
                        break;
                    case "boolean":
                        builder.append("boolean");
                }
            } else {
//                if (String.class.getCanonicalName().equals(type)) builder.append("string");
//                else if (Map.class.getCanonicalName().equals(type)) builder.append("Map<Object, Object>");
//                else if (type.startsWith(Map.class.getCanonicalName())) {
//
//                }
                builder.append("Object");
            }
            builder.append(";\n");
        }
        builder.append("}");
        return builder.toString();
    }
//
//    private String checkGeneric(String string) {
//
//    }
}
