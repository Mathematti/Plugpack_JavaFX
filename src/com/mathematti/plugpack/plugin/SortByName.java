package com.mathematti.plugpack.plugin;

import java.util.Comparator;

public class SortByName implements Comparator<Plugin> {
    public int compare(Plugin a, Plugin b) {
        return a.getName().compareTo(b.getName());
    }
}
