package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.GetCommentDto;
import ru.practicum.shareit.item.model.Comment;

@UtilityClass
public class CommentMapper {
    public GetCommentDto toGetCommentDtoFromComment(Comment comment) {
        return GetCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .build();
    }

    public Comment toCommentFromCreateCommentDto(CreateCommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }
}
