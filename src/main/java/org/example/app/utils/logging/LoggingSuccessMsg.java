package org.example.app.utils.logging;

import lombok.Getter;

@Getter
public enum LoggingSuccessMsg {
    DB_ENTITY_ADDED("Entity added to database"),
    DB_ENTITY_DELETED("Entity deleted from database"),
    DB_ENTITY_UPDATED("Entity updated in database");

    private final String msg;

    LoggingSuccessMsg(String msg) {
        this.msg = msg;
    }

}
