package ru.practicum.shareit.item.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwner(Long id, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%',?1,'%') ) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%',?1,'%') ) AND i.available = true )")
    Page<Item> findItemByText(String word, Pageable pageable);
    List<ItemDtoForRequest> findAllByRequestId(Long requestId);
}
