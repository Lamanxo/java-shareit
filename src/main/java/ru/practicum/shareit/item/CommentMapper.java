package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto makeCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static Comment makeComment(CommentDto cDto) {
        return Comment.builder()
                .text(cDto.getText())
                .created(LocalDateTime.now())
                .build();
    }
}
