package com.portfolio.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    private static final String ALGORITHM = "HmacSHA256";
    private static final int MIN_SECRET_BYTES = 32;
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
    private static final String ENCODED_HEADER =
            ENCODER.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
    private static final Pattern SUB_PATTERN = Pattern.compile("\"sub\":\"([^\"]*)\"");
    private static final Pattern EXP_PATTERN = Pattern.compile("\"exp\":(\\d+)");

    private final byte[] secret;
    private final Duration validity;
    private final Clock clock;

    @Autowired
    public JwtProvider(@Value("${app.admin.jwt-secret}") String secret,
                       @Value("${app.admin.token-valid-hours}") long validHours) {
        this(secret, Duration.ofHours(validHours), Clock.systemUTC());
    }

    JwtProvider(String secret, Duration validity, Clock clock) {
        if (secret == null || secret.isBlank()) {
            byte[] random = new byte[MIN_SECRET_BYTES];
            new SecureRandom().nextBytes(random);
            this.secret = random;
            log.warn("JWT_SECRET 이 설정되지 않아 임의의 키를 사용합니다. 서버를 재시작하면 관리자 로그인이 풀립니다.");
        } else {
            byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
            if (bytes.length < MIN_SECRET_BYTES) {
                throw new IllegalStateException("JWT_SECRET 은 32바이트(영문 32자) 이상이어야 합니다.");
            }
            this.secret = bytes;
        }
        this.validity = validity;
        this.clock = clock;
    }

    public IssuedToken createToken(String subject) {
        Instant now = clock.instant();
        Instant expiresAt = now.plus(validity);
        String payload = "{\"sub\":\"" + subject + "\",\"iat\":" + now.getEpochSecond()
                + ",\"exp\":" + expiresAt.getEpochSecond() + "}";
        String unsigned = ENCODED_HEADER + "." + ENCODER.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return new IssuedToken(unsigned + "." + ENCODER.encodeToString(sign(unsigned)), expiresAt);
    }

    public boolean isValid(String token, String expectedSubject) {
        if (token == null || token.isBlank()) {
            return false;
        }
        String[] parts = token.split("\\.", -1);
        if (parts.length != 3 || !ENCODED_HEADER.equals(parts[0])) {
            return false;
        }
        try {
            byte[] expected = sign(parts[0] + "." + parts[1]);
            byte[] actual = DECODER.decode(parts[2]);
            if (!MessageDigest.isEqual(expected, actual)) {
                return false;
            }
            String payload = new String(DECODER.decode(parts[1]), StandardCharsets.UTF_8);
            Matcher sub = SUB_PATTERN.matcher(payload);
            Matcher exp = EXP_PATTERN.matcher(payload);
            if (!sub.find() || !exp.find()) {
                return false;
            }
            return expectedSubject.equals(sub.group(1))
                    && clock.instant().getEpochSecond() < Long.parseLong(exp.group(1));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Duration getValidity() {
        return validity;
    }

    private byte[] sign(String data) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(secret, ALGORITHM));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("토큰 서명에 실패했습니다.", e);
        }
    }
}
