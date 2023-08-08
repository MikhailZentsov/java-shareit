package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateUpdateItemDto;
import ru.practicum.shareit.item.dto.GetCommentDto;
import ru.practicum.shareit.item.dto.GetItemDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private static CreateUpdateItemDto correctItem;
    private static CreateCommentDto correctComment;
    private static GetItemDto getItemDto;
    private static GetCommentDto getCommentDto;
    private static List<GetItemDto> listOfItems;

    @BeforeAll
    static void beforeAll() {
        correctItem = CreateUpdateItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        correctComment = CreateCommentDto.builder()
                .text("comment")
                .build();

        getItemDto = GetItemDto.builder()
                .id(1L)
                .name(correctItem.getName())
                .description(correctItem.getDescription())
                .available(correctItem.getAvailable())
                .build();

        getCommentDto = GetCommentDto.builder()
                .id(1L)
                .text(correctItem.getDescription())
                .build();

        listOfItems = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            listOfItems.add(getItemDto.toBuilder().id(i + 1L).build());
        }
    }

    @Test
    void shouldGetExceptionWithCreateWithoutHeader() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).create(anyLong(), any(CreateUpdateItemDto.class));
    }

    @Test
    void shouldCreateItem() throws Exception {
        when(itemService.create(anyLong(), any(CreateUpdateItemDto.class)))
                .thenReturn(getItemDto);

        String jsonItem = objectMapper.writeValueAsString(correctItem);

        mockMvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getItemDto.getId()))
                .andExpect(jsonPath("$.name").value(getItemDto.getName()))
                .andExpect(jsonPath("$.description").value(getItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(getItemDto.getAvailable()));
        verify(itemService, times(1)).create(anyLong(), any(CreateUpdateItemDto.class));
    }

    @Test
    void shouldGetExceptionWithUpdateWithoutHeader() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).update(anyLong(), anyLong(), any(CreateUpdateItemDto.class));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any(CreateUpdateItemDto.class)))
                .thenReturn(getItemDto);

        String jsonItem = objectMapper.writeValueAsString(correctItem);

        mockMvc.perform(patch("/items/1")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .content(jsonItem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getItemDto.getId()))
                .andExpect(jsonPath("$.name").value(getItemDto.getName()))
                .andExpect(jsonPath("$.description").value(getItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(getItemDto.getAvailable()));
        verify(itemService, times(1)).update(anyLong(), anyLong(), any(CreateUpdateItemDto.class));
    }


    @Test
    void shouldGetExceptionWithDeleteWithoutHeader() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).delete(anyLong(), anyLong());
    }

    @Test
    void shouldDeleteItem() throws Exception {
        doNothing().when(itemService).delete(anyLong(), anyLong());

        mockMvc.perform(delete("/items/1")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemService, times(1)).delete(anyLong(), anyLong());
    }

    @Test
    void shouldGetExceptionWithGetAllByUserIdWithoutHeader() throws Exception {
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getAllByUserId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldGetAllByUserId() throws Exception {
        when(itemService.getAllByUserId(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfItems);

        mockMvc.perform(get("/items")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*]").isArray())
                .andExpect(jsonPath("$.size()").value(20))
                .andExpect(jsonPath("$.[0].id").value(2L))
                .andExpect(jsonPath("$.[19].id").value(21L));
        verify(itemService, times(1)).getAllByUserId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldGetExceptionWithGetByItemIdWithoutHeader() throws Exception {
        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getOneById(anyLong(), anyLong());
    }

    @Test
    void shouldGetByItemId() throws Exception {
        when(itemService.getOneById(anyLong(), anyLong()))
                .thenReturn(getItemDto);

        mockMvc.perform(get("/items/1")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getItemDto.getId()))
                .andExpect(jsonPath("$.name").value(getItemDto.getName()))
                .andExpect(jsonPath("$.description").value(getItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(getItemDto.getAvailable()));
        verify(itemService, times(1)).getOneById(anyLong(), anyLong());
    }

    @Test
    void shouldGetExceptionWithSearchWithoutHeader() throws Exception {
        mockMvc.perform(get("/items/search?text=kek")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void shouldSearch() throws Exception {
        when(itemService.search(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(listOfItems);

        mockMvc.perform(get("/items/search?text=kek")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*]").isArray())
                .andExpect(jsonPath("$.size()").value(20))
                .andExpect(jsonPath("$.[0].id").value(2L))
                .andExpect(jsonPath("$.[19].id").value(21L));
        verify(itemService, times(1)).search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void shouldGetExceptionWithCommentWithoutHeader() throws Exception {
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).createComment(anyLong(), anyLong(), any(CreateCommentDto.class));
    }

    @Test
    void shouldCreateComment() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any(CreateCommentDto.class)))
                .thenReturn(getCommentDto);

        String jsonComment = objectMapper.writeValueAsString(correctComment);

        mockMvc.perform(post("/items/1/comment")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .content(jsonComment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any(CreateCommentDto.class));
    }
}