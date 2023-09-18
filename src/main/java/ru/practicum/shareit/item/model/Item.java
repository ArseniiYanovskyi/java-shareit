package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "isAvailable", nullable = false)
    private Boolean isAvailable;

    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    private long request;
}
