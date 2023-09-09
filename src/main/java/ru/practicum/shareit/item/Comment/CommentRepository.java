package ru.practicum.shareit.item.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItem_IdOrderByIdDesc(long itemId);
}
