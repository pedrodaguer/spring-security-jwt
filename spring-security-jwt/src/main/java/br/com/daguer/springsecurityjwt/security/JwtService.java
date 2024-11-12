package br.com.daguer.springsecurityjwt.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service("JwtService")
public class JwtService {

    private final JwtEncoder encoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.encoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long Expiry = 3600L;

        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));

        var Claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(Expiry))
                .claim("scope", scopes)
                .build();

        return encoder.encode(JwtEncoderParameters.from(Claims)).getTokenValue();

    }

}
