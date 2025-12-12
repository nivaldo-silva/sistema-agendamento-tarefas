package io.github.nivaldosilva.cadastro_usuarios.config.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwkConfig {

    @Value("${jwt.public.key}")
    private String publicKeyPath;

    @Value("${jwt.private.key}")
    private String privateKeyPath;

    @Bean
    public JWKSet jwkSet() throws Exception {
        RSAPublicKey publicKey = loadPublicKey();
        RSAPrivateKey privateKey = loadPrivateKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("rsa-key")
                .build();

        return new JWKSet(rsaKey);
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/" + publicKeyPath.replace("classpath:", ""))) {
            byte[] keyBytes = is.readAllBytes();
            String publicKeyPEM = new String(keyBytes)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "");

            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decoded));
        }
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/" + privateKeyPath.replace("classpath:", ""))) {
            byte[] keyBytes = is.readAllBytes();
            String privateKeyPEM = new String(keyBytes)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");

            byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
        }
    }
}
