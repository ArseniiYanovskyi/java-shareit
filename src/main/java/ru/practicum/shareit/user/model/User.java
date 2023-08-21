package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class User {
    long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
