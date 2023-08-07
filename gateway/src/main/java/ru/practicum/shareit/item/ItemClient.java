package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

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

    public ResponseEntity<Object> getAllByUserId(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getOneById(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> create(long userId, CreateUpdateItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(long userId, long itemId, CreateUpdateItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public void delete(long userId, long itemId) {
        delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> search(long userId, String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size);
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(long userId, long itemId, CreateCommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
