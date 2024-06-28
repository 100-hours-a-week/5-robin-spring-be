package org.com.community.controller;

import org.com.community.dto.UserDTO;
import org.com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderController {
    @Autowired
    private UserService userService;

    @GetMapping("/header")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {

        try {
            String email = userDetails.getUsername();
            UserDTO userDTO = userService.findByEmail(email);
            String response = "{\"id\":\"" + userDTO.getId() + "\", \"profile_path\":\"" + userDTO.getProfilePath() + "\"}";
            return ResponseEntity.ok(response);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }
}
