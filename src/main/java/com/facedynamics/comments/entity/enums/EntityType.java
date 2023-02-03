package com.facedynamics.comments.entity.enums;

public enum EntityType {

    comment("comment"),
    reply("reply"),
    post("post"),
    repost("repost");
    private final String label;
    EntityType(String s) {
        label = s;
    }
}
