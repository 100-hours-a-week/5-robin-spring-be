package org.com.community.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", insertable = false, nullable = false)
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "del_flag", insertable = false, nullable = false)
    private Integer delFlag;

    @Builder
    public Comment(String content, LocalDateTime createAt, User user, Post post) {
        this.content = content;
        this.createAt = LocalDateTime.now(); // 기본값 설정
        this.user = user;
        this.post = post;
        this.delFlag = 0;
    }

    public void updateComment(String content) {
        this.content = content;
    }
    public void deleteComment() {
        this.delFlag = 1;
    }
}
