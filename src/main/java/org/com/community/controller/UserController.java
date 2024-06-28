package org.com.community.controller;

import org.com.community.config.jwt.TokenProvider;
import org.com.community.domain.User;
import org.com.community.dto.LoginDTO;
import org.com.community.dto.UserDTO;
import org.com.community.exception.UserNotFoundException;
import org.com.community.service.RefreshTokenService;
import org.com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/{id}")
    public ResponseEntity<String> currentGetUser(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDTO userDTO = userService.findByEmail(email);
        String response = String.format(
                "{\"id\":\"%d\", \"profilePath\":\"%s\", \"nickname\":\"%s\", \"email\":\"%s\"}",
                userDTO.getId(),
                userDTO.getProfilePath(),
                userDTO.getNickname(),
                userDTO.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        try {
            if(userService.isEmailDuplicate(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already");
            }
            if(userService.isNicknameDuplicate(userDTO.getNickname())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already");
            }
            userService.addUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User Created");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> findByEmailAndPassword(@RequestBody LoginDTO loginDTO) {
        try {
            UserDTO user = userService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPwd());
            if(user != null) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPwd())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = tokenProvider.generateToken(user);

                String refreshToken = tokenProvider.generateRefreshToken(user);
                refreshTokenService.createRefreshToken(user.getId(), refreshToken);

                return ResponseEntity.ok("{\"token\":\"" + jwt + "\", \"refreshToken\":\"" + refreshToken + "\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 현재 인증된 사용자의 이메일을 통해 사용자 ID를 가져옵니다.
            String email = userDetails.getUsername();
            // 해당 사용자의 리프레시 토큰을 무효화합니다.
            refreshTokenService.deleteRefreshTokenByUserId(email);

            return ResponseEntity.ok("Successfully logged out");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            return ResponseEntity.ok("updated user");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            userService.updatePassword(userDTO, email);
            return ResponseEntity.ok("updated user");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            userService.deleteUser(email);
            return ResponseEntity.ok("deleted user");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }
}
