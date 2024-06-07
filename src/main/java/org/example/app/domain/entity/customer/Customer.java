package org.example.app.domain.entity.customer;

import jakarta.persistence.*;
import lombok.*;
import org.example.app.domain.entity.base_entity.BaseEntity;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Component
@Entity
@Table(name = "customers", indexes = {
        @Index(name = "idx_email", columnList = "email")
})
public class Customer extends BaseEntity {
    @UuidGenerator
    @Column(name = "customer_id", nullable = false, unique = true, updatable = false)
    private UUID customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private Long createdAt = System.currentTimeMillis();

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt = System.currentTimeMillis();

    public Customer(UUID customerId, String firstName, String lastName, String email, String phoneNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer( String firstName, String lastName, String email, String phoneNumber) {
        this.customerId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer(Long id, UUID customerId, String firstName, String lastName, String email, String phoneNumber) {
        super(id);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
