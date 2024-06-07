package org.example.app.utils.logging;

import lombok.Getter;

@Getter
public enum LoggingErrorsMsg {
    DB_CONNECTION_ERROR("Database connection error"),
    DB_QUERY_ERROR("Database query error"),
    DB_UPDATE_ERROR("Database update error"),
    DB_INSERT_ERROR("Database insert error"),
    DB_EMAIL_EXISTS("Email already exists in database"),
    DB_DELETE_ERROR("Database delete error");

    private final String msg;

    LoggingErrorsMsg(String msg) {
        this.msg = msg;
    }

}
