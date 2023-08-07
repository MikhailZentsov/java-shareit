package ru.practicum.shareit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.Constants.SORT_BY_ID_ASC;

class OffsetBasedPageRequestTest {

    @BeforeAll
    static void beforeAll() {
        OffsetBasedPageRequest ofpg1 = new OffsetBasedPageRequest(0, 1);
        OffsetBasedPageRequest ofpg2 = new OffsetBasedPageRequest(0, 1, Sort.Direction.DESC, "id");
        OffsetBasedPageRequest ofpg3 = new OffsetBasedPageRequest(1, 1, SORT_BY_ID_ASC);
    }

    @Test
    void shouldExceptionIllegalArgumentExceptionByOffset() {
        final IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new OffsetBasedPageRequest(-1, 1)
        );

        assertEquals("Offset index must not be less than zero!",
                exception.getMessage());
    }

    @Test
    void shouldExceptionIllegalArgumentExceptionByLimit() {
        final IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new OffsetBasedPageRequest(0, 0)
        );

        assertEquals("Limit must not be less than one!",
                exception.getMessage());
    }

    @Test
    void shouldGetCorrectPageNumber() {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(10, 5);
        assertEquals(2, pageRequest.getPageNumber());
    }

    @Test
    void shouldGetCorrectEquals() {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(10, 5);
        OffsetBasedPageRequest pageRequest2 = new OffsetBasedPageRequest(10, 5);
        assertEquals(pageRequest2, pageRequest);
    }

    @Test
    void shouldGetCorrectToString() {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(10, 5);
        assertEquals(pageRequest.toString(), pageRequest.toString());
    }

    @Test
    void shouldGetCorrectHashCode() {
        OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(10, 5);
        assertEquals(pageRequest.hashCode(), pageRequest.hashCode());
    }
}