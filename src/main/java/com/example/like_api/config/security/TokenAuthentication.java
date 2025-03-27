package com.example.like_api.config.security;


import com.example.like_api.model.security.TokenData;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class TokenAuthentication extends UsernamePasswordAuthenticationToken {

    private TokenData tokenData;

    public TokenAuthentication(TokenData tokenData) {
        super(tokenData.getUsername(), null, tokenData.getAuthorities());
        this.tokenData = tokenData;
    }
}
