package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "item_id")
    Long itemId;
    @Column(name = "booker_id")
    Long bookerId;
    @Column(name = "start_time")
    LocalDateTime start;
    @Column(name = "end_time")
    LocalDateTime end;
    @Column
    Status status;
}
