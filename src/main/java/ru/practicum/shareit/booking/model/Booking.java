package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "rental_start", nullable = false)
    private LocalDateTime start;
    @Column(name = "rental_end", nullable = false)
    private LocalDateTime end;
    @JoinColumn(name = "booker_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User booker;
    @JoinColumn(name = "item_id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;
    @Column(name = "status")
    private Status status;
}
