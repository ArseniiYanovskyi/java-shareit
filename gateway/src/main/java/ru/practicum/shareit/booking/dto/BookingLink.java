package src.main.java.ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingLink {
    private long id;
    private long bookerId;
}
