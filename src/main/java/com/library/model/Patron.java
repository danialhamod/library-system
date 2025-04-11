package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "patron", 
       uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patron {
    public Patron(String name, String email, String contactInfo) {
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Contact information is required")
    @Size(max = 100, message = "Contact information cannot exceed 100 characters")
    private String contactInfo;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
}