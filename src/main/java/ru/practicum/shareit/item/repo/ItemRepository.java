package ru.practicum.shareit.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(Long id);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%',:word,'%') ) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%',:word,'%') ) AND i.available = true )")
    List<Item> findItemByText(@Param("word") String word);


}
