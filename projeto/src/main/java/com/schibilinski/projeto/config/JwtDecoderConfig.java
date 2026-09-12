package com.schibilinski.projeto.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    private static final String EMISSOR =
        "api-de-tarefas";

    @Bean
    public JwtDecoder jwtDecoder(
        @Value("${security.jwt.secret}") String secret
    ) {
        SecretKey chaveSecreta = criarChave(secret);

        NimbusJwtDecoder decoder =
            NimbusJwtDecoder
                .withSecretKey(chaveSecreta)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        decoder.setJwtValidator(
            JwtValidators.createDefaultWithIssuer(EMISSOR)
        );

        return decoder;
    }

    private SecretKey criarChave(String secret) {
        byte[] chaveDecodificada =
            Base64.getDecoder().decode(secret);

        return new SecretKeySpec(
            chaveDecodificada,
            "HmacSHA256"
        );
    }

}
