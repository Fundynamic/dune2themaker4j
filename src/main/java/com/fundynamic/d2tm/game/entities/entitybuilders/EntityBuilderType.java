package com.fundynamic.d2tm.game.entities.entitybuilders;


import java.util.ArrayList;
import java.util.List;

public enum EntityBuilderType {
    NONE,            // does not build a thing
    STRUCTURES,      // builds structures
    UNITS,           // builds units
    SUPERPOWERS;      // super powers (death-hand, fremen, etc)

    /**
     * For now like this, TODO: optimize (caching, etc)
     * @return
     */
    public static List<String> getValues() {
        List<String> result = new ArrayList<>();
        for (EntityBuilderType val : values()) {
            result.add(val.name());
        }
        return result;
    }
}
