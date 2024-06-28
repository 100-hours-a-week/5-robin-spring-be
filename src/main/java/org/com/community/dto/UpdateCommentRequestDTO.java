package org.com.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.com.community.domain.Comment;
import org.com.community.domain.Post;
import org.com.community.domain.User;

@NoArgsConstructor
@Getter
public class UpdateCommentRequestDTO {
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
