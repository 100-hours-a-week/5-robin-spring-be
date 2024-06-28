package org.com.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.com.community.domain.Post;
import org.com.community.domain.User;


@NoArgsConstructor
@Getter
public class UpdatePostRequestDTO {
    private String title;
    private String content;
    private String filePath;
    private Long userId;

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .filePath(filePath)
                .user(user)
                .build();
    }
}
