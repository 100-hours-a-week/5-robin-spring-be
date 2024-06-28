package org.com.community.service;

import lombok.RequiredArgsConstructor;
import org.com.community.dao.UserDAO;
import org.com.community.domain.RefreshToken;
import org.com.community.domain.User;
import org.com.community.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    @Autowired
    private UserDAO userDAO;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        System.out.println(refreshToken);
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
    public RefreshToken createRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(existingToken -> {
                    existingToken.update(token);
                    return existingToken;
                })
                .orElseGet(() -> new RefreshToken(userId, token));
        return refreshTokenRepository.save(refreshToken);
    }
    @Transactional
    public void deleteRefreshTokenByUserId(String email) {
        User user = userDAO.findByEmail(email);
        refreshTokenRepository.deleteByUserId(user.getId());
    }
}
