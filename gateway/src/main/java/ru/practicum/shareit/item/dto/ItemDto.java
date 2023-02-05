package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    long id;
    @NotBlank
    String name;
    @NotNull
    String description;
    @NotNull
    Boolean available;
    List<CommentDto> comments;
    BookingDtoItem lastBooking;
    BookingDtoItem nextBooking;
    Long requestId;
}
