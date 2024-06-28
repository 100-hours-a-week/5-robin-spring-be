package org.com.community.service;

import org.com.community.dao.UserDAO;
import org.com.community.domain.User;
import org.com.community.dto.LoginDTO;
import org.com.community.dto.UserDTO;
import org.com.community.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //GET
    public UserDTO findByEmailAndPassword(String email, String pwd) {
        try {

            User user = userDAO.findByEmail(email);
            if(user != null && passwordEncoder.matches(pwd, user.getPwd())) {
                return UserDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .pwd(user.getPwd())
                        .nickname(user.getNickname())
                        .profilePath(user.getProfilePath())
                        .createAt(user.getCreateAt())
                        .delFlag(user.getDelFlag())
                        .build();
            } else {
                throw new UserNotFoundException("Invalid password or user not found");
            }
        } catch (UserNotFoundException e) {
            return null;
        }
    }
    public boolean isEmailDuplicate(String email) {
        return userDAO.existsByEmail(email);
    }
    public boolean isNicknameDuplicate(String nickname) {
        return userDAO.existsByNickname(nickname);
    }
    public UserDTO findByEmail(String email) {
        try {
            User user =  userDAO.findByEmail(email);
            return UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .pwd(user.getPwd())
                    .nickname(user.getNickname())
                    .profilePath(user.getProfilePath())
                    .createAt(user.getCreateAt())
                    .delFlag(user.getDelFlag())
                    .build();
        } catch(UserNotFoundException e) {
            return null;
        }
    }
    public UserDTO findById(Long id) {
        try {
            User user =  userDAO.findById(id);
            return UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .pwd(user.getPwd())
                    .nickname(user.getNickname())
                    .profilePath(user.getProfilePath())
                    .createAt(user.getCreateAt())
                    .delFlag(user.getDelFlag())
                    .build();
        } catch(UserNotFoundException e) {
            return null;
        }

    }

    //POST
    public Long addUser(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPwd());
        User user = User.builder()
                .email(userDTO.getEmail())
                .pwd(encodedPassword)
                .nickname(userDTO.getNickname())
                .profilePath(userDTO.getProfilePath())
                .build();
        return userDAO.addUser(user);
    }

    //PATCH
    public Long updateUser(UserDTO userDTO) {
        User user = userDAO.findByEmail(userDTO.getEmail());
        user.updateUser(userDTO.getNickname(), userDTO.getProfilePath());
        return userDAO.updateUser(user);
    }
    public Long updatePassword(UserDTO userDTO, String email) {
        User user = userDAO.findByEmail(email);
        String encodedPassword = passwordEncoder.encode(userDTO.getPwd());
        user.updatePassword(encodedPassword);
        return userDAO.updatePassword(user);
    }

    //DELETE
    public Long deleteUser(String email) {
        User user = userDAO.findByEmail(email);
        user.deleteUser();
        return userDAO.deleteUser(user);
    }
}
