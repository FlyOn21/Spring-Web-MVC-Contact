package org.example.app.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.repository.impl.CustomerRepository;
import org.example.app.utils.logging.print.ErrorsHashMapToString;
import org.example.app.utils.utils_obj.ResponseData;
import org.example.app.validation.create_update_form.ValidationFormProcessing;

import java.util.Collections;

public class HandlerExceptions<T> {
    private static final Logger SERVICE_LOGGER =
            LogManager.getLogger(CustomerRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");

    private static final String VALIDATION_FAILED = "Validation failed";
    private static final String FAILED_TO = "Failed to ";

    public ResponseData<T> handleException(String operation, Exception e) {
        SERVICE_LOGGER.error(FAILED_TO + "{}: {}", operation, e.getMessage());
        CONSOLE_LOGGER.error(FAILED_TO + "{}: {}", operation, e.getMessage());
        return new ResponseData<>(
                false,
                false,
                null,
                null,
                FAILED_TO + operation,
                Collections.singletonList(e.getMessage()),
                null
        );
    }

    public ResponseData<T> handleValidationFormErrors(ValidationFormProcessing formData) {
        String logMessage = ErrorsHashMapToString.errorsHashMapToString(formData.getValidationFormErrors());
        SERVICE_LOGGER.error("Validation errors: {}", logMessage);
        CONSOLE_LOGGER.error("Validation errors: {}", logMessage);
        return new ResponseData<>(
                false,
                false,
                null,
                formData.getData(),
                VALIDATION_FAILED,
                Collections.emptyList(),
                formData.getValidationFormErrors()
        );
    }
}
