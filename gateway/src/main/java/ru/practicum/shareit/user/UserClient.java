package ru.practicum.shareit.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAll(int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        return get("", null, parameters);
    }

    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> create(CreateUpdateUserDto createUpdateUserDto) {
        return post("", createUpdateUserDto);
    }

    public ResponseEntity<Object> update(long userId, CreateUpdateUserDto createUpdateUserDto) {
        return patch("/" + userId, createUpdateUserDto);
    }

    public ResponseEntity<Object> deleteById(long userId) {
        return delete("/" + userId);
    }
}
