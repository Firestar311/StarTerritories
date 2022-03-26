package com.starmediadev.plugins.starterritories.objects.flag;

import java.util.*;

public class FlagList implements Iterable<Flag> {
    
    private Map<String, Flag> flags;
    
    public FlagList() {
        Map<String, Flag> generatedFlags = new HashMap<>();
        for (Flags flag : Flags.values()) {
            generatedFlags.put(flag.name().toLowerCase(), flag.createInstance());
        }
        flags = Collections.unmodifiableMap(generatedFlags);
    }
    
    public Flag get(String name) {
        return flags.get(name.toLowerCase());
    }
    
    public int size() {
        return flags.size();
    }
    
    public boolean isEmpty() {
        return flags.isEmpty();
    }
    
    public boolean contains(Object o) {
        if (o instanceof String str) {
            return flags.containsKey(str);
        } else if (o instanceof Flag flag) {
            return flags.containsValue(flag);
        }
        return false;
    }
    
    public Iterator<Flag> iterator() {
        return flags.values().iterator();
    }
}
