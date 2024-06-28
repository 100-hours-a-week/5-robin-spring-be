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
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ColumnDefault("0")
    @Column(name = "likes", nullable = false)
    private Long likes;

    @ColumnDefault("0")
    @Column(name = "hits", nullable = false)
    private Long hits;

    @ColumnDefault("0")
    @Column(name = "comments", nullable = false)
    private Long comments;

    @CreatedDate
    @Column(name = "created_at", insertable = false, nullable = false)
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "del_flag", insertable = false, nullable = false)
    private Integer delFlag;

    @Builder
    public Post(String title, String content, String filePath, Long likes, Long hits, Long comments, LocalDateTime createAt, User user) {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
        this.likes = 0L; // 기본값 설정
        this.hits = 0L; // 기본값 설정
        this.comments = 0L; // 기본값 설정
        this.createAt = LocalDateTime.now(); // 기본값 설정
        this.user = user;
        this.delFlag = 0;
    }

    public void updatePost(String title, String content, String filePath) {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
    }
    public void deletePost() {
        this.delFlag = 1;
    }
    public void incrementCommentsCount() {
        this.comments++;
    }

    public void decrementCommentsCount() {
        this.comments--;
    }
    public void incrementHitsCount() {
        this.hits++;
    }
}
