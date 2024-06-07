package org.example.app.service.DTO;

import lombok.Getter;
import org.example.app.domain.entity.customer.Customer;
import org.example.app.utils.time.TimeConverter;

import java.util.UUID;

@Getter
public class CustomerDTO {
    private Long id;
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String createdAt;
    private String updatedAt;

    public CustomerDTO() {
    }

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.customerId = customer.getCustomerId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.createdAt = TimeConverter.millisToDateTime(customer.getCreatedAt());
        this.updatedAt = TimeConverter.millisToDateTime(customer.getUpdatedAt());
    }
}
