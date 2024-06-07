package org.example.app.utils.utils_obj;

import lombok.*;
import org.example.app.validation.create_update_form.FormDataForValidate;

import java.util.HashMap;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {
    private boolean isSuccess;
    private boolean isValid;
    private List<T> data;
    private FormDataForValidate formData;
    private String msg;
    private List<String> commonErrors;
    private HashMap<String, String> validationFormErrors;
}
