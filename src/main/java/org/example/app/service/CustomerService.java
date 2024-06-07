package org.example.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.domain.entity.customer.Customer;
import org.example.app.exceptions.CRUDException;
import org.example.app.repository.impl.CustomerRepository;
import org.example.app.service.DTO.CustomerDTO;
import org.example.app.utils.HandlerExceptions;
import org.example.app.utils.utils_obj.QueryResult;
import org.example.app.utils.utils_obj.ResponseData;
import org.example.app.validation.create_update_form.FormDataForValidate;
import org.example.app.validation.create_update_form.ValidationFormProcessing;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service("customerService")
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    private static final Logger SERVICE_LOGGER =
            LogManager.getLogger(CustomerRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");



    public ResponseData<Customer> create(FormDataForValidate input) {
        HandlerExceptions<Customer> handlerExceptions = new HandlerExceptions<>();
        try {
            ValidationFormProcessing validationForm = new ValidationFormProcessing(repository, input);
            if (!validationForm.isValidForm()) {
                return handlerExceptions.handleValidationFormErrors(validationForm);
            }

            Customer customer = new Customer(UUID.randomUUID(), input.getFirstName(), input.getLastName(), input.getEmail(), input.getPhoneNumber());
            QueryResult<Customer> queryResult = repository.create(customer);
            if (!queryResult.isSuccess()) {
                throw new CRUDException("Database operation failed");
            }
            return new ResponseData<>(
                    true,
                    true,
                    queryResult.getEntity(),
                    null,
                    queryResult.getMsg(),
                    Collections.emptyList(),
                    null
            );

        } catch (HibernateException | CRUDException e) {
            return handlerExceptions.handleException("Create customer", e);
        }
    }

        public ResponseData<CustomerDTO> getAll() {
            HandlerExceptions<CustomerDTO> handlerExceptions = new HandlerExceptions<>();
        try{
            QueryResult<Customer> queryResult = repository.readAll();
            List<CustomerDTO> customerDTOList = queryResult.getEntity().stream()
                    .map(CustomerDTO::new)
                    .toList();
            return new ResponseData<>(
                    queryResult.isSuccess(),
                    queryResult.isSuccess(),
                    customerDTOList,
                    null,
                    queryResult.getMsg(),
                    Collections.emptyList(),
                    null
            );
        } catch (HibernateException e) {
            SERVICE_LOGGER.error("Failed to get customers: {}", e.getMessage());
            CONSOLE_LOGGER.error("Failed to get customers: {}", e.getMessage());
            return handlerExceptions.handleException("Get customers", e);
        }
    }



    public ResponseData<Customer> getById(Long id) {
        HandlerExceptions<Customer> handlerExceptions = new HandlerExceptions<>();
        try {
            QueryResult<Customer> getByIdResult = repository.readById(id);
            if (!getByIdResult.isSuccess()) {
                return new ResponseData<>(
                        false,
                        false,
                        null,
                        null,
                        getByIdResult.getMsg(),
                        List.of("Customer not found"),
                        null
                );
            }
            return new ResponseData<>(
                    true,
                    true,
                    getByIdResult.getEntity(),
                    null,
                    getByIdResult.getMsg(),
                    Collections.emptyList(),
                    null
            );
        } catch (HibernateException e) {
            return handlerExceptions.handleException("Get customer", e);
        }
    }

    public ResponseData<Customer> update(FormDataForValidate input) {
        HandlerExceptions<Customer> handlerExceptions = new HandlerExceptions<>();
        try {
            QueryResult<Customer> customerData = repository.readById(input.getId());
            if (!customerData.isSuccess()) {
                return new ResponseData<>(
                        false,
                        false,
                        null,
                        null,
                        customerData.getMsg(),
                        List.of("Customer not found"),
                        null
                );
            }
            Customer customer = customerData.getEntity().getFirst();
            ValidationFormProcessing validationForm = new ValidationFormProcessing(repository, input, customer.getId());
            if (!validationForm.isValidForm()) {
                return handlerExceptions.handleValidationFormErrors(validationForm);
            }

            customer.setFirstName(input.getFirstName() == null ? customer.getFirstName() : input.getFirstName());
            customer.setLastName(input.getLastName() == null ? customer.getLastName() : input.getLastName());
            customer.setEmail(input.getEmail() == null ? customer.getEmail() : input.getEmail());
            customer.setPhoneNumber(input.getPhoneNumber() == null ? customer.getPhoneNumber() : input.getPhoneNumber());
            customer.setUpdatedAt(System.currentTimeMillis());
            QueryResult<Customer> queryResult = repository.update(customer);
            if (!queryResult.isSuccess()) {
                throw new CRUDException("Database update operation failed");
            }
            return new ResponseData<>(
                    true,
                    true,
                    queryResult.getEntity(),
                    null,
                    queryResult.getMsg(),
                    Collections.emptyList(),
                    null
            );

        } catch (HibernateException | CRUDException e) {
            return handlerExceptions.handleException("Update customer", e);
        }
    }

    public ResponseData<Customer> delete(Long id) {
        HandlerExceptions<Customer> handlerExceptions = new HandlerExceptions<>();
        try {
            QueryResult<Customer> getByIdResult = repository.readById(id);
            if (!getByIdResult.isSuccess()) {
                return new ResponseData<>(
                        false,
                        false,
                        null,
                        null,
                        getByIdResult.getMsg(),
                        List.of("Customer not found"),
                        null
                );
            }
            QueryResult<Customer> deleteResult = repository.delete(id);
            if (!deleteResult.isSuccess()) {
                throw new CRUDException("Database delete operation failed");
            }
            return new ResponseData<>(
                    true,
                    true,
                    deleteResult.getEntity(),
                    null,
                    deleteResult.getMsg(),
                    Collections.emptyList(),
                    null
            );

        } catch (DataAccessException | CRUDException e) {
            return handlerExceptions.handleException("delete customer", e);
        }
    }
}
