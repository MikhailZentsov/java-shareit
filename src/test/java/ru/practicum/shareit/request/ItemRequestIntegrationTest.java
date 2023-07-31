package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@Slf4j
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestIntegrationTest {
    private final ItemRequestService requestService;
    private final UserStorage userStorage;
    private final EntityManager entityManager;

    private static User user;
    private static User user2;
    private static CreateItemRequestDto requestDto;

    @BeforeAll
    static void beforeAll() {
        user = User.builder()
                .id(1L)
                .name("userName")
                .email("mail@ya.ru")
                .build();

        user2 = User.builder()
                .id(2L)
                .name("userName")
                .email("mail2@ya.ru")
                .build();

        requestDto = CreateItemRequestDto.builder()
                .description("need item")
                .build();
    }

    @Test
    void shouldCreateRequest() {
        userStorage.save(user);
        requestService.createRequest(user.getId(), requestDto);

        TypedQuery<ItemRequest> query = entityManager.createQuery("select r from ItemRequest r where r.id = :id", ItemRequest.class);
        ItemRequest request = query.setParameter("id", 1L).getSingleResult();

        assertThat(requestDto.getDescription(), equalTo(request.getDescription()));
        assertThat(user, equalTo(request.getRequester()));
        assertNull(request.getItems());
    }

    @Test
    void shouldGetRequestById() {
        userStorage.save(user);
        requestService.createRequest(user.getId(), requestDto);

        GetItemRequestDto request = requestService.getRequestById(user.getId(), 1L);

        assertThat(requestDto.getDescription(), equalTo(request.getDescription()));
        assertThat(List.of(), equalTo(request.getItems()));
    }

    @Test
    void shouldGetAllRequests() throws InterruptedException {
        userStorage.save(user);
        userStorage.save(user2);
        CreateItemRequestDto requestDto2 = CreateItemRequestDto.builder().description("request2").build();
        CreateItemRequestDto requestDto3 = CreateItemRequestDto.builder().description("request3").build();
        CreateItemRequestDto requestDto4 = CreateItemRequestDto.builder().description("request4").build();
        CreateItemRequestDto requestDto5 = CreateItemRequestDto.builder().description("request5").build();
        CreateItemRequestDto requestDto6 = CreateItemRequestDto.builder().description("request6").build();
        CreateItemRequestDto requestDto7 = CreateItemRequestDto.builder().description("request7").build();

        requestService.createRequest(user.getId(), requestDto);
        Thread.sleep(1000);
        requestService.createRequest(user2.getId(), requestDto5);
        Thread.sleep(1000);
        requestService.createRequest(user2.getId(), requestDto6);
        Thread.sleep(1000);
        requestService.createRequest(user2.getId(), requestDto7);
        Thread.sleep(1000);
        requestService.createRequest(user.getId(), requestDto2);
        Thread.sleep(1000);
        requestService.createRequest(user.getId(), requestDto3);
        Thread.sleep(1000);
        requestService.createRequest(user.getId(), requestDto4);

        List<GetItemRequestDto> requests = requestService.getAllRequests(2L, 1, 5);
        log.info(requests.toString());

        Assertions.assertThat(requests)
                .isNotEmpty()
                .hasSize(3)
                .satisfies(list -> {
                    Assertions.assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 6L)
                            .hasFieldOrPropertyWithValue("description", requestDto3.getDescription());
                    Assertions.assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 5L)
                            .hasFieldOrPropertyWithValue("description", requestDto2.getDescription());
                    Assertions.assertThat(list.get(2)).hasFieldOrPropertyWithValue("id", 1L)
                            .hasFieldOrPropertyWithValue("description", requestDto.getDescription());
                });
    }

    @Test
    void shouldGetAllRequestsByUserId() throws InterruptedException {
        userStorage.save(user.toBuilder().build());
        userStorage.save(user2.toBuilder().build());
        CreateItemRequestDto requestDto1 = CreateItemRequestDto.builder().description("request1").build();
        CreateItemRequestDto requestDto2 = CreateItemRequestDto.builder().description("request2").build();
        CreateItemRequestDto requestDto3 = CreateItemRequestDto.builder().description("request3").build();
        CreateItemRequestDto requestDto4 = CreateItemRequestDto.builder().description("request4").build();
        CreateItemRequestDto requestDto5 = CreateItemRequestDto.builder().description("request5").build();
        CreateItemRequestDto requestDto6 = CreateItemRequestDto.builder().description("request6").build();
        CreateItemRequestDto requestDto7 = CreateItemRequestDto.builder().description("request7").build();

        requestService.createRequest(user.getId(), requestDto1);
        Thread.sleep(1000);
        requestService.createRequest(user2.getId(), requestDto5);
        Thread.sleep(1000);
        requestService.createRequest(user2.getId(), requestDto6);
        Thread.sleep(1000);
        requestService.createRequest(user2.getId(), requestDto7);
        Thread.sleep(1000);
        requestService.createRequest(user.getId(), requestDto2);
        Thread.sleep(1000);
        requestService.createRequest(user.getId(), requestDto3);
        Thread.sleep(1000);
        requestService.createRequest(user.getId(), requestDto4);

        List<GetItemRequestDto> requests1 = requestService.getAllRequestsByUserId(2L);
        log.info(requests1.toString());

        Assertions.assertThat(requests1)
                .isNotEmpty()
                .hasSize(3)
                .satisfies(list -> {
                    Assertions.assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 4L)
                            .hasFieldOrPropertyWithValue("description", requestDto7.getDescription());
                    Assertions.assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 3L)
                            .hasFieldOrPropertyWithValue("description", requestDto6.getDescription());
                    Assertions.assertThat(list.get(2)).hasFieldOrPropertyWithValue("id", 2L)
                            .hasFieldOrPropertyWithValue("description", requestDto5.getDescription());
                });
    }
}
