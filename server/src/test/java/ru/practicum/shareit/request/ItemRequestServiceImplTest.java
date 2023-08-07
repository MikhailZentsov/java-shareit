package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {
    private static ItemRequestService requestService;
    private static UserStorage userStorage;
    private static ItemRequestStorage requestStorage;

    private static User user;
    private static ItemRequest request;
    private static LocalDateTime currentTime;
    private static CreateItemRequestDto requestDto;
    private static List<ItemRequest> listOfRequests;

    @BeforeAll
    static void beforeAll() {
        currentTime = LocalDateTime.now();

        user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@ya.ru")
                .build();

        request = ItemRequest.builder()
                .id(1L)
                .description("request description")
                .requester(user)
                .created(currentTime)
                .items(new HashSet<>())
                .build();

        requestDto = CreateItemRequestDto.builder()
                .description("request description")
                .build();

        listOfRequests = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            listOfRequests.add(request.toBuilder().id(i + 1L).build());
        }
    }

    @BeforeEach
    void setUp() {
        userStorage = Mockito.mock(UserStorage.class);
        requestStorage = Mockito.mock(ItemRequestStorage.class);
        requestService = new ItemRequestServiceImpl(requestStorage, userStorage);
    }

    @Test
    void shouldCreateRequest() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.save(any(ItemRequest.class)))
                .thenReturn(request);

        GetItemRequestDto getItemRequestDto = requestService.createRequest(user.getId(), requestDto);

        assertThat(getItemRequestDto)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("items", new ArrayList<>());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void shouldGetExceptionWithCreateRequestNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.save(any(ItemRequest.class)))
                .thenReturn(request);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestService.createRequest(user.getId(), requestDto)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).save(any(ItemRequest.class));
    }

    @Test
    void shouldGetRequestById() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(request));

        GetItemRequestDto getItemRequestDto = requestService.getRequestById(user.getId(), request.getId());

        assertThat(getItemRequestDto)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrProperty("created")
                .hasFieldOrPropertyWithValue("items", new ArrayList<>());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetExceptionWithRequestByIdNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(request));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestService.getRequestById(user.getId(), request.getId())
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
    }

    @Test
    void shouldGetExceptionWithRequestByIdNotFoundRequest() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.empty());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestService.getRequestById(user.getId(), request.getId())
        );

        assertEquals("Запрос на вещь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetAllRequestsByUserId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.getAllByRequester(any(User.class), any(Sort.class)))
                .thenReturn(listOfRequests);

        List<GetItemRequestDto> requests = requestService.getAllRequestsByUserId(user.getId());

        assertThat(requests)
                .isNotEmpty()
                .hasSize(20)
                .satisfies(list -> assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 2L)
                        .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                        .hasFieldOrProperty("created")
                        .hasFieldOrPropertyWithValue("items", new ArrayList<>()));
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).getAllByRequester(any(User.class), any(Sort.class));
    }

    @Test
    void shouldGetExceptionWithGetAllRequestsByUserIdNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.getAllByRequester(any(User.class), any(Sort.class)))
                .thenReturn(listOfRequests);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestService.getAllRequestsByUserId(user.getId())
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).getAllByRequester(any(User.class), any(Sort.class));
    }

    @Test
    void shouldGetAllRequests() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.getAllByRequesterNot(any(User.class), any(Pageable.class)))
                .thenReturn(listOfRequests);

        List<GetItemRequestDto> requests = requestService.getAllRequests(user.getId(), 7, 3);

        assertThat(requests)
                .isNotEmpty()
                .hasSize(20)
                .satisfies(list -> assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 2L)
                        .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                        .hasFieldOrProperty("created")
                        .hasFieldOrPropertyWithValue("items", new ArrayList<>()));
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).getAllByRequesterNot(any(User.class), any(Pageable.class));
    }

    @Test
    void shouldGetExceptionWithGetAllRequestsNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.getAllByRequesterNot(any(User.class), any(Pageable.class)))
                .thenReturn(listOfRequests);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> requestService.getAllRequests(user.getId(), 7, 3)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).getAllByRequesterNot(any(User.class), any(Pageable.class));
    }
}