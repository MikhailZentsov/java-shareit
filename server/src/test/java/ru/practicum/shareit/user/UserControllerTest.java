package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.CreateUpdateUserDto;
import ru.practicum.shareit.user.dto.GetUserDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static CreateUpdateUserDto correctUser;
    private static CreateUpdateUserDto userWithoutName;
    private static CreateUpdateUserDto userWithBlankName;
    private static CreateUpdateUserDto userWithoutEmail;
    private static CreateUpdateUserDto userWithWrongEmailNoName;
    private static CreateUpdateUserDto userWithWrongEmailNoDomainSecondLevel;
    private static CreateUpdateUserDto userWithWrongEmailNoDomainFirstLevel;
    private static CreateUpdateUserDto userWithWrongEmailNoAt;
    private static CreateUpdateUserDto userWithWrongEmailToShortDomainFirstLevel;
    private static CreateUpdateUserDto userWithWrongEmailToLongDomainFirstLevel;
    private static List<GetUserDto> listOfUsers;
    private static GetUserDto getUserDto;

    @BeforeAll
    static void beforeAll() {
        correctUser = CreateUpdateUserDto.builder()
                .name("user")
                .email("user@user.com")
                .build();

        userWithoutName = CreateUpdateUserDto.builder()
                .email("user@user.com")
                .build();

        userWithBlankName = CreateUpdateUserDto.builder()
                .name("")
                .email("user@user.com")
                .build();

        userWithoutEmail = CreateUpdateUserDto.builder()
                .name("user")
                .build();

        userWithWrongEmailNoName = CreateUpdateUserDto.builder()
                .name("user")
                .email("@user.com")
                .build();

        userWithWrongEmailNoDomainSecondLevel = CreateUpdateUserDto.builder()
                .name("user")
                .email("user@.com")
                .build();

        userWithWrongEmailNoDomainFirstLevel = CreateUpdateUserDto.builder()
                .name("user")
                .email("user@user.")
                .build();

        userWithWrongEmailNoAt = CreateUpdateUserDto.builder()
                .name("user")
                .email("user.user.com")
                .build();

        userWithWrongEmailToShortDomainFirstLevel = CreateUpdateUserDto.builder()
                .name("user")
                .email("user@user.c")
                .build();

        userWithWrongEmailToLongDomainFirstLevel = CreateUpdateUserDto.builder()
                .name("user")
                .email("user@user.commm")
                .build();

        getUserDto = GetUserDto.builder()
                .id(1L)
                .name("user")
                .email("user@user.com")
                .build();

        listOfUsers = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            listOfUsers.add(getUserDto.toBuilder().id(i + 2L).build());
        }
    }

    @Test
    void shouldGetExceptionWithCreateUserWithoutName() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithoutName);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithBlankName() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithBlankName);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithoutEmail() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithoutEmail);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithWrongEmailNoName() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoName);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithWrongEmailNoDomainSecondLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoDomainSecondLevel);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithWrongEmailNoDomainFirstLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoDomainFirstLevel);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithWrongEmailNoAt() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoAt);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithWrongEmailToShortDomainFirstLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailToShortDomainFirstLevel);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithCreateUserWithWrongEmailToLongDomainFirstLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailToLongDomainFirstLevel);

        mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.create(any(CreateUpdateUserDto.class)))
                .thenReturn(getUserDto);

        String jsonUser = objectMapper.writeValueAsString(correctUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getUserDto.getId()))
                .andExpect(jsonPath("$.name").value(getUserDto.getName()))
                .andExpect(jsonPath("$.email").value(getUserDto.getEmail()));

        verify(userService, times(1)).create(correctUser);
    }

    @Test
    void shouldGetExceptionWithUpdateUserWithWrongEmailNoName() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoName);

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithUpdateUserWithWrongEmailNoDomainSecondLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoDomainSecondLevel);

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithUpdateUserWithWrongEmailNoDomainFirstLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoDomainFirstLevel);

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithUpdateUserWithWrongEmailNoAt() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailNoAt);

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithUpdateUserWithWrongEmailToShortDomainFirstLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailToShortDomainFirstLevel);

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldGetExceptionWithUpdateUserWithWrongEmailToLongDomainFirstLevel() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(userWithWrongEmailToLongDomainFirstLevel);

        mockMvc.perform(patch("/users/1")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldUpdateUserWithoutName() throws Exception {
        when(userService.update(anyLong(), any(CreateUpdateUserDto.class)))
                .thenReturn(getUserDto);

        String jsonUser = objectMapper.writeValueAsString(userWithoutName);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getUserDto.getId()))
                .andExpect(jsonPath("$.name").value(getUserDto.getName()))
                .andExpect(jsonPath("$.email").value(getUserDto.getEmail()));
        verify(userService, times(1)).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldUpdateUserWithoutEmail() throws Exception {
        when(userService.update(anyLong(), any(CreateUpdateUserDto.class)))
                .thenReturn(getUserDto);

        String jsonUser = objectMapper.writeValueAsString(userWithoutEmail);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getUserDto.getId()))
                .andExpect(jsonPath("$.name").value(getUserDto.getName()))
                .andExpect(jsonPath("$.email").value(getUserDto.getEmail()));
        verify(userService, times(1)).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        when(userService.update(anyLong(), any(CreateUpdateUserDto.class)))
                .thenReturn(getUserDto);

        String jsonUser = objectMapper.writeValueAsString(correctUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getUserDto.getId()))
                .andExpect(jsonPath("$.name").value(getUserDto.getName()))
                .andExpect(jsonPath("$.email").value(getUserDto.getEmail()));
        verify(userService, times(1)).update(anyLong(), any(CreateUpdateUserDto.class));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(anyLong());

        mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON));
        verify(userService, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldGetById() throws Exception {
        when(userService.getById(anyLong()))
                .thenReturn(getUserDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getUserDto.getId()))
                .andExpect(jsonPath("$.name").value(getUserDto.getName()))
                .andExpect(jsonPath("$.email").value(getUserDto.getEmail()));
        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    void shouldGetExceptionWithGetAllWithFromLessThen0() throws Exception {
        mockMvc.perform(get("/users?from=-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).getById(anyLong());
    }

    @Test
    void shouldGetExceptionWithGetAllWithFromMoreThenMaxInt() throws Exception {
        mockMvc.perform(get("/users?from=2147483648")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).getById(anyLong());
    }

    @Test
    void shouldGetExceptionWithGetAllWithFromSizeLessThen1() throws Exception {
        mockMvc.perform(get("/users?size=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).getById(anyLong());
    }

    @Test
    void shouldGetExceptionWithGetAllWithSizeMoreThen20() throws Exception {
        mockMvc.perform(get("/users?size=21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, never()).getById(anyLong());
    }

    @Test
    void shouldGetAll() throws Exception {
        when(userService.getAll(anyInt(), anyInt()))
                .thenReturn(listOfUsers);
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*]").isArray())
                .andExpect(jsonPath("$.size()").value(20))
                .andExpect(jsonPath("$.[0].id").value(2L))
                .andExpect(jsonPath("$.[19].id").value(21L));
        verify(userService, times(1)).getAll(anyInt(), anyInt());
    }
}