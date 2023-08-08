package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {
    private final UserService userService;
    private final EntityManager entityManager;

    private static CreateUpdateUserDto createUpdateUserDto;

    @BeforeAll
    static void beforeAll() {
        createUpdateUserDto = CreateUpdateUserDto.builder()
                .name("name")
                .email("email@ya.ru")
                .build();
    }

    @Test
    void shouldCreateUser() {
        userService.create(createUpdateUserDto);

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 1L).getSingleResult();

        assertThat(createUpdateUserDto.getName(), equalTo(user.getName()));
        assertThat(createUpdateUserDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void shouldUpdateUser() {
        userService.create(createUpdateUserDto);

        CreateUpdateUserDto updateUserDto = CreateUpdateUserDto.builder().name("newName").build();

        userService.update(1L, updateUserDto);

        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User updatedUser = query.setParameter("id", 1L).getSingleResult();

        assertThat(updateUserDto.getName(), equalTo(updatedUser.getName()));
    }

    @Test
    void shouldDeleteById() {
        userService.create(createUpdateUserDto);

        assertThat(userService.getAll(0, 20).size(), equalTo(1));

        userService.deleteById(1L);

        assertThat(userService.getAll(0, 20).size(), equalTo(0));
    }

    @Test
    void shouldGetById() {
        userService.create(createUpdateUserDto);

        GetUserDto user = userService.getById(1L);

        assertThat(createUpdateUserDto.getName(), equalTo(user.getName()));
        assertThat(createUpdateUserDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void shouldGetAll() {
        CreateUpdateUserDto userDto2 = createUpdateUserDto.toBuilder().email("mail2@ya.ru").build();
        CreateUpdateUserDto userDto3 = createUpdateUserDto.toBuilder().email("mail3@ya.ru").build();
        CreateUpdateUserDto userDto4 = createUpdateUserDto.toBuilder().email("mail4@ya.ru").build();
        CreateUpdateUserDto userDto5 = createUpdateUserDto.toBuilder().email("mail5@ya.ru").build();
        CreateUpdateUserDto userDto6 = createUpdateUserDto.toBuilder().email("mail6@ya.ru").build();
        CreateUpdateUserDto userDto7 = createUpdateUserDto.toBuilder().email("mail7@ya.ru").build();

        userService.create(createUpdateUserDto);
        userService.create(userDto2);
        userService.create(userDto3);
        userService.create(userDto4);
        userService.create(userDto5);
        userService.create(userDto6);
        userService.create(userDto7);

        List<GetUserDto> users = userService.getAll(2, 3);

        assertThat(users)
                .isNotEmpty()
                .hasSize(3)
                .satisfies(list -> {
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(list.get(0)).hasFieldOrPropertyWithValue("email", userDto3.getEmail());
                    assertThat(list.get(1)).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(list.get(1)).hasFieldOrPropertyWithValue("email", userDto4.getEmail());
                    assertThat(list.get(2)).hasFieldOrPropertyWithValue("id", 5L);
                    assertThat(list.get(2)).hasFieldOrPropertyWithValue("email", userDto5.getEmail());
                });
    }
}
