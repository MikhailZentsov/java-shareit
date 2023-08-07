package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.GetItemRequestDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService requestService;

    private static CreateItemRequestDto correctRequest;
    private static CreateItemRequestDto requestWithBlankDescription;
    private static CreateItemRequestDto requestWithDescriptionSize1001;
    private static GetItemRequestDto getItemRequestDto;
    private static List<GetItemRequestDto> listOfRequests;

    @BeforeAll
    static void beforeAll() {
        correctRequest = CreateItemRequestDto.builder()
                .description("description")
                .build();

        requestWithBlankDescription = CreateItemRequestDto.builder()
                .description(" ")
                .build();

        requestWithDescriptionSize1001 = CreateItemRequestDto.builder()
                .description("A".repeat(1001))
                .build();

        getItemRequestDto = GetItemRequestDto.builder()
                .id(1L)
                .description("description")
                .build();

        listOfRequests = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            listOfRequests.add(getItemRequestDto.toBuilder().id(i + 1L).build());
        }
    }

    @Test
    void shouldExceptionWithCreateRequestWithoutHeader() throws Exception {
        when(requestService.createRequest(anyLong(), any(CreateItemRequestDto.class)))
                .thenReturn(getItemRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(correctRequest);

        mockMvc.perform(post("/requests")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).createRequest(anyLong(), any(CreateItemRequestDto.class));
    }

    @Test
    void shouldExceptionWithCreateRequestWithRequestWithBlankDescription() throws Exception {
        when(requestService.createRequest(anyLong(), any(CreateItemRequestDto.class)))
                .thenReturn(getItemRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestWithBlankDescription);

        mockMvc.perform(post("/requests")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).createRequest(anyLong(), any(CreateItemRequestDto.class));
    }

    @Test
    void shouldExceptionWithCreateRequestWithRequestWithDescriptionSize1001() throws Exception {
        when(requestService.createRequest(anyLong(), any(CreateItemRequestDto.class)))
                .thenReturn(getItemRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestWithDescriptionSize1001);

        mockMvc.perform(post("/requests")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).createRequest(anyLong(), any(CreateItemRequestDto.class));
    }

    @Test
    void shouldCreateRequest() throws Exception {
        when(requestService.createRequest(anyLong(), any(CreateItemRequestDto.class)))
                .thenReturn(getItemRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(correctRequest);

        mockMvc.perform(post("/requests")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getItemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(getItemRequestDto.getDescription()));
        verify(requestService, times(1)).createRequest(anyLong(), any(CreateItemRequestDto.class));
    }

    @Test
    void shouldExceptionWithGetAllRequestsByUserIdWithRequestWithoutHeader() throws Exception {
        when(requestService.getAllRequestsByUserId(anyLong()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getAllRequestsByUserId(anyLong());
    }

    @Test
    void shouldGetAllRequestsByUserId() throws Exception {
        when(requestService.getAllRequestsByUserId(anyLong()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*]").isArray())
                .andExpect(jsonPath("$.size()").value(20))
                .andExpect(jsonPath("$.[0].id").value(2L))
                .andExpect(jsonPath("$.[19].id").value(21L));
        verify(requestService, times(1)).getAllRequestsByUserId(anyLong());
    }

    @Test
    void shouldExceptionWithGetAllRequestsWithRequestWithoutHeader() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldExceptionWithGetAllRequestsWithFromLessThen0() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests/all?from=-1")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldExceptionWithGetAllRequestsWithFromMoreThenMaxInt() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests/all?from=2147483648")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldExceptionWithGetAllRequestsWithSizeLessThen1() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests/all?size=0")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldExceptionWithGetAllRequestsWithSizeMoreThen20() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests/all?size=21")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldGetAllRequests() throws Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(listOfRequests);

        mockMvc.perform(get("/requests/all")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*]").isArray())
                .andExpect(jsonPath("$.size()").value(20))
                .andExpect(jsonPath("$.[0].id").value(2L))
                .andExpect(jsonPath("$.[19].id").value(21L));
        verify(requestService, times(1)).getAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void shouldExceptionWithGetRequestByIdWithRequestWithoutHeader() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(getItemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(requestService, never()).getRequestById(anyLong(), anyLong());
    }

    @Test
    void shouldGetRequestById() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(getItemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header(REQUEST_HEADER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getItemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(getItemRequestDto.getDescription()));
        verify(requestService, times(1)).getRequestById(anyLong(), anyLong());
    }
}