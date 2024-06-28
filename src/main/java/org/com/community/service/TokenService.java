package org.com.community.service;

import lombok.RequiredArgsConstructor;
import org.com.community.config.jwt.TokenProvider;
import org.com.community.domain.User;
import org.com.community.dto.UserDTO;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) throws IllegalAccessException {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalAccessException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        UserDTO user = userService.findById(userId);
        return tokenProvider.generateToken(user);
    }

}
