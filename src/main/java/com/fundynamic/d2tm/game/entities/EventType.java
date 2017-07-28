package com.fundynamic.d2tm.game.entities;


public enum EventType {
    ENTITY_DESTROYED(1),
    DUMMY(9999);

    private int id;

    EventType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
