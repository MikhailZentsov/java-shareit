package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager entityManager;

    private static CreateUpdateItemDto createUpdateItemDto;
    private static CreateUpdateUserDto createUpdateUserDto;

    @BeforeAll
    static void beforeAll() {
        createUpdateItemDto = CreateUpdateItemDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();

        createUpdateUserDto = CreateUpdateUserDto.builder()
                .name("userName")
                .email("user@ya.ru")
                .build();
    }

    @Test
    void shouldCreateItem() {
        userService.create(createUpdateUserDto);
        itemService.create(1L, createUpdateItemDto);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", 1L).getSingleResult();

        assertThat(createUpdateItemDto.getName(), equalTo(item.getName()));
        assertThat(createUpdateItemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(createUpdateItemDto.getAvailable(), equalTo(item.getAvailable()));
        assertNull(createUpdateItemDto.getRequestId());
    }

    @Test
    void shouldUpdateItem() {
        userService.create(createUpdateUserDto);
        itemService.create(1L, createUpdateItemDto);

        CreateUpdateItemDto updateItemDto = createUpdateItemDto.toBuilder()
                .name("newName")
                .description("newDescription")
                .available(false)
                .build();

        itemService.update(1L, 1L, updateItemDto);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", 1L).getSingleResult();

        assertThat(updateItemDto.getName(), equalTo(item.getName()));
        assertThat(updateItemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(updateItemDto.getAvailable(), equalTo(item.getAvailable()));
        assertNull(createUpdateItemDto.getRequestId());
    }

    @Test
    void shouldDeleteItem() {
        userService.create(createUpdateUserDto);
        itemService.create(1L, createUpdateItemDto);

        assertThat(itemService.getAllByUserId(1L, 0, 20).size(), equalTo(1));

        itemService.delete(1L, 1L);

        assertThat(itemService.getAllByUserId(1L, 0, 20).size(), equalTo(0));
    }

    @Test
    void shouldGetOneById() {
        userService.create(createUpdateUserDto);
        itemService.create(1L, createUpdateItemDto);

        GetItemDto item = itemService.getOneById(1L, 1L);

        assertThat(createUpdateItemDto.getName(), equalTo(item.getName()));
        assertThat(createUpdateItemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(createUpdateItemDto.getAvailable(), equalTo(item.getAvailable()));
        assertNull(createUpdateItemDto.getRequestId());
    }

    @Test
    void shouldGetAllByUserId() {
        CreateUpdateItemDto itemDto2 = createUpdateItemDto.toBuilder().name("name2").build();
        CreateUpdateItemDto itemDto3 = createUpdateItemDto.toBuilder().name("name3").build();
        CreateUpdateItemDto itemDto4 = createUpdateItemDto.toBuilder().name("name4").build();
        CreateUpdateItemDto itemDto5 = createUpdateItemDto.toBuilder().name("name5").build();
        CreateUpdateItemDto itemDto6 = createUpdateItemDto.toBuilder().name("name6").build();
        CreateUpdateItemDto itemDto7 = createUpdateItemDto.toBuilder().name("name7").build();

        userService.create(createUpdateUserDto);

        itemService.create(1L, createUpdateItemDto);
        itemService.create(1L, itemDto2);
        itemService.create(1L, itemDto3);
        itemService.create(1L, itemDto4);
        itemService.create(1L, itemDto5);
        itemService.create(1L, itemDto6);
        itemService.create(1L, itemDto7);

        List<GetItemDto> items = itemService.getAllByUserId(1L,5, 2);

        Assertions.assertThat(items)
                .isNotEmpty()
                .hasSize(2)
                .satisfies(list -> {
                    Assertions.assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 6L);
                    Assertions.assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", itemDto6.getName());
                    Assertions.assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 7L);
                    Assertions.assertThat(list.get(1)).hasFieldOrPropertyWithValue("name", itemDto7.getName());
                });
    }

    @Test
    void shouldSearch() {
        CreateUpdateItemDto itemDto2 = createUpdateItemDto.toBuilder().name("name2").build();
        CreateUpdateItemDto itemDto3 = createUpdateItemDto.toBuilder().name("name3").build();
        CreateUpdateItemDto itemDto4 = createUpdateItemDto.toBuilder().name("name4").build();
        CreateUpdateItemDto itemDto5 = createUpdateItemDto.toBuilder().name("name5").build();
        CreateUpdateItemDto itemDto6 = createUpdateItemDto.toBuilder().name("name6 name4").build();
        CreateUpdateItemDto itemDto7 = createUpdateItemDto.toBuilder().name("name7").description("name4").build();

        userService.create(createUpdateUserDto);

        itemService.create(1L, createUpdateItemDto);
        itemService.create(1L, itemDto2);
        itemService.create(1L, itemDto3);
        itemService.create(1L, itemDto4);
        itemService.create(1L, itemDto5);
        itemService.create(1L, itemDto6);
        itemService.create(1L, itemDto7);

        List<GetItemDto> items = itemService.search(1L, "name4", 0, 2);

        Assertions.assertThat(items)
                .isNotEmpty()
                .hasSize(2)
                .satisfies(list -> {
                    Assertions.assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 4L);
                    Assertions.assertThat(list.get(0)).hasFieldOrPropertyWithValue("name", itemDto4.getName());
                    Assertions.assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 6L);
                    Assertions.assertThat(list.get(1)).hasFieldOrPropertyWithValue("name", itemDto6.getName());
                });
    }
}
