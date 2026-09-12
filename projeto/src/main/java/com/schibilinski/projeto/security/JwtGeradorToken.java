package com.schibilinski.projeto.security;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Component;

import com.schibilinski.projeto.entity.Usuario;

@Component
public class JwtGeradorToken implements GeradorToken {

    private static final String EMISSOR =
        "api-de-tarefas";

    private final JwtEncoder jwtEncoder;
    private final Duration duracaoToken;

    public JwtGeradorToken(
        JwtEncoder jwtEncoder,
        @Value("${security.jwt.expiracao}") Duration duracaoToken
    ) {
        this.jwtEncoder = jwtEncoder;
        this.duracaoToken = duracaoToken;
    }

    @Override
    public TokenAcesso gerar(Usuario usuario) {
        Instant emitidoEm = Instant.now();
        Instant expiraEm = emitidoEm.plus(duracaoToken);

        JwtClaimsSet claims = criarClaims(
            usuario,
            emitidoEm,
            expiraEm
        );

        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .build();

        Jwt jwt = jwtEncoder.encode(
            JwtEncoderParameters.from(header, claims)
        );

        return new TokenAcesso(
            jwt.getTokenValue(),
            expiraEm
        );
    }

    private JwtClaimsSet criarClaims(
        Usuario usuario,
        Instant emitidoEm,
        Instant expiraEm
    ) {
        return JwtClaimsSet.builder()
            .issuer(EMISSOR)
            .issuedAt(emitidoEm)
            .expiresAt(expiraEm)
            .subject(usuario.getId().toString())
            .claim("email", usuario.getEmail())
            .build();
    }
}
