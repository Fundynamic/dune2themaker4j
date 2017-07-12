package com.fundynamic.d2tm.game.entities;


public enum EventType {
    ENTITY_DESTROYED(1),
    DUMMY(9999);


    EventType(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }
}
