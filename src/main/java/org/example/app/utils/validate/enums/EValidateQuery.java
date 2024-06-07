package org.example.app.utils.validate.enums;

import lombok.Getter;

@Getter
public enum EValidateQuery implements IValidateUnit {
    ID("id must be integer number", "^[0-9]+$");

    private final String errorMsg;
    private final String validationRegex;

    EValidateQuery(String errorMsg, String validationRegex) {
        this.errorMsg = errorMsg;
        this.validationRegex = validationRegex;
    }

}
