package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    @Email
    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;
}
