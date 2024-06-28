package org.com.community.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "pwd", nullable = false)
    private String pwd;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "profile_path")
    private String profilePath;

    @Column(name = "created_at", insertable = false, nullable = false)
    private LocalDateTime createAt;

    @Column(name = "del_flag", insertable = false, nullable = false)
    private Integer delFlag;

    @Builder
    public User(Long id,String email, String pwd, String nickname, String profilePath, LocalDateTime createAt, Integer delFlag) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.profilePath = profilePath;
        this.nickname = nickname;
        this.createAt = createAt;
        this.delFlag = delFlag;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void updateUser(String nickname, String profilePath) {
        this.nickname = nickname;
        this.profilePath = profilePath;
    }
    public void updatePassword(String pwd) {
        this.pwd = pwd;
    }
    public void deleteUser() {
        this.delFlag = 1;
    }
}
