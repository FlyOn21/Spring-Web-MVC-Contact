package org.example.app.validation.create_update_form;

import lombok.Getter;
import org.example.app.repository.impl.CustomerRepository;
import org.example.app.utils.validate.Validator;
import org.example.app.utils.validate.enums.EValidateCustomer;
import org.example.app.utils.validate.enums.IValidateUnit;
import org.example.app.utils.validate.validate_entity.ValidateResult;

import java.util.HashMap;


@Getter
public class ValidationFormProcessing {
    private final HashMap<String, String> validationFormErrors = new HashMap<>();
    private boolean isValidForm = true;
    private final FormDataForValidate data;
    private final CustomerRepository repository;
    private Long id;


    public ValidationFormProcessing(CustomerRepository repository, FormDataForValidate data) {
        this.repository = repository;
        this.data = data;
        validateFirstName(data.getFirstName());
        validateLastName(data.getLastName());
        validateEmail(data.getEmail());
        validatePhone(data.getPhoneNumber());
    }



    public ValidationFormProcessing(CustomerRepository repository, FormDataForValidate data, Long id) {
        this.repository = repository;
        this.id = id;
        this.data = data;
        validateFirstName(data.getFirstName());
        validateLastName(data.getLastName());
        validateEmail(data.getEmail());
        validatePhone(data.getPhoneNumber());
    }


    private ValidateResult validateField(String value, IValidateUnit validationType) {
        Validator<IValidateUnit> validator = new Validator<>();
        if (id != null) {
            if (value == null) {
                ValidateResult answer = new ValidateResult();
                answer.setValid(true);
                return answer;
            }
            return validator.validate(value, validationType);
        } 
        if (value==null) {
            ValidateResult answer = new ValidateResult();
            answer.addError(" Is empty (required)");
            return answer;
        }
        return validator.validate(value, validationType);
    }

    private void validateFirstName(String firstName) {
            ValidateResult answer = validateField(firstName, EValidateCustomer.FIRST_NAME);
            if (answer.isValid()) {
                isValidForm = false;
                validationFormErrors.put("firstName", String.join(", ", answer.getErrorsList()));
            }

    }

    private void validateLastName(String  lastName) {
        ValidateResult answer = validateField(lastName, EValidateCustomer.LAST_NAME);
        if (answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("lastName", String.join(", ", answer.getErrorsList()));
        }
    }

    private void validateEmail(String  email) {
        ValidateResult answer = validateField(email, EValidateCustomer.EMAIL);
        if (answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("email", String.join(", ", answer.getErrorsList()));
        }
        if (id == null && isEmailExists(email)) {
            isValidForm = false;
            answer.addError("Email already exists");
            validationFormErrors.put("email", String.join(", ", answer.getErrorsList()));
        }
        if (id != null && isEmailExists(email) && !isCustomerEmail(email, id)) {
            isValidForm = false;
            answer.addError("Email already exists");
            validationFormErrors.put("email", String.join(", ", answer.getErrorsList()));
        }
    }

    private void validatePhone(String  phone) {
        ValidateResult answer = validateField(phone, EValidateCustomer.PHONE);
        if (answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("phoneNumber", String.join(", ", answer.getErrorsList()));
        }
    }

    private boolean isEmailExists(String email) {
        return repository.isEmailExists(email);
    }

    private boolean isCustomerEmail(String email, Long id) {
        return repository.isCustomerEmail(email, id);
    }
}
