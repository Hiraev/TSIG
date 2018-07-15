package processor;

import java.util.*;

public class TSCodeGenerator {
    private final List<String> toArrayTypes = Arrays.asList(List.class.getCanonicalName(),
            ArrayList.class.getCanonicalName(), AbstractList.class.getCanonicalName(),
            AbstractSequentialList.class.getCanonicalName(), Queue.class.getCanonicalName(),
            Deque.class.getCanonicalName(), AbstractQueue.class.getCanonicalName(),
            LinkedList.class.getCanonicalName(), PriorityQueue.class.getCanonicalName(),
            Vector.class.getCanonicalName(), Stack.class.getCanonicalName());
    private final List<String> toSetTypes = Arrays.asList(Set.class.getCanonicalName(),
            AbstractSet.class.getCanonicalName(), SortedSet.class.getCanonicalName(),
            HashSet.class.getCanonicalName(),
            NavigableSet.class.getCanonicalName(), TreeSet.class.getCanonicalName());
    private final List<String> toMapTypes = Arrays.asList(Map.class.getCanonicalName(),
            HashMap.class.getCanonicalName(), TreeMap.class.getCanonicalName(),
            NavigableMap.class.getCanonicalName(), LinkedHashMap.class.getCanonicalName(),
            Hashtable.class.getCanonicalName(), Properties.class.getCanonicalName());

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
        return checkObject(type, true);
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

            case "java.lang.Byte":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Float":
            case "java.lang.Long":
            case "java.lang.Double":
                return "number";
            case "char":
            case "java.lang.Character":
                return "string";
            case "boolean":
            case "java.lang.Boolean":
                return "boolean";
        }
        return null;
    }

    private String checkGeneric(String type) {
        final int firstScope = type.indexOf('<');
        final String basicType = checkObject(type.substring(0, firstScope), false);
        if ("Object".equals(basicType)) return basicType;
        StringBuilder innerTypes = new StringBuilder();
        final String generic = type.substring(firstScope + 1, type.length() - 1);
        List<String> genericTypes = new ArrayList<>();
        int triangleScopes = 0;
        int start = 0;
        int end = 0;
        for (char c : generic.toCharArray()) {
            if (c == '<') triangleScopes++;
            else if (c == '>') triangleScopes--;
            if (c == ',' && triangleScopes == 0) {
                genericTypes.add(generic.substring(start, end));
                start = end + 1;
            }
            end++;
        }
        genericTypes.add(generic.substring(start, end));
        final int lastIndex = genericTypes.size() - 1;
        for (int i = 0; i < genericTypes.size(); i++) {
            innerTypes.append(checkType(genericTypes.get(i).trim()));
            if (i != lastIndex) innerTypes.append(", ");
        }
        return basicType + "<" + innerTypes.toString() + ">";

    }

    private String checkObject(String type, boolean empty) {
        if (String.class.getCanonicalName().equals(type)) return "string";
        else if (toArrayTypes.contains(type)) {
            if (empty) return "Array<Object>";
            else return "Array";
        } else if (toMapTypes.contains(type)) {
            if (empty) return "Map<Object, Object>";
            else return "Map";
        } else if (toSetTypes.contains(type)) {
            if (empty) return "Set<Object>";
            else return "Set";
        }
        else return "Object";
    }
}