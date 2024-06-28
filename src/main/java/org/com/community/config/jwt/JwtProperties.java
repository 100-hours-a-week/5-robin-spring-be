package org.com.community.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
@ConfigurationProperties("jwt") //자바 클래스 프로피티 값을 가져와라
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private long jwtAccessExpiration;
    private long jwtRefreshExpiration;
}
