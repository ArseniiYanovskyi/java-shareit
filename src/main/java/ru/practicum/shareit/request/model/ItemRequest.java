package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(name = "publisher")
    private long publisher;
    @NotNull
    @NotBlank
    @Column(name = "description")
    private String description;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
}
