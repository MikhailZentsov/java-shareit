package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {
    public static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";

    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "startDate");

    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

    public static final Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

}
