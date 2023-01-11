package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "items")
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "available")
    private Boolean available;
    @Column(name = "owner_id", nullable = false)
    private Long owner;
}
