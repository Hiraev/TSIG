import annotations.TSIgnore;
import annotations.TSInterface;

import java.util.*;

@TSInterface(accessLevel = {
        TSInterface.AccessLevel.PRIVATE,
        TSInterface.AccessLevel.PUBLIC,
        TSInterface.AccessLevel.NO})
class Person {
    public String surname;
    private String name;
    public int numbers[];
    public int[] keys;
    @TSIgnore
    public boolean isMarried;
    public Map<String, String> map;
    public Map<List<Map<Integer, Float>>, Set<String[][]>> veryBigType[];
    @TSIgnore
    int height;
}

@TSInterface(accessLevel = {TSInterface.AccessLevel.PRIVATE})
class Car {
    private String name;
    private int number;
    private List<String> owners;
    private Integer year;
}

@TSInterface
class AllPrimitiveAndWrappers {
    public byte type_byte;
    public short type_short;
    public int type_int;
    public float type_float;
    public long type_long;
    public double type_double;

    public Byte type_Byte;
    public Short type_Short;
    public Integer type_Integer;
    public Float type_Float;
    public Long type_Long;
    public Double type_Double;

    public boolean type_boolean;
    public Boolean type_Boolean;

    public char type_char;
    public Character type_Character;

    public List type_List;
    public ArrayList type_ArrayList;
    public AbstractList type_AbstractList;
    public AbstractSequentialList type_AbstractSequentialList;
    public Queue type_Queue;
    public Deque type_Deque;
    public AbstractQueue type_AbstractQueue;
    public LinkedList type_LinkedList;
    public PriorityQueue type_PriorityQueue;
    public Vector type_Vector;
    public Stack type_Stack;

    public Set type_Set;
    public AbstractSet type_AbstractSet;
    public SortedSet type_SortedSet;
    public HashSet type_HashSet;
    public NavigableSet type_NavigableSet;
    public TreeSet type_TreeSet;

    public Map type_Map;
    public HashMap type_HashMap;
    public TreeMap type_TreeMap;
    public NavigableMap type_NavigableMap;
    public LinkedHashMap type_LinkedHashMap;
    public Hashtable type_Hashtable;
    public Properties type_Properties;

    public Set<Map<Hashtable<Character, Integer>, NavigableMap<Boolean, Stack>>> something;
}