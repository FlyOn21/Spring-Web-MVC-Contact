package org.example.app.utils.validate.validate_entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
public class ValidateResult {
    @Setter
    private boolean isValid = false;
    @Getter
    private final List<String> errorsList = new ArrayList<>();


    public boolean isValid() {
        return !this.isValid;
    }

    public void addError(String error) {
        this.errorsList.add(error);
    }

}
