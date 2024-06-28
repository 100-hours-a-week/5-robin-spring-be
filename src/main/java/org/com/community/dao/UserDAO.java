package org.com.community.dao;


import org.com.community.domain.User;


public interface UserDAO {
    Long addUser(User user);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    User findById(Long id);
    User findByEmail(String email);
    Long updateUser(User user);
    Long updatePassword(User user);
    Long deleteUser(User user);
}
