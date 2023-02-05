package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";


    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemDto itemDto, Long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(Long userId, Integer from, Integer size) {
        return get("?from" + from + "&size=" + size, userId);
    }

    public ResponseEntity<Object> searchItem(String word, Integer from, Integer size) {
        return get("/search?text=" + word + "&from=" + from + "&size=" + size);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, Long id, Long userId) {
        return patch("/" + id, userId, itemDto);
    }

    public ResponseEntity<Object> addComment(CommentDto commentDto, Long userId, Long itemId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

}
