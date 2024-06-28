package org.com.community.service;

import lombok.RequiredArgsConstructor;
import org.com.community.dao.UserDAO;
import org.com.community.domain.User;
import org.com.community.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    public UserDetails loadUserByUsername(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
           throw new UserNotFoundException("User not found");
        }
        System.out.println(user);
        return user;
    }
}
