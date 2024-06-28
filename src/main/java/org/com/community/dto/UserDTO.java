package org.com.community.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String pwd;
    private String nickname;
    private String profilePath;
    private LocalDateTime createAt;
    private Integer delFlag;
}
