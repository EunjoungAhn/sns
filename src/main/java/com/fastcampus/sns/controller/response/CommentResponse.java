package com.fastcampus.sns.controller.response;

import com.fastcampus.sns.model.Comment;
import com.fastcampus.sns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Integer id;
    private String comment;
    private String uerName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static CommentResponse fromComment(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUsername(),
                comment.getPostId(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getRemovedAt()
        );
    }
}
