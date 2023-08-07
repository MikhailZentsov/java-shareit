package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.MethodArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.util.ServerConstants.orderByCreatedDesc;

class ItemServiceImplTest {
    private static ItemService itemService;
    private static ItemStorage itemStorage;
    private static UserStorage userStorage;
    private static CommentStorage commentStorage;
    private static ItemRequestStorage requestStorage;

    private static User user;
    private static ItemRequest request;
    private static LocalDateTime currentTime;
    private static CreateUpdateItemDto createItemDto;
    private static CreateUpdateItemDto updateItemDto;
    private static CreateCommentDto createCommentDto;
    private static Item item;
    private static Item updatedItem;
    private static Booking booking;
    private static Comment comment;
    private static List<Item> listOfItems;

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

        createItemDto = CreateUpdateItemDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .requestId(1L)
                .build();

        updateItemDto = CreateUpdateItemDto.builder()
                .name("updatedName")
                .description("updatedDescription")
                .available(false)
                .build();

        createCommentDto = CreateCommentDto.builder()
                .text("comment")
                .build();

        item = Item.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .request(request)
                .owner(user)
                .bookings(Set.of())
                .comments(null)
                .build();

        updatedItem = Item.builder()
                .id(1L)
                .name("updatedName")
                .description("updatedDescription")
                .available(false)
                .request(request)
                .request(request)
                .owner(user)
                .bookings(Set.of())
                .comments(null)
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().minusDays(1))
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .author(user)
                .created(LocalDateTime.now())
                .build();

        item.setBookings(Set.of(booking));

        listOfItems = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            listOfItems.add(item.toBuilder().id(i + 1L).build());
        }
    }

    @BeforeEach
    void setUp() {
        itemStorage = Mockito.mock(ItemStorage.class);
        userStorage = Mockito.mock(UserStorage.class);
        commentStorage = Mockito.mock(CommentStorage.class);
        requestStorage = Mockito.mock(ItemRequestStorage.class);
        itemService = new ItemServiceImpl(itemStorage, userStorage, commentStorage, requestStorage);
    }

    @Test
    void shouldCreateItemWithRequest() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item);

        GetItemDto itemDto = itemService.create(user.getId(), createItemDto);

        assertThat(itemDto)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", createItemDto.getName())
                .hasFieldOrPropertyWithValue("description", createItemDto.getDescription())
                .hasFieldOrPropertyWithValue("requestId", 1L)
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", null)
                .hasFieldOrPropertyWithValue("comments", new TreeSet<>(orderByCreatedDesc));
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).save(any(Item.class));
    }

    @Test
    void shouldGetExceptionWithCreateWithNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.create(1L, createItemDto)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).save(any(Item.class));
    }

    @Test
    void shouldGetExceptionWithCreateWithNotFoundRequest() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.create(1L, createItemDto)
        );

        assertEquals("Запрос на вещь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, times(1)).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).save(any(Item.class));
    }

    @Test
    void shouldUpdateItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));
        when(itemStorage.save(updatedItem))
                .thenReturn(updatedItem);

        GetItemDto itemDto = itemService.update(user.getId(), updatedItem.getId(), updateItemDto);

        assertThat(itemDto)
                .hasFieldOrPropertyWithValue("id", updatedItem.getId())
                .hasFieldOrPropertyWithValue("name", updateItemDto.getName())
                .hasFieldOrPropertyWithValue("description", updateItemDto.getDescription())
                .hasFieldOrPropertyWithValue("requestId", 1L)
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", null)
                .hasFieldOrPropertyWithValue("comments", new TreeSet<>(orderByCreatedDesc));
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(itemStorage, times(1)).save(any(Item.class));
    }

    @Test
    void shouldGetExceptionWithUpdateItemWithNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));
        when(itemStorage.save(updatedItem))
                .thenReturn(updatedItem);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(1L, 1L, updateItemDto)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).findById(anyLong());
        verify(itemStorage, never()).save(any(Item.class));
    }

    @Test
    void shouldGetExceptionWithUpdateItemWithNotFoundItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(itemStorage.save(updatedItem))
                .thenReturn(updatedItem);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(1L, 1L, updateItemDto)
        );

        assertEquals("Вещь не найдена",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(itemStorage, never()).save(any(Item.class));
    }

    @Test
    void shouldGetExceptionWithUpdateItemWithNotFoundOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user.toBuilder().id(2L).build()));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));
        when(itemStorage.save(updatedItem))
                .thenReturn(updatedItem);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.update(1L, 1L, updateItemDto)
        );

        assertEquals("У пользователя с ID = 2 не найдена вещь с ID = 1",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(itemStorage, never()).save(any(Item.class));
    }

    @Test
    void shouldDeleteItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));
        doNothing().when(itemStorage).deleteById(anyLong());

        itemService.delete(user.getId(), item.getId());

        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(itemStorage, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldGetExceptionWithDeleteItemWithNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));
        doNothing().when(itemStorage).deleteById(anyLong());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.delete(1L, 1L)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).findById(anyLong());
        verify(itemStorage, never()).deleteById(anyLong());
    }

    @Test
    void shouldGetExceptionWithDeleteItemWithNotFoundItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        doNothing().when(itemStorage).deleteById(anyLong());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.delete(1L, 1L)
        );

        assertEquals("Вещь не найдена",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(itemStorage, never()).deleteById(anyLong());
    }

    @Test
    void shouldGetExceptionWithDeleteItemWithNotFoundOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user.toBuilder().id(2L).build()));
        when(requestStorage.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(commentStorage.findById(anyLong()))
                .thenReturn(null);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));
        doNothing().when(itemStorage).deleteById(anyLong());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.delete(1L, 1L)
        );

        assertEquals("У пользователя с ID = 2 не найдена вещь с ID = 1",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(itemStorage, never()).deleteById(anyLong());
    }

    @Test
    void shouldGetByIdByOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));

        itemService.getOneById(user.getId(), item.getId());

        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetByIdByNotOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user.toBuilder().id(2L).build()));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));

        itemService.getOneById(2L, item.getId());

        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetExceptionGetByIdByWithNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item.toBuilder().build()));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getOneById(1L, 1L)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).findById(anyLong());
    }

    @Test
    void shouldGetExceptionGetByIdByWithNotFoundItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.empty());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getOneById(1L, 1L)
        );

        assertEquals("Вещь не найдена",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetAllByUserIdByOwner() {
        when(itemStorage.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(listOfItems));

        List<GetItemDto> items = itemService.getAllByUserId(1L, 7, 3);

        assertThat(items)
                .isNotEmpty()
                .hasSize(20)
                .satisfies(list -> {
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "itemName");
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("description", "itemDescription");
                });
        verify(userStorage, never()).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findAllByOwnerId(anyLong(), any(Pageable.class));
    }

    @Test
    void shouldGetAllByUserIdByNotOwner() {
        when(itemStorage.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(listOfItems));

        List<GetItemDto> items = itemService.getAllByUserId(2L, 7, 3);

        assertThat(items)
                .isEmpty();
        verify(userStorage, never()).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findAllByOwnerId(anyLong(), any(Pageable.class));
    }

    @Test
    void shouldSearch() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.search(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(listOfItems));

        List<GetItemDto> items = itemService.search(1L, "text", 7, 3);

        assertThat(items)
                .isNotEmpty()
                .hasSize(20)
                .satisfies(list -> {
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", "itemName");
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("description", "itemDescription");
                });
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).search(anyString(), any(Pageable.class));
    }

    @Test
    void shouldExceptionWithSearchNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(itemStorage.search(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(listOfItems));

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.search(1L, "text", 7, 3)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).search(anyString(), any(Pageable.class));
    }

    @Test
    void shouldGetEmptyListWithSearchWithBlankText() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.search(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(listOfItems));

        List<GetItemDto> items = itemService.search(1L, " ", 7, 3);

        assertThat(items)
                .isEmpty();
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).search(anyString(), any(Pageable.class));
    }

    @Test
    void shouldCreateComment() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(commentStorage.save(any(Comment.class)))
                .thenReturn(comment);

        itemService.createComment(1L, 1L, createCommentDto);

        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(commentStorage, times(1)).save(any(Comment.class));
    }

    @Test
    void shouldGetExceptionWithCreateCommentWithNotFoundUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(commentStorage.save(any(Comment.class)))
                .thenReturn(comment);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(1L, 1L, createCommentDto)
        );

        assertEquals("Пользователь не найден",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, never()).findById(anyLong());
        verify(commentStorage, never()).save(any(Comment.class));
    }

    @Test
    void shouldGetExceptionWithCreateCommentWithNotFoundItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(commentStorage.save(any(Comment.class)))
                .thenReturn(comment);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(1L, 1L, createCommentDto)
        );

        assertEquals("Вещь не найдена",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(commentStorage, never()).save(any(Comment.class));
    }

    @Test
    void shouldGetExceptionWithCreateCommentWithNotFoundBooking() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user.toBuilder().id(2L).build()));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(commentStorage.save(any(Comment.class)))
                .thenReturn(comment);

        final MethodArgumentException exception = Assertions.assertThrows(
                MethodArgumentException.class,
                () -> itemService.createComment(2L, 1L, createCommentDto)
        );

        assertEquals("Пользователь с ID = 2 не брал в аренду вещь с ID = 1",
                exception.getMessage());
        verify(userStorage, times(1)).findById(anyLong());
        verify(requestStorage, never()).findById(anyLong());
        verify(commentStorage, never()).findById(anyLong());
        verify(itemStorage, times(1)).findById(anyLong());
        verify(commentStorage, never()).save(any(Comment.class));
    }
}