import annotations.TSIgnore;
import annotations.TSInterface;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@TSInterface(accessLevel = {
        TSInterface.AccessLevel.PRIVATE,
        TSInterface.AccessLevel.PUBLIC,
        TSInterface.AccessLevel.NO})
class Person {
    private String name;
    public int age[];
    @TSIgnore
    public boolean isMarried;
    public Map<String, String> map;
    @TSIgnore
    int height;
}