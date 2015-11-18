package net.pkhapps.dart.server.certificatemanager.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
class CSRTokenServiceImpl implements CSRTokenService {

    private final Clock clock;
    private ConcurrentHashMap<String, CSRToken> tokenMap = new ConcurrentHashMap<>();
    private AtomicLong nextFreeRunningNumber = new AtomicLong(1L);

    @Autowired
    CSRTokenServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public CSRToken generateNewToken() {
        CSRToken token = new CSRToken(clock.instant(), nextFreeRunningNumber.getAndIncrement());
        tokenMap.put(token.getHash(), token);
        return token;
    }

    @Override
    public void validateToken(String tokenHash) throws NoSuchTokenException, TokenExpiredException {
        CSRToken token = tokenMap.remove(sanitizeToken(tokenHash));
        if (token == null) {
            throw new NoSuchTokenException();
        }
        if (token.isExpired(clock)) {
            throw new TokenExpiredException();
        }
    }

    private static String sanitizeToken(String token) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < token.length(); ++i) {
            Character c = token.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }


}
