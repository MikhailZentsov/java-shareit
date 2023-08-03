package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.GetCommentDto;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@UtilityClass
public class Constants {
    public static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";

    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "startDate");

    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

    public static final Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final Comparator<Booking> orderByStartDateDesc = (a, b) -> {
        if (a.getStartDate().isAfter(b.getStartDate())) {
            return -1;
        } else if (a.getStartDate().isBefore(b.getStartDate())) {
            return 1;
        } else {
            return 0;
        }
    };

    public static final Comparator<Booking> orderByStartDateAsc = (a, b) -> {
        if (a.getStartDate().isAfter(b.getStartDate())) {
            return 1;
        } else if (a.getStartDate().isBefore(b.getStartDate())) {
            return -1;
        } else {
            return 0;
        }
    };

    public static final Comparator<GetCommentDto> orderByCreatedDesc = (a, b) -> {
        if (a.getCreated().isAfter(b.getCreated())) {
            return 1;
        } else if (a.getCreated().isBefore(b.getCreated())) {
            return -1;
        } else {
            return 0;
        }
    };
}
