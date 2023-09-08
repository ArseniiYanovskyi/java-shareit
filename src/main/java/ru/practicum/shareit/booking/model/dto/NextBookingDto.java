package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NextBookingDto {
    private long id;
    private long bookerId;
}
