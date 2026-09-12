package com.schibilinski.projeto.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtEncoderConfig {

    @Bean
    public JwtEncoder jwtEncoder(
        @Value("${security.jwt.secret}") String secret
    ) {
        byte[] chaveDecodificada =
            Base64.getDecoder().decode(secret);

        SecretKey chaveSecreta = new SecretKeySpec(
            chaveDecodificada,
            "HmacSHA256"
        );

        OctetSequenceKey chaveJwt =
            new OctetSequenceKey.Builder(chaveSecreta)
                .algorithm(JWSAlgorithm.HS256)
                .build();

        JWKSource<SecurityContext> fonteDeChaves =
            new ImmutableJWKSet<>(
                new JWKSet(chaveJwt)
            );

        return new NimbusJwtEncoder(fonteDeChaves);
    }
}
